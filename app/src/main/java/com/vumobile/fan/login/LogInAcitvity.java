package com.vumobile.fan.login;

import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.ParentActivity;
import com.vumobile.celeb.R;
import com.vumobile.celeb.ui.CelebrityProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInAcitvity extends AppCompatActivity implements View.OnClickListener {

    PendingIntent pendingIntent;
    private static final String TAG = "LogInAcitvity.java";
    private EditText etUserName, etUserPhone, etVerificationCode;
    private TextView txtBecomeCeleb, txtCopyright;
    private String uName, uPhone, verificationCode, tempVerificationCode;
    private Button btnSubmitCode, btnLogInCont;
    TextInputLayout userphoneWrapper, usernameWrapper, userCodeWrapper;
    private boolean isCeleb;
    public static String whichButtonClicked;

    private LinearLayout linearLayoutLoginMain, linearLayoutOtpVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_acitvity);


        initUI();

        isLogin();

    }



    private void isLogin() {

        boolean celebOrNot = new Session().isCeleb(LogInAcitvity.this, Session.IS_CELEB);

        if (Session.isLogin(LogInAcitvity.this, Session.CHECK_LOGIN)) {

            if (celebOrNot) {
                Intent intent = new Intent(LogInAcitvity.this, CelebrityProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
            } else {

                if (Session.isFbLogIn(getApplicationContext(),Session.FB_LOGIN_STATUS)){
                    Intent intent = new Intent(LogInAcitvity.this, ParentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                }else {
                    Intent intent = new Intent(LogInAcitvity.this, CelebrityProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                }
            }


        }
    }

    private void initUI() {

        etVerificationCode = (EditText) findViewById(R.id.etVerificationCode);
        userCodeWrapper = (TextInputLayout) findViewById(R.id.userCodeWrapper);
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        userphoneWrapper = (TextInputLayout) findViewById(R.id.userphoneWrapper);
        btnSubmitCode = (Button) findViewById(R.id.btnSubmitCode);
        btnLogInCont = (Button) findViewById(R.id.btnLoginCont);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserPhone = (EditText) findViewById(R.id.etUserPhone);
        txtBecomeCeleb = (TextView) findViewById(R.id.txt_become_celeb);
        txtCopyright = (TextView) findViewById(R.id.txtCopyright);

        txtBecomeCeleb.setOnClickListener(this);

        linearLayoutLoginMain = (LinearLayout) findViewById(R.id.linearLayoutLoginMain);
        linearLayoutOtpVerification = (LinearLayout) findViewById(R.id.linearLayoutOtpVerification);


    }

    // login for fan
    public void btnLoginCont(View view) {

        isCeleb = false;
        uName = etUserName.getText().toString();
        uPhone = etUserPhone.getText().toString();


        if (uName.isEmpty()) {
            usernameWrapper.setError("Required");
        } else {
            usernameWrapper.setError(null);
        }

        if (uPhone.isEmpty()) {
            userphoneWrapper.setError("Required");
        } else {
            userphoneWrapper.setError(null);
        }

        if (usernameWrapper.getError() == null && userphoneWrapper.getError() == null) {


            if (uPhone.startsWith("01")) {
                uPhone = "88" + uPhone;
                Log.d("msisdn", uPhone);

                showConfirmDialog(uPhone, "0");


            } else if (uPhone.equals("") || uPhone.equals(" ")
                    || uPhone.equals(null) || uPhone.isEmpty()
                    || uPhone.length() > 13 || uPhone.length() < 11 || !uPhone.startsWith("01")) {
                Toast.makeText(getApplicationContext(),
                        "Please Enter Correct Mobile Number",
                        Toast.LENGTH_LONG).show();
            }


            //TODO
            //server request for verification code
            //temporary save verification code into 'tempVerificationCode' comes from server
            //if user close app tempVerificationCode will be null
            tempVerificationCode = "vhgdd";
        }

    }

    private void showConfirmDialog(String uPhone, String rqst) {

        whichButtonClicked = rqst;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Is this your mobile number?\n\n" + "+" + uPhone + "\n\n\nYou will receive sms soon");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                otpRequest(rqst);
                // only visible verify button
//                userphoneWrapper.setVisibility(View.GONE);
//                txtBecomeCeleb.setVisibility(View.GONE);
//                usernameWrapper.setVisibility(View.GONE);
//                btnLogInCont.setVisibility(View.GONE);
//                btnSubmitCode.setVisibility(View.VISIBLE);
//                userCodeWrapper.setVisibility(View.VISIBLE);
                linearLayoutLoginMain.setVisibility(View.GONE);
                linearLayoutOtpVerification.setVisibility(View.VISIBLE);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void otpRequest(String s) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_OTP_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            tempVerificationCode = jsonObj.getString("CODE");
                            etVerificationCode.setText(tempVerificationCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer", "" + error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MSISDN", uPhone);
                params.put("flag", s);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void btnSubmitCode(View view) {

        // here user put verification code
        verificationCode = etVerificationCode.getText().toString();

        if (verificationCode.isEmpty()) {
            userCodeWrapper.setError("Required");
        } else {
            userCodeWrapper.setError(null);
        }

        if (userCodeWrapper.getError() == null) {

            // TODO
            /*Hit server to check verification code.
            If verification code check success then go to home page*/

            if (tempVerificationCode.equals(verificationCode)) {
                // if verification code length is 6 then it will be fan
                if (verificationCode.length() == 6) {
                    new Session().saveData(uName, uPhone, isCeleb, true, LogInAcitvity.this);
                    Intent intent = new Intent(LogInAcitvity.this, CelebrityProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    // if verification code length is less then 6 then it will be celebrity
                } else if (verificationCode.length() < 6) {
                    new Session().saveData(uName, uPhone, isCeleb, true, LogInAcitvity.this);
                    Intent intent = new Intent(LogInAcitvity.this, CelebrityProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "You enter incorrect code", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            // become a celebrity
            case R.id.txt_become_celeb:

                isCeleb = true;
                uName = etUserName.getText().toString();
                uPhone = etUserPhone.getText().toString();


                if (uName.isEmpty()) {
                    usernameWrapper.setError("Required");
                } else {
                    usernameWrapper.setError(null);
                }

                if (uPhone.isEmpty()) {
                    userphoneWrapper.setError("Required");
                } else {
                    userphoneWrapper.setError(null);
                }

                if (usernameWrapper.getError() == null && userphoneWrapper.getError() == null) {

                    if (uPhone.startsWith("01")) {
                        uPhone = "88" + uPhone;

                        Log.d("msisdn", uPhone);
                        showConfirmDialog(uPhone, "1");
//                        otpRequest("1");
//                        // only visible verify button
//                        userphoneWrapper.setVisibility(View.GONE);
//                        txtBecomeCeleb.setVisibility(View.GONE);
//                        usernameWrapper.setVisibility(View.GONE);
//                        btnLogInCont.setVisibility(View.GONE);
//                        btnSubmitCode.setVisibility(View.VISIBLE);
//                        userCodeWrapper.setVisibility(View.VISIBLE);

                    } else if (uPhone.equals("") || uPhone.equals(" ")
                            || uPhone.equals(null) || uPhone.isEmpty()
                            || uPhone.length() > 13 || uPhone.length() < 11 || !uPhone.startsWith("01")) {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter Correct Mobile Number",
                                Toast.LENGTH_LONG).show();
                    }

                }

                break;
        }

    }

    public void btnResendPinSms(View view) {
        otpRequest(whichButtonClicked);
        Toast.makeText(this, "Request has been send, Please wait.", Toast.LENGTH_SHORT).show();
    }
}
