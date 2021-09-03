package com.intellif.intellif.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.intellif.FaceRecAttrResult;
import com.intellif.FaceRect;
import com.intellif.FaceUtils;
import com.intellif.ImageFormat;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;
import com.intellif.intellif.view.GrayInterface;
import com.intellif.intellif.view.GrayListener;
import com.intellif.intellif.view.GraySurfaceView;

public class InfraredActivity extends BaseActivity implements GrayListener {
    public GraySurfaceView surfaceView;
    public TextView textView;
    public boolean isLiving = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infrared);
        loadModel();
        initview();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initview() {
        surfaceView = findViewById(R.id.surfaceview);
        textView = findViewById(R.id.tv_result);
        GrayInterface.getInstance().init(this);
        GrayInterface.getInstance().initRatio(1280);
        GrayInterface.getInstance().setLivingCallBack(this);
    }

    @Override
    public void grayData(byte[] living_data, int width, int height) {
        if (isLiving&&!isActive()) {
            return;
        }
        isLiving = true;
        LivingTask mTask = new LivingTask();
        mTask.setData(living_data, width, height);
        mTask.execute();
    }


    public FaceRect getBigFace(FaceRect[] faceRects) {
        int postion = 0;
        int size = 0;
        for (int i = 0; i < faceRects.length; i++) {
            if ((faceRects[i].dRectBottom - faceRects[i].dRectTop) > size) {
                postion = i;
                size = faceRects[i].dRectBottom - faceRects[i].dRectTop;
            }
        }
        return faceRects[postion];
    }

    class LivingTask extends AsyncTask<Void, Void, FaceRecAttrResult[]> {
        public byte[] data;
        public int width;
        public int height;

        public void setData(byte[] data, int width, int height) {
            this.data = data;
            this.width = width;
            this.height = height;
        }

        @Override
        protected FaceRecAttrResult[] doInBackground(Void... voids) {
            FaceRect[] faceRect = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle, data, width, height, ImageFormat.IFACEREC_IMG_NV21);
            if (faceRect == null) {
                return null;
            }

            Log.e("face-detect-size", "================" + faceRect.length);
            FaceRect detect_face = getBigFace(faceRect);
            FaceRecAttrResult[] attrResults = FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle, data, width, height, detect_face, 0x80, ImageFormat.IFACEREC_IMG_NV21);
            return attrResults;
        }

        @Override
        protected void onPostExecute(FaceRecAttrResult[] attrResults) {
            super.onPostExecute(attrResults);
            if (attrResults == null) {
                textView.setText("活体检测出错");
            } else {
                for (int i = 0; i < attrResults.length; i++) {
                    if (attrResults[i].IFaceRecAttrType == 13) {
                        if (attrResults[i].fConfidence > 0.5) {
                            textView.setText("当前检测为活体，当前值为=====" + attrResults[i].fConfidence);
                        } else {
                            textView.setText("当前检测为非活体，当前值为=====" + attrResults[i].fConfidence);

                        }
                    }
                }
            }
            isLiving = false;
        }
    }
}
