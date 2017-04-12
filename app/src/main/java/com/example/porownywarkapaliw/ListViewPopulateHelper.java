package com.example.porownywarkapaliw;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Администратор on 12.04.2017.
 */

public class ListViewPopulateHelper extends CursorAdapter {

    Context context;
    private LayoutInflater layoutInflater;
    private Layout layout;
    private int activityCustomItemsToPopulate;

    public ListViewPopulateHelper(Context context, Cursor c,int activityCustomItemsToPopulate) {
        super(context, c, 0);
        this.activityCustomItemsToPopulate = activityCustomItemsToPopulate;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(activityCustomItemsToPopulate,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //place to bind views (get their id and than using cursor.getString populate data
        /*
         TextView tvHistoryAlarmHour,tvHistoryAlarmMinute;
         int hour =0,minute =0;
         String name ="";

         tvHistoryAlarmHour = (TextView) view.findViewById(R.id.tvHistoryAlarmHour);
         tvHistoryAlarmMinute = (TextView) view.findViewById(R.id.tvHistoryAlarmMinute);
         tvHistoryAlarmName = (TextView) view.findViewById(R.id.tvHistoryAlarmName);
        try{
         name = "\"" + cursor.getString(cursor.getColumnIndexOrThrow(AlarmDBValues.COLUMN_MESSAGE_TEXT)) + "\"";
          hour = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDBValues.COLUMN_HOUR));
         minute =cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDBValues.COLUMN_MINUTE));
        }
        catch (IllegalArgumentException e){
            ShowLogs.i("CursorAdapterOverrider IllegalArgumentException");
        }

        if(minute<10)
            tvHistoryAlarmMinute.setText(": "+"0"+minute);
        else
            tvHistoryAlarmMinute.setText(": "+minute+"");
        tvAlarmMessageTextSetTextMethod(name);

        String sHour = String.valueOf(hour)+" ";
        tvHistoryAlarmHour.setText(sHour);
         */
    }
}
