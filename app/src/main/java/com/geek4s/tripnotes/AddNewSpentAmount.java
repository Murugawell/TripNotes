package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Murugavel on 12/22/2017.
 */
public class AddNewSpentAmount {

    Context context;
    Trip trip;
    People people;
    public static AlertDialog alert;

    AddNewSpentAmount(Context con, Trip trip, People people) {
        context = con;
        this.trip = trip;
        this.people = people;
    }

    public void addNewSpentDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.addnewamount, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alert = alertDialogBuilder.create();
        /*Window window = alert.getWindow();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme2; //style id
        WindowManager.LayoutParams wlp = window.getAttributes();*/
        // wlp.gravity = Gravity.BOTTOM;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
        final EditText editText_person_name = (EditText) promptsView.findViewById(R.id.add_amount_person_name);
        final EditText editText_spentAmountValue = (EditText) promptsView.findViewById(R.id.add_amount_value);
        final EditText editText_spentAmountFor = (EditText) promptsView.findViewById(R.id.add_amount_for);
        Button add = (Button) promptsView.findViewById(R.id.trip_add_ok);
        final Button cancel = (Button) promptsView.findViewById(R.id.trip_add_cancel);
        alert.setTitle("Add New Spent");
        add.setText("Add");
        cancel.setText("Cancel");
        editText_person_name.setVisibility(View.GONE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spentAmountValue, spentAmountFor;
                spentAmountValue = editText_spentAmountValue.getText().toString();
                spentAmountFor = editText_spentAmountFor.getText().toString().trim();
                if (spentAmountFor.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter the item you spent", Snackbar.LENGTH_SHORT).show();
                    editText_spentAmountFor.requestFocus();
                } else if (spentAmountValue.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter the amount you spent", Snackbar.LENGTH_SHORT).show();
                    editText_spentAmountValue.requestFocus();
                } else {
                    String output = "";
                    newAmount(trip, people, spentAmountFor, spentAmountValue);
                    Toast.makeText(context, output, Toast.LENGTH_LONG).show();
                    alert.cancel();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });

        alert.show();

    }

    private void newAmount(Trip trip, People people, String amountFor, String amount) {
        try {
            JSONArray jsonArray = people.getAmountSpentJSON();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("for", amountFor);
            jsonObject.put("amount", amount);
            jsonArray.put(jsonObject);
            JSONObject tripJsonObject1 = trip.getTripJSON();
            JSONArray peopleArray = trip.getPeoplesJSON();
            for (int i = 0; i < peopleArray.length(); i++) {
                if (people.getName().equals(peopleArray.getJSONObject(i).getString("name"))) {
                    peopleArray.getJSONObject(i).put("amountSpent", jsonArray);
                }
            }
            tripJsonObject1.put("people", peopleArray);
            Datas datas = new Datas(context);
            datas.open();
            datas.deleteTrip(trip.getName());
            datas.createTrip(trip.getName(), tripJsonObject1.toString());
            datas.close();
        } catch (Exception e) {
        }
    }

}



/*
personname = editText_person_name.getText().toString();
if (personname.trim().length() <= 0) {
        Snackbar.make(view, "Please enter person name to create new spent", Snackbar.LENGTH_SHORT).show();
        editText_person_name.requestFocus();
        } else*/
