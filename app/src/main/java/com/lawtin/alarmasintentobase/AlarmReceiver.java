package com.lawtin.alarmasintentobase;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String EXTRA_ALARM_STATE = "alarm_state";
    private static final String ACTION_START_MUSIC = "com.example.app.ACTION_START_MUSIC";
    private static final String ACTION_STOP_MUSIC = "com.example.app.ACTION_STOP_MUSIC";

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmState = intent.getStringExtra(EXTRA_ALARM_STATE);
        if (alarmState == null) return;

        boolean isRunning = isServiceRunning(context, Music.class);

        Intent musicIntent = new Intent(context, Music.class);
        if (ACTION_START_MUSIC.equals(alarmState) && !isRunning) {
            context.startService(musicIntent);
        } else if (ACTION_STOP_MUSIC.equals(alarmState)) {
            context.stopService(musicIntent);
            MainActivity.activeAlarm = "";
        }
    }

    private boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) return false;

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
