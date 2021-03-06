package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Murugavel on 12/29/2017.
 */
public class DeleteTrip {

    Context context;
    public static AlertDialog alert;

    public DeleteTrip(Context context) {
        this.context = context;
    }

    public void deleteTripDialog(Context context, final Trip trip) {
        String title = "<h4><b><font color=#FF1000>Remove ?</b></font></h4>";
        String message = "Are you sure to remove " + "<b><font color=#FF34DD>" + trip.getName() + "</font></b> trip ?";
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
        imageview_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delete));
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
                deleteTrip(trip);
                alert.cancel();
            }
        });

        alert.show();
    }

    private String deleteTrip(Trip trip) {
        Datas datas = new Datas(context);
        datas.open();
        datas.deleteTrip(trip.getName());
        datas.close();
        return "Deleted Successfuly";
    }


}
