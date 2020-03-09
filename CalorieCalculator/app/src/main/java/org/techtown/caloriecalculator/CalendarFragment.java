package org.techtown.caloriecalculator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment {
    public static String CUR_PICKDATE_KEY="thisDate";
    public static int UPDATE_DAILY_CODE=500;
    private TextView basic;
    private TextView lessOrMore,dailyKcal;
    private CalendarView calendarView;
    private Button dailyUpdate;
    private String targetDate=null;
    private static int Sum=0;
    private static int dailySum=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.calendar_fragment,container,false);
        initID(view);
        setEvents();

        FirstOpenFrag_CalcRateByInfo();

        Calendar curDate=Calendar.getInstance();
        curDate.setTimeInMillis(calendarView.getDate());
        targetDate=curDate.get(Calendar.YEAR)+"-"+(curDate.get(Calendar.MONTH)+1)+"-"+curDate.get(Calendar.DATE);
        //해당 target 날짜에 해당하는 sum값을 DB에서 가져와서 Sum과 비교해 잔여 칼로리와 섭취한 칼로리를 갱신한다.

        //처음 킬 때,
        updateMyFragment();

        return view;
    }
    public void updateBasicRate(int basic,int alpha){
        this.basic.setText("적정 섭취량 : "+basic+"kcal+"+alpha+"kcal="+(basic+alpha)+"kcal");
        Sum=(basic+alpha);
        lessOrMore.setText("잔여 칼로리 : "+(Sum-dailySum)+"kcal");
    }
    public void setEvents(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                targetDate=year+"-"+(month+1)+"-"+dayOfMonth;
                //해당 target 날짜에 해당하는 sum값을 DB에서 가져와서 Sum과 비교해 잔여 칼로리와 섭취한 칼로리를 갱신한다.

                updateMyFragment();
            }
        });
        dailyUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),DailyMealActivity.class);
                intent.putExtra(CalendarFragment.CUR_PICKDATE_KEY,targetDate);
                startActivityForResult(intent,UPDATE_DAILY_CODE);
            }
        });
    }
    public void initID(View view){
        basic=view.findViewById(R.id.myEatRate);
        dailyKcal=view.findViewById(R.id.daily_kcal);
        lessOrMore=view.findViewById(R.id.lessOrmore);
        calendarView=view.findViewById(R.id.mycalendar);
        dailyUpdate=view.findViewById(R.id.daily_update);
    }
    public void FirstOpenFrag_CalcRateByInfo(){
        Cursor cursor=getContext().getContentResolver().query(DataHandler.CONTENT_URI_INFO,new String[]{"basic","alpha"},null,null,null);
        if(cursor.moveToNext()) {
            int basic = cursor.getInt(0);
            int alpha = cursor.getInt(1);
            Sum = basic + alpha;
            this.basic.setText("적정 섭취량 : "+basic+"kcal+"+alpha+"kcal="+Sum+"kcal");
        }
        else{
            this.basic.setText("적정 섭취량 : "+0+"kcal+"+0+"kcal="+Sum+"kcal");
        }
        cursor.close();
    }
    public void updateMyFragment(){
        Cursor cursor=getContext().getContentResolver().query(DataHandler.CONTENT_URI_DAILY,new String[]{"sum"},"thisdate=?",new String[]{targetDate},null);
        if(cursor.moveToNext()) {
            dailySum = cursor.getInt(0);
            dailyKcal.setText("섭취한 칼로리 : " + dailySum + "kcal");
            lessOrMore.setText("잔여 칼로리 : "+(Sum-dailySum)+"kcal");
        }
        else{
            dailyKcal.setText("섭취한 칼로리 : " + 0 + "kcal");
            lessOrMore.setText("잔여 칼로리 : "+Sum+"kcal");
        }
        cursor.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==UPDATE_DAILY_CODE&&resultCode==RESULT_OK){
            dailySum=data.getIntExtra(DailyMealActivity.UPDATE_RESULT,0);
            dailyKcal.setText("섭취한 칼로리 : " + dailySum + "kcal");
            lessOrMore.setText("잔여 칼로리 : "+(Sum-dailySum)+"kcal");
        }
    }
}
