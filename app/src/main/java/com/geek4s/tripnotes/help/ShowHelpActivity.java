package com.geek4s.tripnotes.help;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek4s.tripnotes.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by Murugavel on 1/11/2018.
 */

public class ShowHelpActivity extends AppCompatActivity implements View.OnClickListener {
    Button trip, people, spent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpmain);

        try {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " Help");
        } catch (Exception e) {
            e.printStackTrace();
        }
        trip = (Button) findViewById(R.id.triphelp);
        people = (Button) findViewById(R.id.peoplehelp);
        spent = (Button) findViewById(R.id.spenthelp);

        trip.setText("Trip");
        people.setText("People");
        spent.setText("Spent");

        trip.setOnClickListener(this);
        people.setOnClickListener(this);
        spent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.triphelp: {
                Intent in = new Intent(this, ShowHelpActivityTrip.class);
                startActivity(in);
                break;
            }
            case R.id.peoplehelp: {
                Intent in = new Intent(this, ShowHelpActivityPeople.class);
                startActivity(in);
                break;
            }
            case R.id.spenthelp: {
                Intent in = new Intent(this, ShowHelpActivitySpent.class);
                startActivity(in);
                break;
            }
        }
    }
}