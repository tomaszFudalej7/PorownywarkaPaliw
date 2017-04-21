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
            case R.layout.custom_list_users_activity:{
                usersTableBindListView(view,context,cursor);
            }break;
        }

    }

    private void usersTableBindListView(View view,Context context,Cursor cursor){
        String id="",email="",permission="",creationData="",name="",surname="",town="";

        TextView tvCLUA_id,tvCLUA_email,tvCLUA_permission,tvCLUA_creationData,tvCLUA_name,tvCLUA_surname,tvCLUA_town;
        tvCLUA_id = (TextView) view.findViewById(R.id.tvCLUA_id);
        tvCLUA_name = (TextView) view.findViewById(R.id.tvCLUA_name);
        tvCLUA_surname = (TextView) view.findViewById(R.id.tvCLUA_surname);
        tvCLUA_email = (TextView) view.findViewById(R.id.tvCLUA_email);
        tvCLUA_town = (TextView) view.findViewById(R.id.tvCLUA_town);
        tvCLUA_permission = (TextView) view.findViewById(R.id.tvCLUA_permission);
        tvCLUA_creationData = (TextView) view.findViewById(R.id.tvCLUA_creationData);

        try{
            id = "id: " + cursor.getInt(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_ID));
            name = "name: " + cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_NAME));
            surname = "surname: " + cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_SURNAME));
            email = "email: " + cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_EMAIL));
            town = "town :" + cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_TOWN));
            permission = "permission: " + cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_PERMISSION));
            creationData = "creation data " + cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_CREATION_DATA));
        }
        catch (IllegalArgumentException e){
            ShowLogs.i("ListViewPopulateHelper.usersTableBindListView error :" + e.getMessage() );
        }
        tvCLUA_id.setText(id);
        tvCLUA_name.setText(name);
        tvCLUA_surname.setText(surname);
        tvCLUA_email.setText(email);
        tvCLUA_town.setText(town);
        tvCLUA_permission.setText(permission);
        tvCLUA_creationData.setText(creationData);

    }
}
