package com.vumobile.fan.login;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.vumobile.Config.Api;
import com.vumobile.ParentActivity;
import com.vumobile.celeb.R;
import com.vumobile.celeb.ui.CelebHomeActivity;
import com.vumobile.celeb.ui.CelebrityProfileActivity;
import com.vumobile.utils.MyInternetCheckReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInAcitvity extends AppCompatActivity implements View.OnClickListener {

    public boolean alreadyReg = false;
    private static final int REQUEST_GET_ACCOUNT = 112;
    PendingIntent pendingIntent;
    private static final String TAG = "LogInAcitvity.java";
    public EditText etUserName, etUserPhone;
    public static EditText etVerificationCode;
    private TextView txtBecomeCeleb, txtCopyright;
    private String uName, uPhone, verificationCode, tempVerificationCode;
    private Button btnSubmitCode, btnLogInCont, btnNewUser, btnLogin;
    TextInputLayout userphoneWrapper, usernameWrapper, userCodeWrapper;
    String msisdn = "null";
    private boolean isCeleb;
    public static String whichButtonClicked;
    String reqflag = "0";

    private LinearLayout activity_log_in_acitvity, linearLayoutLoginMain, linearLayoutOtpVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_acitvity);

        initUI();

        if (android.os.Build.VERSION.SDK_INT > 22) {
            if (isReadStorageAllowed()) {
                isLogin();
                return;
            } else {
                requestStoragePermission();
            }
        } else {
            isLogin();
        }

        // show snackbar while no internet
        MyInternetCheckReceiver.isNetworkAvailableShowSnackbar(this, activity_log_in_acitvity);
        new MyInternetCheckReceiver(activity_log_in_acitvity);

    } // end of onCreate

    private boolean isReadStorageAllowed() {

        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int result4 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int result5 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result6 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result7 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED &&
                result5 == PackageManager.PERMISSION_GRANTED &&
                result6 == PackageManager.PERMISSION_GRANTED &&
                result7 == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.GET_ACCOUNTS) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {

            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_CALENDAR}, REQUEST_GET_ACCOUNT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_GET_ACCOUNT:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean BIND_NOTIFICATION_LISTENER_SERVICE = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted && BIND_NOTIFICATION_LISTENER_SERVICE) {

                    }
                    // Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        //Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.GET_ACCOUNTS)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS,
                                                                    Manifest.permission.READ_PHONE_STATE,
                                                                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR},
                                                            REQUEST_GET_ACCOUNT);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LogInAcitvity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void isLogin() {

        boolean celebOrNot = new Session().isCeleb(LogInAcitvity.this, Session.IS_CELEB);

        if (Session.isLogin(LogInAcitvity.this, Session.CHECK_LOGIN)) {

            if (celebOrNot) {
                if (Session.isFbLogIn(getApplicationContext(), Session.FB_LOGIN_STATUS) == false) {
                    Intent intent = new Intent(LogInAcitvity.this, CelebrityProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                } else if (Session.isReg(getApplicationContext(), Session.REGISTERED_CELEB) == true) {
                    Intent intent = new Intent(LogInAcitvity.this, CelebHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                } else {
                    Intent intent = new Intent(LogInAcitvity.this, CelebrityProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                }
            } else {

                if (Session.isFbLogIn(getApplicationContext(), Session.FB_LOGIN_STATUS)) {
                    Intent intent = new Intent(LogInAcitvity.this, ParentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                }
//                else {
//                    Intent intent = new Intent(LogInAcitvity.this, CelebrityProfileActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    this.finish();
//                }

            }


        }
    }

    private void initUI() {

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnNewUser = (Button) findViewById(R.id.btnNewUser);
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

        activity_log_in_acitvity = (LinearLayout) findViewById(R.id.activity_log_in_acitvity);
        linearLayoutLoginMain = (LinearLayout) findViewById(R.id.linearLayoutLoginMain);
        linearLayoutOtpVerification = (LinearLayout) findViewById(R.id.linearLayoutOtpVerification);


    }

    // login for fan
    public void btnLoginCont(View view) {

        isCeleb = false;
        //uName = etUserName.getText().toString();
        uName = "null";
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

                linearLayoutLoginMain.setVisibility(View.GONE);
                linearLayoutOtpVerification.setVisibility(View.VISIBLE);
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

    private void otpRequest(String s) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_OTP_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            tempVerificationCode = jsonObj.getString("CODE");
                            //etVerificationCode.setText(tempVerificationCode);
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
            /* Hit server to check verification code.
            If verification code check success then go to home page */

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

        if (btnLogin.getVisibility() == View.GONE) {

            btnLogin.setVisibility(View.VISIBLE);
            btnNewUser.setVisibility(View.VISIBLE);
            txtBecomeCeleb.setVisibility(View.GONE);
            btnLogInCont.setVisibility(View.GONE);

        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            // become a celebrity
            case R.id.txt_become_celeb:

                isCeleb = true;
                //uName = etUserName.getText().toString();
                uName = "null";
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


    public void btnNewUser(View view) {

        btnLogin.setVisibility(View.GONE);
        btnNewUser.setVisibility(View.GONE);
        txtBecomeCeleb.setVisibility(View.VISIBLE);
        btnLogInCont.setVisibility(View.VISIBLE);

    }

    public void btnLogin(View view) {

        uName = "null";
        uPhone = etUserPhone.getText().toString();


        if (uPhone.isEmpty()) {
            userphoneWrapper.setError("Required");
        } else {
            userphoneWrapper.setError(null);
        }

        if (usernameWrapper.getError() == null && userphoneWrapper.getError() == null) {

            if (uPhone.startsWith("01")) {
                uPhone = "88" + uPhone;

                Log.d("msisdn", uPhone);
                //showConfirmDialog(uPhone, "1");

                checkRegOrNot(uPhone);
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

    }

    private void checkRegOrNot(String registeredPhone) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://wap.shabox.mobi/testwebapi/Celebrity/Identity?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre&MSISDN=" + registeredPhone, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("result");

                    JSONObject object = array.getJSONObject(0);

                    String user = object.getString("user");
                    String status = object.getString("status");

                    if (user.equals("1")) {
                        isCeleb = true;
                    } else {
                        isCeleb = false;
                    }

                    if (status.equals("1")) {

                        alreadyReg = true;
                        new Session().saveFbLoginStatus(LogInAcitvity.this, true);
                        showConfirmDialog(registeredPhone, user);

                    } else {

                        TastyToast.makeText(LogInAcitvity.this, "You have to register first!", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        btnLogin.setVisibility(View.GONE);
                        btnNewUser.setVisibility(View.GONE);
                        txtBecomeCeleb.setVisibility(View.VISIBLE);
                        btnLogInCont.setVisibility(View.VISIBLE);
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

    public static void setVerifyPinCodeFromSms(String pinCode) {
        try {
            if (etVerificationCode != null && pinCode != null) {
                etVerificationCode.setText(pinCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
