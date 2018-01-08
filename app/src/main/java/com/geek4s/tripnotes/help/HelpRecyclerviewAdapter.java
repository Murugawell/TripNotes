package com.geek4s.tripnotes.help;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek4s.tripnotes.R;
import com.geek4s.tripnotes.ShowMorePeopleExpandRecyclerAdapter;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

/**
 * Created by Murugavel on 1/8/2018.
 */

public class HelpRecyclerviewAdapter extends RecyclerView.Adapter<HelpRecyclerviewAdapter.ViewHolder> {
    private Context context;
    List list;
    SparseBooleanArray expandState = new SparseBooleanArray();


    HelpRecyclerviewAdapter(List list) {
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            expandState.append(i, false);

        }

    }

    @Override
    public HelpRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        return new HelpRecyclerviewAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.help_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(final HelpRecyclerviewAdapter.ViewHolder holder, final int position) {


        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_dark_background));
        holder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableLayout.setExpanded(expandState.get(position));

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
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView question, answer;
        public RelativeLayout buttonLayout;
        ExpandableLinearLayout expandableLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            question = (TextView) itemView.findViewById(R.id.question);
            answer = (TextView) itemView.findViewById(R.id.answer);
            buttonLayout = (RelativeLayout) itemView.findViewById(R.id.button);
            expandableLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.expandableLayout);

        }
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
