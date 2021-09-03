package com.intellif.intellif.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.intellif.FaceRect;
import com.intellif.FaceTrackListener;
import com.intellif.FaceUtils;
import com.intellif.IFaceRecSearchResult;
import com.intellif.ImageFormat;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;
import com.intellif.intellif.view.FaceView;
import com.intellif.intellif.view.LivingInterface;
import com.intellif.intellif.view.LivingListener;
import com.intellif.intellif.view.LivingSurfaceView;

import java.io.File;

public class VideoSearchActivity extends BaseActivity implements FaceTrackListener, LivingListener {
    public String LICENSE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "license";
    public String MODEL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "model";
    private MyHandler myHandler = new MyHandler();
    public LivingSurfaceView surfaceView;
    public FaceView faceView;
    public TextView tv_result;
    private int width = 0;
    private int height = 0;
    private int view_width = 0;
    private int view_height = 0;
    private boolean isSearch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);
        initLicense();
        intiview();
        getViewWH();
    }

    private void intiview() {
        tv_result = findViewById(R.id.tv_result);
        surfaceView = findViewById(R.id.surfaceview);
        faceView = findViewById(R.id.faceview);
        LivingInterface.getInstance().init(this);
        LivingInterface.getInstance().initRatio(1280);
        LivingInterface.getInstance().setLivingCallBack(this);
    }

    public void initLicense() {
        loadModel();
        FaceUtils.getInstance().IFaceRecSDK_SetCaptureCallBack(captureHandle, this);
    }

    @Override
    public void onTrackerListener(byte[] bytes, int width, int height, FaceRect[] faceRects) {
        Log.d("TAG2", "onTrackerListener active: "+isActive());
        this.width = width;
        this.height = height;
        Message m = myHandler.obtainMessage();
        m.what = 0;
        m.obj = faceRects;
        m.sendToTarget();
        if (faceRects != null) {
            if (!isSearch) {
                isSearch = true;
                CompareTask task = new CompareTask();
                task.setData(bytes, width, height, faceRects[0]);
                task.execute();
            }
        }
    }

    @Override
    public void livingData(byte[] living_data, int width, int height) {
        if(isActive()){
            FaceUtils.getInstance().IFaceRecSDK_FaceCaptureReq(captureHandle, living_data, width, height);
        }
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                FaceRect[] faceInfos = (FaceRect[]) msg.obj;
                faceView.setFaces(faceInfos, width, height, view_width, view_height);
            }
        }
    }

    class CompareTask extends AsyncTask<Void, Void, IFaceRecSearchResult> {
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
        protected IFaceRecSearchResult doInBackground(Void... voids) {
            IFaceRecSearchResult[] result = null;
            result = FaceUtils.getInstance().IFaceRecSDK_FaceSearch(searchHandle, data, width, height, faceRect, 0.92f, ImageFormat.IFACEREC_IMG_BGR);
            if (result != null) {
                return result[0];
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(IFaceRecSearchResult iFaceRecSearchResult) {
            super.onPostExecute(iFaceRecSearchResult);
            if (iFaceRecSearchResult == null) {
                tv_result.setText("比对失败");
                //Toast.makeText(VideoSearchActivity.this, "比对失败", Toast.LENGTH_SHORT).show();
            } else {
                tv_result.setText("比对成功，人脸id==============" + iFaceRecSearchResult.dId + "==============" + iFaceRecSearchResult.fScore);
                //Toast.makeText(VideoSearchActivity.this, "比对成功，人脸id==============" + iFaceRecSearchResult.dId + "==============" + iFaceRecSearchResult.fScore, Toast.LENGTH_SHORT).show();
            }
            isSearch = false;
        }
    }
}
