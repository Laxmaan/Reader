package com.notes.reader;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Laxmaan on 15-12-2017.
 */

public class MyUtils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
