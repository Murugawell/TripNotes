package com.geek4s.tripnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        getSupportActionBar().setTitle(RecyclerViewActivity.class.getSimpleName());

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<ItemModel> data = new ArrayList<>();
        data.add(new ItemModel(
                "0 ACCELERATE_DECELERATE_INTERPOLATOR",
                R.color.colorAccent,
                R.color.colorPrimary,
                Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        data.add(new ItemModel(
                "1 ACCELERATE_INTERPOLATOR", R.color.colorAccent,
                R.color.colorPrimary,
                Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)));
//        recyclerView.setAdapter(new RecyclerViewRecyclerAdapter(data));
    }
}
