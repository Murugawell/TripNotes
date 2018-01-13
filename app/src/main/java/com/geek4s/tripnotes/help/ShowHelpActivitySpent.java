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

public class ShowHelpActivitySpent extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_for_item_recycle);


        try {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " Help");
        } catch (Exception e) {
            e.printStackTrace();
        }


        TextView itemname = (TextView) findViewById(R.id.people_item_name);
        TextView amount = (TextView) findViewById(R.id.people_item_amount);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutitem);

        List l = new ArrayList();
        HashMap h;

        itemname.setText("FOOD");
        amount.setText("XXXX");


        h = new HashMap<>();
        h.put("view", itemname);
        h.put("content", "This is name of the item");
        h.put("dismisstext", "Got It");
        l.add(h);

        h = new HashMap<>();
        h.put("view", amount);
        h.put("content", "This is amount spent for the item");
        h.put("dismisstext", "Got It");
        l.add(h);


        h = new HashMap<>();
        h.put("view", layout);
        h.put("content", "Long click this to edit or remove the spent item");
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
