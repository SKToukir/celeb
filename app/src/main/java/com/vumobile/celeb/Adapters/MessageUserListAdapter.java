package com.vumobile.celeb.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.MessageListClass;
import com.vumobile.fan.login.Session;

import java.util.HashMap;
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

    public static String IMAGE_URL="urlimg";
    public static String NAME = "name";


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
            TextView tt4 = (TextView) v.findViewById(R.id.txtMessageCount);
            TextView tt3 = (TextView) v.findViewById(R.id.txtFanMessageName);
            ImageView imgFan = (ImageView) v.findViewById(R.id.imageFan);
            LinearLayout linearLayoutMessageList = (LinearLayout) v.findViewById(R.id.linearLayoutMessageList);

            if (Session.isCeleb(mContext, Session.IS_CELEB)){

                if (!requestClass.getCelebrityMessageCount().equals("0")){
                    tt4.setVisibility(View.VISIBLE);
                    tt4.setText(requestClass.getCelebrityMessageCount());
                    Log.d("popopopopCeleb",requestClass.getCelebrityMessageCount());
                }else {
                    tt4.setVisibility(View.GONE);
                    Log.d("popopopopCeleb","else"+requestClass.getCelebrityMessageCount());
                }

            }else {
                if (!requestClass.getFanMessageCount().equals("0")){
                    tt4.setVisibility(View.VISIBLE);
                    tt4.setText(requestClass.getFanMessageCount());
                    Log.d("popopopop",requestClass.getFanMessageCount());
                }else {
                    tt4.setVisibility(View.GONE);
                    Log.d("popopopopCeleb","else"+requestClass.getCelebrityMessageCount());
                }
            }

            if (tt3 != null) {
                tt3.setText(requestClass.getName());
                HashMap<String, String> tagNameAndImage = new HashMap<>();
                tagNameAndImage.put(NAME, requestClass.getName());
                tagNameAndImage.put(IMAGE_URL, requestClass.getImageUrl());
                linearLayoutMessageList.setTag(tagNameAndImage);
            }

            if (tt3 != null) {
                Picasso.with(mContext).load(requestClass.getImageUrl()).into(imgFan);
            }
        }

        return v;
    }


//
//    @Nullable
//    @Override
//    public MessageListClass getItem(int position) {
//        return super.getItem(getCount() - position - 1);
//    }
}
