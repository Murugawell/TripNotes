package com.geek4s.tripnotes.help;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geek4s.tripnotes.DividerItemDecoration;
import com.geek4s.tripnotes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

/**
 * Created by Murugavel on 1/8/2018.
 */

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_main);

        try {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " - Help");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.help_recyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List list = getData();
        recyclerView.setAdapter(new HelpRecyclerviewAdapter(list));


    }

    private List getData() {
        List<HashMap<String, String>> faq = null;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("faq");
            faq = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String question = jo_inside.getString("question");
                String answer = jo_inside.getString("answer");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("question", question);
                m_li.put("answer", answer);

                faq.add(m_li);
            }
            return faq;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return faq;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("helpjson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);

    }
}




/*
*
*
*
*         BarChart barChart = (BarChart) findViewById(R.id.bar_chart_vertical);
        barChart.setBarMaxValue(100);

        BarChartModel barChartModel = new BarChartModel();
        barChartModel.setBarValue(50);
        barChartModel.setBarColor(Color.parseColor("#9C27B0"));
        barChartModel.setBarTag(""); //You can set your own tag to bar model
        barChartModel.setBarText("2500");

        BarChartModel barChartModel2 = new BarChartModel();
        barChartModel2.setBarValue(90);
        barChartModel2.setBarColor(Color.parseColor("#9C2878"));
        barChartModel2.setBarTag(null); //You can set your own tag to bar model
        barChartModel2.setBarText("70000");

        barChart.addBar(barChartModel);
        barChart.addBar(barChartModel2);
        barChart.addBar(barChartModel);
        barChart.addBar(barChartModel2);
        barChart.addBar(barChartModel);
        barChart.addBar(barChartModel2);
        barChart.addBar(barChartModel);
        barChart.addBar(barChartModel2);
        barChart.addBar(barChartModel);
        barChart.addBar(barChartModel2);
        barChart.addBar(barChartModel);
        barChart.addBar(barChartModel2);
        barChart.addBar(barChartModel);
        barChart.addBar(barChartModel2);

        barChart.setOnBarClickListener(new BarChart.OnBarClickListener() {
            @Override
            public void onBarClick(BarChartModel barChartModel) {
                Toast.makeText(HelpActivity.this, "" + barChartModel.getBarTag(), Toast.LENGTH_SHORT).show();
            }
        });

*
*
*
*
*
* */