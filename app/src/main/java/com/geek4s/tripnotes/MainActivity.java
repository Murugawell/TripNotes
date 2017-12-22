package com.geek4s.tripnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FoldingCellListAdapter adapter;
    public static FoldingCellListAdapter adap;
    Datas datas;
    ArrayList<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        try {
            setContentView(R.layout.activity_main);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context con = this;
        //Initializing Data
        datas = new Datas(this);
        datas.open();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewTrip(con).addTripDialog();
            }
        });
        //Get all trip datas in arrayList
        trips = datas.getAllTrips();
        // get our list view
        ListView theListView = (ListView) findViewById(R.id.mainListView);
        // prepare elements to display
        final ArrayList<Item> items = Item.getTestingList();
/*

        // add custom btn handler to first list item
        items.get(0).setRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "CUSTOM HANDLER FOR FIRST BUTTON", Toast.LENGTH_SHORT).show();
            }
        });
*/
        if (items.size() > 0 && items != null) {
            // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
            adapter = new FoldingCellListAdapter(this, items);
            adap = adapter;
            // add default btn handler for each request btn on each item if custom handler not found
            adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
                }
            });

            // set elements to adapter
            theListView.setAdapter(adapter);

        }

/*
        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
//                adapter.registerToggle(pos);
            }
        });
*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void adataperChanges() {
        adap.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datas.close();
    }
}
