package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.JSON;
import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Murugavel on 12/22/2017.
 */
public class EditSpentAmount {

    Context context;
    Trip trip;
    People people;
    public static AlertDialog alert;

    EditSpentAmount(Context context) {
        this.context = context;
    }

    public void editSpentDialog(final Context context, final Trip trip, final People people, JSONObject jsonObject) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.addnewamount, null);
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
        final EditText editText_person_name = (EditText) promptsView.findViewById(R.id.add_amount_person_name);
        final EditText editText_spentAmountValue = (EditText) promptsView.findViewById(R.id.add_amount_value);
        final EditText editText_spentAmountFor = (EditText) promptsView.findViewById(R.id.add_amount_for);
        Button update = (Button) promptsView.findViewById(R.id.trip_add_ok);
        final Button cancel = (Button) promptsView.findViewById(R.id.trip_add_cancel);
        alert.setTitle("Edit Spent");
        update.setText("Update");
        cancel.setText("Cancel");
        editText_person_name.setVisibility(View.GONE);

        try {
            editText_spentAmountFor.setText(jsonObject.getString("for"));
            editText_spentAmountValue.setText(jsonObject.getString("amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        update.setOnClickListener(new View.OnClickListener() {
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
                    updateAmount(trip, people, spentAmountFor, spentAmountValue);
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

    private String updateAmount(Trip trip, People people, String amountFor, String amount) {
        JSONObject tripObject = trip.getTripJSON();
        try {
            JSONArray peopleArray = getNewPeoples(trip, people, new JSONObject().put(JSON.Amount.amountFor, amountFor).put(JSON.Amount.amount, amount));
            tripObject.put(JSON.Trip.people, peopleArray);
        } catch (JSONException e) {
            return e.getMessage();
        }
        Datas datas = new Datas(context);
        datas.open();
        datas.deleteTrip(trip.getName());
        datas.createTrip(trip.getName(), tripObject.toString());
        datas.close();
        return "Updated Successfuly";
    }

    private JSONArray getNewPeoples(Trip trip, People people, JSONObject jsonObject) throws JSONException {
        JSONArray peopleArray = trip.getPeoplesJSON();
        for (int i = 0; i < peopleArray.length(); i++) {
            if (peopleArray.getJSONObject(i).getString(JSON.People.name).equals(people.getName())) {
                peopleArray.getJSONObject(i).put(JSON.People.amountSpent, newAmountSpent(people, jsonObject));
            }
        }
        return peopleArray;
    }

    private JSONArray newAmountSpent(People people, JSONObject jsonObject) throws JSONException {
        JSONArray array = people.getAmountSpentJSON();
        JSONArray array1 = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getString(JSON.Amount.amountFor).equals(jsonObject.getString(JSON.Amount.amountFor))) {
                array.getJSONObject(i).put(JSON.Amount.amountFor, jsonObject.getString(JSON.Amount.amountFor));
                array.getJSONObject(i).put(JSON.Amount.amount, jsonObject.getString(JSON.Amount.amount));
            }
            array1.put(array.getJSONObject(i));
        }
        return array1;
    }

}



/*
personname = editText_person_name.getText().toString();
if (personname.trim().length() <= 0) {
        Snackbar.make(view, "Please enter person name to create new spent", Snackbar.LENGTH_SHORT).show();
        editText_person_name.requestFocus();
        } else*/
