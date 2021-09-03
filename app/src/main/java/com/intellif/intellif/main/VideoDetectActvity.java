package com.intellif.intellif.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.intellif.FaceRect;
import com.intellif.FaceTrackListener;
import com.intellif.FaceUtils;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;
import com.intellif.intellif.view.FaceView;
import com.intellif.intellif.view.LivingInterface;
import com.intellif.intellif.view.LivingListener;
import com.intellif.intellif.view.LivingSurfaceView;

public class VideoDetectActvity extends BaseActivity implements LivingListener, FaceTrackListener {
    private MyHandler myHandler = new MyHandler();
    public LivingSurfaceView surfaceView;
    public FaceView faceView;
    private int width = 0;
    private int height = 0;
    private int view_width = 0;
    private int view_height = 0;
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detect_actvity);
        initLicense();
        intiview();
        getViewWH();
    }

    private void intiview() {
        img = findViewById(R.id.img);
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
    public void onTrackerListener(final byte[] bytes, final int width, final int height, FaceRect[] faceRects) {
        Log.d("TAG1", "width: " + width + "  height: " + height);
        /*new Thread(){
            @Override
            public void run() {
                Bitmap bitmap = MatrixYuvUtils.rgb2Bitmap(bytes, width, height);
                Message m = myHandler.obtainMessage();
                m.what = 1;
                m.obj = bitmap;
                m.sendToTarget();
            }
        }.start();*/
        this.width = width;
        this.height = height;
        Message m = myHandler.obtainMessage();
        m.what = 0;
        m.obj = faceRects;
        m.sendToTarget();

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
            } else if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                img.setImageBitmap(bitmap);
            }
        }
    }
}
