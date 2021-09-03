package com.intellif.intellif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.intellif.FaceRect;
import com.intellif.intellif.tool.Constants;

public class FaceView extends View {
	private static final String TAG = FaceView.class.getSimpleName();
	private Context mContext;
	private Paint mLinePaint;
	private Paint mTextPaint;
	private FaceRect[] mFaces;
	private RectF mRect = new RectF();
	private Preferences preferences; //获得保存的摄像头ID
	private int cmeraId; //前后摄像头对应id
	private int height; //图片宽度
	private int width; //图片高度
	private int view_width; //控件宽度
	private int view_height; //控件高度

	private int rectF_left;
	private int rectF_right;
	private int rectF_top;
	private int rectF_bottom;

	private int rectF_buffer;
	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initPaint();
		mContext = context;
		preferences = new Preferences(context, Preferences.SETTING);
		cmeraId = preferences.getCameraId();
	}

	/**
	 * 设置宽高
	 * @param faces
	 * @param width
	 * @param height
	 * @param view_width
	 * @param view_height
	 */
	public void setFaces(FaceRect[] faces, int width, int height, int view_width, int view_height){
		this.mFaces = faces;
		this.width = width;
		this.height = height;
		this.view_width = view_width;
		this.view_height = view_height;
		invalidate();
	}
	public void setFaces(FaceRect[] faces){
		this.mFaces = faces;
		invalidate();
	}
	public void clearFaces(){
		mFaces = null;
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// TODO Auto-generated method stub
		if(mFaces == null || mFaces.length < 1){
			return;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(-1, 1);
		canvas.save();
		canvas.rotate(-0);   //Canvas.rotate()默认是逆时针
		for(int i = 0; i< mFaces.length; i++){

			if(!Constants.face_frame_mirror){
				if(cmeraId == Camera.CameraInfo.CAMERA_FACING_BACK){
					rectF_left = mFaces[i].dRectLeft;
					rectF_right = mFaces[i].dRectRight;
				}else{
					rectF_left = width - mFaces[i].dRectRight;
					rectF_right = width - mFaces[i].dRectLeft;
				}
			}else{
				if(cmeraId == Camera.CameraInfo.CAMERA_FACING_BACK){
					rectF_left = width - mFaces[i].dRectRight;
					rectF_right = width - mFaces[i].dRectLeft;
				}else{
					rectF_left = mFaces[i].dRectLeft;
					rectF_right = mFaces[i].dRectRight;
				}
			}
			if(Constants.face_frame_reverse){
				rectF_buffer = rectF_left;
				rectF_left = rectF_right;
				rectF_right = rectF_buffer;
			}

			rectF_top = mFaces[i].dRectTop;
			rectF_bottom = mFaces[i].dRectBottom;
			mRect.set((rectF_left) * view_width / width,
					(rectF_top) * view_height / height,
					(rectF_right) * view_width / width,
					(rectF_bottom) * view_height / height);
			canvas.drawRect(mRect, mLinePaint);
		}
		canvas.restore();
	}

	private void initPaint(){
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int stroke = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, metrics);
		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setDither(true);
		mLinePaint.setColor(Color.GREEN);
		mLinePaint.setStrokeWidth(stroke);
		mLinePaint.setStyle(Style.STROKE);
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setDither(true);
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, metrics);
		mTextPaint.setTextSize(size);
		mTextPaint.setColor(Color.RED);
		mTextPaint.setStyle(Style.FILL);
	}

}
