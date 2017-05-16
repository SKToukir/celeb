package com.vumobile.celeb.Adapters;

import android.annotation.SuppressLint;
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
import com.vumobile.celeb.model.MessageListClass;

import java.util.List;

/**
 * Created by toukirul on 16/5/2017.
 */

/**
 * Created by toukirul on 6/4/2017.
 */
public class MessageUserListAdapter extends ArrayAdapter<MessageListClass> {

    private Context mContext;
    private ImageView img;
    private TextView txtName;


    public MessageUserListAdapter(Context context, int resource, List<MessageListClass> items) {
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
            v = vi.inflate(R.layout.row_message_fan_list, null);
        }

        MessageListClass requestClass = getItem(position);

        if (requestClass != null) {
            TextView tt3 = (TextView) v.findViewById(R.id.txtFanMessageName);
            ImageView imgFan = (ImageView) v.findViewById(R.id.imageFan);


            if (tt3 != null) {
                tt3.setText(requestClass.getName());
            }

            if (tt3 != null) {
                Picasso.with(mContext).load(requestClass.getImageUrl()).into(imgFan);
            }
        }

        return v;
    }



    @Nullable
    @Override
    public MessageListClass getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }
}
