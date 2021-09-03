package com.intellif.intellif.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.intellif.intellif.R;
import com.intellif.intellif.SettingActivity;
import com.intellif.intellif.tool.Constants;
import com.intellif.intellif.tool.GlideImageLoader;
import com.intellif.intellif.tool.SPUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_test,bt_pic_detect,bt_video_detect,bt_compare_pic,bt_compare_video,bt_detect_living,bt_face_lib,bt_face_more,bt_face_attr,bt_living_ir;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initSetting();
        requestPermission();
        initImagePicker();
    }


    private void initView() {
        bt_pic_detect = findViewById(R.id.bt_pic_detect);
        bt_video_detect = findViewById(R.id.bt_video_detect);
        bt_compare_pic = findViewById(R.id.bt_compare_pic);
        bt_compare_video = findViewById(R.id.bt_compare_video);
        bt_face_attr = findViewById(R.id.bt_face_attr);
        bt_detect_living = findViewById(R.id.bt_detect_living);
        bt_face_lib = findViewById(R.id.bt_face_lib);
        bt_face_more = findViewById(R.id.bt_face_more);
        bt_living_ir = findViewById(R.id.bt_living_ir);
        bt_test = findViewById(R.id.bt_test);
    }

    private void initEvent() {
        bt_pic_detect.setOnClickListener(this);
        bt_video_detect.setOnClickListener(this);
        bt_compare_pic.setOnClickListener(this);
        bt_compare_video.setOnClickListener(this);
        bt_detect_living.setOnClickListener(this);
        bt_face_lib.setOnClickListener(this);
        bt_face_more.setOnClickListener(this);
        bt_face_attr.setOnClickListener(this);
        bt_living_ir.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bt_pic_detect){
            Intent intent = new Intent(MainActivity.this,PicDetectActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_video_detect){
            Intent intent = new Intent(MainActivity.this,VideoDetectActvity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_compare_pic){
            Intent intent = new Intent(MainActivity.this,PicCompareActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_compare_video){
            Intent intent = new Intent(MainActivity.this,VideoCompareActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_detect_living){
            Intent intent = new Intent(MainActivity.this,LivingDetectActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_face_attr){
            Intent intent = new Intent(MainActivity.this,FaceAttrActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_face_lib){
            Intent intent = new Intent(MainActivity.this,DBActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_face_more){
            Intent intent = new Intent(MainActivity.this,VideoSearchActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_living_ir){
            Intent intent = new Intent(MainActivity.this,InfraredActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.bt_test){
            Intent intent = new Intent(MainActivity.this,InfraredActivity.class);
            startActivity(intent);
        }
    }

    private void initSetting(){
        Constants.recognition_overturn_camera1 = SPUtil.readCamera1();
        Constants.recognition_overturn_camera2 = SPUtil.readCamera2();
        Constants.face_frame_mirror = SPUtil.readFaceFrameMirror();
        Constants.face_frame_reverse = SPUtil.readFaceFrameReverse();
        Constants.select_screen_rotate = SPUtil.readScreenRotate();
    }

    public void setting(View view) {
        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(intent);
    }

    private void requestPermission(){
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if(!granted){
                            Toast.makeText(MainActivity.this, "请同意所需权限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }

    private void initImagePicker(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);
        //imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }
}
