package org.techtown.caloriecalculator;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AlarmFragment extends Fragment {
    public static String TONIC="tonic";
    public static String ALARM="alarm";
    public static String NOTI_STATE="isnotify";
    public static String NOTIFY_ALARM_TIME="notifyalarm";
    private TimePicker picker;
    private EditText tonicText;
    private ImageButton saveAlarm;
    private ArrayList<Drawable> notifList=new ArrayList<Drawable>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alarm_fragment,container,false);
        initID(v);

        SharedPreferences sharedPreferences=getContext().getSharedPreferences(ALARM, Context.MODE_PRIVATE);
        long time=sharedPreferences.getLong(NOTIFY_ALARM_TIME, -1);
        String tonic=sharedPreferences.getString(TONIC, "");
        boolean isnotify=sharedPreferences.getBoolean(NOTI_STATE,false); //false면 알람설정이 되지 않은것
        tonicText.setText(tonic);

        if(time!=-1) {
            Calendar notifyTime=new GregorianCalendar();
            notifyTime.setTimeInMillis(time);

            Date mySettingDate = notifyTime.getTime();
            String date = new SimpleDateFormat("hh시 mm분", Locale.getDefault()).format(mySettingDate);
            Toast.makeText(getContext(), date + "에 알람 설정이 되어있습니다!", Toast.LENGTH_SHORT).show();

            Date curTime=notifyTime.getTime();
            SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
            SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

            int hour=Integer.parseInt(HourFormat.format(curTime));
            int min=Integer.parseInt(MinuteFormat.format(curTime));

            if(Build.VERSION.SDK_INT>=23){
                picker.setHour(hour);
                picker.setMinute(min);
            }
            else{
                picker.setCurrentHour(hour);
                picker.setCurrentMinute(min);
            }
        }
        setEvents(v);

        return v;
    }
    public void initID(View v){
        picker=v.findViewById(R.id.timePicker);
        tonicText=v.findViewById(R.id.tonicName);
        saveAlarm=v.findViewById(R.id.alarmSave);
        picker.setIs24HourView(true);
        notifList.add(getContext().getResources().getDrawable(R.drawable.outline_notifications_active_black_24));
        notifList.add(getContext().getResources().getDrawable(R.drawable.outline_notifications_off_black_24));
    }
    public void setEvents(View v){
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = picker.getHour();
                    minute = picker.getMinute();
                }
                else{
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }

                // 타임피커의 시간,분을 캘린더 객체에 세팅
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // 현재 시간보다 전 시간을 설정했다면 다음 시간으로
                if (calendar.before(Calendar.getInstance()))
                    calendar.add(Calendar.DATE, 1);

                //설정 시간 저장
                SharedPreferences.Editor editor = getContext().getSharedPreferences(ALARM, Context.MODE_PRIVATE).edit();
                editor.putLong(NOTIFY_ALARM_TIME, calendar.getTimeInMillis());
                editor.putString(TONIC,tonicText.getText().toString());
                editor.putBoolean(NOTI_STATE,true);
                editor.commit();

                settingNotification(calendar,true,v);

            }
        });
    }
    @SuppressLint("WrongConstant")
    public void settingNotification(Calendar calendar, boolean flag, View v){
        ComponentName receiver = new ComponentName(getContext(), BootingReceiver.class);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0); //해당 intent에 해당하는 리시버 얻기
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE); //알람 서비스

        if(flag) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent); //해당 시간에 해당 리시버를 작동

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            else{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }*/

            getContext().getPackageManager().setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP); //부팅수신기의 작동
            SharedPreferences.Editor editor = getContext().getSharedPreferences(ALARM, Context.MODE_PRIVATE).edit();
            editor.putBoolean(NOTI_STATE,true);
            editor.commit();
            Snackbar.make(v,"알람설정 완료!",Snackbar.LENGTH_SHORT).show();
        }
        else{
            if (pendingIntent!=null && alarmManager != null) {
                alarmManager.cancel(pendingIntent);
                Snackbar.make(v,"알람이 비활성화 되었습니다.",Snackbar.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = getContext().getSharedPreferences(ALARM, Context.MODE_PRIVATE).edit();
                editor.putBoolean(NOTI_STATE,false);
                editor.commit();
            }
            getContext().getPackageManager().setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP); //부팅수신기의 미작동
        }
    }
}
