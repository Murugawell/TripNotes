package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.JSON;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Murugavel on 12/22/2017.
 */
public class EditTrip {

    Context context;
    public static AlertDialog alert;
    public static boolean isCancel;
    Trip trip;

    EditTrip(Context con, Trip t) {
        context = con;
        this.trip = t;

    }

    public void editTripDialog(Trip t, String option) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.addtrip, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alert = alertDialogBuilder.create();
        Window window = alert.getWindow();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme1; //style id
        WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        final EditText editText_tripName = (EditText) promptsView.findViewById(R.id.add_trip_name);
        final EditText editText_tripEstimateAmount = (EditText) promptsView.findViewById(R.id.add_trip_estimation_amount);
        final EditText editText_tripFrom = (EditText) promptsView.findViewById(R.id.add_trip_from);
        final EditText editText_tripTo = (EditText) promptsView.findViewById(R.id.add_trip_to);
        Button add = (Button) promptsView.findViewById(R.id.trip_add_ok);
        final Button cancel = (Button) promptsView.findViewById(R.id.trip_add_cancel);
        alert.setTitle("Edit Trip");
        add.setText("Update");
        cancel.setText("Cancel");


        editText_tripName.setVisibility(View.GONE);
        editText_tripEstimateAmount.setVisibility(View.GONE);
        editText_tripFrom.setVisibility(View.GONE);
        editText_tripTo.setVisibility(View.GONE);
        switch (option) {
            case "all": {
                editText_tripName.setVisibility(View.VISIBLE);
                editText_tripEstimateAmount.setVisibility(View.VISIBLE);
                editText_tripFrom.setVisibility(View.VISIBLE);
                editText_tripTo.setVisibility(View.VISIBLE);
                break;
            }
            case "fromto": {
                editText_tripFrom.setVisibility(View.VISIBLE);
                editText_tripTo.setVisibility(View.VISIBLE);
                break;
            }
            case "from": {
                editText_tripFrom.setVisibility(View.VISIBLE);
                break;
            }
            case "to": {
                editText_tripTo.setVisibility(View.VISIBLE);
                break;
            }
            case "estimatedamount": {
                editText_tripEstimateAmount.setVisibility(View.VISIBLE);
                break;
            }
        }

        editText_tripFrom.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        editText_tripName.setText(t.getName());
        editText_tripEstimateAmount.setText(t.getEstimateAmount() + "");
        editText_tripFrom.setText(t.getFrom());
        editText_tripTo.setText(t.getTo());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tripName, tripestimateamount, from, to;
                tripName = editText_tripName.getText().toString();
                tripestimateamount = editText_tripEstimateAmount.getText().toString();
                from = editText_tripFrom.getText().toString().trim();
                to = editText_tripTo.getText().toString().trim();
                if (tripName.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter trip name", Snackbar.LENGTH_SHORT).show();
                    editText_tripName.requestFocus();
                    editText_tripName.requestFocus();
                } else if (tripestimateamount.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter estimate amount for this trip", Snackbar.LENGTH_SHORT).show();
                    editText_tripEstimateAmount.requestFocus();
                } else {
                    String output = updateTrip(tripName, tripestimateamount, from, to);
                    Toast.makeText(context, output, Toast.LENGTH_SHORT).show();
                    isCancel = false;
                    if (output.equalsIgnoreCase("Successfully updated")) {
                        alert.cancel();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isCancel = true;
                alert.cancel();
            }
        });

        alert.show();

    }

    private String updateTrip(String tripName, String tripEstimateAmount, String from, String to) {
//     return  successful message as  'Successfully updated'
        String s = null;
        Datas datas = new Datas(context);
        datas.open();
        try {
            Trip check = datas.getTrip(tripName);
            if (check == null || (check.getTime() == trip.getTime())) {
                JSONObject jsonObject = trip.getTripJSON();
                JSONArray jsonArrayPeople = trip.getPeoplesJSON();
                jsonObject.put(JSON.Trip.estimatedAmount, tripEstimateAmount);
                jsonObject.put(JSON.Trip.from, from);
                jsonObject.put(JSON.Trip.to, to);
                jsonObject.put(JSON.Trip.time, trip.getTime());
                jsonObject.put("people", jsonArrayPeople);
                datas.deleteTrip(trip.getName());
                datas.createTrip(tripName, jsonObject.toString());
                datas.close();
                return "Successfully updated";
            } else {
                return "Already trip is exist. Try with different trip name";
            }
        } catch (Exception e) {
            datas.close();
            return e.toString();
        }
    }
}
