package com.intellif.intellif.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.intellif.FaceRecAttrResult;
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

public class LivingDetectActivity extends BaseActivity implements LivingListener, FaceTrackListener {
    private MyHandler myHandler = new MyHandler();
    public LivingSurfaceView surfaceView;
    public FaceView faceView;
    public TextView textView;
    private int width = 0;
    private int height = 0;
    private int view_width = 0;
    private int view_height = 0;
    private boolean isLiving = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_detect);
        intiview();
        getViewWH();
        initLicense();
    }

    private void intiview() {
        surfaceView = findViewById(R.id.surfaceview);
        faceView = findViewById(R.id.faceview);
        textView = findViewById(R.id.tv_result);
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
        this.width = width;
        this.height = height;
        Message m = myHandler.obtainMessage();
        m.what = 0;
        m.obj = faceRects;
        m.sendToTarget();
        if (faceRects == null) {
            return;
        }
        if (isLiving) {
            return;
        }
        isLiving = true;
        LivingTask mTask = new LivingTask();
        mTask.setData(bytes, width, height, faceRects[0]);
        mTask.execute();
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

    class LivingTask extends AsyncTask<Void, Void, FaceRecAttrResult[]> {
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
        protected FaceRecAttrResult[] doInBackground(Void... voids) {
            FaceRecAttrResult[] attrResults = FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle, data, width, height, faceRect, 0x80, ImageFormat.IFACEREC_IMG_BGR);
            return attrResults;
        }

        @Override
        protected void onPostExecute(FaceRecAttrResult[] attrResults) {
            super.onPostExecute(attrResults);
            if (attrResults == null || attrResults.length <= 0) {
                textView.setText("检测出错");
            } else {
                textView.setText("当前结果：\n");
                for (int i = 0; i < attrResults.length; i++) {
                    Log.e("TAG", attrResults.length + " , " + i + " ,type: " + attrResults[i].IFaceRecAttrType + " , value: " + attrResults[i].nValue + ", confidence: " + attrResults[i].fConfidence);
                    if (attrResults[i].IFaceRecAttrType == 0) {
                        String str;
                        switch (attrResults[i].nValue) {
                            case 1:
                                str = "男";
                                break;
                            case 2:
                                str = "女";
                                break;
                            default:
                                str = "未知";
                        }
                        textView.append("性别:" + str);
                    } else if (attrResults[i].IFaceRecAttrType == 1) {
                        textView.append(", 年龄:" + attrResults[i].nValue);
                    } else if (attrResults[i].IFaceRecAttrType == 6) {
                        String str;
                        switch (attrResults[i].nValue) {
                            case 1:
                                str = "有";
                                break;
                            case 2:
                                str = "太阳镜";
                                break;
                            default:
                                str = "无";
                        }
                        textView.append(", 眼镜:" + str);
                    } else if (attrResults[i].IFaceRecAttrType == 7) {
                        String str = attrResults[i].nValue == 1 ? "有" : "无";
                        textView.append(", 帽子:" + str);
                    } else if (attrResults[i].IFaceRecAttrType == 2) {
                        textView.append("\n以x轴为中心,脸部上下俯仰角度:" + attrResults[i].nValue);
                    } else if (attrResults[i].IFaceRecAttrType == 3) {
                        textView.append("\n以y轴为中心,脸部左右旋转角度:" + attrResults[i].nValue);
                    } else if (attrResults[i].IFaceRecAttrType == 4) {
                        textView.append("\n以中心点为中心，x-y平面旋转角度:" + attrResults[i].nValue);
                    } else if (attrResults[i].IFaceRecAttrType == 5) {
                        textView.append("\n人脸质量:" + attrResults[i].fConfidence);
                    } else if (attrResults[i].IFaceRecAttrType == 9) {
                        String str = attrResults[i].fConfidence > 1.2 ? "活体" : "非活体";
                        textView.append("\n活体检测:" + attrResults[i].fConfidence + "," + str);
                    }
                }
            }
            isLiving = false;
        }
    }
}
