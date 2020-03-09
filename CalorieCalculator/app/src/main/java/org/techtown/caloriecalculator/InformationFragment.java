package org.techtown.caloriecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class InformationFragment extends Fragment {
    private String[] Activities={"LOW","MIDDLE","HIGH"};
    private static final int[] Sex={ 1, 2 }; //1은 남성, 2는 여성
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private RadioGroup radioGroup;
    private EditText Age;
    private EditText Weight;
    private EditText Height;
    private Spinner Activity;
    private Button Save;
    private SendValuesInformationToCalendar pointer;
    private int Mysex=1;
    interface SendValuesInformationToCalendar{
        void sendSignal(int basic,int alpha);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.information_fragment,container,false);
        FindByIDs_setEvents(view);
        int isFirst=0;
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Activities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Activity.setAdapter(adapter);
        Activity.setSelection(0);

        /*만약 select 값이 0이라면 캘린더 프래그먼트에 기초대사량값과 추가값을 0으로 던져준다.(처음)
        * isFirst 값은 한번 저장버튼을 누르면 1로 변화하고, 다시 게산된 기초대사량 값과 추가값을 던져준다.*/

        Cursor cursor=getContext().getContentResolver().query(DataHandler.CONTENT_URI_INFO,new String[]{"sex","age","weight","height","activity","basic","alpha"},null,null,null);
        while(cursor.moveToNext()){
            isFirst++;
            int sex=cursor.getInt(0);
            int age=cursor.getInt(1);
            double weight=cursor.getDouble(2);
            double height=cursor.getDouble(3);
            String activity=cursor.getString(4);

            if(sex==Sex[0])
                maleButton.setChecked(true);
            else if(sex==Sex[1])
                femaleButton.setChecked(true);

            Age.setText(String.valueOf(age));
            Weight.setText(String.valueOf(weight));
            Height.setText(String.valueOf(height));

            if(activity.equals(Activities[0]))
                Activity.setSelection(0);
            else if(activity.equals(Activities[1]))
                Activity.setSelection(1);
            else if(activity.equals(Activities[2]))
                Activity.setSelection(2);
        }
        setEvents(isFirst);


        return view;
    }
    public void FindByIDs_setEvents(View view){
        maleButton=view.findViewById(R.id.male);
        femaleButton=view.findViewById(R.id.female);
        radioGroup=view.findViewById(R.id.group);
        Age=view.findViewById(R.id.age);
        Weight=view.findViewById(R.id.weight);
        Height=view.findViewById(R.id.height);
        Activity=view.findViewById(R.id.activity);
        Save=view.findViewById(R.id.save);
    }
    public void setEvents(final int isFirst){
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickEvents(isFirst);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.male)
                    Mysex=1;
                else if(checkedId==R.id.female)
                    Mysex=2;
            }
        });
    }
    public void setPointer(SendValuesInformationToCalendar context){
        pointer=context;
    }
    public void ClickEvents(int isFirst){
        if(isFirst==0){
            if(Age.getText().toString().equals("")||Weight.getText().toString().equals("")||Height.getText().toString().equals("")){
                Toast.makeText(getContext(),"모두 입력해주십시오",Toast.LENGTH_SHORT).show();
                return;
            }
            int a,basic,alpha;
            double w,h;
            String act=Activity.getSelectedItem().toString();
            ContentValues values=new ContentValues();
            a=Integer.parseInt(Age.getText().toString());
            w=Double.parseDouble(Weight.getText().toString());
            h=Double.parseDouble(Height.getText().toString());
            values.put("sex",Mysex);
            values.put("age",a);
            values.put("weight",w);
            values.put("height",h);
            values.put("activity",act);
            if(Mysex==1) {
                basic=(int) (66.47 + (13.75 * w) + (5 * h) - (6.76 * a));
                values.put("basic", basic);
            }
            else {
                basic=(int) (655.1 + (9.56 * w) + (1.85 * h) - (4.68 * a));
                values.put("basic", basic);
            }

            if(act.equals(Activities[0])) {
                alpha=Integer.parseInt(String.valueOf((int)Math.floor(Double.parseDouble(String.valueOf(basic))*0.1)));
                values.put("alpha", alpha);
            }
            else if(act.equals(Activities[1])) {
                alpha=Integer.parseInt(String.valueOf((int)Math.floor(Double.parseDouble(String.valueOf(basic))*0.3)));
                values.put("alpha", alpha);
            }
            else {
                alpha=Integer.parseInt(String.valueOf((int)Math.floor(Double.parseDouble(String.valueOf(basic))*0.5)));
                values.put("alpha", alpha);
            }
            Uri uri=getContext().getContentResolver().insert(DataHandler.CONTENT_URI_INFO,values);
            if(uri==null){
                Toast.makeText(getContext(),"저장에 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(),"저장에 성공하였습니다.",Toast.LENGTH_SHORT).show();
            pointer.sendSignal(basic,alpha);
        }
        else{
            if(Age.getText().toString().equals("")||Weight.getText().toString().equals("")||Height.getText().toString().equals("")){
                Toast.makeText(getContext(),"모두 입력해주십시오",Toast.LENGTH_SHORT).show();
                return;
            }
            int a,basic,alpha;
            double w,h;
            String act=Activity.getSelectedItem().toString();
            ContentValues values=new ContentValues();
            a=Integer.parseInt(Age.getText().toString());
            w=Double.parseDouble(Weight.getText().toString());
            h=Double.parseDouble(Height.getText().toString());
            values.put("sex",Mysex);
            values.put("age",a);
            values.put("weight",w);
            values.put("height",h);
            values.put("activity",act);
            if(Mysex==1) {
                basic=(int) (66.47 + (13.75 * w) + (5 * h) - (6.76 * a));
                values.put("basic", basic);
            }
            else {
                basic=(int) (655.1 + (9.56 * w) + (1.85 * h) - (4.68 * a));
                values.put("basic", basic);
            }

            if(act.equals(Activities[0])) {
                alpha=Integer.parseInt(String.valueOf((int)Math.floor(Double.parseDouble(String.valueOf(basic))*0.1)));
                values.put("alpha", alpha);
            }
            else if(act.equals(Activities[1])) {
                alpha=Integer.parseInt(String.valueOf((int)Math.floor(Double.parseDouble(String.valueOf(basic))*0.3)));
                values.put("alpha", alpha);
            }
            else {
                alpha=Integer.parseInt(String.valueOf((int)Math.floor(Double.parseDouble(String.valueOf(basic))*0.5)));
                values.put("alpha", alpha);
            }
            int count=getContext().getContentResolver().update(DataHandler.CONTENT_URI_INFO,values,null,null);
            if(count==-1||count==0){
                Toast.makeText(getContext(),"업데이트를 실패하거나 데이터가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(getContext(),"업데이트에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                pointer.sendSignal(basic, alpha);
            }
        }
    }
}
