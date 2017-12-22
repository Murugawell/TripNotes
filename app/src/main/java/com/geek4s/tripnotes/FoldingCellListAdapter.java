package com.geek4s.tripnotes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.aakira.expandablelayout.Utils;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private int OPENEDCELL = -1;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;


    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.price = (TextView) cell.findViewById(R.id.title_price);
            viewHolder.time = (TextView) cell.findViewById(R.id.title_time_label);
            viewHolder.date = (TextView) cell.findViewById(R.id.title_date_label);
            viewHolder.fromAddress = (TextView) cell.findViewById(R.id.title_from_address);
            viewHolder.toAddress = (TextView) cell.findViewById(R.id.title_to_address);
            viewHolder.requestsCount = (TextView) cell.findViewById(R.id.title_people_count);
            viewHolder.pledgePrice = (TextView) cell.findViewById(R.id.title_amount);
            viewHolder.contentRequestBtn = (TextView) cell.findViewById(R.id.content_request_btn);
//

            final FoldingCell finalCell = cell;
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        finalCell.toggle(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            final RecyclerView recyclerView = (RecyclerView) cell.findViewById(R.id.recyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            final List<ItemModel> data = new ArrayList<>();
            data.add(new ItemModel(
                    "Person " + position,
                    R.color.colorAccent,
                    R.color.colorPrimary,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Person " + position, R.color.colorAccent,
                    R.color.colorPrimary,
                    Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)));
            recyclerView.setAdapter(new RecyclerViewRecyclerAdapter(data));

            cell.setTag(viewHolder);

            if (OPENEDCELL == position) {
                cell.unfold(true);
            }

        } else {
            // for existing cell set valid valid state(without animation)
            if (OPENEDCELL == position) {
                cell.unfold(true);
            }

            viewHolder = (ViewHolder) cell.getTag();

        }

        // bind data from selected element to view through view holder
        viewHolder.price.setText(item.getTripname());
        viewHolder.time.setText(item.getTime());
        viewHolder.date.setText(item.getDate());
        viewHolder.fromAddress.setText(item.getFromAddress());
        viewHolder.toAddress.setText(item.getToAddress());
        viewHolder.requestsCount.setText(String.valueOf(item.getRequestsCount()));
        viewHolder.pledgePrice.setText(item.getNoofpeople());

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
        }


        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView fromAddress;
        TextView toAddress;
        TextView requestsCount;
        TextView date;
        TextView time;
    }
}


