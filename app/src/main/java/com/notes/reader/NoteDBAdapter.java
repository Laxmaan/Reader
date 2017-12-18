package com.notes.reader;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Laxmaan on 10-12-2017.
 */

public class NoteDBAdapter {

    ArrayList<Note> myNotes;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference,notesReference;
    Calendar mCalendar, minDateCalendar,maxDateCalendar;

    public NoteDBAdapter(){}

    public NoteDBAdapter(FirebaseDatabase fdb, DatabaseReference dr,String whoseNotes){
        mDatabase=fdb;
        mDatabaseReference=dr;

        notesReference = mDatabaseReference.child(whoseNotes);
        if(whoseNotes.equals("For Me")){
            minDateCalendar.set(2017,11,8);
            maxDateCalendar.set(2018,11,8);
        }
        else if(whoseNotes.equals("By Me")){
            minDateCalendar.set(2016,10,14);
            maxDateCalendar.set(2017,10,14);
        }
    }
}
