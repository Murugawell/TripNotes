package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Murugavel on 12/22/2017.
 */
public class ShowOptionsDialogs {

    Context context;
    Trip trip;
    People people;
    public static AlertDialog alert;
    public static String selectedOption;

    ShowOptionsDialogs(Context con) {
        context = con;
    }

    public void showOptionsDialogMethod(String[] options, String title, Drawable[] draw) {

        selectedOption = "";
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.show_options, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alert = alertDialogBuilder.create();
        Window window = alert.getWindow();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme2; //style id
        WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alert.setTitle(title);
        Button update = (Button) promptsView.findViewById(R.id.show_options_update);
        Button delete = (Button) promptsView.findViewById(R.id.show_options_delete);
        Button swap = (Button) promptsView.findViewById(R.id.show_options_swap);
        Button skip = (Button) promptsView.findViewById(R.id.show_options_skip);

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        update.setCompoundDrawablesWithIntrinsicBounds(null, draw[0], null, null);
        delete.setCompoundDrawablesWithIntrinsicBounds(null, draw[1], null, null);
        Drawable d = context.getResources().getDrawable(R.drawable.swap_spent_amount);
        swap.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);

        update.setText(" Edit ");
        delete.setText("Remove");
        swap.setText(" Swap ");

        if (options.length != 3) {
            swap.setVisibility(View.GONE);
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = "update";
                alert.cancel();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = "delete";
                alert.cancel();
            }
        });

        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = "swap";
                alert.cancel();

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = "skip";
                alert.cancel();

            }
        });


        alert.show();
    }


}



/*
personname = editText_person_name.getText().toString();
if (personname.trim().length() <= 0) {
        Snackbar.make(view, "Please enter person name to create new spent", Snackbar.LENGTH_SHORT).show();
        editText_person_name.requestFocus();
        } else*/
