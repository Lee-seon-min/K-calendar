# K-calendar
Android App(K-calendar)

AlarmManager 는 SDK버전에 따라 알람설정 메소드가 다르다.
setExactAndAllowWhileIdle() : VERSION.SDK_INT >= VERSION_CODES.M
setExact() : VERSION_CODES.M > VERSION.SDK_INT >= VERSION_CODES.KITKAT
set() : VERSION_CODES.KITKAT > VERSION.SDK_INT


