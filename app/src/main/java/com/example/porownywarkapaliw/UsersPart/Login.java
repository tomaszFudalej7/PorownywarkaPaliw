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
import com.example.porownywarkapaliw.FragmentHelper;
import com.example.porownywarkapaliw.ManagePart.Admin;
import com.example.porownywarkapaliw.R;
import com.example.porownywarkapaliw.SQLDataBase.DBValues;
import com.example.porownywarkapaliw.ShowLogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment implements View.OnClickListener {
    private static final String PCP_LOGIN_URL = "http://mrkostua.net16.net/PCPLogowanie.php";

    private ProgressBar pbALWaitingForResponse;
    private RequestQueue requestQueue;
    private FragmentHelper fragmentHelper;

    private View view;

    @Override
    public String toString() {
        return "Fragment Login";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHelper = new FragmentHelper(getActivity());

        view = inflater.inflate(R.layout.activity_login, container, false);
        pbALWaitingForResponse = (ProgressBar) view.findViewById(R.id.pbALWaitingForResponse);
        Button b = (Button) view.findViewById(R.id.bALLogin);
        View bRegistration = view.findViewById(R.id.tvALRegistrationHere);
        b.setOnClickListener(this);
        bRegistration.setOnClickListener(this);
        return view;
    }

    public void onResume() {
        super.onResume();
        pbALWaitingForResponse.setVisibility(View.GONE);
    }

    private void etALPassword_ClickListener() {
        pbALWaitingForResponse.setVisibility(View.VISIBLE);

        EditText etALPhoneNumber, etALPassword;
        etALPassword = (EditText) view.findViewById(R.id.etALPassword);
        etALPhoneNumber = (EditText) view.findViewById(R.id.etALPhoneNumber);

        final String phoneNumber = etALPhoneNumber.getText().toString().trim();
        final String password = etALPassword.getText().toString().trim();

        if (!(phoneNumber.isEmpty() || password.isEmpty())) {
            try {
                loginResponse(phoneNumber, password);
            } catch (IOException e) {
                ShowLogs.i(" IOException etALPassword_ClickListener " + e.getMessage());
            }
        } else {
            Toast.makeText(view.getContext(), "Please fill all statements first", Toast.LENGTH_LONG).show();
        }
    }

    private void loginResponse(final String email, final String password) throws IOException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PCP_LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbALWaitingForResponse.setVisibility(View.GONE);
                ShowLogs.i("loginResponse  onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean errorCheck = jsonObject.getBoolean("error");
                    if (!errorCheck) {
                        JSONObject userData = jsonObject.getJSONObject("userData");
                        String permission = userData.getString("permission");
                        switch (permission) {
                            case "A":
                                fragmentHelper.loadFragment(new Admin());
                                break;
                            case "M":
                                break;
                            case "U":
                                String name = userData.getString("name");
                                String surname = userData.getString("surname");
                                String email = userData.getString("email");
                                String town = userData.getString("town");
                                String phoneNumber = userData.getString("phoneNumber");

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Login user data")
                                        .setMessage("name :" + name + "\n" +
                                                "permission :" + permission + "\n" +
                                                "surname :" + surname + "\n" +
                                                "email :" + email + "\n" +
                                                "town :" + town + "\n" +
                                                "phone number :" + phoneNumber + "\n").create().show();
                                break;
                        }
                    } else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        ShowLogs.i("loginResponse errorMessage" + errorMessage);
                        Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    ShowLogs.i("loginResponse JSONException " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowLogs.i("loginResponse onErrorResponse  " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(DBValues.COLUMN_KEY_PASSWORD, password);
                params.put(DBValues.COLUMN_KEY_EMAIL, email);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null)
            requestQueue.cancelAll(ShowLogs.TAG);
        pbALWaitingForResponse.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bALLogin:
                etALPassword_ClickListener();
                break;
            case R.id.tvALRegistrationHere:
                fragmentHelper.loadFragment(new Registration());
                break;
        }
    }
}
