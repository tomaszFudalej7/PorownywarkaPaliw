package com.example.porownywarkapaliw;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.porownywarkapaliw.SQLDataBase.DBValues;


public class ListViewPopulateHelper extends CursorAdapter {

    Context context;
    private LayoutInflater layoutInflater;
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

        switch (activityCustomItemsToPopulate){
            case R.layout.custom_list_userstable_activity:{
                usersTableBindListView(view,context,cursor);
            }break;
        }

    }

    private void usersTableBindListView(View view,Context context,Cursor cursor){
        String id="",email="",permission="",creationData="";

        TextView tvCLUA_id,tvCLUA_name,tvCLUA_email,tvCLUA_permission,tvCLUA_creationData;
        tvCLUA_id = (TextView) view.findViewById(R.id.tvCLUA_id);
        tvCLUA_email = (TextView) view.findViewById(R.id.tvCLUA_email);
        tvCLUA_permission = (TextView) view.findViewById(R.id.tvCLUA_permission);
        tvCLUA_creationData = (TextView) view.findViewById(R.id.tvCLUA_creationData);

        try{
            id = " " + cursor.getInt(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_ID)) + " ";
            email = " " + cursor.getInt(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_EMAIL)) + " ";
            permission = " " + cursor.getInt(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_PERMISSION)) + " ";
            creationData = " " + cursor.getInt(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_ID)) + " ";
        }
        catch (IllegalArgumentException e){
            ShowLogs.i("ListViewPopulateHelper.usersTableBindListView error :" + e.getMessage() );
        }
        tvCLUA_id.setText(id);
        tvCLUA_email.setText(email);
        tvCLUA_permission.setText(permission);
        tvCLUA_creationData.setText(creationData);

    }
}
