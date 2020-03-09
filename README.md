# K-calendar
Android App(K-calendar)

AlarmManager 는 SDK버전에 따라 알람설정 메소드가 다르다.

---------------------------주석처리 부분-------------------------------------

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
  else{
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    } else {
      alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
  }
