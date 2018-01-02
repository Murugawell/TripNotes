package com.geek4s.tripnotes;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.Trip;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends ArrayAdapter<Trip> {

    private int OPENEDCELL = -1;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;


    public FoldingCellListAdapter(Context context, List<Trip> objects) {
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
        Trip item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.f_estimatedAmount = (TextView) cell.findViewById(R.id.title_estimateamount);
            viewHolder.f_time = (TextView) cell.findViewById(R.id.title_time_label);
            viewHolder.f_day = (TextView) cell.findViewById(R.id.title_day_label);
            viewHolder.f_date = (TextView) cell.findViewById(R.id.title_date_label);
            viewHolder.f_fromAddress = (TextView) cell.findViewById(R.id.title_from_address);
            viewHolder.f_toAddress = (TextView) cell.findViewById(R.id.title_to_address);
            viewHolder.f_peopleCount = (TextView) cell.findViewById(R.id.title_people_count);
            viewHolder.f_tripAmount = (TextView) cell.findViewById(R.id.title_amount);
            viewHolder.f_triptitle = (TextView) cell.findViewById(R.id.cell_title_trip_name_textview);
            viewHolder.b_triptitle = (TextView) cell.findViewById(R.id.content_header_trip_name);
            viewHolder.b_estimatedAmount = (TextView) cell.findViewById(R.id.content_estimated_amount);
            viewHolder.b_tripAmount = (TextView) cell.findViewById(R.id.content_amount);
            viewHolder.b_peopleCount = (TextView) cell.findViewById(R.id.content_people_count);
            viewHolder.b_time = (TextView) cell.findViewById(R.id.content_created_time);
            viewHolder.b_day = (TextView) cell.findViewById(R.id.content_created_day);
            viewHolder.b_date = (TextView) cell.findViewById(R.id.content_created_date);
            viewHolder.b_fromAddress = (TextView) cell.findViewById(R.id.content_from);
            viewHolder.b_toAddress = (TextView) cell.findViewById(R.id.content_to);
            viewHolder.b_addPeople = (Button) cell.findViewById(R.id.content_add_people_btn);


//            viewHolder.b_addPeople.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
//            int[] state = new int[]{android.R.attr.state_window_focused, android.R.attr.state_focused};
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

            cell.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getContext(), "Long", Toast.LENGTH_LONG).show();
                    return false;
                }
            });


            viewHolder.b_addPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AddNewPeople(getContext()).addPeopleDialog();
                }
            });


            final RecyclerView recyclerView = (RecyclerView) cell.findViewById(R.id.recyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            final List<ItemModel> data = new ArrayList<>();
//            data.add(new ItemModel(
//                    "Person " + position,
//                    R.color.colorAccent,
//                    R.color.colorPrimary,
//                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
//            data.add(new ItemModel(
//                    "Person " + position, R.color.colorAccent,
//                    R.color.colorPrimary,
//                    Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)));
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


        try {
            // bind data from selected element to view through view holder
            // front side
            viewHolder.f_triptitle.setText(item.getName());
            viewHolder.f_estimatedAmount.setText(item.getEstimateAmount() + "");
            viewHolder.f_date.setText(getDateTime(item.getTime(), "date"));
            viewHolder.f_time.setText(getDateTime(item.getTime(), "time"));
            viewHolder.f_day.setText(getDateTime(item.getTime(), "day"));
            viewHolder.f_fromAddress.setText(item.getFrom());
            viewHolder.f_toAddress.setText(item.getTo());
            viewHolder.f_peopleCount.setText(item.getPeoples().length + "");
            viewHolder.f_tripAmount.setText(item.getAmountSpent() + "");

            // back side
            viewHolder.b_triptitle.setText(item.getName());
            viewHolder.b_estimatedAmount.setText(item.getEstimateAmount() + "");
            viewHolder.b_peopleCount.setText(item.getPeoples().length + "");
            viewHolder.b_tripAmount.setText(item.getAmountSpent() + "");
            viewHolder.b_date.setText(getDateTime(item.getTime(), "date"));
            viewHolder.b_time.setText(getDateTime(item.getTime(), "time"));
            viewHolder.b_day.setText(getDateTime(item.getTime(), "day"));
            viewHolder.b_fromAddress.setText(item.getFrom());
            viewHolder.b_toAddress.setText(item.getTo());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
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
        TextView f_estimatedAmount;
        TextView contentRequestBtn;
        TextView f_tripAmount;
        TextView f_fromAddress;
        TextView f_toAddress;
        TextView f_peopleCount;
        TextView f_date;
        TextView f_time;
        TextView f_day;
        TextView f_triptitle;
        TextView b_triptitle;
        TextView b_tripAmount;
        TextView b_fromAddress;
        TextView b_toAddress;
        TextView b_peopleCount;
        TextView b_estimatedAmount;
        TextView b_date;
        TextView b_time;
        TextView b_day;

        Button b_addPeople;

    }

    private String getDateTime(long t, String input) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(t);
        if (input.equalsIgnoreCase("date"))
            return DateFormat.format("dd MMM y", calendar).toString();
        else if (input.equalsIgnoreCase("time"))
            return DateFormat.format("hh:mm a", calendar).toString();
        else if (input.equalsIgnoreCase("day"))
            return DateFormat.format("EEEE", calendar).toString();
        return "";
    }


}


