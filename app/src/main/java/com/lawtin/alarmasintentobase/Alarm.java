package com.lawtin.alarmasintentobase;

import java.util.Calendar;

public class Alarm {
    private int id;
    private int hour;
    private int minute;
    private boolean status;
    private String name;

    public Alarm() {}

    public Alarm(int hour, int minute, boolean status, String name) {
        this.hour = hour;
        this.minute = minute;
        this.status = status;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour () {
        return hour;
    }

    public void setHour (int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute (int minute) {
        this.minute = minute;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus (boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String hourString, minuteString, format;
        if (hour > 12) {
            hourString = (hour - 12) + "";
            format = "PM";
        } else if (hour == 0) {
            hourString = "12";
            format = "AM";
        } else if (hour == 12) {
            hourString = "12";
            format = "PM";
        } else {
            hourString = hour + "";
            format = "AM";
        }
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = " "+ minute;
        }
        return hourString + ":" + minuteString + format;
    }

    // Método para obtener el tiempo de la alarma en milisegundos
    public long getAlarmTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTimeInMillis();
    }
}
