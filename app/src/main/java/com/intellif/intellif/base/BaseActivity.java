package com.intellif.intellif.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.intellif.FaceUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {
    public static final String MODEL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/model";
    public static final String LICENSE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/license";


    protected static final int IMAGE_PICKER_ONE = 100;
    protected static final int IMAGE_PICKER_TWO = 200;

    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }



    public int detectHandle = 0;
    public int extractHandle = 0;
    public int compareHandle = 0;
    public int predictorHandle = 0;
    public int captureHandle = 0;
    public int searchHandle = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onPause() {
        isActive = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void  loadModel(){
       int result = FaceUtils.getInstance().IFaceRecSDK_Init(LICENSE_PATH);
       String license = FaceUtils.getInstance().IFaceRecSDK_GetSDKInfo();
       Log.e("TAG", "result "+result);
       Log.e("TAG", "license "+license);
       initHandle();
    }

    public void initHandle(){
        detectHandle = FaceUtils.getInstance().IFaceRecSDK_Detect_Create(MODEL_PATH,48);
        extractHandle = FaceUtils.getInstance().IFaceRecSDK_Extract_Create(MODEL_PATH);
        compareHandle = FaceUtils.getInstance().IFaceRecSDK_Compare_Create(MODEL_PATH);
        predictorHandle = FaceUtils.getInstance().IFaceRecSDK_Predictor_Create(MODEL_PATH);
        captureHandle = FaceUtils.getInstance().IFaceRecSDK_CapturerCreate(MODEL_PATH);
        searchHandle = FaceUtils.getInstance().IFaceRecSDK_Search_Create(MODEL_PATH);
    }

    public void destoryModel(){
        detectHandle = FaceUtils.getInstance().IFaceRecSDK_Detect_Destory(detectHandle);
        extractHandle = FaceUtils.getInstance().IFaceRecSDK_Extract_Destory(extractHandle);
        compareHandle = FaceUtils.getInstance().IFaceRecSDK_Compare_Destory(compareHandle);
        predictorHandle = FaceUtils.getInstance().IFaceRecSDK_Predictor_Destory(predictorHandle);
        captureHandle = FaceUtils.getInstance().IFaceRecSDK_CapturerDestroy(captureHandle);
        searchHandle = FaceUtils.getInstance().IFaceRecSDK_Search_Destory(searchHandle);
        FaceUtils.getInstance().IFaceRecSDK_UnInit();
    }

    protected void imagePicker(int code){
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, code);
    }

    protected Bitmap resultImagePicker(int resultCode, Intent data){
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    Bitmap bitmap = BitmapFactory.decodeFile(images.get(0).path);
                    return bitmap;
                }
            }
        }
        return null;
    }

    public void showShortToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }
}
