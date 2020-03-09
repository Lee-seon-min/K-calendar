package org.techtown.caloriecalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DailyMealActivity extends AppCompatActivity implements RecyclerViewAdaptor.ISetOnUpdateButtonListener {
    private static int MORNING_KEY=101;
    private static int LUNCH_KEY=102;
    private static int EVENING_KEY=103;
    private static int SNACK_KEY=104;
    public static String UPDATE_RESULT="updating";
    private RecyclerViewAdaptor adaptor;
    private ArrayList<Drawable> ImageList=new ArrayList<Drawable>();
    private String date;
    private List<MealItem> list=new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymeal);
        recyclerView=findViewById(R.id.recyclerview);

        initImageList();
        initList();

        Intent intent=getIntent();
        date=intent.getStringExtra(CalendarFragment.CUR_PICKDATE_KEY);

        Cursor cursor=getContentResolver().query(DataHandler.CONTENT_URI_DAILY,new String[]{
                "morning",
                "lunch",
                "evening",
                "snack",
                "sum"},"thisdate = ?",new String[]{date},null);
        while(cursor.moveToNext()){
            int morning=cursor.getInt(0);
            int lunch=cursor.getInt(1);
            int evening=cursor.getInt(2);
            int snack=cursor.getInt(3);
            list.get(0).setKcal(morning);
            list.get(1).setKcal(lunch);
            list.get(2).setKcal(evening);
            list.get(3).setKcal(snack);
        }
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor=new RecyclerViewAdaptor(ImageList);
        adaptor.setList(list);
        adaptor.setListener(this);
        recyclerView.setAdapter(adaptor);
    }
    @Override
    public void updateListener(int pos) {
        switch(pos){
            case 0://morning requestCode를 다르게 하여 값을 보낸다.
                Intent intent1=new Intent(DailyMealActivity.this,FoodSearcher.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent1,MORNING_KEY);
                break;
            case 1://lunch
                Intent intent2=new Intent(DailyMealActivity.this,FoodSearcher.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent2,LUNCH_KEY);
                break;
            case 2://evening
                Intent intent3=new Intent(DailyMealActivity.this,FoodSearcher.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent3,EVENING_KEY);
                break;
            case 3://snack
                Intent intent4=new Intent(DailyMealActivity.this,FoodSearcher.class);
                intent4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent4,SNACK_KEY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==MORNING_KEY && resultCode==RESULT_OK){
            double resultKcal=data.getDoubleExtra(FoodSearcher.RESULT_KCAL_KEY,0);
            list.get(0).setKcal((int)resultKcal);
            adaptor.setList(list);
            adaptor.notifyDataSetChanged();
        }
        else if(requestCode==LUNCH_KEY && resultCode==RESULT_OK){
            double resultKcal=data.getDoubleExtra(FoodSearcher.RESULT_KCAL_KEY,0);
            list.get(1).setKcal((int)resultKcal);
            adaptor.setList(list);
            adaptor.notifyDataSetChanged();
        }
        else if(requestCode==EVENING_KEY && resultCode==RESULT_OK){
            double resultKcal=data.getDoubleExtra(FoodSearcher.RESULT_KCAL_KEY,0);
            list.get(2).setKcal((int)resultKcal);
            adaptor.setList(list);
            adaptor.notifyDataSetChanged();

        }
        else if(requestCode==SNACK_KEY && resultCode==RESULT_OK){
            double resultKcal=data.getDoubleExtra(FoodSearcher.RESULT_KCAL_KEY,0);
            list.get(3).setKcal((int)resultKcal);
            adaptor.setList(list);
            adaptor.notifyDataSetChanged();
        }

    }

    public void initList(){
        list.add(new MealItem("아침",0));
        list.add(new MealItem("점심",0));
        list.add(new MealItem("저녁",0));
        list.add(new MealItem("간식",0));
    }
    public void initImageList(){
        ImageList.add(getResources().getDrawable(R.drawable.sharp_wb_sunny_black_36));
        ImageList.add(getResources().getDrawable(R.drawable.sharp_brightness_6_black_36));
        ImageList.add(getResources().getDrawable(R.drawable.sharp_nights_stay_black_36));
        ImageList.add(getResources().getDrawable(R.drawable.sharp_local_cafe_black_36));
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra(UPDATE_RESULT,list.get(0).getKcal()+list.get(1).getKcal()+list.get(2).getKcal()+list.get(3).getKcal());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        boolean Flag=false;
        int resultSum=0;
        Cursor cursor=getContentResolver().query(DataHandler.CONTENT_URI_DAILY,new String[]{"thisdate"},"thisdate = ?",new String[]{date},null);
        while(cursor.moveToNext()){
            Flag=true;
        }
        if(Flag){//update
            resultSum=list.get(0).getKcal()+list.get(1).getKcal()+list.get(2).getKcal()+list.get(3).getKcal();
            ContentValues values=new ContentValues();
            values.put("morning",list.get(0).getKcal());
            values.put("lunch",list.get(1).getKcal());
            values.put("evening",list.get(2).getKcal());
            values.put("snack",list.get(3).getKcal());
            values.put("sum",resultSum);
            int count=getContentResolver().update(DataHandler.CONTENT_URI_DAILY,values,"thisdate = ?",new String[]{date});
        }
        else{//insert
            resultSum=list.get(0).getKcal()+list.get(1).getKcal()+list.get(2).getKcal()+list.get(3).getKcal();
            ContentValues values=new ContentValues();
            values.put("thisdate",date);
            values.put("morning",list.get(0).getKcal());
            values.put("lunch",list.get(1).getKcal());
            values.put("evening",list.get(2).getKcal());
            values.put("snack",list.get(3).getKcal());
            values.put("sum",resultSum);
            Uri uri = getContentResolver().insert(DataHandler.CONTENT_URI_DAILY,values);
        }

        list.clear();
        super.onDestroy();
    }
}
