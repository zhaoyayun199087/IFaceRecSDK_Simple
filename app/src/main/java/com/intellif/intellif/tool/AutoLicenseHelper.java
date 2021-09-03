package com.intellif.intellif.tool;


import com.anruxe.downloadlicense.HttpDownload;

public class AutoLicenseHelper {

    public static void getLisenseFile(String licensePath,final String apiKey, final String apiSecret, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int error_code = HttpDownload.getInstance().getLisenseFile(licensePath, apiKey, apiSecret);
                    listener.onFinish(error_code);
                } catch (Exception e) {
                    listener.onError(e);
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public interface HttpCallbackListener{
        void onFinish(int errCode);
        void onError(Exception e);
    }

}
