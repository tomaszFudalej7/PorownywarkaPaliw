package com.example.porownywarkapaliw.ManagePart;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.porownywarkapaliw.FragmentHelper;
import com.example.porownywarkapaliw.R;
import com.example.porownywarkapaliw.SQLDataBase.DBAdapter;
import com.example.porownywarkapaliw.SQLDataBase.DBValues;
import com.example.porownywarkapaliw.ShowLogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Admin extends Fragment implements View.OnClickListener {

    private static final String PCP_ADMIN_SHOW_ALL_USERS_URL = "http://mrkostua.net16.net/AdminGetAllUsers.php";
    private View view;
    private FragmentHelper fragmentHelper;
    private ProgressBar pbAD_waitForResponse;
    private RequestQueue requestQueue;

    private String usersPermissionsType [] ={"U","A","M"};
    //private int type = 0;
    private DBAdapter dbAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_admin,container,false);
        fragmentHelper = new FragmentHelper(getActivity());

        pbAD_waitForResponse = (ProgressBar) view.findViewById(R.id.pbAD_waitForResponse);
        pbAD_waitForResponse.setVisibility(View.GONE);

        dbAdapter = new DBAdapter(view.getContext());
        dbAdapter.openDB();

        Button bAA_ShowListOfAllUsers = (Button) view.findViewById(R.id.bAA_ShowListOfAllUsers);
        bAA_ShowListOfAllUsers.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bAA_ShowListOfAllUsers:
                bAA_ShowListOfAllUsersClickMethod();
            break;
        }
    }

    private void bAA_ShowListOfAllUsersClickMethod(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Choose users permission")
                .setSingleChoiceItems(usersPermissionsType, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //type=which;
                    }
                }).setPositiveButton("show", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pbAD_waitForResponse.setVisibility(View.VISIBLE);
                ShowLogs.i("bAA_ShowListOfAllUsersClickMethod setPositiveButton");
                try {
                    showAllUsers_StringResponse();
                }
                catch (IOException e) {
                    ShowLogs.i(" IOException bAA_ShowListOfAllUsersClickMethod " + e.getMessage());
                }
            }
        }).setNegativeButton("back",null).create().show();
    }

    private void showAllUsers_StringResponse() throws IOException {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(PCP_ADMIN_SHOW_ALL_USERS_URL,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    ShowLogs.i("showAllUsers_StringResponse  onResponse" + response);
                    parseJSONArrayData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbAD_waitForResponse.setVisibility(View.GONE);
                Toast.makeText(view.getContext(), "acquired some problems :)", Toast.LENGTH_SHORT).show();
                ShowLogs.i("Admin.class showAllUsers_StringResponse onErrorResponse :" + error.getMessage());
            }
        });
        requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(jsonArrayRequest);
}

    private void parseJSONArrayData(JSONArray jsonArray){
        JSONObject json;
        List<UserObject> userObjectsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            UserObject userObject = new UserObject();
            json = null;
            try {
                json = jsonArray.getJSONObject(i);
                userObject.setId(json.getInt(DBValues.COLUMN_KEY_ID));
                userObject.setName(json.getString(DBValues.COLUMN_KEY_NAME));
                userObject.setSurname(json.getString(DBValues.COLUMN_KEY_SURNAME));
                userObject.setEmail(json.getString(DBValues.COLUMN_KEY_EMAIL));
                userObject.setTown(json.getString(DBValues.COLUMN_KEY_TOWN));
                userObject.setPhoneNumber(json.getString(DBValues.COLUMN_KEY_PHONE_NUMBER));
                userObject.setPermission(json.getString(DBValues.COLUMN_KEY_PERMISSION));
                userObject.setCreationData(json.getString(DBValues.COLUMN_KEY_CREATION_DATA));
            }
            catch (JSONException e) {
                ShowLogs.i("Admin.class parseJSONArrayData JSONException :" + e.getMessage());
            }
            userObjectsList.add(userObject);
        }
        insertUsersDataToDB(userObjectsList);
    }

    private void insertUsersDataToDB(List<UserObject> userObjectsList){
        dbAdapter.DeleteAllRows();
        for(UserObject o: userObjectsList){
            dbAdapter.GetDataToSaveInDB(o.getId(),o.getName(),o.getSurname(),o.getEmail(),
                    o.getTown(),o.getPhoneNumber(),o.getPermission(),o.getCreationData());
            ShowLogs.i("bAA_ShowListOfAllUsersClickMethod for each" + o.toString());
            if(dbAdapter.InsertRow() == -1){
                ShowLogs.i("Admin.class bAA_ShowListOfAllUsersClickMethod insert row error");
            }
        }
        pbAD_waitForResponse.setVisibility(View.GONE);
        fragmentHelper.loadFragment(new FragmentListOfAllUsers());
    }


    @Override
    public void onStop() {
        super.onStop();
        dbAdapter.closeDB();
        if (requestQueue != null)
            requestQueue.cancelAll(ShowLogs.TAG);
        pbAD_waitForResponse.setVisibility(View.GONE);
    }
}
