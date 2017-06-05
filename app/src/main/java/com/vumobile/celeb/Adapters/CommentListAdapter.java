package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.CommentClass;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toukirul on 6/4/2017.
 */
public class CommentListAdapter extends ArrayAdapter<CommentClass> {

    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    Matcher m;
    Context mContext;

    public CommentListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CommentListAdapter(Context context, int resource, List<CommentClass> items) {
        super(context, resource, items);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pattern pattern = Pattern.compile(URL_REGEX);
        View v = convertView;


            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.custom_comment_list, null);


        CommentClass p = getItem(position);

        if (p != null) {
            ImageView tt5 = (ImageView) v.findViewById(R.id.imgUserComment);
            ImageView tt4 = (ImageView) v.findViewById(R.id.cmntRowImage);
            TextView tt1 = (TextView) v.findViewById(R.id.txtCommentUserName);
            TextView tt2 = (TextView) v.findViewById(R.id.txtUserComment);
            TextView tt3 = (TextView) v.findViewById(R.id.txtTime);

            m = pattern.matcher(p.getuComment());

            if (m.find()){
                tt2.setVisibility(View.GONE);
                tt5.setVisibility(View.VISIBLE);
               // Picasso.with(parent.getContext()).load(p.getuComment()).into(tt5);
                Glide.with(mContext).load(p.getuComment()).thumbnail(0.5f).into(tt5);
            }

            if (tt1 != null) {
                tt1.setText(p.getUserName());
            }

            if (tt2 != null) {
                tt2.setText(p.getuComment());
            }

            if (tt3 != null) {
                tt3.setText(p.getTime());
            }

            if (tt4 != null) {
                Picasso.with(parent.getContext()).load(p.getImage()).into(tt4);
            }
        }

        return v;
    }

    @Nullable
    @Override
    public CommentClass getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }
}
