package com.example.sensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();


        float need = intent.getFloatExtra("need", 0);
        Intent i = new Intent(context, AlarmActivity.class);
        i.putExtra("need", need);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


    }
}
