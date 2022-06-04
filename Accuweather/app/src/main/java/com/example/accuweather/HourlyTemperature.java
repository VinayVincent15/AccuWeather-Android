package com.example.accuweather;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Locale;



public class HourlyTemperature extends Service{
    boolean state = true;
    boolean threadState = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(state){
            state = false;
            new Thread(
                    () -> {
                        while (threadState) {
                            Log.d("--------service test------", "run: foreground service test");
                            MainActivity2.getInstance().getLocation();
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).start();

            Intent notificationIntent = new Intent(this, MainActivity2.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent,
                            PendingIntent.FLAG_IMMUTABLE);
            final String CHANNELID = "Foreground Service ID";
            Notification notification =
                    new Notification.Builder(this, CHANNELID)
                            .setContentTitle("AccuWeather background service")
                            .setContentText("Service is running")
                            .setSmallIcon(R.drawable.app_logo)
                            .setContentIntent(pendingIntent)
                            .build();
            int notificationId = createID();
            startForeground(notificationId, notification);


//            NotificationChannel channel = new NotificationChannel(
//                    CHANNELID,
//                    CHANNELID,
//                    NotificationManager.IMPORTANCE_LOW
//            );
//            getSystemService(NotificationManager.class).createNotificationChannel(channel);
//            Notification.Builder notification = new Notification.Builder(this,CHANNELID)
//                    .setContentText("Service is running")
//                    .setContentTitle("AccuWeather background service")
//                    .setSmallIcon(R.drawable.app_logo);

//            startForeground(notificationId,notification.build());
        }
        else {
            Log.d("Foreground service else condition", "onStartCommand: FOREGROUND service destroyed");
            stopForeground(true);
            stopSelf();
            stopSelfResult(startId);
            state = true;
            threadState = false;
        }

        return super.onStartCommand(intent, flags, startId);
    }


    public int createID(){
        Date now = new Date();
        return Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.getDefault()).format(now));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
