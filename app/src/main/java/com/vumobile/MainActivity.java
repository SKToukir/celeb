package com.vumobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.LogInAcitvity;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayoutOtpVerificationRegisteredUser;
    private RelativeLayout relativeLayout;
    private Button btnLogIn, btnNewUser, btnResendPinSmsR, btnSubmitCodeR;
    private EditText etMobileNumber, etVerificationCodeR;
    private String registeredPhone;
    private TextInputLayout userRegisteredphoneWrapper, userCodeWrapperR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {

        userCodeWrapperR = (TextInputLayout) findViewById(R.id.userCodeWrapperR);
        etVerificationCodeR = (EditText) findViewById(R.id.etVerificationCodeR);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        linearLayoutOtpVerificationRegisteredUser = (LinearLayout) findViewById(R.id.linearLayoutOtpVerificationRegisteredUser);
        userRegisteredphoneWrapper = (TextInputLayout) findViewById(R.id.userRegisteredphoneWrapper);
        etMobileNumber = (EditText) findViewById(R.id.etRegisteredPhone);
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        btnNewUser = (Button) findViewById(R.id.btnNewUser);
        btnResendPinSmsR = (Button) findViewById(R.id.btnResendPinSmsR);
        btnSubmitCodeR = (Button) findViewById(R.id.btnSubmitCodeR);

        btnSubmitCodeR.setOnClickListener(this);
        btnResendPinSmsR.setOnClickListener(this);
        btnNewUser.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnLogin:

                registeredPhone = etMobileNumber.getText().toString();

                if (registeredPhone.isEmpty()) {
                    userRegisteredphoneWrapper.setError("Required");
                } else {
                    userRegisteredphoneWrapper.setError(null);
                }
                if (userRegisteredphoneWrapper.getError() == null && userRegisteredphoneWrapper.getError() == null) {
                    if (registeredPhone.startsWith("01")) {
                        registeredPhone = "88" + registeredPhone;
                        Log.d("msisdn", registeredPhone);

                        showConfirmDialog(registeredPhone);

                    } else if (registeredPhone.equals("") || registeredPhone.equals(" ")
                            || registeredPhone.equals(null) || registeredPhone.isEmpty()
                            || registeredPhone.length() > 13 || registeredPhone.length() < 11 || !registeredPhone.startsWith("01")) {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter Correct Mobile Number",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btnNewUser:
                Intent intent = new Intent(MainActivity.this, LogInAcitvity.class);
                intent.putExtra("msisdn", registeredPhone);
                intent.putExtra("reqflag", "0");
                intent.putExtra("userflag", "faka");
                startActivity(intent);
                finish();
                break;
            case R.id.btnResendPinSmsR:
                break;
            case R.id.btnSubmitCodeR:
                break;
        }
    }

    private void showConfirmDialog(String registeredPhone) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Is this your mobile number?\n\n" + "+" + registeredPhone + "\n\n\nYou will receive sms soon");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                checkRegOrNot(registeredPhone);

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void checkRegOrNot(String registeredPhone) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://wap.shabox.mobi/testwebapi/Celebrity/Identity?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre&MSISDN="+registeredPhone, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("result");

                    JSONObject object  = array.getJSONObject(0);

                    String user = object.getString("user");
                    String status = object.getString("status");

                    if (status.equals("1")){

                        if (user.equals("0")){
                            new Session().saveCelebState(MainActivity.this, false);
                            new Session().saveFbLoginStatus(MainActivity.this, true);
                            new Session().saveMsisdn(registeredPhone, MainActivity.this);
                        }else {
                            new Session().saveCelebState(MainActivity.this, true);
                            new Session().saveFbLoginStatus(MainActivity.this, true);
                            new Session().saveMsisdn(registeredPhone, MainActivity.this);
                        }

                        Intent intent = new Intent(MainActivity.this, LogInAcitvity.class);
                        intent.putExtra("msisdn", registeredPhone);
                        intent.putExtra("reqflag", "1");
                        intent.putExtra("userflag", user);
                        startActivity(intent);
                        finish();
                    }else if (status.equals("0")){
                        TastyToast.makeText(getApplicationContext(),"You are not registered!",TastyToast.LENGTH_LONG,TastyToast.INFO);
                        startActivity(new Intent(MainActivity.this, LogInAcitvity.class));
                    }else {
                        TastyToast.makeText(getApplicationContext(),"Something wrong!",TastyToast.LENGTH_LONG,TastyToast.INFO);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
