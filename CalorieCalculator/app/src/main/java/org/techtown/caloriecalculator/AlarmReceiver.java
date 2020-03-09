package org.techtown.caloriecalculator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences=context.getSharedPreferences(AlarmFragment.ALARM,Context.MODE_PRIVATE);
        String tonic=preferences.getString(AlarmFragment.TONIC,"");
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"default");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default","기본",NotificationManager.IMPORTANCE_DEFAULT));

            builder.setSmallIcon(R.drawable.ic_alarm_icon);
        }
        else
            builder.setSmallIcon(R.mipmap.ic_launcher_alarm_icon);

        builder.setColor(Color.GREEN);
        builder.setContentTitle("영양제 알림");
        builder.setContentText("'"+tonic+"'드실 시간입니다! 어서 챙겨드세요!");
        Uri ring= RingtoneManager.getActualDefaultRingtoneUri(context,RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ring);

        long[] pattern={0,100,200,300};
        builder.setVibrate(pattern);
        builder.setAutoCancel(true);

        if(manager!=null){
            manager.notify(1,builder.build());
            Calendar notifyTime=Calendar.getInstance();
            notifyTime.add(Calendar.DATE,1);

            SharedPreferences.Editor editor=context.getSharedPreferences(AlarmFragment.ALARM,Context.MODE_PRIVATE).edit();
            editor.putLong(AlarmFragment.NOTIFY_ALARM_TIME,notifyTime.getTimeInMillis());
            editor.commit();
        }

    }
}
