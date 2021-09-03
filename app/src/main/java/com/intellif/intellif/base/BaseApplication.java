package com.intellif.intellif.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.intellif.intellif.tool.AutoLicenseHelper;

import java.io.File;

public class BaseApplication extends Application {

    private static Context context;

    //在线获取授权
    private final String apiKey = "w4bOMag1qRYdmdZJ";
    private final String apiSecret = "Qm6bG4ALPNZjAJ4uRT8kxRpGPWTkGz";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        //使用在线授权
        File file = new File(BaseActivity.LICENSE_PATH+"/license");
        if(!file.exists()){
            AutoLicenseHelper.getLisenseFile(BaseActivity.LICENSE_PATH, apiKey, apiSecret,
                    new AutoLicenseHelper.HttpCallbackListener() {
                        @Override
                        public void onFinish(int errCode) {
                            Log.e("TAG", "errCode:"+errCode);
                            if(errCode == 200){
                                Log.e("TAG", "下载成功");
                            }else{
                                Log.e("TAG", "下载失败");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            Log.e("TAG", "下载失败，发生错误");
                        }
                    });
        }
    }

    public static Context getContext(){
        return context;
    }

}
