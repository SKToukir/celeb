package com.vumobile.fan.login.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.ui.ChatRoomActivity;
import com.vumobile.celeb.ui.LiveRoomActivity;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.adapter.GiftRecyclerViewAdapter;
import com.vumobile.fan.login.model.GiftItemModel;
import com.vumobile.fan.login.model.TempInfoModel;
import com.vumobile.fan.login.serverrequest.AllVolleyInterfaces;
import com.vumobile.fan.login.serverrequest.MyVolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class Gifts extends Fragment {

    private static final String TAG = "Gift Fragment";
    private GiftRecyclerViewAdapter giftRecyclerViewAdapter;
    private RecyclerView recyclerViewGift;
    private SwipeRefreshLayout swipeRefreshLayoutGift;

    GiftItemModel giftItemModel;
    List<GiftItemModel> giftItemModels;
    public static int sendFrom = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gifts, container, false);
        //    linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout);

        // swipe to refresh
        swipeRefreshLayoutGift = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutGift);
        swipeRefreshLayoutGift.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchGiftImages();
            }
        });

        swipeRefreshLayoutGift.post(() -> {
            fetchGiftImages();
        });

        // Data models
        giftItemModels = new ArrayList<>();

        recyclerViewGift = (RecyclerView) rootView.findViewById(R.id.recyclerViewGift);
        recyclerViewGift.setItemAnimator(new DefaultItemAnimator());

        // Set up the RecyclerView for image
        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerViewGift.setLayoutManager(gridLayoutManager);

        giftRecyclerViewAdapter = new GiftRecyclerViewAdapter(getActivity(), giftItemModels);

        giftRecyclerViewAdapter.setClickListener((view, position) -> {
            // view.findViewById(R.id.textViewPrice).getTag()
            if (getActivity().getLocalClassName().contains("FanCelebProfileActivity")) {
                //  Toast.makeText(getActivity().getApplicationContext(), "Gift item :" + getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                sendFrom = 3; // direct from profile
            } else if (getActivity().getLocalClassName().contains("ChatRoomActivity")) {
                //   Toast.makeText(getActivity().getApplicationContext(), "Gift item :" + getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
                ChatRoomActivity.postComment(getActivity().getApplicationContext(), giftItemModels.get(position).getPreviewURL());
                Log.d(TAG, "onCreateView: " + giftItemModels.get(position).getPreviewURL());
                getActivity().onBackPressed();
                sendFrom = 2;
            } else if (getActivity().getLocalClassName().contains("LiveRoomActivity")) {
                //   Toast.makeText(getActivity().getApplicationContext(), "Gift item :" + getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
                LiveRoomActivity.postComment(getActivity().getApplicationContext(), giftItemModels.get(position).getPreviewURL(), giftItemModels.get(position).getChargePrice());
                Log.d(TAG, "onCreateView: " + giftItemModels.get(position).getPreviewURL());
                getActivity().onBackPressed();
                sendFrom = 1;
            }

            //    dialog.dismiss();
            // save sticker send info to db
            sendGiftSendingInformation(giftItemModels.get(position), sendFrom);





//
//            // custom dialog
//            final Dialog dialog = new Dialog(getActivity());
//            dialog.setContentView(R.layout.dialog_confirm_gift);
//            dialog.setTitle("");
//
//            // set the custom dialog components - text, image and button
//            TextView text = (TextView) dialog.findViewById(R.id.textViewDialogMessage);
//            text.setText("This will cost " + giftItemModels.get(position).getChargePrice() + "\nWant to send this Gift?");
//            ImageView imageViewDialogGift = (ImageView) dialog.findViewById(R.id.imageViewDialogGift);
//            imageViewDialogGift.setImageResource(R.mipmap.ic_launcher);
//            Glide.with(getActivity()).load(giftItemModels.get(position).getPreviewURL()).into(imageViewDialogGift);
//
//            Button buttonDialogCancel = (Button) dialog.findViewById(R.id.buttonDialogCancel);
//            Button buttonDialogOk = (Button) dialog.findViewById(R.id.buttonDialogOk);
//            // if button is clicked, close the custom dialog
//            buttonDialogCancel.setOnClickListener(v -> {
//                dialog.dismiss();
//            });
//            // if button is clicked, send gift
//            buttonDialogOk.setOnClickListener(v -> {
//
//                // view.findViewById(R.id.textViewPrice).getTag()
//                if (getActivity().getLocalClassName().contains("FanCelebProfileActivity")) {
//                    //  Toast.makeText(getActivity().getApplicationContext(), "Gift item :" + getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
//                    getActivity().onBackPressed();
//                    sendFrom = 3; // direct from profile
//                } else if (getActivity().getLocalClassName().contains("ChatRoomActivity")) {
//                    //   Toast.makeText(getActivity().getApplicationContext(), "Gift item :" + getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
//                    ChatRoomActivity.postComment(getActivity().getApplicationContext(), giftItemModels.get(position).getPreviewURL());
//                    Log.d(TAG, "onCreateView: " + giftItemModels.get(position).getPreviewURL());
//                    getActivity().onBackPressed();
//                    sendFrom = 2;
//                } else if (getActivity().getLocalClassName().contains("LiveRoomActivity")) {
//                    //   Toast.makeText(getActivity().getApplicationContext(), "Gift item :" + getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
//                    LiveRoomActivity.postComment(getActivity().getApplicationContext(), giftItemModels.get(position).getPreviewURL(), giftItemModels.get(position).getChargePrice());
//                    Log.d(TAG, "onCreateView: " + giftItemModels.get(position).getPreviewURL());
//                    getActivity().onBackPressed();
//                    sendFrom = 1;
//                }
//
//                dialog.dismiss();
//                // save sticker send info to db
//                sendGiftSendingInformation(giftItemModels.get(position), sendFrom);
//
//            });
//
//            dialog.show();

        });

        return rootView;
    }

    private void sendGiftSendingInformation(GiftItemModel giftItemModel, int sendFrom) { // sendFrom : 1=live,2=chat,3=direct
        Log.d(TAG, "sendGiftSendingInformation: " + TempInfoModel.getCelebMsisdn());

        HashMap<String, String> params = new HashMap<>();
        params.put(Api.GIFT_SEND_ID, giftItemModel.get_id());
        params.put(Api.GIFT_SEND_GRAPHICS_CODE, giftItemModel.getGraphicsCode());
        params.put(Api.GIFT_SEND_FAN, Session.retreivePhone(getActivity(), Session.USER_PHONE));
        params.put(Api.GIFT_SEND_CELEBRITY, TempInfoModel.getCelebMsisdn());
        params.put(Api.GIFT_SEND_SENDING_TIME, String.valueOf(new Timestamp(System.currentTimeMillis())));
        params.put(Api.GIFT_SEND_SEND_FROM, String.valueOf(sendFrom));

        String myUrl = Api.URL_GIFT_SEND;
        MyVolleyRequest.sendAllGenericDataJsonObject(getActivity().getApplicationContext(),
                Request.Method.POST,
                myUrl,
                params,
                new AllVolleyInterfaces.ResponseString() {
                    @Override
                    public void getResponse(String responseResult) {
                        Log.d(TAG, "getResponse: giftSend " + responseResult);

                        if (getActivity().getLocalClassName().contains("FanCelebProfileActivity")){
                            Toast.makeText(getActivity(), "Gift sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void getResponseErr(String responseResultErr) {
                        Log.d(TAG, "getResponseErr: giftNotSend " + responseResultErr);
                     //   Toast.makeText(getActivity(), "Gift not sent", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void fetchGiftImages() {

        swipeRefreshLayoutGift.setRefreshing(true);
        giftItemModels.clear();

        String url = Api.URL_GIFT;
        MyVolleyRequest.getAllGifts(getActivity().getApplicationContext(), Request.Method.GET, url, new AllVolleyInterfaces.ResponseString() {
            @Override
            public void getResponse(String responseResult) {
                Log.d("gift", "getResponse: " + responseResult);
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                        giftItemModel = new GiftItemModel(
                                jo.getString("ID"),
                                jo.getString("GraphicsCode"),
                                jo.getString("ContentTitle"),
                                jo.getString("ContentType"),
                                jo.getString("PhysicalFileName"),
                                jo.getString("PreviewUrl"),
                                jo.getString("ChargeType"),
                                jo.getString("ChargePrice"),
                                jo.getString("TimeStamp")
                        );
                        Log.d(TAG, "getResponse: " + giftItemModel.toString());
                        giftItemModels.add(giftItemModel);
                        recyclerViewGift.setAdapter(giftRecyclerViewAdapter);
                        giftRecyclerViewAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void getResponseErr(String responseResultErr) {
                Log.d("gift", "getResponseErr: " + responseResultErr);
            }
        });

        swipeRefreshLayoutGift.setRefreshing(false);
    }


}
