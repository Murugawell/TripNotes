package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek4s.tripnotes.bean.JSON;
import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Murugavel on 1/7/2018.
 */

public class SwapSpentAmount {
    Context context;
    Trip trip;
    People people;
    public static AlertDialog alert;
    private String[] listOfPeoples;

    SwapSpentAmount(Context context) {
        this.context = context;
    }


    public void swapSpentAMountDialog(final Context context, final Trip trip, final People people, final JSONObject jsonObject, List listOfPeople) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.swap_spent_amount, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final TextView textView_message = (TextView) promptsView.findViewById(R.id.swap_spent_amount_message);
        final TextView textView_title = (TextView) promptsView.findViewById(R.id.swap_spent_amount_title);
        final TextView textView_waring = (TextView) promptsView.findViewById(R.id.swap_spent_amount_message_warning);
        final TextView textView_spinner_hint = (TextView) promptsView.findViewById(R.id.textview_spinner_hint);
        final ImageView imageview_icon = (ImageView) promptsView.findViewById(R.id.swap_spent_amount_imageview);
        final Button confirm = (Button) promptsView.findViewById(R.id.confirm);
        final Button cancel = (Button) promptsView.findViewById(R.id.cancel);
        final AppCompatSpinner listOfPeopleSpinner = (AppCompatSpinner) promptsView.findViewById(R.id.list_of_people_spinner);

        listOfPeoples = (String[]) listOfPeople.toArray(new String[0]);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, listOfPeoples);

        listOfPeopleSpinner.setAdapter(adapter);

        String message = "";
        try {
            message = "Are you sure to swap " + "<b><font color=#FF34DD>" + jsonObject.getString("for") + "</font></b>" + " cost of " + "<b><font color=#FF34DD>" + jsonObject.getString("amount") + "</b></font>" + " from " + "<b><font color=#FF34DD>" + people.getName() + "</font></b> ? ";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String title = "<h4><b><font color=#FF1000>Swap ?</b></font></h4>";
        String warning = "<i>This can't be reversed</i>";

        textView_message.setText(Html.fromHtml(message));
        textView_waring.setText(Html.fromHtml(warning));
        textView_title.setText(Html.fromHtml(title));
        textView_title.setVisibility(View.GONE);

        textView_waring.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));

        String confirmText = "Swap";
        String cancelText = "Cancel";
        String spinnerHint = "Choose the people whom you want to swap";
        confirm.setText(Html.fromHtml(confirmText));
        cancel.setText(Html.fromHtml(cancelText));

        textView_spinner_hint.setText(Html.fromHtml(spinnerHint));
        imageview_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.swap_spent_amount));
        alert = alertDialogBuilder.create();
        Window window = alert.getWindow();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme2; //style id
        WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

//        alert.setTitle(title);


        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Drawable d = context.getResources().getDrawable(R.drawable.swap_spent_amount);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personSeleted = listOfPeopleSpinner.getSelectedItem().toString();
                swapSpentAmount(trip, people, jsonObject, personSeleted);
                alert.cancel();
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

    private String swapSpentAmount(Trip trip, People people, JSONObject jsonObject, String personSeleted) {
        JSONObject tripObject = trip.getTripJSON();
        try {
            JSONArray peopleArray = getNewPeoples(trip, people, jsonObject, personSeleted);
            tripObject.put(JSON.Trip.people, peopleArray);
        } catch (JSONException e) {
            return e.getMessage();
        }
        Datas datas = new Datas(context);
        datas.open();
        datas.deleteTrip(trip.getName());
        datas.createTrip(trip.getName(), tripObject.toString());
        datas.close();
        return "Swaped Successfuly";
    }

    private JSONArray getNewPeoples(Trip trip, People people, JSONObject jsonObject, String personSelected) throws JSONException {
        JSONArray peopleArray = trip.getPeoplesJSON();
        for (int i = 0; i < peopleArray.length(); i++) {
            if (peopleArray.getJSONObject(i).getString(JSON.People.name).equals(people.getName())) {
                peopleArray.getJSONObject(i).put(JSON.People.amountSpent, newAmountSpentClear(people, jsonObject));
            }
            if (peopleArray.getJSONObject(i).getString(JSON.People.name).equals(personSelected)) {
                People people1 = trip.getPeople(personSelected);
                peopleArray.getJSONObject(i).put(JSON.People.amountSpent, newAmountSpentAdd(people1, jsonObject));
            }
        }
        return peopleArray;
    }

    private JSONArray newAmountSpentClear(People people, JSONObject jsonObject) throws JSONException {
        JSONArray array = people.getAmountSpentJSON();
        JSONArray array1 = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getString(JSON.Amount.amountFor).equals(jsonObject.getString(JSON.Amount.amountFor))) {
                continue;
            }
            array1.put(array.getJSONObject(i));
        }
        return array1;
    }

    private JSONArray newAmountSpentAdd(People people, JSONObject jsonObject) throws JSONException {
        JSONArray array = people.getAmountSpentJSON();
        array.put(jsonObject);
        return array;
    }
}
