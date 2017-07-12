package com.vumobile.celeb.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;
import com.sinch.android.rtc.SinchError;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.Session;
import com.vumobile.videocall.SinchService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class SetScheduleActivity extends com.vumobile.videocall.BaseActivity implements View.OnClickListener, SinchService.StartFailedListener {

    private  String am_pm = "";
    private String sHour, sMin, eHour, eMin;
    private String request_type;
    private String LIVE_SCHEDULE = "0";
    private ProgressDialog mSpinner;
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
    public String startTime, endTime;
    private String fanMsisdn;
    private String selectedYear, selectedMonth, selectedDate;
    private String room_name, temp_key;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);
        toolbar = (Toolbar) findViewById(R.id.toolbar_set_schedule);
        //setSupportActionBar(toolbar);


        Intent intent = getIntent();
        LIVE_SCHEDULE = intent.getStringExtra("live");

//        Log.d("LIVE_SCHEDULE","l"+LIVE_SCHEDULE);

        fanMsisdn = intent.getStringExtra("msisdn");
        request_type = intent.getStringExtra("request_type");

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
        });
        thread.start();
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
//        etDate.setShowSoftInputOnFocus(false);
//        etToTime.setShowSoftInputOnFocus(false);
//        etFromTime.setShowSoftInputOnFocus(false);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backCelebSchedule:
                intent = new Intent(SetScheduleActivity.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
            case R.id.etTime:
                final Calendar c = Calendar.getInstance();

                sHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                sMin = String.valueOf(c.get(Calendar.MINUTE));

                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String timeSet = "";
                                if (hourOfDay > 12) {
                                    hourOfDay -= 12;
                                    timeSet = "PM";
                                } else if (hourOfDay == 0) {
                                    hourOfDay += 12;
                                    timeSet = "AM";
                                } else if (hourOfDay == 12){
                                    timeSet = "PM";
                                }else{
                                    timeSet = "AM";
                                }

                                etFromTime.setText(hourOfDay + ":" + minute + " " + timeSet);

                                startTime = String.valueOf(selectedYear) + "-" + String.valueOf(selectedMonth) + "-" + String.valueOf(selectedDate) + " " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + ":" + "00" + " " + timeSet;
                                Log.d("dates", startTime);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.etToTime:
                final Calendar cc = Calendar.getInstance();

                eHour = String.valueOf(cc.get(Calendar.HOUR_OF_DAY));
                eMin = String.valueOf(cc.get(Calendar.MINUTE));

                mHour = cc.get(Calendar.HOUR_OF_DAY);
                mMinute = cc.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String timeSet = "";
                                if (hourOfDay > 12) {
                                    hourOfDay -= 12;
                                    timeSet = "PM";
                                } else if (hourOfDay == 0) {
                                    hourOfDay += 12;
                                    timeSet = "AM";
                                } else if (hourOfDay == 12){
                                    timeSet = "PM";
                                }else{
                                    timeSet = "AM";
                                }


                                etToTime.setText(hourOfDay + ":" + minute + " " + timeSet);

                                endTime = String.valueOf(selectedYear) + "-" + String.valueOf(selectedMonth) + "-" + String.valueOf(selectedDate) + " " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + ":" + "00" + " " + timeSet;
                                Log.d("dates", endTime);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog1.show();
                break;
            case R.id.etDate:
                showDialog(999);
                break;
            case R.id.btnConfirmTime:
                if (!getSinchServiceInterface().isStarted()) {
                    SinchService.uName = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
                    startService(new Intent(SetScheduleActivity.this, SinchService.class));
                    getSinchServiceInterface().startClient(Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME));
                    showSpinner();
                } else {
                    //openPlaceCallActivity(celeb_name);
                    Intent intent = new Intent(SetScheduleActivity.this, SinchService.class);
                    SinchService.uName = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
                    startService(intent);
                }


//                if (LIVE_SCHEDULE.equals("3")){
//
//                    setLiveSchedule(Api.API_LIVE_SCHEDULE);
//
//                }else {


                if (request_type.matches("1")) {
                    addEvent(selectedYear, selectedMonth, selectedDate, Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME + " set schedule in this time for fan"));
                    confirmation(Api.URL_REQUESTS_ACCEPT, "1", "1");
                } else {
                    addEvent(selectedYear, selectedMonth, selectedDate, Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME + " set schedule in this time for fan"));
                    confirmation(Api.URL_REQUESTS_ACCEPT, "1", "2");
                }


//                }

                //confirmation(Api.URL_REQUESTS_ACCEPT, "1");
                //}


                break;
        }
    }

    private void addEvent(String selectedYear, String selectedMonth, String selectedDate, String title) {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), Integer.parseInt(selectedDate), Integer.parseInt(sHour), Integer.parseInt(sMin));

        ContentResolver cr = getContentResolver();
        Calendar endTimes = Calendar.getInstance();
        beginTime.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), Integer.parseInt(selectedDate), Integer.parseInt(eHour), Integer.parseInt(eMin));
        ContentValues l_event = new ContentValues();
        l_event.put("calendar_id", 1);
        l_event.put("title", title);
        //l_event.put("description",  "This is test event");
        //l_event.put("eventLocation", "School");
        l_event.put("dtstart", beginTime.getTimeInMillis());
        l_event.put("dtend", endTimes.getTimeInMillis());
        l_event.put("allDay", 0);
        l_event.put("rrule", "FREQ=YEARLY");
        l_event.put(CalendarContract.Events.CALENDAR_ID, 1);
        // status: 0~ tentative; 1~ confirmed; 2~ canceled
        // l_event.put("eventStatus", 1);

        l_event.put("eventTimezone", "India");
        Uri l_eventUri;
//        if (Build.VERSION.SDK_INT >= 8) {
//            l_eventUri = Uri.parse("content://com.android.splanner/events");
//        } else {
//            l_eventUri = Uri.parse("content://splanner/events");
//        }
        Uri l_uri = cr.insert(CalendarContract.Events.CONTENT_URI, l_event);

        long eventId = Long.parseLong(l_uri.getLastPathSegment());
        Log.d("Ketan_Event_Id", String.valueOf(eventId));

    }

    private void setLiveSchedule(String apiLiveSchedule) {

        room_name = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE) + fanMsisdn;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiLiveSchedule,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LIVE_SCHEDULE", response.toString());

                        TastyToast.makeText(getApplicationContext(), response.toString(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                        intent = new Intent(SetScheduleActivity.this, CelebHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LIVE_SCHEDULE", "" + error.getMessage());
                        //    TastyToast.makeText(mContext, "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("LIVE_SCHEDULE", "set live schedule");
                params.put("Celebrity", Session.retreivePhone(getApplicationContext(), Session.USER_PHONE));
                params.put("StartTime", startTime);
                params.put("EndTime", endTime);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void confirmation(String urlRequestsAccept, String flag, String rqstType) {

        room_name = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE) + fanMsisdn;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRequestsAccept,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());

                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            createRoomOnFirebase(room_name);
                            Log.d("room_name", room_name);

                            if (result.equals("Confirmed")){

                                TastyToast.makeText(getApplicationContext(), result, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                intent = new Intent(SetScheduleActivity.this, CelebHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }else {

                                showAlert(result);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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
                params.put("Celebrity", Session.retreivePhone(getApplicationContext(), Session.USER_PHONE));
                params.put("flag", flag);
                params.put("StartTime", startTime);
                params.put("EndTime", endTime);
                params.put("RoomNumber", room_name);
                params.put("RequestType", rqstType);
                Log.d("LIVE_SCHEDULE", startTime + " " + endTime);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void showAlert(String result) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Change Schedule Time");
        alertDialogBuilder.setMessage(result);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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

    private void createRoomOnFirebase(String room_name) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(room_name, "");
        root.updateChildren(map);

    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }


    @Override
    public void onStarted() {

    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait..");
        mSpinner.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetScheduleActivity.this, CelebHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
