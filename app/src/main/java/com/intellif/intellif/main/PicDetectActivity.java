package com.intellif.intellif.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anruxe.downloadlicense.FileUtils;
import com.intellif.FaceRect;
import com.intellif.FaceUtils;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;
import com.intellif.intellif.tool.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicDetectActivity extends BaseActivity {

    private ImageView imageView;
    private TextView tv_result;
    private Bitmap faceBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detect);
        loadModel();
        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.img);
        tv_result = findViewById(R.id.tv_result);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker(IMAGE_PICKER_ONE);
            }
        });
    }

    public void detect(View view) {

        String path = "/sdcard/微看云天励飞样本";
        File file = new File(path);
        if( file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for (int iFileLength = 0; iFileLength < files.length; iFileLength++) {
                // 判断是否为文件夹
                if (!files[iFileLength].isDirectory()) {
                    String filename = files[iFileLength].getAbsolutePath();
                    Log.e("eee","文件名 ： " + filename);
                    faceBitmap = BitmapFactory.decodeFile(filename);
                    if (faceBitmap != null) {
                        FaceRect faceRect[] = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle, faceBitmap);
                        if (faceRect == null) {
                            Toast.makeText(PicDetectActivity.this, "当前图片没有人脸", Toast.LENGTH_SHORT).show();
                            tv_result.setText("当前检测到人脸数:0");
                        } else {
                            Bitmap bitmap1 = Tools.drawBitmap(faceRect, faceBitmap);
                            imageView.setImageBitmap(bitmap1);
                            saveImageToGallery(bitmap1, files[iFileLength].getName());
                            tv_result.setText("当前检测到人脸数:" + faceRect.length);
                        }
                    }
                }
            }
        }

//        if (faceBitmap != null) {
//            FaceRect faceRect[] = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle, faceBitmap);
//            if (faceRect == null) {
//                Toast.makeText(PicDetectActivity.this, "当前图片没有人脸", Toast.LENGTH_SHORT).show();
//                tv_result.setText("当前检测到人脸数:0");
//            } else {
//                Bitmap bitmap1 = Tools.drawBitmap(faceRect, faceBitmap);
//                imageView.setImageBitmap(bitmap1);
//                tv_result.setText("当前检测到人脸数:" + faceRect.length);
//            }
//        }
    }
    public int saveImageToGallery(Bitmap bmp, String name) {
        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "pic";
        File appDir = new File(root , dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        //获取文件
        File file = new File(appDir, name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            PicDetectActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            return 2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = resultImagePicker(resultCode, data);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            faceBitmap = bitmap;
        }else{
            Toast.makeText(this, "选取照片失败，请重新选取", Toast.LENGTH_SHORT).show();
        }
    }
}
