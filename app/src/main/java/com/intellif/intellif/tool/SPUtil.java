package com.intellif.intellif.tool;

import com.intellif.intellif.base.BaseApplication;

public class SPUtil {
    private static final String recognition_overturn_camera1 = "recognition_overturn_camera1";
    private static final String recognition_overturn_camera2 = "recognition_overturn_camera2";
    private static final String face_frame_mirror = "face_frame_mirror";
    private static final String face_frame_reverse = "face_frame_reverse";
    private static final String screen_rotate = "screen_rotate";

    public static void writeCamera1(boolean isOverturn){
        SharedPreferencesUtils.setParam(BaseApplication.getContext(), recognition_overturn_camera1,isOverturn);
    }

    public static void writeCamera2(boolean isOverturn){
        SharedPreferencesUtils.setParam(BaseApplication.getContext(), recognition_overturn_camera2,isOverturn);
    }

    public static void writeFaceFrameMirror(boolean isMirror){
        SharedPreferencesUtils.setParam(BaseApplication.getContext(), face_frame_mirror,isMirror);
    }

    public static void writeFaceFrameReverse(boolean isReverse){
        SharedPreferencesUtils.setParam(BaseApplication.getContext(), face_frame_reverse,isReverse);
    }

    public static boolean readCamera1(){
        return (boolean) SharedPreferencesUtils.getParam(BaseApplication.getContext(), recognition_overturn_camera1,false);
    }

    public static boolean readCamera2(){
        return (boolean) SharedPreferencesUtils.getParam(BaseApplication.getContext(), recognition_overturn_camera2,false);
    }

    public static boolean readFaceFrameMirror(){
        return (boolean) SharedPreferencesUtils.getParam(BaseApplication.getContext(), face_frame_mirror,false);
    }

    public static boolean readFaceFrameReverse(){
        return (boolean) SharedPreferencesUtils.getParam(BaseApplication.getContext(), face_frame_reverse,false);
    }

    public static void writeScreenRotate(int rotate){
        SharedPreferencesUtils.setParam(BaseApplication.getContext(), screen_rotate,rotate);
    }

    public static int readScreenRotate(){
        return (int) SharedPreferencesUtils.getParam(BaseApplication.getContext(), screen_rotate, 0);
    }
}
