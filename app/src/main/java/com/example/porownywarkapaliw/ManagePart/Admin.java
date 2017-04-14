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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.porownywarkapaliw.FragmentHelper;
import com.example.porownywarkapaliw.R;
import com.example.porownywarkapaliw.SQLDataBase.DBAdapter;
import com.example.porownywarkapaliw.SQLDataBase.DBValues;
import com.example.porownywarkapaliw.ShowLogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin extends Fragment implements View.OnClickListener {

    private static final String PCP_ADMIN_SHOW_ALL_USERS_URL = "http://mrkostua.net16.net/AdminGetAllUsers.php";
    private View view;
    private FragmentHelper fragmentHelper;
    private ProgressBar pbAD_waitForResponse;
    private RequestQueue requestQueue;

    private String usersPermissionsType [] ={"U","A","M"};
    private int type = 0;
    private List<UserObject> userObjectsList;

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

        userObjectsList = new ArrayList<>();
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

    private void showAllUsers_StringResponse(final String permission) throws IOException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,PCP_ADMIN_SHOW_ALL_USERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ShowLogs.i("showAllUsers_StringResponse  onResponse" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONObject userData = jsonObject.getJSONObject("userData");
                        UserObject userObject = new UserObject(Integer.parseInt(userData.getString("id")),
                                userData.getString("name"),userData.getString("surname"),
                                userData.getString("email"),userData.getString("town"),userData.getString("phoneNumber"),
                                userData.getString("permission"),userData.getString("creationData"));

                        userObjectsList.add(userObject);
                        ShowLogs.i("Admin.class added new userObject size :" +userObjectsList.size());
                    }
                    else {
                        pbAD_waitForResponse.setVisibility(View.GONE);
                        Toast.makeText(view.getContext(), "acquired some problems :)", Toast.LENGTH_SHORT).show();
                        ShowLogs.i("Admin.class showAllUsers_StringResponse errorMessage :" + jsonObject.getString("errorMessage"));
                    }

                } catch (JSONException e) {
                    pbAD_waitForResponse.setVisibility(View.GONE);
                    Toast.makeText(view.getContext(), "acquired some problems :)", Toast.LENGTH_SHORT).show();
                    ShowLogs.i("Admin.class showAllUsers_StringResponse JSONException :" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbAD_waitForResponse.setVisibility(View.GONE);
                Toast.makeText(view.getContext(), "acquired some problems :)", Toast.LENGTH_SHORT).show();
                ShowLogs.i("Admin.class showAllUsers_StringResponse onErrorResponse :" + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put(DBValues.COLUMN_KEY_PERMISSION,permission);
                return param;
            }
        };
        requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
}

    @Override
    public void onStop() {
        super.onStop();
        dbAdapter.closeDB();
        if (requestQueue != null)
            requestQueue.cancelAll(ShowLogs.TAG);
        pbAD_waitForResponse.setVisibility(View.GONE);
    }

    private void bAA_ShowListOfAllUsersClickMethod(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Choose users permission")
                .setSingleChoiceItems(usersPermissionsType, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type=which;
                    }
                }).setPositiveButton("show", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pbAD_waitForResponse.setVisibility(View.VISIBLE);
                        ShowLogs.i("bAA_ShowListOfAllUsersClickMethod setPositiveButton");

                        try {
                            showAllUsers_StringResponse(usersPermissionsType[type]);
                            ShowLogs.i("showAllUsers_StringResponse finished");
                        }
                        catch (IOException e) {
                            ShowLogs.i(" IOException bAA_ShowListOfAllUsersClickMethod " + e.getMessage());
                        }
                            for(UserObject o: userObjectsList){
                                dbAdapter.GetDataToSaveInDB(o.getId(),o.getName(),o.getSurname(),o.getEmail(),
                                        o.getTown(),o.getPhoneNumber(),o.getPermission(),o.getCreationData());
                                ShowLogs.i("bAA_ShowListOfAllUsersClickMethod for each" + o.toString());
                                if(dbAdapter.InsertRow() == -1){
                                    ShowLogs.i("Admin.class bAA_ShowListOfAllUsersClickMethod insert row error");
                                }
                            }


                    }
                }).setNegativeButton("back",null).create().show();

        pbAD_waitForResponse.setVisibility(View.GONE);
        fragmentHelper.loadFragment(new FragmentListOfAllUsers());
    }
}
