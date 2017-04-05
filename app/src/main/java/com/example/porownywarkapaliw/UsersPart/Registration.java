package com.example.porownywarkapaliw.UsersPart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }



    public void bARRegistration_OnClickListener(View view){
         EditText etARUsername,etARPassword;

        etARPassword = (EditText) findViewById(R.id.etARPassword);
        etARUsername = (EditText) findViewById(R.id.etARUsername);

        final String password = etARPassword.getText().toString().trim();
        final String username = etARUsername.getText().toString().trim();
        if(!(password.isEmpty() || username.isEmpty() || password == null || username==null)){
            try {
                registerNewUser(password,username);
            } catch (JSONException e) {
                ShowLogs.i(e.getMessage());
            }
        }else {
            Toast.makeText(this,"Pleas fill in all fields",Toast.LENGTH_LONG).show();
        }
    }

    private void registerNewUser(final String password, final String username) throws JSONException{
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PCP_REGISTRATION_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ShowLogs.i("OnResponse"+response);
                Toast.makeText(Registration.this,response, Toast.LENGTH_LONG).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowLogs.i("onErrorResponse"+ error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        requestQueue.add(stringRequest);

    }

}
