package com.example.sensors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class AlarmActivity extends Activity {
    private int x;

    CardView cardView;

    @Override
    public void onBackPressed() {

    }


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        x = getIntent().getIntExtra("need", 5);
        cardView = findViewById(R.id.cardview);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        CountDownTimer counterTimer = new CountDownTimer(600 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) (millisUntilFinished / 1000);
//                System.out.println(time);
//                if (time % 2 == 0){
//                    cardView.setBackgroundColor(Color.WHITE);
//                }else {
//                    cardView.setBackgroundColor(Color.BLACK);
//                }

                switch (time % 6) {
                    case 0:
                        cardView.setBackgroundColor(Color.WHITE);
                        break;
                    case 1:
                        cardView.setBackgroundColor(Color.RED);
                        break;
                    case 2:
                        cardView.setBackgroundColor(Color.BLUE);
                        break;
                    case 3:
                        cardView.setBackgroundColor(Color.GREEN);
                        break;
                    case 4:
                        cardView.setBackgroundColor(Color.YELLOW);
                        break;
                    case 5:
                        cardView.setBackgroundColor(Color.BLACK);
                }


            }

            public void onFinish() {
                mediaPlayer.release();
                finish();
            }

        };
        counterTimer.start();


        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float f = (float) x;
                if (event.values[2] > f) {
//                    Toast.makeText(mainActivity, "bye", Toast.LENGTH_SHORT).show();
                    System.out.println(event.values[2]);
                    mediaPlayer.release();
                    finish();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);


        System.out.println(x);


    }
}
