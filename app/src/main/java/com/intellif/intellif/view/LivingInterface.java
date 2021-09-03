package com.intellif.intellif.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;

import com.intellif.intellif.tool.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LivingInterface implements Camera.PreviewCallback{
	private static final String TAG = LivingInterface.class.getSimpleName();
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static final int REQUEST_CAMERA = 2;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE};
	private Context mContext;
	private Camera mCamera;
	private Camera.Parameters mParams;
	private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
	private Activity mActivity;
	private int mHandle = 0;
	private static LivingInterface mCameraInterface;
	private int mDisplayRotation;
	private int mDisplayOrientation;
	private int previewWidth;
	private int previewHeight;
	private int prevSettingWidth;
	private int prevSettingHeight;
	private int ratio=1280;
	private LivingListener livingListener;
	private LivingInterface(){
	}
	public static synchronized LivingInterface getInstance(){
		if(mCameraInterface == null){
			mCameraInterface = new LivingInterface();
		}
		return mCameraInterface;
	}
	public void init(Activity activity){
		this.mActivity = activity;
	}
	public void initRatio(int ratio){
		this.ratio = ratio;
	}
	public void setLivingCallBack(LivingListener listener){
		this.livingListener = listener;
	}
	public void changeCamera(int cameraId,SurfaceHolder holder) throws IOException {
		mCamera.stopPreview();
		mCamera.release();
		mCamera.stopPreview();//停掉原来摄像头的预览
		mCamera.release();//释放资源
		mCamera = null;//取消原来摄像头
		mCamera = Camera.open(cameraId);//打开当前选中的摄像头
		try {
			mCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.startPreview();//开始预览
	}
	private void saveToSDCard(String name, int resId) throws Throwable {
		InputStream inStream = mActivity.getApplication().getResources().openRawResource(resId);
		File file = new File(Environment.getExternalStorageDirectory(), name);
		FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
		byte[] buffer = new byte[10];
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int len = 0;
		while((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] bs = outStream.toByteArray();
		fileOutputStream.write(bs);
		outStream.close();
		inStream.close();
		fileOutputStream.flush();
		fileOutputStream.close();
	}
	public void openCamera(int cameraId, SurfaceHolder surfaceHolder){
		try {
			this.mCameraId = cameraId;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			mCamera = Camera.open(cameraId);
			Camera.getCameraInfo(cameraId, cameraInfo);
			mCamera.setPreviewDisplay(surfaceHolder);
		} catch (Exception e) {
			Log.e(TAG, "Could not preview the image.", e);
		}
	}
	public void startPreview(SurfaceHolder surfaceHolder, int width, int height){
		if (surfaceHolder.getSurface() == null) {
			return;
		}
		try {
			mCamera.stopPreview();

			configureCamera(width, height);
			setDisplayOrientation();
			startPreview();
		} catch (Exception e) {
		}
	}
	// 停止预览，释放Camera
	public void stopCamera(){
		if(null != mCamera)
		{
			mCamera.setPreviewCallbackWithBuffer(null);
			mCamera.setErrorCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
	//获取Camera.Parameters
	public Camera.Parameters getCameraParams(){
		if(mCamera != null){
			mParams = mCamera.getParameters();
			return mParams;
		}
		return null;
	}
	public Camera getCameraDevice(){
		return mCamera;
	}
	public int getCameraId(){
		return mCameraId;
	}
	private void configureCamera(int width, int height) {
		Camera.Parameters parameters = mCamera.getParameters();
		// Set the PreviewSize and AutoFocus:..
		setOptimalPreviewSize(parameters, width, height);
		setAutoFocus(parameters);
//		parameters.setZoom(10);
		// And set the parameters:
		mCamera.setParameters(parameters);
	}
	private void setOptimalPreviewSize(Camera.Parameters cameraParameters, int width, int height) {
		previewWidth=640;
		previewHeight=480;
		Log.e(TAG, "previewWidth-->"+previewWidth);
		Log.e(TAG, "previewHeight-->"+previewHeight);
		cameraParameters.setPreviewSize(previewWidth,previewHeight);
	}
	//设置聚焦
	private void setAutoFocus(Camera.Parameters cameraParameters) {
		List<String> focusModes = cameraParameters.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
			cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
	}
	private void setDisplayOrientation() {
		// Now set the display orientation:
		mDisplayRotation = MatrixYuvUtils.getDisplayRotation(mActivity);
		if(Constants.select_screen_rotate == 0){
			mDisplayOrientation = MatrixYuvUtils.getDisplayOrientation(mDisplayRotation, mCameraId);
		}else{
			mDisplayOrientation = Constants.SCREEN_ROTATE[Constants.select_screen_rotate];
		}
		mCamera.setDisplayOrientation(mDisplayOrientation);
	}
	private void startPreview() {
		if (mCamera != null) {
			mCamera.startPreview();
			mCamera.setPreviewCallback(this);
		}
	}
	@Override
	public void onPreviewFrame(final byte[] _data, Camera camera) {
		if(Constants.recognition_overturn_camera2 || mCameraId==Camera.CameraInfo.CAMERA_FACING_BACK){
			if(mDisplayOrientation==0){
				livingListener.livingData(_data,previewWidth,previewHeight);
			}else if(mDisplayOrientation==90){
				livingListener.livingData(MatrixYuvUtils.rotateYUV420Degree90(_data,previewWidth,previewHeight),previewHeight,previewWidth);
			}else  if(mDisplayOrientation==180){
				livingListener.livingData(MatrixYuvUtils.rotateYUV420Degree180(_data,previewWidth,previewHeight),previewWidth,previewHeight);
			}else{
				livingListener.livingData(MatrixYuvUtils.rotateYUV420Degree270(_data,previewWidth,previewHeight),previewHeight,previewWidth);
			}
		}else if(Constants.recognition_overturn_camera1 || mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
			if(mDisplayOrientation==0){
				livingListener.livingData(MatrixYuvUtils.rotateYUV420Degree180(_data,previewWidth,previewHeight),previewWidth,previewHeight);
			}else if(mDisplayOrientation==90){
				livingListener.livingData(MatrixYuvUtils.rotateYUV420Degree270(_data,previewWidth,previewHeight),previewHeight,previewWidth);
			}else  if(mDisplayOrientation==180){
				livingListener.livingData(_data,previewWidth,previewHeight);
			}else{
				livingListener.livingData(MatrixYuvUtils.rotateYUV420Degree90(_data,previewWidth,previewHeight),previewHeight,previewWidth);
			}
		}
		Log.i(TAG, "cameraID: "+ mCameraId+" , mDisplayOrientation: "+mDisplayOrientation);
	}
}
