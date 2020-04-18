package com.example.sensors;

import android.app.TimePickerDialog;
import android.content.Intent;
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

    TextView textView;
    private Intent intent;

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

    final static int RQS_1 = 1;

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
                if (textAlarmPrompt.isChecked() && flag){
                    stopService(intentForService);
                    intentForService = new Intent(mainActivity, AlarmService.class);
                    intentForService.putExtra("millis", calendar.getTimeInMillis());
                    intentForService.putExtra("need", seekBar.getProgress());
                    startService(intentForService);
                }
            }
        });


        angularSpeed = findViewById(R.id.textView);


        textAlarmPrompt = (Switch) findViewById(R.id.alarm);

        textAlarmPrompt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && flag){
                    stopService(intentForService);
                    intentForService = new Intent(mainActivity, AlarmService.class);
                    intentForService.putExtra("millis", calendar.getTimeInMillis());
                    intentForService.putExtra("need", seekBar.getProgress());
                    startService(intentForService);
                }else if (!isChecked && flag){
                    stopService(intentForService);
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


        if (flag){
            stopService(intentForService);
        }
        intentForService = new Intent(this, AlarmService.class);
        intentForService.putExtra("millis", targetCal.getTimeInMillis());
        intentForService.putExtra("need", seekBar.getProgress());
        textAlarmPrompt.setChecked(true);
        flag = true;
        startService(intentForService);

    }
}


