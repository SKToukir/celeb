package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.CelebScheduleClass;

import java.util.List;

/**
 * Created by toukirul on 15/5/2017.
 */

public class ScheduleAdapter extends ArrayAdapter<CelebScheduleClass> {

    public ScheduleAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ScheduleAdapter(Context context, int resource, List<CelebScheduleClass> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_schedule_layout, null);
        }

        CelebScheduleClass p = getItem(position);

        if (p != null) {
            ImageView imageView = (ImageView) v.findViewById(R.id.fanImages);
            TextView tt1 = (TextView) v.findViewById(R.id.txtNames);
            TextView tt2from = (TextView) v.findViewById(R.id.txtSceduleTimeFrom);
            TextView tt3to = (TextView) v.findViewById(R.id.txtSceduleTimeTo);
            TextView tt4type = (TextView) v.findViewById(R.id.txtSceduleRequestType);

            if (p.getRequestType().equals("3")){
                tt4type.setText("Live Schedule");
            }else if (p.getRequestType().equals("1")){
                tt4type.setText("Video Schedule");
            }else {
                tt4type.setText("Chat Schedule");
            }

                if (imageView != null) {
                    Picasso.with(getContext()).load(p.getImageUrl()).into(imageView);
                }

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2from != null) {
                tt2from.setText(p.getStart_time());
            }

            if (tt3to != null) {
                tt3to.setText(p.getEnd_time());
            }

//            if (tt4 != null) {
//                Picasso.with(parent.getContext()).load(p.getImage()).into(tt4);
//            }
        }

        return v;
    }

    @Nullable
    @Override
    public CelebScheduleClass getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }
}
