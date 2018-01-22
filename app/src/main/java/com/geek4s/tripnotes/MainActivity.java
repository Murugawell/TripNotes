package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.Trip;
import com.geek4s.tripnotes.graph.BarChartActivity;
import com.geek4s.tripnotes.help.HelpActivity;
import com.geek4s.tripnotes.help.OtherApps;
import com.geek4s.tripnotes.help.ShowHelpActivity;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Trip> allTrips = null;
    static ListView theListView;
    private static FoldingCellListAdapter adapter;
    public static FoldingCellListAdapter adap;
    public static FloatingActionButton fab;
    private MenuItem helpMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context con = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewTrip(con).addTripDialog();
                AddNewTrip.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (AddNewTrip.resultAlertAction.equalsIgnoreCase("confirm")) {
                            getTripListFromDB(MainActivity.this);
                            showListOfTrips(MainActivity.this);
                        }
                    }
                });

            }
        });

        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);

        // get list of trips from database
        getTripListFromDB(this);

        // show all the trips
        showListOfTrips(this);


        ShowcaseConfig showcaseConfig = new ShowcaseConfig();
        showcaseConfig.setDelay(500); // half second between each showcase view
        showcaseConfig.setMaskColor(0xDD000000);
        showcaseConfig.setDismissTextColor(Color.MAGENTA);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "1234");
        sequence.addSequenceItem(fab, "Click this button to create new trip", "Got It");
        sequence.setConfig(showcaseConfig);
        sequence.start();
    }

    public static void getTripListFromDB(Context context) {
        Datas da = new Datas(context);
        try {
            da.open();
            allTrips = da.getAllTrips();
            da.close();
//            Collections.reverse(allTrips);
//            Toast.makeText(context, allTrips.size() + "", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public static void showListOfTrips(Context context) {
        if (allTrips != null) {
            if (allTrips.size() > 0) {
                theListView.setVisibility(View.VISIBLE);

                // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
                adapter = new FoldingCellListAdapter(context, allTrips);
                adap = adapter;

                // set elements to adapter
                theListView.setAdapter(adapter);
            }
            if (allTrips.size() == 0) {
                theListView.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

//            startActivity(new Intent(MainActivity.this, OtherApps.class));
//            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

            return true;
        } else if (id == R.id.action_refresh) {
            Toast.makeText(getApplicationContext(), "Trips Updated", Toast.LENGTH_SHORT).show();
            getTripListFromDB(this);
            showListOfTrips(this);
            return true;
        } else if (id == R.id.action_help) {
            startActivity(new Intent(MainActivity.this, ShowHelpActivity.class));
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
