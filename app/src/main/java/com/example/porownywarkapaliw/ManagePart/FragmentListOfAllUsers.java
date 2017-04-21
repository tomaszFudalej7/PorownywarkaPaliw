package com.example.porownywarkapaliw.ManagePart;

import android.app.Fragment;
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
private String[] settingsArray = {"Block user","Change user permission","..."};
private String usersPermissionsType [] ={"U","M","A"};
private int whichSelected = 0;

//Test changes
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_of_all_users,container,false);

        lvALOAU_listOfUsers = (ListView) view.findViewById(R.id.lvALOAU_listOfUsers);
        lvALOAU_listOfUsers.setOnItemClickListener(this);

        dbAdapter = new DBAdapter(view.getContext());
        dbAdapter.openDB();

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

        Cursor cursor = dbAdapter.GetRow(id);
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
                                        try {
                                            adminResponse(email,usersPermissionsType[whichSelected]);
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

    private void adminResponse(final String email, final String permission) throws IOException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PCP_ADMIN_CHANGE_USER_PERMISSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ShowLogs.i("FragmentListOfAllUsers adminResponse  onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean errorCheck = jsonObject.getBoolean("error");
                    if (!errorCheck) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        ShowLogs.i("FragmentListOfAllUsers adminResponse errorMessage" + errorMessage);
                        Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    ShowLogs.i("FragmentListOfAllUsers adminResponse JSONException " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowLogs.i("FragmentListOfAllUsers adminResponse onErrorResponse  " + error.toString());
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

    @Override
    public void onStop() {
        super.onStop();
        dbAdapter.closeDB();
    }
}
