package com.geek4s.tripnotes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import java.util.ArrayList;
import java.util.List;

public class ShowMorePeopleViewActivity extends AppCompatActivity {
    public static Trip trip;
    public static People people;
    RecyclerView recyclerView;
    FloatingActionButton addNewPeople;
    TextView heading;
    public static AppCompatImageButton refresh;
    private FloatingActionButton back;
    private TextView info;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        people = new People();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_more_people_activity);
        try {
            if (trip != null)
                getSupportActionBar().setTitle(trip.getName());
            addNewPeople = (FloatingActionButton) findViewById(R.id.add_new_people_fab);
            back = (FloatingActionButton) findViewById(R.id.back_fab);
            heading = (TextView) findViewById(R.id.show_more_people_heading_text_view);
            info = (TextView) findViewById(R.id.show_more_people_info);
            refresh = (AppCompatImageButton) findViewById(R.id.show_more_people_refresh);
            recyclerView = (RecyclerView) findViewById(R.id.show_more_people_recyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(this));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            String text = "<b><font color=#FFFFFF>People(</font><font color=#000000>" + trip.getPeoples().length + "</font><" + "<font color=#FFFFFF>)</font></b>";
            heading.setText(Html.fromHtml(text));
            final List<People> peopleList = convertJSONARRAYtoLIST(trip.getPeoples());
            setAdapter(peopleList);


            String txt = "No people available. Click <b><i> Add New People</i></b> to add new people in <b>" + trip.getName() + "</b> trip";
            info.setText(Html.fromHtml(txt));
            info.setBackgroundColor(getResources().getColor(R.color.btnRequest));


            if (peopleList.size() == 0) {
                info.setVisibility(View.VISIBLE);
            } else {
                info.setVisibility(View.GONE);

            }

            final Context con = this;
            addNewPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AddNewPeople addNewPeople = new AddNewPeople(con, trip.getName().toString());
                    addNewPeople.addPeopleDialog();
                    AddNewPeople.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (AddNewPeople.resultAlertAction.equalsIgnoreCase("confirm")) {
                                refreshData(ShowMorePeopleViewActivity.this);
                            }
                        }
                    });
                }
            });

            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshData(ShowMorePeopleViewActivity.this);
                    Snackbar.make(view, "Updated successfully", Snackbar.LENGTH_SHORT).show();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    people = new People();
                    overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setAdapter(List<People> peopleList) {
        recyclerView.setAdapter(new ShowMorePeopleExpandRecyclerAdapter(peopleList, trip, people));
    }

    public void refreshData(Context context) {
        Datas datas = new Datas(context);
        datas.open();
        trip = datas.getTrip(trip.getName());
//        Toast.makeText(this, "Reload Successfully" + trip.getPeoples().length, Toast.LENGTH_SHORT).show();
//        Snackbar.make(view, "Reload Successfully", Snackbar.LENGTH_LONG).show();
        final List<People> peopleList = convertJSONARRAYtoLIST(trip.getPeoples());
        setAdapter(peopleList);
        datas.close();
        String text = "<b><font color=#FFFFFF>People(</font><font color=#000000>" + trip.getPeoples().length + "</font><" + "<font color=#FFFFFF>)</font></b>";
        heading.setText(Html.fromHtml(text));

        if (peopleList.size() == 0) {
            info.setVisibility(View.VISIBLE);
        } else {
            info.setVisibility(View.GONE);

        }

    }

    private List<People> convertJSONARRAYtoLIST(People[] peoplesJSON) {
        List<People> list = new ArrayList<>();
        for (int i = 0; i < peoplesJSON.length; i++) {
            list.add(i, peoplesJSON[i]);
        }
        return list;
    }

    public static void refresh(Trip t, People item, Context context) {
//
        refresh.performClick();
//        try {
//            people = item;
//            trip = t;
//            ((Activity) context).finish();
//            context.startActivity(new Intent(context, ShowMorePeopleViewActivity.class));
//        } catch (Exception e) {
//
//        }

    }
}

