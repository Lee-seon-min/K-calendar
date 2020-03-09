package org.techtown.caloriecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements InformationFragment.SendValuesInformationToCalendar{
    private static CalendarFragment calendarFragment=new CalendarFragment();
    private static InformationFragment informationFragment=new InformationFragment();
    private static AlarmFragment alarmFragment=new AlarmFragment();
    private FragmentManager fragmentManager=null;
    private BottomNavigationView bottomNavigationView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.botNavi);

        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.myFrame,calendarFragment).commit();

        setEvents();
        informationFragment.setPointer(this);
    }
    void setEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.calendar:
                        fragmentManager.beginTransaction().replace(R.id.myFrame,calendarFragment).commit();
                        break;
                    case R.id.myInfo:
                        fragmentManager.beginTransaction().replace(R.id.myFrame,informationFragment).commit();
                        break;
                    case R.id.alarm:
                        fragmentManager.beginTransaction().replace(R.id.myFrame,alarmFragment).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void sendSignal(int basic,int alpha) {
        calendarFragment.updateBasicRate(basic,alpha);
    }
}
