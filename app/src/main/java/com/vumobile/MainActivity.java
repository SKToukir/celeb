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

import com.vumobile.celeb.R;
import com.vumobile.fan.login.LogInAcitvity;

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
                startActivity(new Intent(MainActivity.this, LogInAcitvity.class));
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

                //TODO
                // here check user registered or not

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
}
