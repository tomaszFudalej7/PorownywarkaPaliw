package com.example.porownywarkapaliw.ManagePart;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.porownywarkapaliw.ControlPanelStringResponseHelper;
import com.example.porownywarkapaliw.ListViewPopulateHelper;
import com.example.porownywarkapaliw.R;
import com.example.porownywarkapaliw.SQLDataBase.DBAdapter;
import com.example.porownywarkapaliw.SQLDataBase.DBValues;
import com.example.porownywarkapaliw.ShowLogs;

import java.io.IOException;

public class FragmentListOfAllUsers extends Fragment implements AdapterView.OnItemClickListener {
    private View view;
    private ListView lvALOAU_listOfUsers;
    private DBAdapter dbAdapter;
    private Cursor cursor;

    private String[] settingsArray = {"Block user","Change user permission","..."};
    private String usersPermissionsType [] ={"U","M","A"};
    private String blockStatusArray [] ={"false","true"};
    private int whichSelected = 0;

    private ProgressDialog progressDialog ;

    private ControlPanelStringResponseHelper controlPanelStringResponseHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_of_all_users,container,false);

        lvALOAU_listOfUsers = (ListView) view.findViewById(R.id.lvALOAU_listOfUsers);
        lvALOAU_listOfUsers.setOnItemClickListener(this);

        dbAdapter = new DBAdapter(view.getContext());
        dbAdapter.openDB();

        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Updating database ....");
        controlPanelStringResponseHelper = new ControlPanelStringResponseHelper(view.getContext());
        populateUsersListView();
        return view;
    }

    private void populateUsersListView(){
        ShowLogs.i("FragmentListOfAllUsers populateUsersListView");
        ListViewPopulateHelper listViewPopulateHelper = new ListViewPopulateHelper(view.getContext(),dbAdapter.GetAllRows(),
                R.layout.custom_list_users_activity);

        lvALOAU_listOfUsers.setAdapter(listViewPopulateHelper);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cursor = dbAdapter.GetRow(id);
        String email = cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_EMAIL));
        alertDialog_onClickItem(email);
    }

    private void alertDialog_onClickItem(final String email){
        whichSelected=0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Settings for user :" + email)
                .setSingleChoiceItems(settingsArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case 0://block user
                                builder.setSingleChoiceItems(blockStatusArray, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        whichSelected=which;
                                    }
                                }).setPositiveButton("Let's do it", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.show();
                                        try {
                                            if(controlPanelStringResponseHelper.stringRequestBlockUser(email,blockStatusArray[whichSelected])){
                                                refreshLocalDB(email,
                                                        cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_PERMISSION)),
                                                        blockStatusArray[whichSelected]);
                                                ShowLogs.i("TRUEEEEE");
                                            }
                                            else {
                                                ShowLogs.i("FALSEEEEEEEEEEE");
                                                progressDialog.dismiss();
                                            }
                                        } catch (IOException e) {
                                            ShowLogs.i("FragmentListOfAllUsers alertDialog_onClickItem IOException" + e.getMessage());
                                        }
                                    }
                                }).create().show();

                            break;
                            case 1://change permission
                                builder.setSingleChoiceItems(usersPermissionsType, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        whichSelected=which;
                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.show();
                                        try {
                                            if(controlPanelStringResponseHelper.stringRequestChangeUserPermissionStatus(email,usersPermissionsType[whichSelected]))
                                            refreshLocalDB(email,usersPermissionsType[whichSelected],
                                                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_BLOCK_STATUS)));
                                            else
                                                progressDialog.dismiss();
                                        } catch (IOException e) {
                                            ShowLogs.i("FragmentListOfAllUsers alertDialog_onClickItem IOException" + e.getMessage());
                                        }
                                    }}).create().show();

                            break;
                            case 2://fuck off

                                Toast.makeText(view.getContext(), "fuck off", Toast.LENGTH_SHORT).show();

                            break;
                        }
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //For confirming actions on user account (exception : change permission,blockStatus)
                    }
                })
                .setNegativeButton("Cancel",null).create().show();
    }




    private void refreshLocalDB(String email,String permission,String blockStatus){
        try {
            //update local DB after operation
            dbAdapter.GetDataToSaveInDB(cursor.getInt(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_SURNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_TOWN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_PHONE_NUMBER)),
                    permission,
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_CREATION_DATA)),
                    blockStatus);
            if(dbAdapter.UpdateRow(email))
                ShowLogs.i("FragmentListOfAllUsers stringRequestChangeUserPermissionStatus  UpdateRow true");
            else
                ShowLogs.i("FragmentListOfAllUsers stringRequestChangeUserPermissionStatus  UpdateRow false");
        }
        catch (IllegalArgumentException e){
            ShowLogs.i("FragmentListOfAllUsers stringRequestChangeUserPermissionStatus IllegalArgumentException" + e);
        }
        //refresh users data
        populateUsersListView();
        progressDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        dbAdapter.closeDB();
        progressDialog.dismiss();
    }
}
