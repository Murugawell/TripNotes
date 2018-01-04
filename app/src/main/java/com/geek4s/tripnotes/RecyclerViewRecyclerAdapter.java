package com.geek4s.tripnotes;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class RecyclerViewRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewRecyclerAdapter.ViewHolder> {

    private final List<People> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    Trip trip;

    public RecyclerViewRecyclerAdapter(final List<People> data, Trip trip) {
        this.data = data;
        this.trip = trip;
        for (int i = 0; i < data.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final People item = data.get(position);
        int maxLength = 10;
        holder.setIsRecyclable(false);

        String displayPeopleName = formatedString(item.getName(), maxLength);
        final String peopleName = item.getName();

        holder.textView.setText(displayPeopleName);

        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "This is " + peopleName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        holder.textviewamount.setText(item.getTotalAmountSpent() + "");
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.background1));
        holder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableLayout.setExpanded(expandState.get(position));
        JSONArray amt = item.getAmountSpentJSON();
        if (amt != null) {
            {
                if (amt.length() > 0) {
                    try {
                        holder.layitem1.setVisibility(View.VISIBLE);
                        String name = amt.getJSONObject(0).getString("for");
                        String amount = amt.getJSONObject(0).getString("amount");
                        holder.item1name.setText(formatedString(name, 10));
                        holder.item1amount.setText(amount);
                        if (amt.length() > 1) {
                            holder.layitem2.setVisibility(View.VISIBLE);
                            String name2 = amt.getJSONObject(1).getString("for");
                            String amount2 = amt.getJSONObject(1).getString("amount");
                            holder.item2name.setText(formatedString(name2, 10));
                            holder.item2amount.setText(amount2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (amt.length() < 3) {
                    holder.showmore.setVisibility(View.GONE);
                }
            }
        }


        holder.addNewSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewSpentAmount(context, trip, item).addNewSpentDialog();
            }
        });
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.buttonLayout, 0f, 180f).start();
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.buttonLayout, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    //    merthod to add padding at the eod of string
    private String formatedString(String name, int maxLength) {
        if (name.length() > maxLength) {
            name = name.substring(0, maxLength) + "..";
        }
        if (name.length() < maxLength) {
            int l = maxLength - name.length() + 2;
            name = String.format("%-" + l + "s", name);
        }
        return name;

    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button showmore;
        public TextView textView, listofPeolpleTextView, textviewamount;
        public LinearLayout layitem1, layitem2;
        public ImageView item1icon, item2icon;
        public TextView item1name, item2name, item1amount, item2amount;
        public Button addNewSpent;

        public RelativeLayout buttonLayout;
        /**
         * You must use the ExpandableLinearLayout in the recycler view.
         * The ExpandableRelativeLayout doesn't work.
         */
        public ExpandableLinearLayout expandableLayout;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            item1name = (TextView) v.findViewById(R.id.people_item1_name);
            item1amount = (TextView) v.findViewById(R.id.people_item1_amount);
            item1icon = (ImageView) v.findViewById(R.id.people_item1_list_icon);
            item2name = (TextView) v.findViewById(R.id.people_item2_name);
            item2amount = (TextView) v.findViewById(R.id.people_item2_amount);
            item2icon = (ImageView) v.findViewById(R.id.people_item2_list_icon);
            addNewSpent = (Button) v.findViewById(R.id.addnewspent_indiv_People);
            showmore = (Button) v.findViewById(R.id.addnewspent_showmore_people);
            textviewamount = (TextView) v.findViewById(R.id.textView_amount);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            layitem1 = (LinearLayout) v.findViewById(R.id.layoutitem1);
            layitem2 = (LinearLayout) v.findViewById(R.id.layoutitem2);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);
            layitem1.setVisibility(View.GONE);
            layitem2.setVisibility(View.GONE);

//            listofPeolpleTextView = (TextView) v.findViewById(R.id.people_list_textview);


//            listofPeolpleTextView.setMovementMethod(new ScrollingMovementMethod());

        }


    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}