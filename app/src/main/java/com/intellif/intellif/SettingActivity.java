package com.intellif.intellif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.intellif.intellif.tool.Constants;
import com.intellif.intellif.tool.SPUtil;

public class SettingActivity extends AppCompatActivity {

    private Switch switch_recognition_camera1, switch_recognition_camera2, switch_mirroring, switch_reverse;
    private Spinner spinner_rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initViews(){
        switch_recognition_camera1 = findViewById(R.id.switch_recognition_camera1);
        switch_recognition_camera2 = findViewById(R.id.switch_recognition_camera2);
        switch_mirroring = findViewById(R.id.switch_mirroring);
        switch_reverse = findViewById(R.id.switch_reverse);
        spinner_rotate = findViewById(R.id.spinner_rotate);
    }

    private void initDatas(){
        switch_recognition_camera1.setChecked(Constants.recognition_overturn_camera1);
        switch_recognition_camera2.setChecked(Constants.recognition_overturn_camera2);
        switch_mirroring.setChecked(Constants.face_frame_mirror);
        switch_reverse.setChecked(Constants.face_frame_reverse);
        spinner_rotate.setSelection(Constants.select_screen_rotate);

        switch_recognition_camera1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.recognition_overturn_camera1 = isChecked;
                SPUtil.writeCamera1(isChecked);
            }
        });
        switch_recognition_camera2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.recognition_overturn_camera2 = isChecked;
                SPUtil.writeCamera2(isChecked);
            }
        });
        switch_mirroring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.face_frame_mirror = isChecked;
                SPUtil.writeFaceFrameMirror(isChecked);
            }
        });
        switch_reverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.face_frame_reverse = isChecked;
                SPUtil.writeFaceFrameReverse(isChecked);
            }
        });

        spinner_rotate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constants.select_screen_rotate = position;
                SPUtil.writeScreenRotate(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
