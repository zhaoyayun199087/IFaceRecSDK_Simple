package com.intellif.intellif.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.intellif.FaceRect;
import com.intellif.FaceUtils;
import com.intellif.IFaceRecSearchResult;
import com.intellif.intellif.R;
import com.intellif.intellif.base.BaseActivity;

public class TestActivity extends BaseActivity {

    private ImageView imageView;
    private EditText et_result;
    private TextView tv_label_total, tv_search_result;
    private Bitmap faceBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        loadModel();
        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.img);
        et_result = findViewById(R.id.et_result);
        tv_label_total = findViewById(R.id.tv_label_total);
        tv_search_result = findViewById(R.id.tv_search_result);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker(IMAGE_PICKER_ONE);
            }
        });
    }

    /**
     * 入库
     *
     * @param view
     */
    public void detect(View view) {
        if (faceBitmap != null) {
            String idStr = et_result.getText().toString().trim();
            if (idStr.isEmpty()) {
                Toast.makeText(this, "请输入ID", Toast.LENGTH_SHORT).show();
                return;
            }
            int id = 0;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "ID号输入有误,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            FaceRect faceRect[] = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle, faceBitmap);
            int result = -100;
            if (faceRect != null && faceRect.length > 0) {
                result = FaceUtils.getInstance().IFaceRecSDK_FaceInsert(searchHandle, faceBitmap, faceRect[0], id);
            }
            if (result == 0) {
                Toast.makeText(this, "入库成功", Toast.LENGTH_SHORT).show();
            } else if (result == -100) {
                Toast.makeText(this, "图片无人脸", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "入库失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 删库
     */
    public void delect(View view) {
        String idStr = et_result.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "请输入ID", Toast.LENGTH_SHORT).show();
            return;
        }
        int id = 0;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "ID号输入有误,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        int result = FaceUtils.getInstance().IFaceRecSDK_FaceDelete(searchHandle, id);
        if (result == 0) {
            Toast.makeText(this, "删库成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "删库失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 清空
     */
    public void clear(View view) {
        int result = FaceUtils.getInstance().IFaceRecSDK_FaceClear(searchHandle);
        if (result == 0) {
            Toast.makeText(this, "清空数据库成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "清空数据库失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchAll(View view) {
        if (faceBitmap != null) {
            FaceRect faceRect[] = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle, faceBitmap);
            if (faceRect != null && faceRect.length > 0) {
                IFaceRecSearchResult[] results = FaceUtils.getInstance().IFaceRecSDK_FaceSearch(searchHandle, faceBitmap, faceRect[0], 0.0f);
                if (results != null) {
                    tv_label_total.setText("总人数: " + results.length);
                } else {
                    tv_label_total.setText("总人数: 0");
                }
            } else {
                Toast.makeText(this, "图片无人脸", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 搜索
     *
     * @param view
     */
    public void search(View view) {
        if (faceBitmap != null) {
            FaceRect faceRect[] = FaceUtils.getInstance().IFaceRecSDK_Face_Detect(detectHandle, faceBitmap);
            if (faceRect == null || faceRect.length <= 0) {
                Toast.makeText(this, "图片无人脸", Toast.LENGTH_SHORT).show();
                return;
            }
            IFaceRecSearchResult[] results = FaceUtils.getInstance().IFaceRecSDK_FaceSearch(searchHandle, faceBitmap, faceRect[0], 0.0f);
            if (results != null) {
                tv_search_result.setText("搜索结果: " + results.length + "\n");
                for (IFaceRecSearchResult result : results) {
                    tv_search_result.append("id:" + result.dId + " , score: " + result.fScore + "\n");
                }
            } else {
                tv_search_result.setText("搜索结果: 0 \n");
                Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
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
