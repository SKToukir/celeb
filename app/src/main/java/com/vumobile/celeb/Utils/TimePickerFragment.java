package com.vumobile.celeb.Utils;

/**
 * Created by toukirul on 11/5/2017.
 */
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
//        SetScheduleActivity.hour = String.valueOf(hourOfDay);
//        SetScheduleActivity.min = String.valueOf(minute);

        Log.d("hour", String.valueOf(hourOfDay)+" "+String.valueOf(minute));

        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        //TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        //Set a message for user
//        tv.setText("Your chosen time is...\n\n");
//        //Display the user changed time on TextView
//        tv.setText(tv.getText()+ "Hour : " + String.valueOf(hourOfDay)
//                + "\nMinute : " + String.valueOf(minute) + "\n");
    }
}