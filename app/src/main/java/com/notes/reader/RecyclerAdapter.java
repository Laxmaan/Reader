package com.notes.reader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Laxmaan on 01-12-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context mContext;
    AssetManager mManager;
    ArrayList<String> versions=new ArrayList<>();
    RecyclerAdapter(Context context, AssetManager manager){
        mContext=context;
        mManager=manager;
        try {
            InputStream in =mManager.open("versions.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String s = reader.readLine();
            while(s!=null){
                versions.add(s);
                s = reader.readLine();
            }

        } catch (IOException e) {
            versions.add("Version data not found");
            Log.d("ERROR",e.getMessage());
            e.printStackTrace();
        }

        for(String s:versions)
            Log.d("CON",s);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisItemsView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout,
                parent, false);
        // Call the view holder's constructor, and pass the view to it;
        // return that new view holder
        return new ViewHolder(thisItemsView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(versions.get(position));
    }

    @Override
    public int getItemCount() {
        return versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.versionText);

        }
    }

    
}
