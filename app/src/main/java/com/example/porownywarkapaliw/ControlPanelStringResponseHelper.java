package com.example.porownywarkapaliw;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.porownywarkapaliw.SQLDataBase.DBValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by Администратор on 22.04.2017.
 */

public class ControlPanelStringResponseHelper {
    private Context context;
    private Boolean result = false;


    public ControlPanelStringResponseHelper(Context context){
        this.context = context;
    }

    public Boolean stringRequestChangeUserPermissionStatus(final String email, final String permission) throws IOException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs_store.URL_CHANGE_USER_PERMISSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ShowLogs.i("ControlPanelStringResponseHelper stringRequestChangeUserPermissionStatus  onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean errorCheck = jsonObject.getBoolean("error");
                    if (!errorCheck) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        result = true;
                    }
                    else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        ShowLogs.i("ControlPanelStringResponseHelper stringRequestChangeUserPermissionStatus errorMessage" + errorMessage);
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        result = false;
                    }
                } catch (JSONException e) {
                    ShowLogs.i("ControlPanelStringResponseHelper stringRequestChangeUserPermissionStatus JSONException " + e.getMessage());
                    result = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result = false;
                ShowLogs.i("ControlPanelStringResponseHelper stringRequestChangeUserPermissionStatus onErrorResponse  " + error.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return result;
    }

    public Boolean stringRequestBlockUser(final String email,final String blockStatus) throws IOException{
        result = true;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs_store.URL_BLOCK_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ShowLogs.i("ControlPanelStringResponseHelper stringRequestBlockUser  onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean errorCheck = jsonObject.getBoolean("error");
                    if (!errorCheck) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        result = true;

                    }
                    else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        ShowLogs.i("ControlPanelStringResponseHelper stringRequestBlockUser errorMessage" + errorMessage);
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        result = false;

                    }
                } catch (JSONException e) {
                    ShowLogs.i("ControlPanelStringResponseHelper stringRequestBlockUser JSONException " + e.getMessage());
                    result = false;

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result = false;

                ShowLogs.i("ControlPanelStringResponseHelper stringRequestBlockUser onErrorResponse  " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(DBValues.COLUMN_KEY_EMAIL,email);
                params.put(DBValues.COLUMN_KEY_BLOCK_STATUS,blockStatus);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        return result;
    }
}
