package com.intellif.intellif.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.intellif.FaceRect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 人脸相关处理
 * Created by intellif on 2017/10/17.
 */
public class Tools {
    //    /** 在bitmap上画图（人脸框） */
    public static Bitmap drawBitmap(FaceRect faceRect, Bitmap previewBitmap) {
        if (previewBitmap == null)
            return null;
        int w = previewBitmap.getWidth();
        int h = previewBitmap.getHeight();
        Bitmap newb = previewBitmap.copy(Bitmap.Config.ARGB_8888, true);//创建一个新的位图
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        paint.setStrokeWidth(4);
//        // 画人脸框
//        if (faceRectEntity.getFaceRect() != null && faceRectEntity.getFaceNum() > 0) {
        paint.setColor(Color.GREEN);
        paint.setTextSize(32);
        paint.setStyle(Paint.Style.STROKE);
//            for (int i = 0; i < faceRectEntity.getFaceNum(); i++) {
        cv.drawRect(faceRect.dRectLeft, faceRect.dRectTop, faceRect.dRectRight, faceRect.dRectBottom, paint);
//            }
//        }
        cv.save();
        cv.restore();
        return newb;
    }
    public static Bitmap drawBitmap(FaceRect[] faceRect, Bitmap previewBitmap) {
        if (previewBitmap == null)
            return null;
        int w = previewBitmap.getWidth();
        int h = previewBitmap.getHeight();
        Bitmap newb = previewBitmap.copy(Bitmap.Config.ARGB_8888, true);//创建一个新的位图
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        paint.setStrokeWidth(4);
//        // 画人脸框
//        if (faceRectEntity.getFaceRect() != null && faceRectEntity.getFaceNum() > 0) {
        paint.setColor(Color.GREEN);
        paint.setTextSize(32);
        paint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < faceRect.length; i++) {
        cv.drawRect(faceRect[i].dRectLeft, faceRect[i].dRectTop, faceRect[i].dRectRight, faceRect[i].dRectBottom, paint);
            }
//        }
        cv.save();
        cv.restore();
        return newb;
    }
    /** 旋转Bitmap图像指定角度 */
    public static Bitmap rotateImage(Bitmap srcImage, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);// matrix.postRotate使图片实现反转
        return Bitmap.createBitmap(srcImage, 0, 0, srcImage.getWidth(), srcImage.getHeight(), matrix, true);
    }
    /** jpeg图像裁剪 */
    public static Bitmap getFaceCutImage(byte[] jpegImage, FaceRect faceRect) {
        if (jpegImage == null)
            return null;
        Bitmap imageBmp = BitmapFactory.decodeStream(new ByteArrayInputStream(jpegImage));
        return cutBitmap(imageBmp, faceRect);
    }  /** jpeg图像裁剪 */
    public static Bitmap FaceCutImage(Bitmap jpegImage, FaceRect faceRect) {

        return Bitmap.createBitmap(jpegImage, faceRect.dRectLeft, faceRect.dRectTop, jpegImage.getWidth(), jpegImage.getHeight(),null,false);
    }
    /** YUV图像裁剪 */
    private Bitmap getFaceCutImage(byte[] YUVImage, int width, int height, FaceRect faceRect) {
        if (YUVImage == null || faceRect == null)
            return null;
        YuvImage image = new YuvImage(YUVImage, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compressToJpeg(new Rect(0, 0, width, height), 80, stream);
        Bitmap imageBmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cutBitmap(imageBmp, faceRect);
    }
    /** bitmap图像的裁剪 */
    public static Bitmap cutBitmap(Bitmap imageBmp, FaceRect faceRect) {
        if (imageBmp == null || faceRect == null)
            return null;
        int left = faceRect.dRectLeft;
        int top = faceRect.dRectTop;
        int right = faceRect.dRectRight;
        int bottom = faceRect.dRectBottom;
			/* 图像旋转 */
        /*if (MyApplication.previewRotationAngle == 180) {
            imageBmp = rotateImage(imageBmp, MyApplication.previewRotationAngle);
        }*/
			/* 估算出裁剪的区域 */
        int startLeft = left - (int) ((right - left) * 0.3);
        int startTop = top - (int) ((bottom - top) * 1);
        int width = (int) ((right - left) * 1.6);
        int height = (int) ((bottom - top) * 2.2);
			/* 对估算的边界超出范围的容错处理 */
        startLeft = startLeft < 0 ? 0 : startLeft;
        startTop = startTop < 0 ? 0 : startTop;
        width = (width + startLeft > imageBmp.getWidth()) ? imageBmp.getWidth() - startLeft : width;
        height = (height + startTop > imageBmp.getHeight()) ? imageBmp.getHeight() - startTop : height;
        Log.e("aaaa",width+"");
        Log.e("aaaa",height+"");
        Log.e("aaaa",startLeft+"");
        Log.e("aaaa",startTop+"");
        return Bitmap.createBitmap(imageBmp, startLeft, startTop, width, height);
    }
    /**
     * uri 转 bitmap
     * @param uri
     * @return
     */
    public static Bitmap getBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * uri 转 byte[]
     * @param uri
     * @return
     */
    public static byte[] getJpegData(Context context, Uri uri) {
        Bitmap bitmap = null;
        bitmap = getBitmap(context,uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    /**
     * 获得人脸比对的rgb图片数组
     * @param bitmap
     * @return
     */
    public static byte[] getRGBByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        byte[] data = convertColorToByte(pixels);
        return data;
    }
    /*
 * 像素数组转化为RGB数组
 */
    public static byte[] convertColorToByte(int color[]) {
        if (color == null) {
            return null;
        }
        byte[] data = new byte[color.length * 3];
        for (int i = 0; i < color.length; i++) {
            data[i * 3] = (byte) (color[i] & 0xff);
            data[i * 3 + 1] = (byte) (color[i] >> 8 & 0xff);
            data[i * 3 + 2] = (byte) (color[i]>> 16 & 0xff);
        }
        return data;
    }

    /**
     * @方法描述 Bitmap转RGB
     */
    public static byte[] bitmap2RGB(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();  //返回可用于储存此位图像素的最小字节数

        ByteBuffer buffer = ByteBuffer.allocate(bytes); //  使用allocate()静态方法创建字节缓冲区
        bitmap.copyPixelsToBuffer(buffer); // 将位图的像素复制到指定的缓冲区

        byte[] rgba = buffer.array();
        byte[] pixels = new byte[(rgba.length / 4) * 3];

        int count = rgba.length / 4;

        //Bitmap像素点的色彩通道排列顺序是RGBA
        for (int i = 0; i < count; i++) {

            pixels[i * 3] = rgba[i * 4];        //R
            pixels[i * 3 + 1] = rgba[i * 4 + 1];    //G
            pixels[i * 3 + 2] = rgba[i * 4 + 2];       //B

        }

        return pixels;
    }

    public static FaceRect getMaxFace(FaceRect[] faceRects){
        int maxIndex = 0;
        int maxHeight = faceRects[0].dRectBottom - faceRects[0].dRectTop;
        for (int i = 1; i < faceRects.length; i++) {
            int height = faceRects[i].dRectBottom - faceRects[i].dRectTop;
            if (height > maxHeight) {
                maxHeight = height;
                maxIndex = i;
            }
        }
        return faceRects[maxIndex];
    }
}