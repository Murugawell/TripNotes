package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Murugavel on 12/29/2017.
 */
public class AddNewPeople {

    Context context;
    String tripName;

    AddNewPeople(Context con, String tripName) {
        context = con;
        this.tripName = tripName;
    }

    public void addPeopleDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.addnewperson, null);
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
        final EditText editText_tripName = (EditText) promptsView.findViewById(R.id.add_person_name);
        final EditText editText_assignAmount = (EditText) promptsView.findViewById(R.id.add_person_assignamount);
        final TextInputLayout editText_assignAmountLayout = (TextInputLayout) promptsView.findViewById(R.id.add_person_assignamount_layout);
        final RadioGroup radioGroup = (RadioGroup) promptsView.findViewById(R.id.add_person_radio_group);
        final TextView textview_info = (TextView) promptsView.findViewById(R.id.add_person_info_textview);
        Button add = (Button) promptsView.findViewById(R.id.trip_person_add_btn);
        Button cancel = (Button) promptsView.findViewById(R.id.trip_person_cancel_btn);
        alert.setTitle("Add New People");
        add.setText("Add");
        cancel.setText("Cancel");
        textview_info.setText("");
        final String[] radio = {""};
        editText_assignAmountLayout.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radio[0] = ((RadioButton) promptsView.findViewById(i)).getText().toString();
                if (radio[0].equalsIgnoreCase("share equally")) {
                    textview_info.setText("share");
                    editText_assignAmountLayout.setVisibility(View.GONE);
                } else {
                    textview_info.setText("manual");
                    editText_assignAmountLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String triptitle, tripestimateamount, from, to;
                triptitle = editText_tripName.getText().toString();
                if (triptitle.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter people name", Snackbar.LENGTH_SHORT).show();
                    editText_tripName.requestFocus();
                } else if (radio[0].length() <= 0) {
                    Snackbar.make(view, "Please choose type of sharing", Snackbar.LENGTH_SHORT).show();

                } else if (radio[0].equalsIgnoreCase("assign manually") && editText_assignAmount.getText().toString().trim().length() <= 0) {
                    Snackbar.make(view, "Please enter amount", Snackbar.LENGTH_SHORT).show();

                } else {
                    String name = triptitle;
                    boolean b = true;
                    float maxAmount = 0;
                    if (radio[0].equalsIgnoreCase("assign manually")) {
                        b = false;
                        maxAmount = Float.parseFloat(editText_assignAmount.getText().toString().trim());
                    }
                    String s = addPeople(name, b, maxAmount);
                    Toast.makeText(context, s, Toast.LENGTH_LONG).show();
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

    private String addPeople(String name, boolean b, float maxAmount) {
        Datas datas = new Datas(context);
        datas.open();
        Trip trip = datas.getTrip(tripName);
        if (trip == null) {
            return "Invalid Trip";
        }
        try {
            JSONObject jsonObject = trip.getTripJSON();
            JSONArray jsonArrayPeople = trip.getPeoplesJSON();
            Log.i("First Array", jsonArrayPeople.toString());
            JSONObject jsonObjectPeople = new JSONObject();
            jsonObjectPeople.put("name", name);
            jsonObjectPeople.put("maxamount", maxAmount);
            jsonObjectPeople.put("isShared", b);
            jsonObjectPeople.put("amountSpent", new JSONArray());
            jsonArrayPeople.put(jsonObjectPeople);
            jsonArrayPeople = getNewAmounts(jsonArrayPeople, trip);
            Log.i("Second Array", jsonArrayPeople.toString());
            jsonObject.put("people", jsonArrayPeople);
            datas.deleteTrip(tripName);
            datas.createTrip(tripName, jsonObject.toString());
            Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
            return "Successfully Added";
        }
        catch (Exception e) {
            return e.toString();
        }
    }

    private JSONArray getNewAmounts(JSONArray jsonArray, Trip trip) {
        JSONArray newJsonArray = new JSONArray();
        Log.i("Middle Array", jsonArray.toString());
        try {
            float shareAmount = trip.getEstimateAmount();
            int tPeople = jsonArray.length();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (jsonArray.getJSONObject(i));
                Log.i("First Object", jsonObject.toString());
                if (!jsonObject.getBoolean("isShared")) {
                    shareAmount -= jsonObject.getDouble("maxamount");
                    tPeople--;
                }
            }
            shareAmount /= tPeople ;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (jsonArray.getJSONObject(i));
                Log.i("Second Object", jsonObject.toString());
                if (jsonObject.getBoolean("isShared")) {
                    jsonObject.put("maxamount", shareAmount);
                }
                newJsonArray.put(jsonObject);
            }
        }
        catch (Exception e) {}
        return newJsonArray;
    }

}
