package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.CelebrityClass;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by toukirul on 12/4/2017.
 */

public class CelebrityListAdapter extends ArrayAdapter<CelebrityClass> {

    Context mContext;


    public CelebrityListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CelebrityListAdapter(Context context, int resource, List<CelebrityClass> items) {
        super(context, resource, items);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.celeb_list_row, null);
        }

        CelebrityClass p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.txtCelebName);
            ImageView tt2 = (ImageView) v.findViewById(R.id.imgCeleb);
            ImageView flw = (ImageView) v.findViewById(R.id.imageViewFollower);
            ImageView imageViewOnlineStatus = (ImageView) v.findViewById(R.id.imageViewOnlineStatus);
            TextView textViewFollowerCount = (TextView) v.findViewById(R.id.textViewFollowerCount);

            textViewFollowerCount.setText(p.getFollowerCount());

            if (tt1 != null) {
                tt1.setText(p.getCeleb_name());
            }

            if (tt2 != null) {
                Picasso.with(mContext).load(p.getCeleb_image()).into(tt2);
            }

            String isOnline = p.getIsOnline();
            if (isOnline.equals("1") || isOnline.matches("1")) {
                imageViewOnlineStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.followicononline));
            } else {
                imageViewOnlineStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.followicon));
            }

            // Set follow button
            if (p.getIsfollow().equals("1")) {
                flw.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unfollow));
            } else {
                flw.setImageDrawable(mContext.getResources().getDrawable(R.drawable.follow));
            }

            flw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CelebrityClass p = getItem(position);
                    String ph = Session.retreivePhone(mContext, Session.USER_PHONE);
                    if (flw.getDrawable().getConstantState().equals
                            (mContext.getResources().getDrawable(R.drawable.follow).getConstantState())) {
                        makeFollower(ph, p.getCeleb_code(), textViewFollowerCount);
                        flw.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unfollow));
                    } else {
                        makeUnFollower(ph, p.getCeleb_code(), textViewFollowerCount);
                        flw.setImageDrawable(mContext.getResources().getDrawable(R.drawable.follow));
                    }
                }
            });

        }
        return v;
    }

    private void makeFollower(String fanPhone, String celebPhone, TextView textViewFollowerCount) {
        Log.d("phone of both", "makeFollower: fan:" + fanPhone + " Cel:" + celebPhone);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_POST_FOLLOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer follow", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("result");
                            JSONArray jo = new JSONArray(res);
                            String s = jo.getString(0);
                            JSONObject jst = new JSONObject(s);
                            textViewFollowerCount.setText(jst.getString("Follower"));
                            TastyToast.makeText(mContext, jst.getString("result"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer follow", "" + error.getMessage());
                        //    TastyToast.makeText(mContext, "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("fan", fanPhone);
                params.put("celebrity", celebPhone);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);


    }

    private void makeUnFollower(String fanPhone, String celebPhone, TextView textViewFollowerCount) {
        Log.d("phone of both", "make un Follower: fan:" + fanPhone + " Cel:" + celebPhone);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_POST_UNFOLLOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer follow", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("result");
                            JSONArray jo = new JSONArray(res);
                            String s = jo.getString(0);
                            JSONObject jst = new JSONObject(s);
                            textViewFollowerCount.setText(jst.getString("Follower"));
                            TastyToast.makeText(mContext, jst.getString("result"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer follow", "" + error.getMessage());
                        //    TastyToast.makeText(mContext, "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("fan", fanPhone);
                params.put("celebrity", celebPhone);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);


    }


}
