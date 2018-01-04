package com.geek4s.tripnotes;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

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
        holder.setIsRecyclable(false);
        holder.textView.setText(item.getName());
        holder.textviewamount.setText(item.getTotalAmountSpent() + "");
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.background1));
        holder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableLayout.setExpanded(expandState.get(position));

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

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, listofPeolpleTextView, textviewamount;
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
            addNewSpent = (Button) v.findViewById(R.id.addnewspent_indiv_People);
            textviewamount = (TextView) v.findViewById(R.id.textView_amount);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);
            listofPeolpleTextView = (TextView) v.findViewById(R.id.people_list_textview);


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