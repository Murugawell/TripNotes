package com.geek4s.tripnotes.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.geek4s.tripnotes.R;

/**
 * Created by Murugavel on 1/10/2018.
 */

public class OtherApps extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout l = new LinearLayout(this);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        recyclerView.setAdapter(new AllAppsAdapter());
        recyclerView.setPadding(10, 10, 10, 10);
        l.addView(recyclerView);
        setContentView(l);
    }

    private class AllAppsAdapter extends RecyclerView.Adapter<AllAppsAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout linearLayout1 = new LinearLayout(getApplicationContext());
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

            Button b = new Button(getApplicationContext());
            b.setText("ddfsfds");
            linearLayout1.addView(b, 0);

            linearLayout.addView(linearLayout1);

            return new AllAppsAdapter.ViewHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
