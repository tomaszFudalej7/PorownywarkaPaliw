package com.example.porownywarkapaliw.UsersPart;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.porownywarkapaliw.R;
import com.example.porownywarkapaliw.ShowLogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String PCP_LOGIN_URL="http://mrkostua.net16.net/PCPLogowanie.php";
    private static final String KEY_PASSWORD="password";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";

    private ProgressBar pbALWaitingForResponse;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pbALWaitingForResponse = (ProgressBar) findViewById(R.id.pbALWaitingForResponse);
    }
    @Override
    protected void onResume() {
        super.onResume();
        pbALWaitingForResponse.setVisibility(View.GONE);
    }

    public void etALPassword_ClickListener(View view){
        pbALWaitingForResponse.setVisibility(View.VISIBLE);

        EditText etALPhoneNumber,etALPassword;
        etALPassword = (EditText) findViewById(R.id.etALPassword);
        etALPhoneNumber = (EditText) findViewById(R.id.etALPhoneNumber);

        final String phoneNumber = etALPhoneNumber.getText().toString().trim();
        final String password = etALPassword.getText().toString().trim();

        if(!(phoneNumber.isEmpty() || password.isEmpty())){
            try {
                loginResponse(phoneNumber,password);
            } catch (IOException e) {
                ShowLogs.i(" IOException etALPassword_ClickListener " + e.getMessage());
            }
        }else {
            Toast.makeText(Login.this,"Please fill all statements first",Toast.LENGTH_LONG).show();
        }
    }

    private void loginResponse(final String phoneNumber,final String password) throws IOException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PCP_LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbALWaitingForResponse.setVisibility(View.GONE);
                ShowLogs.i("loginResponse  " + response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean errorCheck = jsonObject.getBoolean("error");
                    if(!errorCheck){
                        JSONObject userData = jsonObject.getJSONObject("userData");
                        String username = userData.getString("username");
                        String phoneNumber = userData.getString("phoneNumber");

                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Login user data")
                                .setMessage("username :" + username + "\n" +
                                        "phone number :" + phoneNumber + "\n" ).create().show();
                    }
                    else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e){
                    ShowLogs.i("loginResponse JSONException " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowLogs.i("loginResponse onErrorResponse  "+ error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(KEY_PASSWORD,password);
                params.put(KEY_PHONE_NUMBER,phoneNumber);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
    }



    public void tvALRegistrationHere_ClickListener(View view){
        Intent intent = new Intent(Login.this,Registration.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null)
            requestQueue.cancelAll(ShowLogs.TAG);
        pbALWaitingForResponse.setVisibility(View.GONE);
    }
}
