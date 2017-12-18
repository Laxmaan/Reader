package com.notes.reader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener{
    int index;
    ViewPager viewPager;
    SwipeAdapter swipeAdapter;
    ArrayList<String> myList,myColorList;
    boolean sortedbyColor,canSortByColor;
    FirebaseDatabase fdb;
    DatabaseReference dr;
    String filename,dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);


        myList = new ArrayList<>();
        myColorList = new ArrayList<>();
        fdb = MyUtils.getDatabase();

        //TODO receive the database to use from calendar toggle.
        dr = fdb.getReference();


        index = getIntent().getIntExtra("index",0);
        filename =getIntent().getStringExtra("file");
        dbname = getIntent().getStringExtra("db");


        String today = new SimpleDateFormat("d MMM yyyy",Locale.getDefault()).format(new Date());
        try {
            InputStream in = getAssets().open(filename+".txt");
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String s =r.readLine();
            while(s!=null){
                myList.add(s);
                if(s.equals(today))
                    break;
                s=r.readLine();

            }

            InputStream in1 = getAssets().open(filename+"Color.txt");
            canSortByColor=true;
            BufferedReader r1 = new BufferedReader(new InputStreamReader(in1));
            String s1=r1.readLine();
            while(s1!=null){
                myColorList.add(s1);
                s1=r1.readLine();
            }

        } catch (IOException e) {
            if(e.getMessage().equals(filename+"Color.txt")){
                canSortByColor=false;
            }

        }




        sortedbyColor=getIntent().getBooleanExtra("sort",false);


        if(sortedbyColor) {
            index = myColorList.indexOf(myList.get(index));
            swipeAdapter = new SwipeAdapter(getSupportFragmentManager(),myColorList,dbname);
            viewPager.setAdapter(swipeAdapter);
            viewPager.setCurrentItem(index,true);

        }else {
            swipeAdapter = new SwipeAdapter(getSupportFragmentManager(),myList,dbname);
            viewPager.setAdapter(swipeAdapter);
            viewPager.setCurrentItem(index,true);
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent(this,CalendarActivity.class);
        int index = viewPager.getCurrentItem();


        if(sortedbyColor){
            index=myList.indexOf(myColorList.get(index));
        }
        resultIntent.putExtra("date",index);
        resultIntent.putExtra("sort",sortedbyColor);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu,menu);
       return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home :{
                onBackPressed();

                //startActivity(resultIntent);
            }break;

            case R.id.sortoptionColor:{
                if(!sortedbyColor && canSortByColor) {
                    int current = viewPager.getCurrentItem();
                    ((SwipeAdapter)viewPager.getAdapter()).updateArray(myColorList);
                    sortedbyColor = true;

                    int modified = myColorList.indexOf(myList.get(current));
                    viewPager.setCurrentItem(modified,true);

                }
                if(!canSortByColor)
                    Toast.makeText(this,"Cannot Sort by Color yet",Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.sortoptionDate :{
                if(sortedbyColor) {
                    int current = viewPager.getCurrentItem();
                    ((SwipeAdapter)viewPager.getAdapter()).updateArray(myList);
                    sortedbyColor = false;

                    int modified = myList.indexOf(myColorList.get(current));
                    viewPager.setCurrentItem(modified,true);

                }
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        index = intent.getIntExtra("index",index);
        Log.d("INDEX","received index is :"+index);
        viewPager.setCurrentItem(index,true);
        Log.d("INDEX","new intent");
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            viewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


}


