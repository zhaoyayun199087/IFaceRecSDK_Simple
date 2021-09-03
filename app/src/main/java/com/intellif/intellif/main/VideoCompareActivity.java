package com.intellif.intellif.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.intellif.FaceRect;
import com.intellif.FaceTrackListener;
import com.intellif.FaceUtils;
import com.intellif.ImageFormat;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;
import com.intellif.intellif.view.FaceView;
import com.intellif.intellif.view.LivingInterface;
import com.intellif.intellif.view.LivingListener;
import com.intellif.intellif.view.LivingSurfaceView;

public class VideoCompareActivity extends BaseActivity implements FaceTrackListener, LivingListener {

    private ImageView imageView;
    private TextView tv_result;
    private Bitmap faceBitmap = null;
    private Button button;
    private byte[] face_feature = null;
    private MyHandler myHandler = new MyHandler();
    public LivingSurfaceView surfaceView;
    public FaceView faceView;
    private int width = 0;
    private int height = 0;
    private int view_width = 0;
    private int view_height = 0;
    private boolean isCompare = false;
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_compare);

        initView();
        getViewWH();
    }

    private void initView() {
        loadModel();
        imageView = findViewById(R.id.img_compare);
        img = findViewById(R.id.img);
        tv_result = findViewById(R.id.tv_result);
        button = findViewById(R.id.bt_upload);
        surfaceView = findViewById(R.id.surfaceview);
        faceView = findViewById(R.id.faceview);
        LivingInterface.getInstance().init(this);
        LivingInterface.getInstance().initRatio(1280);
        LivingInterface.getInstance().setLivingCallBack(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker(IMAGE_PICKER_ONE);
            }
        });
        FaceUtils.getInstance().IFaceRecSDK_SetCaptureCallBack(captureHandle, this);
    }

    //获得容器的高度
    private void getViewWH() {
        ViewTreeObserver vto = faceView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                faceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                view_height = faceView.getHeight();
                view_width = faceView.getWidth();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = resultImagePicker(resultCode, data);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            faceBitmap = bitmap;
            getFaceFeature(bitmap);
        } else {
            Toast.makeText(this, "没有获取到图片", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFaceFeature(Bitmap bitmap){
        FaceRect faceRect[] = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle, bitmap);
        if (faceRect == null) {
            Toast.makeText(VideoCompareActivity.this, "当前选择图片没有人脸", Toast.LENGTH_SHORT).show();
        } else {
            face_feature = FaceUtils.getInstance().IFaceRecSDK_Face_Feature(extractHandle, bitmap, faceRect[0]);
        }
    }

    @Override
    public void onTrackerListener(final byte[] bytes, final int width, final int height, FaceRect[] faceRects) {
        this.width = width;
        this.height = height;
        Message m = myHandler.obtainMessage();
        m.what = 0;
        m.obj = faceRects;
        m.sendToTarget();
        if (faceRects == null) {
            return;
        }
        if (face_feature == null) {
            return;
        }
        if (isCompare) {
            return;
        }
        isCompare = true;
        CompareTask compareTask = new CompareTask();
        compareTask.setData(bytes, width, height, faceRects[0]);
        compareTask.execute();
    }

    @Override
    public void livingData(byte[] living_data, int width, int height) {
        if (isActive()) {
            FaceUtils.getInstance().IFaceRecSDK_FaceCaptureReq(captureHandle, living_data, width, height);
        }
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                FaceRect[] faceInfos = (FaceRect[]) msg.obj;
                faceView.setFaces(faceInfos, width, height, view_width, view_height);
            } else if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                img.setImageBitmap(bitmap);
            }
        }
    }

    class CompareTask extends AsyncTask<Void, Void, Float> {
        public byte[] data;
        public int width;
        public int height;
        public FaceRect faceRect;

        public void setData(byte[] data, int width, int height, FaceRect faceRect) {
            this.data = data;
            this.width = width;
            this.height = height;
            this.faceRect = faceRect;
        }

        @Override
        protected Float doInBackground(Void... voids) {
            //Byte[]数组图片转为Bitmap显示，验证图像旋转角度是否正确
            /*
            Bitmap bitmap = MatrixYuvUtils.rgb2Bitmap(data, width, height);
            Message m = myHandler.obtainMessage();
            m.what = 1;
            m.obj = bitmap;
            m.sendToTarget();
            */

            byte[] feature = FaceUtils.getInstance().IFaceRecSDK_Face_Feature(extractHandle, data, width, height, faceRect, ImageFormat.IFACEREC_IMG_BGR);
            Log.e("feature length", feature.length + "");

            Log.e("face feature length", face_feature.length + "");
            float condifence = FaceUtils.getInstance().IFaceRecSDK_FeatureCompare(compareHandle, feature, face_feature);
            Log.e("confidence", "current  confidence========" + condifence);
            return condifence;
        }

        @Override
        protected void onPostExecute(Float confidence) {
            super.onPostExecute(confidence);
            if (confidence >= 0.92) {
                tv_result.setText("比对成功，当前相似度=====" + confidence);
                tv_result.setTextColor(0xff00FF00);
            } else {
                tv_result.setText("比对失败，当前相似度=====" + confidence);
                tv_result.setTextColor(0xffCD3333);
            }
            isCompare = false;
        }
    }
}
