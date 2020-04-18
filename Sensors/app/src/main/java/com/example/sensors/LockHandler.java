package com.example.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static androidx.core.content.ContextCompat.getSystemService;

public class LockHandler implements SensorEventListener {
    private SensorManager sensorManager ;
    Sensor  acceleromater;

    public LockHandler() {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
