package com.vumobile.fan.login.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import com.vumobile.celeb.R;
import com.vumobile.fan.login.ImageOrVideoView;
import com.vumobile.fan.login.adapter.FanCelebImageRecyclerViewAdapter;
import com.vumobile.fan.login.adapter.FanCelebVideoRecyclerViewAdapterOffline;
import com.vumobile.fan.login.model.FanCelebImageModelEntity;
import com.vumobile.fan.login.model.FanCelebVideoModelEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IT-10 on 5/28/2017.
 */

public class MyGallery extends Fragment {

    private static final String TAG = "MyGallery Fragment";
    private FanCelebImageRecyclerViewAdapter fanCelebImageRecyclerViewAdapter;
    private FanCelebVideoRecyclerViewAdapterOffline fanCelebVideoRecyclerViewAdapterOffline;
    private FanCelebImageModelEntity fanCelebImageModelEntity;
    private FanCelebVideoModelEntity fanCelebVideoModelEntity;
    private List<FanCelebImageModelEntity> fanCelebImageModelEntities;
    private List<FanCelebVideoModelEntity> fanCelebVideoModelEntities;
    private RecyclerView recyclerViewCelebImages, recyclerViewCelebVideos;
    private SwipeRefreshLayout swipeRefreshLayoutCelebImages, swipeRefreshLayoutCelebVideos;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_gallery, container, false);

        // swipe to refresh
        swipeRefreshLayoutCelebImages = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutCelebImages);
        swipeRefreshLayoutCelebVideos = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutCelebVideos);
        swipeRefreshLayoutCelebImages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCelebImages();
            }
        });
        swipeRefreshLayoutCelebVideos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCelebVideos();
            }
        });
        swipeRefreshLayoutCelebImages.post(() -> {
            fetchCelebImages();
        });
        swipeRefreshLayoutCelebVideos.post(() -> {
            fetchCelebVideos();
        });

        // Data models
        fanCelebImageModelEntity = new FanCelebImageModelEntity();
        fanCelebVideoModelEntity = new FanCelebVideoModelEntity();
        fanCelebImageModelEntities = new ArrayList<>();
        fanCelebVideoModelEntities = new ArrayList<>();

        recyclerViewCelebImages = (RecyclerView) rootView.findViewById(R.id.recyclerViewCelebImages);
        recyclerViewCelebVideos = (RecyclerView) rootView.findViewById(R.id.recyclerViewCelebVideos);
        recyclerViewCelebImages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCelebVideos.setItemAnimator(new DefaultItemAnimator());


        // Set up the RecyclerView for image
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        GridLayoutManager gridLayoutManagerV = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerViewCelebImages.setLayoutManager(gridLayoutManager);
        recyclerViewCelebVideos.setLayoutManager(gridLayoutManagerV);

        fanCelebImageRecyclerViewAdapter = new FanCelebImageRecyclerViewAdapter(getActivity(), fanCelebImageModelEntities);
        fanCelebVideoRecyclerViewAdapterOffline = new FanCelebVideoRecyclerViewAdapterOffline(getActivity(), fanCelebVideoModelEntities);

        fanCelebImageRecyclerViewAdapter.setClickListener((view, position) -> {
            Intent intent = new Intent(getActivity(), ImageOrVideoView.class);
            intent.putExtra("IMG_OR_VID", "1");
            intent.putExtra("IMG_OR_VID_URL", view.findViewById(R.id.imageViewRecyclerItem).getTag().toString());
            startActivity(intent);
        });

        fanCelebVideoRecyclerViewAdapterOffline.setClickListener((view, position) -> {
            Intent intent = new Intent(getActivity(), ImageOrVideoView.class);
            intent.putExtra("FROM_OFFLINE_GALLERY", "Y");
            intent.putExtra("IMG_OR_VID", "2");
            intent.putExtra("IMG_OR_VID_URL", view.findViewById(R.id.imageViewRecyclerItemVThumb).getTag().toString());
            startActivity(intent);
        });

        return rootView;
    }


    private void fetchCelebImages() {
        swipeRefreshLayoutCelebImages.setRefreshing(true);
        fanCelebImageModelEntities.clear();


        ArrayList<String> mFiles = new ArrayList<String>();
        File mDirectory;
        String folderPath = Environment.getExternalStorageDirectory().toString() + "/CelebApp/";
        mDirectory = new File(folderPath);

        // Get the files in the directory
        File[] files = mDirectory.listFiles();
        Log.d(TAG, "fetchCelebImages: " + files.length);
        if (files != null && files.length > 0) {

            for (File f : files) {
                String fUrl = f.getAbsolutePath().replaceAll(" ", "%20");
                if (fUrl.toLowerCase().endsWith(".jpg") || fUrl.toLowerCase().endsWith(".png") ||
                        fUrl.toLowerCase().endsWith(".jpeg") || fUrl.toLowerCase().endsWith(".gif")) {
                    fanCelebImageModelEntity = new FanCelebImageModelEntity();
                    // mFiles.add(f.getAbsolutePath());
                    fanCelebImageModelEntity.setImageUrl(fUrl);
                    fanCelebImageModelEntities.add(fanCelebImageModelEntity);
                    recyclerViewCelebImages.setAdapter(fanCelebImageRecyclerViewAdapter);
                    fanCelebImageRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

        }

        swipeRefreshLayoutCelebImages.setRefreshing(false);

    }

    private void fetchCelebVideos() {
        swipeRefreshLayoutCelebVideos.setRefreshing(true);
        fanCelebVideoModelEntities.clear();

        ArrayList<String> mFiles = new ArrayList<String>();
        File mDirectory;
        String folderPath = Environment.getExternalStorageDirectory().toString() + "/CelebApp/";
        mDirectory = new File(folderPath);

        // Get the files in the directory
        File[] files = mDirectory.listFiles();
        Log.d(TAG, "fetchCelebImages: " + files.length);
        if (files != null && files.length > 0) {

            for (File f : files) {
                String fUrl = f.getAbsolutePath().replaceAll(" ", "%20");
                if (fUrl.toLowerCase().endsWith(".jpg") || fUrl.toLowerCase().endsWith(".png") ||
                        fUrl.toLowerCase().endsWith(".jpeg") || fUrl.toLowerCase().endsWith(".gif")) {

                } else {
                    fanCelebVideoModelEntity = new FanCelebVideoModelEntity();
                    // mFiles.add(f.getAbsolutePath());
                    fanCelebVideoModelEntity.setVideoUrl(f.getAbsolutePath().replaceAll(" ", "%20"));
                    fanCelebVideoModelEntities.add(fanCelebVideoModelEntity);
                    recyclerViewCelebVideos.setAdapter(fanCelebVideoRecyclerViewAdapterOffline);
                    fanCelebVideoRecyclerViewAdapterOffline.notifyDataSetChanged();
                }

            }

        }

        swipeRefreshLayoutCelebVideos.setRefreshing(false);

    }
}
