package com.vumobile.celeb.Adapters;

/**
 * Created by toukirul on 7/5/2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.RequestClass;
import com.vumobile.celeb.ui.SetScheduleActivity;

import java.util.List;

/**
 * Created by toukirul on 6/4/2017.
 */
public class RequestAdapter extends ArrayAdapter<RequestClass> {

    private Context mContext;
    private ImageView btnConfirm;
    private ImageView btnDelete;

    public RequestAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public RequestAdapter(Context context, int resource, List<RequestClass> items) {
        super(context, resource, items);
        this.mContext = context;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.request_row, null);
        }

        RequestClass requestClass = getItem(position);

        if (requestClass != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.txtfanName);
            TextView tt2 = (TextView) v.findViewById(R.id.txtfanRequest);
            TextView tt3 = (TextView) v.findViewById(R.id.txtRequestTime);
            ImageView imgFan = (ImageView) v.findViewById(R.id.fanImage);
            btnConfirm = (ImageView) v.findViewById(R.id.btnConfirm);
            btnDelete = (ImageView) v.findViewById(R.id.btnDelete);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TastyToast.makeText(mContext,requestClass.getFanName()+String.valueOf(position),TastyToast.LENGTH_LONG,TastyToast.SUCCESS);

                    Intent intent = new Intent(mContext, SetScheduleActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("msisdn",requestClass.getMSISDN());
                    mContext.startActivity(intent);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TastyToast.makeText(mContext,"Delete"+String.valueOf(position),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                }
            });

            if (tt1 != null) {
                tt1.setText(requestClass.getFanName());
            }

            if (tt2 != null) {
                tt2.setText(requestClass.getRequestToTime());
            }

            if (tt3 != null) {
                tt3.setText(requestClass.getRequest());
            }

            if (imgFan != null) {
                Picasso.with(mContext).load(requestClass.getImageUrl()).into(imgFan);
            }
        }

        return v;
    }



    @Nullable
    @Override
    public RequestClass getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }
}
