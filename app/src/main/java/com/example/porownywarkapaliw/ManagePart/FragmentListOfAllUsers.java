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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.porownywarkapaliw.ListViewPopulateHelper;
import com.example.porownywarkapaliw.R;
import com.example.porownywarkapaliw.SQLDataBase.DBAdapter;
import com.example.porownywarkapaliw.SQLDataBase.DBValues;
import com.example.porownywarkapaliw.ShowLogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentListOfAllUsers extends Fragment implements AdapterView.OnItemClickListener {
    private static final String PCP_ADMIN_CHANGE_USER_PERMISSION = "http://mrkostua.net16.net/AdminChangeUserPermission.php";
    private View view;
    private ListView lvALOAU_listOfUsers;
    private DBAdapter dbAdapter;
    private Cursor cursor;

    private String[] settingsArray = {"Block user","Change user permission","..."};
    private String usersPermissionsType [] ={"U","M","A"};
    private int whichSelected = 0;

    private ProgressDialog progressDialog ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_of_all_users,container,false);

        lvALOAU_listOfUsers = (ListView) view.findViewById(R.id.lvALOAU_listOfUsers);
        lvALOAU_listOfUsers.setOnItemClickListener(this);

        dbAdapter = new DBAdapter(view.getContext());
        dbAdapter.openDB();

        progressDialog = new ProgressDialog(view.getContext());

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
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Settings for user :" + email)
                .setSingleChoiceItems(settingsArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0://block user
                            {
                                Toast.makeText(view.getContext(), "bla bla Block somebody", Toast.LENGTH_SHORT).show();
                            }break;
                            case 1://change permission
                            {
                                builder.setSingleChoiceItems(usersPermissionsType, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        whichSelected=which;
                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setMessage("Updating database ....");
                                        progressDialog.show();
                                        try {
                                            adminResponseChangeUserPermissionStatus(email,usersPermissionsType[whichSelected]);
                                        } catch (IOException e) {
                                            ShowLogs.i("FragmentListOfAllUsers alertDialog_onClickItem IOException" + e.getMessage());
                                        }
                                    }}).create().show();
                            }break;
                            case 2://fuck off
                            {
                                Toast.makeText(view.getContext(), "fuck off", Toast.LENGTH_SHORT).show();
                            }break;
                        }
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //For confirming actions on user account (exception : change permission)
                    }
                })
                .setNegativeButton("Cancel",null).create().show();
    }

    private void adminResponseChangeUserPermissionStatus(final String email, final String permission) throws IOException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PCP_ADMIN_CHANGE_USER_PERMISSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ShowLogs.i("FragmentListOfAllUsers adminResponseChangeUserPermissionStatus  onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean errorCheck = jsonObject.getBoolean("error");
                    if (!errorCheck) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();

                        refreshLocalDB(email,permission);
                    }
                    else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        ShowLogs.i("FragmentListOfAllUsers adminResponseChangeUserPermissionStatus errorMessage" + errorMessage);
                        Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    ShowLogs.i("FragmentListOfAllUsers adminResponseChangeUserPermissionStatus JSONException " + e.getMessage());
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                ShowLogs.i("FragmentListOfAllUsers adminResponseChangeUserPermissionStatus onErrorResponse  " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(DBValues.COLUMN_KEY_EMAIL, email);
                params.put(DBValues.COLUMN_KEY_PERMISSION, permission);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

    private void refreshLocalDB(String email,String permission){
        try {
            //update local DB after operation
            dbAdapter.GetDataToSaveInDB(cursor.getInt(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_SURNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_TOWN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_PHONE_NUMBER)),
                    permission,
                    cursor.getString(cursor.getColumnIndexOrThrow(DBValues.COLUMN_KEY_CREATION_DATA)));
            if(dbAdapter.UpdateRow(email))
                ShowLogs.i("FragmentListOfAllUsers adminResponseChangeUserPermissionStatus  UpdateRow true");
            else
                ShowLogs.i("FragmentListOfAllUsers adminResponseChangeUserPermissionStatus  UpdateRow false");
        }
        catch (IllegalArgumentException e){
            ShowLogs.i("FragmentListOfAllUsers adminResponseChangeUserPermissionStatus IllegalArgumentException" + e);
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
