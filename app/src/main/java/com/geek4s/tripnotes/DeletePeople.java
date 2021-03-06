package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
public class DeletePeople {

    Context context;
    public static AlertDialog alert;

    DeletePeople(Context context) {
        this.context = context;
    }

    public void deletePeopleDialog(final Context context, final Trip trip, final People people) {
        String title = "<h4><b><font color=#FF1000>Remove ?</b></font></h4>";
        String message = "Are you sure to remove " + "<b><font color=#FF34DD>" + people.getName() + "</font></b> from <b><font color=#FF34DD>" + trip.getName() + "</font></b>" + " trip ?";
        String warning = "<i>This can't be reversed</i>";
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.deletepeople, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final TextView textView_message = (TextView) promptsView.findViewById(R.id.delete_people_message);
        final TextView textView_title = (TextView) promptsView.findViewById(R.id.delete_people_title);
        final TextView textView_waring = (TextView) promptsView.findViewById(R.id.delete_people_message_warning);
        final ImageView imageview_icon = (ImageView) promptsView.findViewById(R.id.delete_people_imageview);
        final Button confirm = (Button) promptsView.findViewById(R.id.confirm);
        final Button cancel = (Button) promptsView.findViewById(R.id.cancel);
        String confirmText = "Remove";
        String cancelText = "Cancel";
        confirm.setText(Html.fromHtml(confirmText));
        cancel.setText(Html.fromHtml(cancelText));
        imageview_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.remove_people));
        textView_message.setText(Html.fromHtml(message));
        textView_waring.setText(Html.fromHtml(warning));
        textView_title.setText(Html.fromHtml(title));
        textView_title.setVisibility(View.GONE);

        textView_waring.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));

        alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = alert.getWindow();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme1; //style id
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            alert.getWindow().setElevation(5f);
        }
        WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = deletePeople(trip, people);
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                alert.cancel();
            }
        });

        alert.show();
    }

    private String deletePeople(Trip trip, People people) {
        if (people.getAmountSpent().size() > 0)
            return "Please transfer the spent amount of " + people.getName() + " to other person before deleting";
        JSONArray peopleArray = trip.getPeoplesJSON();
        JSONObject tripArray = trip.getTripJSON();
        try {
            JSONArray newPeopleArray = getNewPeopleArray(peopleArray, trip, people);
            tripArray.put(JSON.Trip.people, newPeopleArray);
        } catch (JSONException e) {
            return e.getMessage();
        }
        Datas datas = new Datas(context);
        datas.open();
        datas.deleteTrip(trip.getName());
        datas.createTrip(trip.getName(), tripArray.toString());
        datas.close();
        return "Successfuly Removed";
    }

    private JSONArray getNewPeopleArray(JSONArray peopleArray, Trip trip, People people) throws JSONException {
        JSONArray array = new JSONArray();
        float amount = trip.maxAmountUnsharedPeople();
        if (people.getIsShared() == false)
            amount -= people.getMaxAmount();
        int shared = trip.totalPeopleSharing();
        if (people.getIsShared())
            shared -= 1;
        amount = trip.getEstimateAmount() - amount;
        amount = amount / shared;
        for (int i = 0; i < peopleArray.length(); i++) {
            JSONObject jsonObject = peopleArray.getJSONObject(i);
            if (jsonObject.getString(JSON.People.name).equals(people.getName())) {
                continue;
            }
            else {
                if (jsonObject.getBoolean(JSON.People.isShared))
                    jsonObject.put(JSON.People.maxAmount, amount);
                array.put(jsonObject);
            }
        }
        return array;
    }


}
