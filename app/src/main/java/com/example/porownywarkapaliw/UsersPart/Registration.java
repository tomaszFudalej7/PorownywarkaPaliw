package com.example.porownywarkapaliw.UsersPart;

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

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private static final String PCP_REGISTRATION_URL="http://mrkostua.net16.net/PCPRegistration.php";
    private static final String KEY_USERNAME="username";
    private static final String KEY_PASSWORD="password";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";

    private RequestQueue requestQueue;
    private ProgressBar pbARWaitingForResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pbARWaitingForResponse = (ProgressBar) findViewById(R.id.pbARWaitingForResponse);
    }



    public void bARRegistration_OnClickListener(View view){
         EditText etARUsername,etARPassword,etARPhoneNumber;

        etARPassword = (EditText) findViewById(R.id.etARPassword);
        etARUsername = (EditText) findViewById(R.id.etARUsername);
        etARPhoneNumber = (EditText) findViewById(R.id.etARPhoneNumber);
        pbARWaitingForResponse.setVisibility(View.VISIBLE);

        final String password = etARPassword.getText().toString().trim(); //remove the space from the begging and the end of the string
        final String username = etARUsername.getText().toString().trim();
        String phoneNumber = etARPhoneNumber.getText().toString().trim();
        if(!(password.isEmpty() || username.isEmpty() || phoneNumber.isEmpty())){
            try {
                registerNewUser(password,username,phoneNumber);
            } catch (JSONException e) {
                ShowLogs.i(e.getMessage());
            }
        }else {
            Toast.makeText(this,"Pleas fill in all fields ",Toast.LENGTH_LONG).show();
        }
    }

    private void registerNewUser(final String password, final String username, final String phoneNumber) throws JSONException{
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PCP_REGISTRATION_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbARWaitingForResponse.setVisibility(View.GONE);
                switch (response){
                    case "success":{
                    Toast.makeText(Registration.this,"Great, you in",Toast.LENGTH_LONG).show();
                    }break;
                    case "phoneNumber exist in DB":{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                        builder.setTitle("error")
                                .setMessage("This phone number :" + phoneNumber + " already exist, please use different number to continue registration")
                                .create().show();
                    }break;
                }
                ShowLogs.i("OnResponse  " + response);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowLogs.i("onErrorResponse  "+ error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_PHONE_NUMBER,phoneNumber);
                return params;
            }
        };
         requestQueue = Volley.newRequestQueue(Registration.this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null)
            requestQueue.cancelAll(ShowLogs.TAG);
        pbARWaitingForResponse.setVisibility(View.GONE);
    }
}
