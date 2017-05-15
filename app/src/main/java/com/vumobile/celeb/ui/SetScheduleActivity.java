package com.vumobile.celeb.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class SetScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    public int mHour, mMinute;
    private Intent intent;
    private Toolbar toolbar;
    private ImageView imgBack;
    private Button btnConfirm;
    private EditText etDate, etFromTime, etToTime;
    public String startTime,endTime;
    private String fanMsisdn;
    private String selectedYear, selectedMonth, selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);
        toolbar = (Toolbar) findViewById(R.id.toolbar_set_schedule);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        fanMsisdn = intent.getStringExtra("msisdn");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        initUI();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getFanRegId();
            }
        });thread.start();
    }

    private void getFanRegId() {



    }

    private void initUI() {
        etDate = (EditText) findViewById(R.id.etDate);
        etFromTime = (EditText) findViewById(R.id.etTime);
        etToTime = (EditText) findViewById(R.id.etToTime);
        btnConfirm = (Button) findViewById(R.id.btnConfirmTime);
        imgBack = (ImageView) toolbar.findViewById(R.id.backSetSchedule);
        imgBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        etToTime.setOnClickListener(this);
        etFromTime.setOnClickListener(this);
        etDate.setOnClickListener(this);

        // for hidden keyboard when press edittext
        etDate.setShowSoftInputOnFocus(false);
        etToTime.setShowSoftInputOnFocus(false);
        etFromTime.setShowSoftInputOnFocus(false);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.backCelebSchedule:
                intent = new Intent(SetScheduleActivity.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
            case R.id.etTime:
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

                                startTime =String.valueOf(selectedYear)+"-"+String.valueOf(selectedMonth)+"-"+String.valueOf(selectedDate)+" "+String.valueOf(hourOfDay)+":"+String.valueOf(minute)+":"+"00";
                                Log.d("dates",startTime);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.etToTime:
                final Calendar cc = Calendar.getInstance();
                mHour = cc.get(Calendar.HOUR_OF_DAY);
                mMinute = cc.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                etToTime.setText(hourOfDay + ":" + minute);

                                endTime =String.valueOf(selectedYear)+"-"+String.valueOf(selectedMonth)+"-"+String.valueOf(selectedDate)+" "+String.valueOf(hourOfDay)+":"+String.valueOf(minute)+":"+"00";
                                Log.d("dates",endTime);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog1.show();
                break;
            case R.id.etDate:
                showDialog(999);
                break;
            case R.id.btnConfirmTime:
                confirmation(Api.URL_REQUESTS_ACCEPT,"1");
                break;
        }
    }

    private void confirmation(String urlRequestsAccept,String flag) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRequestsAccept,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());

                        TastyToast.makeText(getApplicationContext(),response,TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
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

                params.put("Fan", fanMsisdn);
                params.put("Celebrity", Session.retreivePhone(getApplicationContext(),Session.USER_PHONE));
                params.put("flag", flag);
                params.put("StartTime", startTime);
                params.put("EndTime", endTime);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

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
                    selectedMonth = String.valueOf(arg2+1);
                    selectedDate = String.valueOf(arg3);

                    etDate.setText(selectedYear+"-"+selectedMonth+"-"+selectedDate);
                }
            };



    private String getDate() {

        return "";
    }

    private String getFromTime() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        return currentDateTimeString;
    }

    private String getToTime() {

        return "";
    }


}
