package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Murugavel on 12/22/2017.
 */
public class AddNewTrip {
    Context context;

    AddNewTrip(Context con) {
        context = con;
    }

    public void addTripDialog() {

        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.addtrip, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);


        final AlertDialog alert = alertDialogBuilder.create();

        /*Window window = alert.getWindow();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme2; //style id

        WindowManager.LayoutParams wlp = window.getAttributes();*/

        // wlp.gravity = Gravity.BOTTOM;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);


        final EditText editText_tripName = (EditText) promptsView.findViewById(R.id.add_trip_name);
        final EditText editText_tripEstimateAmount = (EditText) promptsView.findViewById(R.id.add_trip_estimation_amount);
        final EditText editText_tripFrom = (EditText) promptsView.findViewById(R.id.add_trip_from);
        final EditText editText_tripTo = (EditText) promptsView.findViewById(R.id.add_trip_to);

        Button add = (Button) promptsView.findViewById(R.id.trip_add_ok);
        Button cancel = (Button) promptsView.findViewById(R.id.trip_add_cancel);

        alert.setTitle("Add New Trip");
        add.setText("Add");
        cancel.setText("Cancel");


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String triptitle, tripestimateamount, from, to;
                triptitle = editText_tripName.getText().toString();
                tripestimateamount = editText_tripEstimateAmount.getText().toString();
                from = editText_tripFrom.getText().toString().trim();
                to = editText_tripTo.getText().toString().trim();

                if (triptitle.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter trip name to create new trip", Snackbar.LENGTH_SHORT).show();
                    editText_tripName.requestFocus();
                } else if (tripestimateamount.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter estimate amount for this trip", Snackbar.LENGTH_SHORT).show();
                    editText_tripEstimateAmount.requestFocus();
                } else {
                    //code to create new trip
                    Toast.makeText(context, "Trip title" + triptitle, Toast.LENGTH_LONG).show();
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
}
