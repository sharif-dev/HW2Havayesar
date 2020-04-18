package com.example.sensors;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SensorHandler extends AppCompatActivity implements SensorEventListener {
    private Button lock , enable , disable;
    private SensorManager sensorManager ;
    Sensor  acceleromater;
    private boolean enableFlag = false;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private ActivityManager activityManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);
        devicePolicyManager = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        componentName = new ComponentName(this , Controller.class);
        enable = (Button) findViewById(R.id.enable_button);
        disable = (Button) findViewById(R.id.disable_button);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN , componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"hello");
                startActivityForResult(intent ,11);
            }
        });
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.removeActiveAdmin(componentName);
                disable.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        boolean isActive = devicePolicyManager.isAdminActive(componentName);


    }
    private void snesorLockHandler(){
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            acceleromater = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(SensorHandler.this , acceleromater , SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 11:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(SensorHandler.this , "hello" , Toast.LENGTH_LONG).show();
                    enableFlag = true;
                    snesorLockHandler();
                }
                else {
                    Toast.makeText(SensorHandler.this , "goodbye" , Toast.LENGTH_LONG).show();

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[2]<-8 && enableFlag ){
            boolean active = devicePolicyManager.isAdminActive(componentName);
            if (active)
                devicePolicyManager.lockNow();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
