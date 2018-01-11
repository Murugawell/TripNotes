package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.JSON;
import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Murugavel on 12/29/2017.
 */
public class EditPeople {

    Context context;
    String tripName;
    public static AlertDialog alert;

    EditPeople(Context con) {
        context = con;
    }

    public void editPeopleDialog(final Trip trip, final People people) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.addnewperson, null);
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
        final EditText editText_tripPeopleName = (EditText) promptsView.findViewById(R.id.add_person_name);
        final EditText editText_tripPeopleAssignAmount = (EditText) promptsView.findViewById(R.id.add_person_assignamount);
        final TextInputLayout editText_tripPeopleAssignAmountLayout = (TextInputLayout) promptsView.findViewById(R.id.add_person_assignamount_layout);
        final RadioGroup sharingRadioGroup = (RadioGroup) promptsView.findViewById(R.id.add_person_radio_group);
        final TextView textview_info = (TextView) promptsView.findViewById(R.id.add_person_info_textview);
        Button update = (Button) promptsView.findViewById(R.id.trip_person_add_btn);
        Button cancel = (Button) promptsView.findViewById(R.id.trip_person_cancel_btn);
        alert.setTitle("Edit People");
        update.setText("Update");
        cancel.setText("Cancel");
        textview_info.setText("");
        final String[] sharingRadioValue = {""};
        editText_tripPeopleAssignAmountLayout.setVisibility(View.GONE);

        editText_tripPeopleName.setText(people.getName() + "");
        editText_tripPeopleAssignAmount.setText(people.getMaxAmount() + "");
        if (people.getIsShared()) {
            sharingRadioGroup.check(R.id.add_person_radio_share);
        } else {
            sharingRadioGroup.check(R.id.add_person_radio_assign_manual);
            editText_tripPeopleAssignAmountLayout.setVisibility(View.VISIBLE);
        }
        sharingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sharingRadioValue[0] = ((RadioButton) promptsView.findViewById(i)).getText().toString();
                if (sharingRadioValue[0].equalsIgnoreCase("share equally")) {
                    textview_info.setText("Total trip amount will be shared equally.");
                    editText_tripPeopleAssignAmountLayout.setVisibility(View.GONE);
                } else {
                    textview_info.setText("You will pay entered amount for this trip.");
                    editText_tripPeopleAssignAmountLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String peopleName;
                peopleName = editText_tripPeopleName.getText().toString();
                if (peopleName.trim().length() <= 0) {
                    Snackbar.make(view, "Please enter people name", Snackbar.LENGTH_SHORT).show();
                    editText_tripPeopleName.requestFocus();
                } else if (sharingRadioValue[0].length() <= 0) {
                    Snackbar.make(view, "Please choose type of sharing", Snackbar.LENGTH_SHORT).show();

                } else if (sharingRadioValue[0].equalsIgnoreCase("assign manually") && editText_tripPeopleAssignAmount.getText().toString().trim().length() <= 0) {
                    Snackbar.make(view, "Please enter amount", Snackbar.LENGTH_SHORT).show();

                } else {
                    String name = peopleName;
                    boolean b = true;
                    float maxAmount = 0;
                    if (sharingRadioValue[0].equalsIgnoreCase("assign manually")) {
                        b = false;
                        maxAmount = Float.parseFloat(editText_tripPeopleAssignAmount.getText().toString().trim());
                    }
                    String s = updatePeople(name, b, maxAmount, trip, people);
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

    private String updatePeople(String name, boolean b, float maxAmount, Trip trip, People people) {
        JSONArray peopleArray = trip.getPeoplesJSON();
        JSONObject tripArray = trip.getTripJSON();
        try {
            JSONArray newPeopleArray = getNewPeopleArray(name, b, maxAmount, peopleArray, trip);
            tripArray.put(JSON.Trip.people, newPeopleArray);
        } catch (JSONException e) {
            return e.getMessage();
        }
        Datas datas = new Datas(context);
        datas.open();
        datas.deleteTrip(trip.getName());
        datas.createTrip(trip.getName(), tripArray.toString());
        datas.close();
        return "Successfuly Updated";
    }

    private JSONArray getNewPeopleArray(String name, boolean b, float maxAmount, JSONArray peopleArray, Trip trip) throws JSONException {
        Toast.makeText(context, maxAmount+"", Toast.LENGTH_SHORT).show();
        JSONArray array = new JSONArray();
        float amount = 0;
        if (b == false)
            amount += maxAmount;
        int shared = trip.totalPeopleSharing();
        if (b)
            shared += 1;
        amount = trip.getEstimateAmount() - amount;
        amount = amount / shared;
        for (int i = 0; i < peopleArray.length(); i++) {
            JSONObject jsonObject = peopleArray.getJSONObject(i);
            if (jsonObject.getString(JSON.People.name).equals(name)) {
                jsonObject.put(JSON.People.name, name);
                jsonObject.put(JSON.People.isShared, b);
                if (b)
                    jsonObject.put(JSON.People.maxAmount, amount);
                else
                    jsonObject.put(JSON.People.maxAmount, maxAmount);
            }
            else {
                if (jsonObject.getBoolean(JSON.People.isShared))
                    jsonObject.put(JSON.People.maxAmount, amount);
            }
            array.put(jsonObject);
        }
        return array;
    }


}
