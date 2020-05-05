package com.example.sensors;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ServiceLockHandler extends Service implements SensorEventListener {
    private SensorManager sensorManager ;
    private SharedPreferences sharedPreferences;
    private Intent intent1;
     private Sensor acceleromater;
     public static DevicePolicyManager devicePolicyManager;
     public static ComponentName componentName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this ,"safdljkaslkja" , Toast.LENGTH_LONG).show();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager!=null;
        acceleromater = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager.registerListener(this, this.acceleromater, SensorManager.SENSOR_DELAY_NORMAL);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (180 * sensorEvent.values[2]/(-9.8)>SensorHandler.degree){

            if (devicePolicyManager != null) {

                devicePolicyManager.lockNow();
            } else {
                devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                devicePolicyManager.lockNow();
            }
        }

    }
    

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
