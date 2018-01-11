package com.geek4s.tripnotes.help;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class ShowHelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cell);
        TextView f_triptitle = (TextView) findViewById(R.id.cell_title_trip_name_textview);
        LinearLayout f_peopleLayout = (LinearLayout) findViewById(R.id.cell_title_people_layout);
        LinearLayout f_spentAmountLayout = (LinearLayout) findViewById(R.id.cell_title_spent_amount_layout);
        ImageView f_trip_open_icon = (ImageView) findViewById(R.id.cell_title_open_icon);
        TextView f_estimatedAmount = (TextView) findViewById(R.id.title_estimateamount);
        TextView f_time = (TextView) findViewById(R.id.title_time_label);
        TextView f_day = (TextView) findViewById(R.id.title_day_label);
        TextView f_date = (TextView) findViewById(R.id.title_date_label);
        TextView f_fromAddress = (TextView) findViewById(R.id.title_from_address);
        TextView f_toAddress = (TextView) findViewById(R.id.title_to_address);
        TextView f_peopleCount = (TextView) findViewById(R.id.title_people_count);
        TextView f_tripAmount = (TextView) findViewById(R.id.title_amount);

        f_estimatedAmount.setText("xxxx");
        f_peopleCount.setText("x");
        f_tripAmount.setText("xxxx");
        f_fromAddress.setText("Source place");
        f_toAddress.setText("Destination place");

        List l = new ArrayList();
        HashMap h;

        h = new HashMap<>();
        h.put("view", f_trip_open_icon);
        h.put("content", "By clicking this icon you can see the full trip details");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", f_triptitle);
        h.put("content", "This is trip title.\n By long clicking this you can delete the trip");
        h.put("dismisstext", "Got It");
        l.add(h);


        h = new HashMap<>();
        h.put("view", f_estimatedAmount);
        h.put("content", "This is estimated amount");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", f_peopleLayout);
        h.put("content", "This is people count of the trip.\nBy clicking or long clicking this you can see the list of people details.");
        h.put("dismisstext", "Got It");
        l.add(h);


        h = new HashMap<>();
        h.put("view", f_spentAmountLayout);
        h.put("content", "This is the total amount spent for a trip.");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", f_fromAddress);
        h.put("content", "This is source place of the trip.");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", f_toAddress);
        h.put("content", "This is destination place of the trip.");
        h.put("dismisstext", "Got It");
        l.add(h);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        config.setMaskColor(0xDD000000);
        config.setDismissTextColor(Color.MAGENTA);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getSaltString());

        sequence.setConfig(config);
        for (int i = 0; i < l.size(); i++) {
            HashMap hashMap = (HashMap) l.get(i);
            sequence.addSequenceItem((View) hashMap.get("view"),
                    (String) hashMap.get("content"), (String) hashMap.get("dismisstext"));
        }

        sequence.start();


    }


    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
