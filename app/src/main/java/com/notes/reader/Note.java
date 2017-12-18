package com.notes.reader;

import java.io.Serializable;

/**
 * Created by Laxmaan on 10-12-2017.
 */

public class Note implements Serializable{
    private String date,message;
    int color;


    Note(){}


    static Note newInstance(){
        Note note = new Note();
        note.date=note.message="";
        note.color=-1;
        return note;
    }

    static Note newInstance(String mDate, String mMessage, int mColor){
        Note note= new Note();
        note.message=mMessage;
        note.date=mDate;
        note.color=mColor;

        return note;
    }

    static Note newInstance(String mDate, String mMessage, String mColor){
        Note note= new Note();
        note.message=mMessage;
        note.date=mDate;
        note.color= Integer.parseInt(mColor);

        return note;
    }

    public int getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return date+"\t"+message+"\t"+String.valueOf(color);
    }
}
