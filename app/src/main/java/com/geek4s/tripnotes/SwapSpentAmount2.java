package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Murugavel on 1/7/2018.
 */

public class SwapSpentAmount2 {
    Context context;
    Trip trip;
    People people;
    public static AlertDialog alert;
    private String[] listOfPeoples;

    SwapSpentAmount2() {

    }


    public void swapSpentAMountDialog(final Context context, final Trip trip, final People people, JSONObject jsonObject) {
      /*  LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.swap_spent_amount, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final TextView textView_message = (TextView) promptsView.findViewById(R.id.swap_spent_amount_message);
        final TextView textView_title = (TextView) promptsView.findViewById(R.id.swap_spent_amount_title);
        final TextView textView_waring = (TextView) promptsView.findViewById(R.id.swap_spent_amount_message_warning);
        final ImageView imageview_icon = (ImageView) promptsView.findViewById(R.id.swap_spent_amount_imageview);
        final Button confirm = (Button) promptsView.findViewById(R.id.confirm);
        final Button cancel = (Button) promptsView.findViewById(R.id.cancel);

        listOfPeoples = new String[]{
                "Belgium", "France", "Italy", "Italy1", "Germany", "Spain"
        };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, listOfPeoples);

        AutoCompleteTextView listOfPeopleAutoComplete = (AutoCompleteTextView)
                promptsView.findViewById(R.id.autocomplete);
        listOfPeopleAutoComplete.setAdapter(adapter);
        listOfPeopleAutoComplete.setThreshold(1);
        String confirmText = "Swap";
        String cancelText = "Cancel";
        confirm.setText(Html.fromHtml(confirmText));
        cancel.setText(Html.fromHtml(cancelText));
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

        final String[] my_var = new String[1];

        listOfPeopleAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                my_var[0] = adapter.getItem(position).toString();
            }
        });
*//**
         * Unset the var whenever the user types. Validation will
         * then fail. This is how we enforce selecting from the list.
         *//*
        listOfPeopleAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                my_var[0] = null;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
*/
    }
}


// spinner disable particular item
/*
add at end of adapter declaration
* {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        }
*
* */