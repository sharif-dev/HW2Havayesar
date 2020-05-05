package com.example.sensors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShakingActivity extends AppCompatActivity {
    Button enable, disable;
    SeekBar seekBar;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_activity);

        final Intent intent = new Intent(getBaseContext(), ShakingService.class);
        seekBar = findViewById(R.id.shake_sense);
        intent.putExtra("sense", seekBar.getProgress());
        textView = findViewById(R.id.texxxt);
        textView.setText("sensitiveness : " + seekBar.getProgress());
        enable = findViewById(R.id.enable);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                startService(intent);
            }
        });

        disable = findViewById(R.id.disable);
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("sensitiveness : " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                intent.putExtra("sense", seekBar.getProgress());
            }
        });
    }
}
