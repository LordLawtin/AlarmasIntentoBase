package com.lawtin.alarmasintentobase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<Alarm> alarmList;
    private LayoutInflater layoutInflater;

    public CustomAdapter(Context context, List<Alarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_item, parent, false);
            holder = new ViewHolder();
            holder.nameTV = convertView.findViewById(R.id.nameTextView);
            holder.alarmTV = convertView.findViewById(R.id.timeTextView);
            holder.toggleButton = convertView.findViewById(R.id.toggle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Alarm selectedAlarm = alarmList.get(position);
        holder.nameTV.setText(selectedAlarm.getName());
        holder.alarmTV.setText(selectedAlarm.toString());
        holder.toggleButton.setChecked(selectedAlarm.getStatus());

        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedAlarm.setStatus(isChecked);
                DatabaseHelper db = new DatabaseHelper(context);
                db.updateAlarm(selectedAlarm);

                alarmList.set(position, selectedAlarm);
                notifyDataSetChanged();

                if (!isChecked && selectedAlarm.toString().equals(MainActivity.activeAlarm)) {
                    Intent serviceIntent = new Intent(context, AlarmReceiver.class);
                    serviceIntent.putExtra("extra", "off");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, position, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.cancel(pendingIntent);
                    }
                    context.sendBroadcast(serviceIntent);
                } else if (isChecked) {
                    Intent serviceIntent = new Intent(context, AlarmReceiver.class);
                    serviceIntent.putExtra("extra", "on");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, position, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, selectedAlarm.getAlarmTimeInMillis(), pendingIntent);
                    }
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView nameTV;
        TextView alarmTV;
        ToggleButton toggleButton;
    }
}
