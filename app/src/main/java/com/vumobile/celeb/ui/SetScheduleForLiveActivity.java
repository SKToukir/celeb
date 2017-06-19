package com.vumobile.celeb.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.Session;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SetScheduleForLiveActivity extends AppCompatActivity implements View.OnClickListener {

    private String LIVE_SCHEDULE = "0";
    private ProgressDialog mSpinner;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    public int mHour, mMinute;
    private Intent intent;
    private ImageView imgBack;
    private Button btnConfirm;
    private EditText etDate, etFromTime, etToTime;
    public String startTime, endTime;
    private String fanMsisdn;
    private String selectedYear, selectedMonth, selectedDate;
    private String room_name, temp_key;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule_for_live);
        toolbar = (Toolbar) findViewById(R.id.toolbar_liveSchedule);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        initUI();
    }

    private void initUI() {

        etDate = (EditText) findViewById(R.id.etDateLive);
        etFromTime = (EditText) findViewById(R.id.etTimeLive);
        btnConfirm = (Button) findViewById(R.id.btnConfirmTimeLive);
        btnConfirm.setOnClickListener(this);
        etDate.setOnClickListener(this);
        etFromTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.etTimeLive:
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                etFromTime.setText(hourOfDay + ":" + minute);

                                startTime = String.valueOf(selectedYear) + "-" + String.valueOf(selectedMonth) + "-" + String.valueOf(selectedDate) + " " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + ":" + "00";
                                Log.d("dates", startTime);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.etDateLive:
                showDialog(999);
                break;
            case R.id.btnConfirmTimeLive:
                confirmation(Api.API_LIVE_SCHEDULE, "1");
                break;

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
//                    showDate(arg1, arg2+1, arg3);
                    selectedYear = String.valueOf(arg1);
                    selectedMonth = String.valueOf(arg2 + 1);
                    selectedDate = String.valueOf(arg3);

                    etDate.setText(selectedYear + "-" + selectedMonth + "-" + selectedDate);
                }
            };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetScheduleForLiveActivity.this, CelebHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void confirmation(String urlRequestsAccept, String flag) {

        room_name = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE) + fanMsisdn;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRequestsAccept,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());

                        TastyToast.makeText(getApplicationContext(), response, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        intent = new Intent(SetScheduleForLiveActivity.this, CelebHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer", "" + error.getMessage());
                        //    TastyToast.makeText(mContext, "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Celebrity", Session.retreivePhone(getApplicationContext(), Session.USER_PHONE));
                params.put("StartTime", startTime);
                params.put("EndTime", startTime);
                Log.d("LIVE_SCHEDULE", "set other schedule "+Session.retreivePhone(getApplicationContext(), Session.USER_PHONE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
