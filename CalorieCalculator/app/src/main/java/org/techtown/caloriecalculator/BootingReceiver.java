package org.techtown.caloriecalculator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class BootingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            ComponentName receiver = new ComponentName(context, BootingReceiver.class);
            Intent intent1 = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            SharedPreferences sharedPreferences = context.getSharedPreferences(AlarmFragment.ALARM, Context.MODE_PRIVATE);
            long time = sharedPreferences.getLong(AlarmFragment.NOTIFY_ALARM_TIME, -1);
            boolean flag=sharedPreferences.getBoolean(AlarmFragment.NOTI_STATE, false);
            //String tonic=sharedPreferences.getString(AlarmFragment.TONIC, "");

            if(time!=-1&&flag) {
                Calendar curTime = Calendar.getInstance();
                Calendar notifyTime = new GregorianCalendar();
                notifyTime.setTimeInMillis(time);

                if (notifyTime.before(curTime))
                    notifyTime.add(Calendar.DATE, 1);

                manager.setRepeating(AlarmManager.RTC_WAKEUP, notifyTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                SharedPreferences.Editor editor = context.getSharedPreferences(AlarmFragment.ALARM, Context.MODE_PRIVATE).edit();
                editor.putBoolean(AlarmFragment.NOTI_STATE,true);
                editor.commit();

                context.getPackageManager().setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

            }
        }
    }
}
