package com.example.sensors;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

public class ShakingService extends Service  implements SensorEventListener {
    @Nullable
    float xA, yA ,zA ,xPA , yPA , zPA ;
    Boolean firstUpdate=true;
    float shakeThreshold= (float) 12.5f;
    Boolean shakeInnit=false;
    Sensor sensorRemoter;
    SensorManager sensorManager;
    private Boolean isAccelrationChange(){
        float dx = Math.abs(xPA - xA);
        float dy = Math.abs(yPA - yA);
        float dz = Math.abs(zPA - zA);
        return (dx > shakeThreshold && dy > shakeThreshold) || (dx > shakeThreshold && dz > shakeThreshold ) || (dy > shakeThreshold && dz > shakeThreshold);
    }
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int x = intent.getIntExtra("sense", 10);
        shakeThreshold = (float)x;
        System.out.println(x);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorRemoter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensorRemoter, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println(isAccelrationChange());
        updateAccelParameter(event.values[0], event.values[1], event.values[2]);
        if(isAccelrationChange()) {
            executeShakeAction();
        }

    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    private void executeShakeAction() {
        PowerManager powerManager = (PowerManager) getBaseContext().getSystemService(getBaseContext().POWER_SERVICE);
        PowerManager.WakeLock  wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");

        //acquire will turn on the display
        wakeLock.acquire();

        //release will release the lock from CPU, in case of that, screen will go back to sleep mode in defined time bt device settings
        wakeLock.release();
        // Intent ii = new Intent(this , SecondActivity.class);
        //  ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  startActivity(ii);
    }

    private void updateAccelParameter(float xNA, float yNA, float zNA) {
        if (firstUpdate){
            xPA = xNA;
            yPA = yNA;
            zPA = zNA;
            firstUpdate=false;
        }else{
            xPA = xA;
            yPA = yA;
            zPA = zA;
        }
        xA = xNA;
        yA = yNA;
        zA = zNA;
    }


}
