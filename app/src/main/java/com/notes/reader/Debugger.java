package com.notes.reader;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Laxmaan on 03-12-2017.
 */

public class Debugger {

    public static String getViewHierarchy(@NonNull View v) {
        StringBuilder desc = new StringBuilder();
        getViewHierarchy(v, desc, 0);
        return desc.toString();
    }

    private static void getViewHierarchy(View v, StringBuilder desc, int margin) {
        desc.append(getViewMessage(v, margin));
        if (v instanceof ViewGroup) {
            margin++;
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                getViewHierarchy(vg.getChildAt(i), desc, margin);
            }
        }
    }

    private static String getViewMessage(View v, int marginOffset) {
        String repeated = new String(new char[marginOffset]).replace("\0", "  ");
        try {
            String resourceId = v.getResources() != null ? (v.getId() > 0 ? v.getResources().getResourceName(v.getId()) : "no_id") : "no_resources";
            return repeated + "[" + v.getClass().getSimpleName() + "] " + resourceId + "\n";
        } catch (Resources.NotFoundException e) {
            return repeated + "[" + v.getClass().getSimpleName() + "] name_not_found\n";
        }
    }
}
