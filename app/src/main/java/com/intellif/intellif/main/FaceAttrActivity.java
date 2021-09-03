package com.intellif.intellif.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.intellif.FaceRecAttrResult;
import com.intellif.FaceRect;
import com.intellif.FaceUtils;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;
import com.intellif.intellif.tool.Tools;

public class FaceAttrActivity extends BaseActivity {

    private ImageView imageView;
    private TextView tv_result ;
    private Bitmap faceBitmap = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detect);
        loadModel();
;
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

    public void detect(View view){
        if(faceBitmap!=null){
            FaceRect faceRect[] = com.intellif.FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle,faceBitmap);
            if(faceRect==null){
                Toast.makeText(FaceAttrActivity.this, "当前图片没有人脸", Toast.LENGTH_SHORT).show();
                tv_result.setText("当前检测到人脸数:0");

            }else {
                Bitmap bitmap1 = Tools.drawBitmap(faceRect,faceBitmap);
                imageView.setImageBitmap(bitmap1);
                //IFACEREC_GENDER  1是男 2是女
                int gender = 0;
                FaceRecAttrResult[] attrResults1 = com.intellif.FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle,faceBitmap,faceRect[0],0x1);
                if(attrResults1!=null&&attrResults1.length>0){
                    for (int i = 0; i < attrResults1.length; i++) {
                        if(attrResults1[i].IFaceRecAttrType==0){
                            gender = attrResults1[i].nValue;
                        }
                    }
                }

                //IFACEREC_AGE
                int age = 0;
                FaceRecAttrResult[] attrResults2 = com.intellif.FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle,faceBitmap,faceRect[0],0x2);
                if(attrResults2!=null&&attrResults2.length>0){
                    for (int i = 0; i < attrResults2.length; i++) {
                        if(attrResults2[i].IFaceRecAttrType==1){
                            age = attrResults2[i].nValue;
                        }
                    }
                }

                //IFACEREC_POSE
                int pitch = 0;
                int yaw = 0;
                int roll  = 0;
                FaceRecAttrResult[] attrResults3 = com.intellif.FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle,faceBitmap,faceRect[0],0x4);
                if(attrResults3!=null&&attrResults3.length>0){
                    for (int i = 0; i < attrResults3.length; i++) {
                        if(attrResults3[i].IFaceRecAttrType==2){
                            pitch = attrResults3[i].nValue;
                        }
                        if(attrResults3[i].IFaceRecAttrType==3){
                            yaw = attrResults3[i].nValue;
                        }
                        if(attrResults3[i].IFaceRecAttrType==4){
                            roll = attrResults3[i].nValue;
                        }
                    }
                }


                //IFACEREC_QUALITY
                float qualilty = 0f;
                FaceRecAttrResult[] attrResults6 = com.intellif.FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle,faceBitmap,faceRect[0],0x8);
                if(attrResults6!=null&&attrResults6.length>0){
                    for (int i = 0; i < attrResults6.length; i++) {
                        if(attrResults6[i].IFaceRecAttrType==5){
                            qualilty = attrResults6[i].fConfidence;
                        }
                    }
                }

               // IFACEREC_GLASSES 0为不带眼镜 1为戴眼镜
                int glass = 0;
                FaceRecAttrResult[] attrResults7 = com.intellif.FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle,faceBitmap,faceRect[0],0x20);
                if(attrResults7!=null&&attrResults7.length>0){
                    for (int i = 0; i < attrResults7.length; i++) {
                        if(attrResults7[i].IFaceRecAttrType==6){
                            glass = attrResults7[i].nValue;
                        }
                    }
                }

                //IFACEREC_HAT 0为不带帽  1为戴帽
                int hat = 0;
                FaceRecAttrResult[] attrResults8 = com.intellif.FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle,faceBitmap,faceRect[0],0x10);
                if(attrResults8!=null&&attrResults8.length>0){
                    for (int i = 0; i < attrResults8.length; i++) {
                        if(attrResults8[i].IFaceRecAttrType==7){
                            hat = attrResults8[i].nValue;
                        }
                    }
                }

                //检测是否是黑白
                String imageColor = "";
                FaceRecAttrResult[] attrResults9 = FaceUtils.getInstance().IFaceRecSDK_AttributePredict(predictorHandle,faceBitmap,faceRect[0],0x1000);
                if(attrResults9!=null&&attrResults9.length>0){
                    for (int i = 0; i < attrResults9.length; i++) {
                        if(attrResults9[i].IFaceRecAttrType==14){
                            if(attrResults9[i].fConfidence >= 0.5){
                                imageColor = "非黑白";
                            }else{
                                imageColor = "黑白";
                            }
                        }
                    }
                }

                String sex = "未知";
                String mao = "未知";
                String yan = "未知";
                if(gender==1){sex= "男"; }else { sex= "女";}
                if(hat==1){mao= "戴帽"; }else { mao= "不带帽";}
                if(glass==1){yan= "戴眼镜"; }else { yan= "不带眼镜";}
                tv_result.setText("性别："+sex+"年龄："+age+" 以x轴为中心,脸部上下俯仰角度："+pitch+"以y轴为中心,脸部左右旋转角度："+yaw+"以中心点为中心，x-y平面旋转角度："+roll+"人脸质量："+qualilty+"帽子："+mao+"眼镜："+yan+" 是否是黑白："+imageColor);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = resultImagePicker(resultCode, data);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            faceBitmap = bitmap;
        } else {
            Toast.makeText(this, "选取照片失败，请重新选取", Toast.LENGTH_SHORT).show();
        }
    }
}
