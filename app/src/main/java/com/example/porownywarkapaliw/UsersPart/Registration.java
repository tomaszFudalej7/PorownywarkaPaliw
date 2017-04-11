package com.example.porownywarkapaliw.UsersPart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.porownywarkapaliw.SQLDataBase.DBValues;
import com.example.porownywarkapaliw.ShowLogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends Fragment implements View.OnClickListener {

    private static final String PCP_REGISTRATION_URL="http://mrkostua.net16.net/PCPRegistration.php";

    private RequestQueue requestQueue;
    private ProgressBar pbARWaitingForResponse;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registration, container, false);
        pbARWaitingForResponse = (ProgressBar) view.findViewById(R.id.pbARWaitingForResponse);
        Button b = (Button) view.findViewById(R.id.bARRegistration);
        b.setOnClickListener(this);
        return view;
    }

    public void onResume() {
        super.onResume();
        pbARWaitingForResponse.setVisibility(View.GONE);
    }

    private void bARRegistration_OnClickListener(){
        pbARWaitingForResponse.setVisibility(View.VISIBLE);
        EditText etARName,etARSurname,etAREmail,etARTown,etARPhoneNumber,etARPassword;

        etARName = (EditText) view.findViewById(R.id.etARName);
        etARSurname = (EditText) view.findViewById(R.id.etARSurname);
        etAREmail = (EditText) view.findViewById(R.id.etAREmail);
        etARTown = (EditText) view.findViewById(R.id.etARTown);
        etARPhoneNumber = (EditText) view.findViewById(R.id.etARPhoneNumber);
        etARPassword = (EditText) view.findViewById(R.id.etARPassword);

        final String name = etARName.getText().toString().trim();
        final String surname = etARSurname.getText().toString().trim();//0
        final String email = etAREmail.getText().toString().trim();
        final String town = etARTown.getText().toString().trim();//0
        final String phoneNumber = etARPhoneNumber.getText().toString().trim();//0
        final String password = etARPassword.getText().toString().trim(); //remove the space from the begging and the end of the string

        if(!(password.isEmpty() || name.isEmpty() || email.isEmpty())){
            try {
                registerNewUser(name,surname,email,town,phoneNumber,password);
            } catch (JSONException e) {
                ShowLogs.i(e.getMessage());
            }
        }else {
            pbARWaitingForResponse.setVisibility(View.GONE);
            Toast.makeText(view.getContext(),"Pleas fill in all fields ",Toast.LENGTH_LONG).show();
        }
    }

    private void registerNewUser(final String name,final String surname,final String email,final String town,
                                 final String phoneNumber, final String password) throws JSONException{
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PCP_REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pbARWaitingForResponse.setVisibility(View.GONE);
                        ShowLogs.i("registerNewUser OnResponse  " + response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean errorCheck = jsonObject.getBoolean("error");
                            if(!errorCheck){
                                JSONObject userData = jsonObject.getJSONObject("userData");

                                String name = userData.getString("name");
                                String surname = userData.getString("surname");
                                String email = userData.getString("email");
                                String town = userData.getString("town");
                                String phoneNumber = userData.getString("phoneNumber");

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Registration user data")
                                        .setMessage("name :" + name + "\n" +
                                                "surname :" + surname + "\n"  +
                                                "email :" + email + "\n"  +
                                                "town :" + town + "\n" +
                                                "phone number :" + phoneNumber + "\n" ).create().show();
                            }
                            else {
                                String errorMessage = jsonObject.getString("errorMessage");
                                ShowLogs.i("errorMessage"+errorMessage);
                                Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e){
                            ShowLogs.i("registerNewUser OnResponse JSONException " + e.getMessage());
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowLogs.i("registerNewUser onErrorResponse  "+ error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(DBValues.KEY_NAME,name);
                params.put(DBValues.KEY_SURNAME,surname);
                params.put(DBValues.KEY_EMAIL,email);
                params.put(DBValues.KEY_TOWN,town);
                params.put(DBValues.KEY_PHONE_NUMBER,phoneNumber);
                params.put(DBValues.KEY_PERMISSION,"U"); // U user
                params.put(DBValues.KEY_PASSWORD,password);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

    }

    public void onStop() {
        super.onStop();
        if(requestQueue != null)
            requestQueue.cancelAll(ShowLogs.TAG);
        pbARWaitingForResponse.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bARRegistration:
                bARRegistration_OnClickListener();
                break;
        }
    }
}
