package com.example.sensors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AlarmService extends Service {
    long calendar;
    int need;
    Intent intent;
    BroadcastReceiver broadcastReceiver;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        alarmManager.cancel(pendingIntent);
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        calendar = intent.getLongExtra("millis", 0);
        need = intent.getIntExtra("need", 0);
        System.out.println(calendar);;
        System.out.println(need);
        Intent i = new Intent(getBaseContext(), AlarmReceiver.class);
        i.putExtra("need", need);
        pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), 1, i, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar,
                pendingIntent);


        return START_STICKY;
    }

    public void extras(){
        calendar = intent.getLongExtra("millis", 0);
        need = intent.getIntExtra("need", 0);
    }
}
