package com.geek4s.tripnotes;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SpentAmountForRecyclerAdapter extends RecyclerView.Adapter<SpentAmountForRecyclerAdapter.ViewHolder> {

    private final JSONArray data;
    private Context context;
    Trip trip;
    People people;

    public SpentAmountForRecyclerAdapter(final JSONArray data, Trip trip, People people) {
        this.data = data;
        this.trip = trip;
        this.people = people;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.amount_for_item_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final JSONObject item = (JSONObject) data.get(position);
            int maxLength = 10;
            String itemName = item.getString("for");
            holder.peopleItemName.setText(itemName);
            String itemAmount = item.getString("amount");
            holder.peopleItemAmount.setText(itemAmount);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    String[] options = {"", "", ""};
                    Drawable[] drawables = {context.getResources().getDrawable(R.drawable.edit_spend_item), context.getResources().getDrawable(R.drawable.delete_spent_item)};
                    String title = "";
                    ShowOptionsDialogs showOptionsDialogs = new ShowOptionsDialogs(context);
                    showOptionsDialogs.showOptionsDialogMethod(options, title, drawables);

                    ShowOptionsDialogs.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("update")) {
                                updateSpentAmount(item);
                            } else if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("delete")) {
                                deleteSpentAmount(item);

                            } else if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("swap")) {
                                swapSpentAmount(item);

                            }
                        }
                    });


                    return false;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void swapSpentAmount(JSONObject item) {
        SwapSpentAmount swapSpentAmount = new SwapSpentAmount();
        List peopleList = getAllPeoplesName(trip);
        swapSpentAmount.swapSpentAMountDialog(context, trip, people, item, peopleList);
        SwapSpentAmount.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
    }

    private List<String> getAllPeoplesName(Trip trip) {
        List<String> ar = new ArrayList<String>();

        for (int i = 0; i < trip.getPeoplesJSON().length(); i++) {
            try {
                JSONObject j = trip.getPeoplesJSON().getJSONObject(i);
                ar.add( j.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ar;
    }

    private void deleteSpentAmount(JSONObject item) {
        DeleteSpentAmount deleteSpentAmount = new DeleteSpentAmount();
        deleteSpentAmount.deleteSpentAmountDialog(context, trip, people, item);
        DeleteSpentAmount.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
    }

    private void updateSpentAmount(JSONObject item) {
        EditSpentAmount editSpentAmount = new EditSpentAmount();
        editSpentAmount.editSpentDialog(context, trip, people, item);
        EditSpentAmount.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

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

    @Override
    public int getItemCount() {
        return data.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView peopleItemName;
        public TextView peopleItemAmount;
        public Button addNewSpent;
        public ImageView peopleItemIcon;

        public ViewHolder(View v) {
            super(v);
            peopleItemName = (TextView) v.findViewById(R.id.people_item_name);
            addNewSpent = (Button) v.findViewById(R.id.addnewspent_indiv_People);
            peopleItemAmount = (TextView) v.findViewById(R.id.people_item_amount);

        }


    }

    public String roundOfFloat(String str, int len) {
        return String.format("%." + len + "f", str);
    }


}