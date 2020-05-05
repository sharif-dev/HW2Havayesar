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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SensorHandler extends AppCompatActivity  {
    private Button lock , enable , disable;

    private Intent intent1;
    public static int degree = 200;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private TextView angularSpeed;


    public int getDegree() {
        return degree;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);


        SeekBar seekBar = (SeekBar) findViewById(R.id.degree_setter);
        devicePolicyManager = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this , Controller.class);
        enable = (Button) findViewById(R.id.enable_button);
        angularSpeed =(TextView) findViewById(R.id.text_degree);
        intent1 = new Intent(SensorHandler.this, ServiceLockHandler.class);

        disable = (Button) findViewById(R.id.disable_button);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 2) {
                    seekBar.setProgress(20);
                }
                angularSpeed.setText("Angular velocity : " + seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                degree = seekBar.getProgress();
                }
            });


        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN , componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"hello");
                Log.d("Taaaag" , "saalal" + degree);
                startActivityForResult(intent ,11);
            }
        });
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.removeActiveAdmin(componentName);
                stopService(intent1);
                disable.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        boolean isActive = devicePolicyManager.isAdminActive(componentName);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 11:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(SensorHandler.this , "hello" , Toast.LENGTH_LONG).show();
                    ServiceLockHandler.componentName = componentName;
                    ServiceLockHandler.devicePolicyManager = devicePolicyManager;
                    startService(intent1);
                }
                else {
                    Toast.makeText(SensorHandler.this , "goodbye" , Toast.LENGTH_LONG).show();

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
