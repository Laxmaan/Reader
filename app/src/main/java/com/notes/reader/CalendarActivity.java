package com.notes.reader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    Calendar calendar,base;
    List<String> myList = new ArrayList<>();
    int offset;
    private static final int requestCode = 968;
    Button button;
    SimpleDateFormat df;
    boolean sortbycolor=false;
    String whose,dbName,fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        whose = getIntent().getStringExtra("user");



        //init widgets
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        button = (Button)findViewById(R.id.button3) ;
        button.setVisibility(View.INVISIBLE);
        calendar = Calendar.getInstance();
        base = Calendar.getInstance();



        switch (whose){
            case "YOURS":{
                base.set(2017,11,8);
                fileName="ForMe";
                dbName="By Me";
                calendarView.setMinDate(base.getTimeInMillis());
                calendar= (Calendar) base.clone();

                calendar.add(Calendar.DATE,365);
                calendarView.setMaxDate(calendar.getTimeInMillis());


                Date d = new Date(); //SET TODAY's date or MAXDATE
                if(d.compareTo(calendar.getTime())>=0){
                    calendarView.setDate(calendar.getTimeInMillis());
                }
                else{
                    calendarView.setMaxDate(d.getTime());
                }

            }break;

            case "MINE":{
                base.set(2016,10,14);
                fileName="ByMe";
                dbName="For Me";
                calendarView.setMinDate(base.getTimeInMillis());
                calendar= (Calendar) base.clone();
                calendar.add(Calendar.DATE,365);
                calendarView.setMaxDate(calendar.getTimeInMillis());


                Date d = new Date(); //SET TODAY's date or MAXDATE
                if(d.compareTo(calendar.getTime())>=0){
                    calendarView.setDate(calendar.getTimeInMillis());
                }
                else{
                    calendarView.setMaxDate(d.getTime());
                }
            }
        }





        button.setVisibility(View.INVISIBLE);

        //init variables

        try {
            InputStream in = getAssets().open("List.txt");

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String s =r.readLine();
            while(s!=null){
                myList.add(s);
                s=r.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }






        //operations on widgets
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if(!button.isShown()){
                    Animation animation = new AlphaAnimation(0.00f,1.00f);
                    Animation animation1 = AnimationUtils.makeInAnimation(getApplicationContext(),true);
                    button.setAnimation(animation1);
                    button.startAnimation(animation1);
                    button.setVisibility(View.VISIBLE);
                }
                calendar.set(year,month,dayOfMonth);

                long difference = calendar.getTimeInMillis()-base.getTimeInMillis();
                offset= (int) (difference/1000/3600/24);
                Log.d("INDEX", String.valueOf(offset));

                df = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                //calendar.


            }
        });


        //operations on variables





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Display(View view){




        Intent intent = new Intent(this,Main2Activity.class);

        intent.putExtra("index",offset);
        intent.putExtra("sort",sortbycolor);
        intent.putExtra("file",fileName);
        intent.putExtra("db",dbName);
        Log.d("INDEX","offset is "+offset);

        startActivityForResult(intent,requestCode);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("INDEX1","activity result"+requestCode);

        switch(requestCode){
            case CalendarActivity.requestCode:{
                if(resultCode==Activity.RESULT_OK){
                    offset=data.getIntExtra("date",0);
                    sortbycolor = data.getBooleanExtra("sort",false);
                    Log.d("INDEX","calendar new intent");
                    Calendar calendar1= (Calendar) base.clone();
                    calendar1.add(Calendar.DATE,offset);
                    calendarView.setDate(calendar1.getTimeInMillis());
                }
            }break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint);
        if (hasFocus) {
            constraintLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
