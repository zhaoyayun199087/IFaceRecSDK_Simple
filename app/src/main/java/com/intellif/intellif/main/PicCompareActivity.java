package com.intellif.intellif.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.intellif.FaceRect;
import com.intellif.FaceUtils;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;
import com.intellif.intellif.tool.Tools;

import java.io.File;

public class PicCompareActivity extends BaseActivity {

    private ImageView imageView;
    private ImageView imageView2;
    private Bitmap faceBitmap1 = null;
    private Bitmap faceBitmap2 = null;
    private TextView tv_result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_compare);

        loadModel();
        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.img);
        imageView2 = findViewById(R.id.img2);
        tv_result = findViewById(R.id.tv_result);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker(IMAGE_PICKER_ONE);

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker(IMAGE_PICKER_TWO);
            }
        });
    }

    public void detect(View view){

//        String path = "/sdcard/微看云天励飞样本";
//        File file = new File(path);
//        if( file.exists() && file.isDirectory()){
//            File[] files = file.listFiles();
//            for (int iFileLength = 0; iFileLength < files.length; iFileLength++) {
//                // 判断是否为文件夹
//                if (!files[iFileLength].isDirectory()) {
//                    String filename = files[iFileLength].getAbsolutePath();
//                    Log.e("eee","文件名 ： " + filename);
//                    if( filename.contains("1人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("1抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("2人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("2抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("3人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("3抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("4人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("4抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("5人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("5抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("6人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("6抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("7人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("7抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("8人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("8抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("9人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("9抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    if( filename.contains("10人脸") ) {
//                        faceBitmap1 = BitmapFactory.decodeFile(filename);
//                    }
//                    if( filename.contains("10抓拍") ) {
//                        faceBitmap2 = BitmapFactory.decodeFile(filename);
//                        if(faceBitmap1!=null&&faceBitmap2!=null){
//                            long time1 = System.currentTimeMillis();
//                            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
//                            long time2 = System.currentTimeMillis();
//                            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
//                            Long time3 = System.currentTimeMillis();
//                            if(faceRect1==null){
//                                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if(faceRect2==null){
//                                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
//                            long time4 = System.currentTimeMillis();
//                            Log.e("eee", filename+ "人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                            tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
//                        }else {
//                            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }
//            }
//        }


        if(faceBitmap1!=null&&faceBitmap2!=null){
            long time1 = System.currentTimeMillis();
            FaceRect[] faceRect1 = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap1);
            long time2 = System.currentTimeMillis();
            FaceRect[] faceRect2= FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap2);
            Long time3 = System.currentTimeMillis();
            if(faceRect1==null){
                Toast.makeText(this, "第一张图未能检测到人脸", Toast.LENGTH_SHORT).show();
                return;
            }
            if(faceRect2==null){
                Toast.makeText(this, "第二张图未能检测到人脸", Toast.LENGTH_SHORT).show();
                return;
            }
            float confidence = FaceUtils.getInstance().IFaceRecSDK_FaceCompare(compareHandle,faceBitmap1,faceRect1[0],faceBitmap2,faceRect2[0]);
            long time4 = System.currentTimeMillis();
                tv_result.setText("人脸对比相似度为："+confidence + " time " + (time2-time1) + "/" + (time3-time2) + "/" + (time4-time3));
        }else {
            Toast.makeText(this, "请上传人脸", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = resultImagePicker(resultCode, data);
        if(bitmap == null){
            Toast.makeText(this, "选取照片失败，请重新选取", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode){
            case IMAGE_PICKER_ONE:
                imageView.setImageBitmap(bitmap);
                faceBitmap1 = bitmap;
                break;
            case IMAGE_PICKER_TWO:
                imageView2.setImageBitmap(bitmap);
                faceBitmap2 = bitmap;
                break;
        }
    }
}
