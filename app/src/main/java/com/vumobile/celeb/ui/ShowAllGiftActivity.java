package com.vumobile.celeb.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.vumobile.celeb.R;

public class ShowAllGiftActivity extends AppCompatActivity {

    RecyclerView.Adapter adapterGift;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_gift);
    }
}
