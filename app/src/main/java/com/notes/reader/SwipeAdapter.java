package com.notes.reader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;


public class SwipeAdapter extends FragmentStatePagerAdapter {

    ArrayList<BlankFragment> notes;
    ArrayList<String> myNotes;
    Comparator<String> datewise,colorwise;
    String mDBname;
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
        notes= new ArrayList<>();
        defineComparators();
    }

    public SwipeAdapter(FragmentManager fm,ArrayList<String> params,String dbname) {
        super(fm);
        notes=new ArrayList<>();
        myNotes = params;
        mDBname = dbname;
        for(String s:myNotes){
            notes.add(BlankFragment.newInstance(s,dbname));
        }


        Log.d("TAG", String.valueOf(notes.size()));


    }

    private void postAssemble() {
        Log.d("SIZZE", String.valueOf(notes.size()));

    }



    private void defineComparators(){
        final SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        datewise = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                try {
                    return df.parse(o1).compareTo(df.parse(o2));
                } catch (ParseException e) {
                    return 0;
                }

            }
        };

        colorwise = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] t1=o1.split("~~~"),t2=o2.split("~~~");

                return Integer.parseInt(t1[1])-Integer.parseInt(t2[1]);
            }
        };
    }



    @Override
    public Fragment getItem(int position) {

            try {
                return notes.get(position);
            }catch (IndexOutOfBoundsException e){
                return null;
            }


    }

    @Override
    public int getCount() {
        return notes.size();
    }



    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public int indexOf(BlankFragment bF){
        return notes.indexOf(bF);
    }

    public void clear(){
        notes.clear();
        notifyDataSetChanged();
    }


    public void updateArray(ArrayList<String> params){
        myNotes = params;
        notes.clear();
        for(String s: myNotes){
            notes.add(BlankFragment.newInstance(s,mDBname));
        }
        notifyDataSetChanged();
    }

    void writeColoredArrayToDisk(Context context,String filename){
        File file = new File(context.getFilesDir(),filename+"Color.txt");
        if(notes.get(0).getDate().equals("Nov 14 2016")){
            ArrayList<String> temp = new ArrayList<>(myNotes);
            temp.sort(new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    return 0;
                }
            });
        }
    }


}
