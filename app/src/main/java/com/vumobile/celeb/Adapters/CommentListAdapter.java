package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.CommentClass;

import java.util.List;

/**
 * Created by toukirul on 6/4/2017.
 */
public class CommentListAdapter extends ArrayAdapter<CommentClass> {

    public CommentListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CommentListAdapter(Context context, int resource, List<CommentClass> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_comment_list, null);
        }

        CommentClass p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.txtCommentUserName);
            TextView tt2 = (TextView) v.findViewById(R.id.txtUserComment);
            TextView tt3 = (TextView) v.findViewById(R.id.txtTime);
            if (tt1 != null) {
                tt1.setText(p.getUserName());
            }

            if (tt2 != null) {
                tt2.setText(p.getuComment());
            }

            if (tt3 != null) {
                tt3.setText(p.getTime());
            }

//            if (tt4 != null) {
//                Picasso.with(parent.getContext()).load(p.getImage()).into(tt4);
//            }
        }

        return v;
    }

    @Nullable
    @Override
    public CommentClass getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }
}
