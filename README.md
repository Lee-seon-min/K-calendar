# K-calendar
Android App(K-calendar)

Use of functions according to SDK version  
setExactAndAllowWhileIdle() : VERSION.SDK_INT >= VERSION_CODES.M  
setExact() : VERSION_CODES.M > VERSION.SDK_INT >= VERSION_CODES.KITKAT  
set() : VERSION_CODES.KITKAT > VERSION.SDK_INT    
```if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
      alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
   else{
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
      } else {
          alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
      }
   }```


