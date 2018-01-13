package com.geek4s.tripnotes.help;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class ShowHelpActivityPeople extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_list_row);
        TextView peoplename = (TextView) findViewById(R.id.textView);
        TextView amount = (TextView) findViewById(R.id.textView_amount);
        LinearLayout peopleLayout = (LinearLayout) findViewById(R.id.textViewLayout);
        RelativeLayout expand = (RelativeLayout) findViewById(R.id.button);


        try {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " Help");
        } catch (Exception e) {
            e.printStackTrace();
        }


        peoplename.setText("ABCDRFG");
        amount.setText("12345");

        List l = new ArrayList();
        HashMap h;

        h = new HashMap<>();
        h.put("view", peoplename);
        h.put("content", "This is name of the people");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", amount);
        h.put("content", "This is amount spent by the people");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", expand);
        h.put("content", "Click this to see full details of the people");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", peopleLayout);
        h.put("content", "Long click this to edit or remove people");
        h.put("dismisstext", "Got It");
        l.add(h);


        showTripHelp(l);
    }

    private void showTripHelp(List l) {
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
