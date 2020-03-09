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
  
## Deprecated
<ul>
   <li>AsyncTask</li>
   <li>Methods of Class : Date</li>
   </ul>


