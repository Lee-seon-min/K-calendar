package org.techtown.caloriecalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FoodSearcher extends AppCompatActivity implements FoodRecyclerViewAdaptor.ISetOnUpdateCheckBoxListener {
    private FoodRecyclerViewAdaptor adaptor;
    String location = null;
    private static String Key = "hiding_key";
    public static String RESULT_KCAL_KEY = "result";
    private double allSumKcal = 0;
    private Button getFoodButton, searching;
    private EditText searchBox;
    private TextView resultText,sumText;
    private RecyclerView recyclerView;
    private List<FoodItem> list = new ArrayList<>(); //파싱할때마다 add시킬것이다.
    private Handler handler = new Handler();
    private AlertDialog dialog;
    private double eachKcal=0;
    private Boolean flag=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodsearcher);
        initID();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor = new FoodRecyclerViewAdaptor();
        adaptor.setList(list);
        adaptor.setListener(this);
        recyclerView.setAdapter(adaptor);

        setEvents();
        dialog=getAlertDialog();
        Toast.makeText(this,"검색 후, 담을 음식을 터치해주세요!",Toast.LENGTH_LONG).show();

    }

    @Override
    public void TouchListener(double kcal) {
        flag=false;
        eachKcal=kcal;
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!flag){
                    if(flag)
                        break;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sumText.setText("총 : "+(Math.round(allSumKcal*100)/100.0)+"kcal");
                    }
                });
            }
        }).start();
    }

    public void setEvents() {
        searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XmlParser();
                    }
                }).start();
            }
        });
        getFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_KCAL_KEY, allSumKcal);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    public AlertDialog getAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("바구니 알림");
        builder.setMessage("해당 식품을 바구니에 담으시겠습니까?");
        builder.setIcon(getResources().getDrawable(R.drawable.sharp_local_grocery_store_black_36));
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                allSumKcal+=eachKcal;
                flag=true;
            }
        });
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        return builder.create();
    }

    public void initID() {
        sumText=findViewById(R.id.sumText);
        searchBox = findViewById(R.id.searchBox);
        searching = findViewById(R.id.searching);
        getFoodButton = findViewById(R.id.getFood);
        recyclerView = findViewById(R.id.foodList);
        resultText=findViewById(R.id.resultText);
    }
    public void XmlParser() { //xml데이터 파싱후 list에 add한다.
        String targetString = searchBox.getText().toString();
        location = null;
        try {
            location = URLEncoder.encode(targetString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String queryUrl = "http://apis.data.go.kr/1470000/FoodNtrIrdntInfoService/getFoodNtrItdntList?ServiceKey="+Key
                    + "&numOfRows=100&pageNo=1"+"&desc_kor="+location;
            try {
                URL url = new URL(queryUrl);
                InputStream is = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser(); //xml파서 객체 선언
                xpp.setInput(new InputStreamReader(is, "utf-8")); //해당 파서는 해당 InputStream 으로 읽어 올것임

                String tag;
                xpp.next();
                int type = xpp.getEventType();
                String name=null;
                double gram=0,kcal=0,carbo=0,prot=0,fat=0;
                while (type != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();
                            if (tag.equals("item")) ;
                            else if (tag.equals("DESC_KOR")) {
                                xpp.next();
                                name=xpp.getText();
                            } else if (tag.equals("SERVING_WT")) {
                                xpp.next();
                                if(xpp.getText().equals("N/A"))
                                    gram=0;
                                else
                                    gram = Double.parseDouble(xpp.getText());
                            } else if (tag.equals("NUTR_CONT1")) {
                                xpp.next();
                                if(xpp.getText().equals("N/A"))
                                    kcal=0;
                                else
                                    kcal = Double.parseDouble(xpp.getText());
                            } else if (tag.equals("NUTR_CONT2")) {
                                xpp.next();
                                if(xpp.getText().equals("N/A"))
                                    carbo=0;
                                else
                                    carbo = Double.parseDouble(xpp.getText());
                            } else if (tag.equals("NUTR_CONT3")) {
                                xpp.next();
                                if(xpp.getText().equals("N/A"))
                                    prot=0;
                                else
                                    prot = Double.parseDouble(xpp.getText());
                            } else if (tag.equals("NUTR_CONT4")) {
                                xpp.next();
                                if(xpp.getText().equals("N/A"))
                                    fat=0;
                                else
                                    fat = Double.parseDouble(xpp.getText());
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag = xpp.getName();
                            if (tag.equals("item"))
                                list.add(new FoodItem(name,gram,kcal,carbo,prot,fat));
                            break;
                    }
                    type=xpp.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adaptor.setList(list);
                        adaptor.notifyDataSetChanged();
                        if(adaptor.getItemCount()==0)
                            resultText.setText("검색결과가 없습니다.");
                         else{
                            resultText.setText("검색결과▼");
                        }
                    }
                });
            }
    }
}
