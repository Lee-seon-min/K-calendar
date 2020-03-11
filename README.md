# K-calendar
#### Android App(K-calendar)    
KOR)  
K-calendar는 기존에 있었던 칼로리 관리 앱과는 달리, 달력자체가 주 UI가 되어 편의를 중점으로  섭취수준을 한눈에 확인하게끔한 1인 개발 작품이다. 그리고 적당한 시간에 기록한 영양제를  상단알람을 활용하여 알려주기도 한다.  
  
ENG)  
Unlike previous calorie management apps, K-Calendar is a one-person development  that allows the calendar itself to become the main UI and check the level of intake at a glance, focusing on convenience. Also, the nutritional supplements  recorded at the appropriate time are notified using the upper alarm.    
## Caution  
### Use of functions according to SDK version(AlarmManager)  
setExactAndAllowWhileIdle() : VERSION.SDK_INT >= VERSION_CODES.M  
setExact() : VERSION_CODES.M > VERSION.SDK_INT >= VERSION_CODES.KITKAT  
set() : VERSION_CODES.KITKAT > VERSION.SDK_INT    
```
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
   alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
else{
   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
   } else {
      alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
   }
}
```
  
  
### Notification Channel  
Oreo versions and above require a notification channel.  
```
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences=context.getSharedPreferences(AlarmFragment.ALARM,Context.MODE_PRIVATE);
        String tonic=preferences.getString(AlarmFragment.TONIC,"");
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"default");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //오레오 이상에서 채널의 필요
            //채널 설정
            manager.createNotificationChannel(new NotificationChannel("default","기본",NotificationManager.IMPORTANCE_DEFAULT));

            builder.setSmallIcon(R.drawable.ic_alarm_icon); //drawable을 사용해야함
        }
        else
            builder.setSmallIcon(R.mipmap.ic_launcher_alarm_icon); //mipmap을 사용해야함

        //소리와 각종 텍스트 설정
        builder.setColor(Color.GREEN);
        builder.setContentTitle("영양제 알림");
        builder.setContentText("'"+tonic+"'드실 시간입니다! 어서 챙겨드세요!");
        Uri ring= RingtoneManager.getActualDefaultRingtoneUri(context,RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ring);

        //진동 패턴 설정
        long[] pattern={0,100,200,300};
        builder.setVibrate(pattern);
        builder.setAutoCancel(true);

        if(manager!=null){
            manager.notify(1,builder.build()); //알람실행
            Calendar notifyTime=Calendar.getInstance();
            notifyTime.add(Calendar.DATE,1);

            SharedPreferences.Editor editor=context.getSharedPreferences(AlarmFragment.ALARM,Context.MODE_PRIVATE).edit();
            editor.putLong(AlarmFragment.NOTIFY_ALARM_TIME,notifyTime.getTimeInMillis());
            editor.commit();
        }

    }
}
```  
  
## Deprecated
<ul>
   <li>AsyncTask</li>
   <li>Methods of Class : Date</li>
   </ul>


