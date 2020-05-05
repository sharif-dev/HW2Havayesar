package com.example.sensors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button lock;
    Button shake;

    TextView textView;
    private Intent intent;


    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent i;


    Intent intentForService;
    TimePicker myTimePicker;
    Button buttonstartSetDialog;
    Switch textAlarmPrompt;
    TimePickerDialog timePickerDialog;
    TextView angularSpeed;
    SeekBar seekBar;
    Calendar calendar;
    boolean flag = false;
    float need;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    final static int RQS_1 = 1;
    static boolean check;

    public static boolean getSwitch() {
        return check;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity mainActivity = this;
        intent = new Intent(this, SensorHandler.class);


        lock = (Button) findViewById(R.id.locking);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        final Intent shakeIntent = new Intent(getBaseContext(), ShakingActivity.class);

        shake = findViewById(R.id.go_to_shaking_page);
        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(shakeIntent);
            }
        });

        sharedPreferences = getSharedPreferences("velocity", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        seekBar = findViewById(R.id.sensivity);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 2) {
                    seekBar.setProgress(2);
                }
                angularSpeed.setText("Angular velocity : " + seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (textAlarmPrompt.isChecked() && flag) {
                    editor.putInt("v", seekBar.getProgress());
                    editor.commit();
                }
            }
        });


        angularSpeed = findViewById(R.id.textView);


        textAlarmPrompt = (Switch) findViewById(R.id.alarm);

        textAlarmPrompt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check = isChecked;
                if (isChecked && flag) {

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            pendingIntent);
                } else if (!isChecked && flag) {
                    alarmManager.cancel(pendingIntent);
                }

            }
        });

        buttonstartSetDialog = (Button) findViewById(R.id.startAlaram);
        buttonstartSetDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openTimePickerDialog(false);

            }
        });


    }


    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(MainActivity.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {
        calendar = targetCal;

        String s = String.valueOf(targetCal.getTime());
        String[] subs = s.split(" ");
        String[] strings = subs[3].split(":");
        textAlarmPrompt.setText("        " + strings[0] + " : " + strings[1]);


        if (flag) {
            alarmManager.cancel(pendingIntent);
        }
        i = new Intent(getBaseContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), 1, i, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent);
        }
        editor.putInt("v", seekBar.getProgress());
        editor.commit();
        textAlarmPrompt.setChecked(true);
        flag = true;


    }




}
