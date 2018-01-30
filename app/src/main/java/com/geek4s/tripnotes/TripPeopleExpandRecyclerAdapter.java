package com.geek4s.tripnotes;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TripPeopleExpandRecyclerAdapter extends RecyclerView.Adapter<TripPeopleExpandRecyclerAdapter.ViewHolder> {

    public List<People> data;
    FoldingCellListAdapter.ViewHolder viewHolder;
    public Context context;
    private static SparseBooleanArray expandState = new SparseBooleanArray();
    ;
    Trip trip;

    public TripPeopleExpandRecyclerAdapter(List<People> people) {
        this.data = people;
        this.notifyDataSetChanged();

    }

    public TripPeopleExpandRecyclerAdapter(final List<People> data, Trip trip, FoldingCellListAdapter.ViewHolder viewHolder) {
        this.data = data;
        this.trip = trip;
        this.viewHolder = viewHolder;
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


        holder.maximumamount.setText(item.getMaxAmount() + "");
        holder.spentamount.setText(item.getTotalAmountSpent() + "");

        float balance = item.getTotalAmountSpent() - item.getMaxAmount();


        if (balance < 0) {
//            holder.balanceamount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus_amount, 0, 0, 0);
            holder.balanceamount.setTextColor(0xFF09FC03);

            String msg = "<b><font color=#FF0000>*</font></b> At the end of trip you have to <b><font color=#FF0000>give</font></b>";
            String text = msg + " <b><font color=#ffffff>" + (balance * -1) + "</font></b>";
            holder.amountinfo.setText(Html.fromHtml(text));

        } else if (balance > 0) {
//            holder.balanceamount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus_amount, 0, 0, 0);
            holder.balanceamount.setTextColor(0xFFFC092E);

            String msg = "<b><font color=#FF0000>*</font></b> At the end of trip you will <b><font color=#00FF00>get</font></b>";
            String text = msg + " <b><font color=#ffffff>" + (balance) + "</font></b>";
            holder.amountinfo.setText(Html.fromHtml(text));

        } else {
            holder.balanceamount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.equal_amount, 0, 0, 0);

            holder.amountinfo.setText("<b><font color=#FF0000>*</font></b> Nothing you have to pay and get");

        }
        holder.amountinfo.setBackgroundColor(Color.BLACK);

        holder.balanceamount.setText(Math.abs(balance) + "");


        holder.textviewamount.setText(item.getTotalAmountSpent() + "");
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_dark_background));
        holder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableLayout.setExpanded(expandState.get(position));
        final JSONArray amt = item.getAmountSpentJSON();


        if (amt.length() == 0) {
            holder.spentAmountHeadersLayout.setVisibility(View.GONE);
            String text = "No spent amount details available. Click <b><i> Add New Spent</i></b> to add new spent";
            holder.spentAmountInfo.setText(Html.fromHtml(text));
            holder.spentAmountInfo.setVisibility(View.VISIBLE);
            holder.spentAmountInfo.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));
        }


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
        holder.item1name.setTextColor(Color.BLACK);
        holder.item1amount.setTextColor(Color.BLACK);
        holder.item2name.setTextColor(Color.BLACK);
        holder.item2amount.setTextColor(Color.BLACK);


        holder.layitem1.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));
        holder.layitem2.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));

        holder.layitem1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showSpentItemOptions(amt, 0, item);
                return false;
            }
        });

        holder.layitem2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showSpentItemOptions(amt, 1, item);
                return false;
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()

        {
            @Override
            public boolean onLongClick(View view) {
                if (!expandState.get(position)) {
                    /*  */

                    String[] options = {"", ""};
                    String title = "";
                    Drawable[] drawables = {context.getResources().getDrawable(R.drawable.edit_people), context.getResources().getDrawable(R.drawable.remove_people)};

                    ShowOptionsDialogs showOptionsDialogs = new ShowOptionsDialogs(context);
                    showOptionsDialogs.showOptionsDialogMethod(options, title, drawables);

                    ShowOptionsDialogs.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("update")) {
                                editPeople(item);
                            } else if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("delete")) {
                                deletePeople(item);
                            }
                        }
                    });


                }

                return false;
            }
        });


        holder.showmore.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                ShowMorePeopleViewActivity.trip = trip;
                ShowMorePeopleViewActivity.people = item;
                Intent in = new Intent(context, ShowMorePeopleViewActivity.class);
                context.startActivity(in);
            }
        });


        holder.addNewSpent.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                new AddNewSpentAmount(context, trip, item).addNewSpentDialog();
                AddNewSpentAmount.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                });
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
        holder.buttonLayout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });

    }

    private void showSpentItemOptions(JSONArray amt, int in, final People people) {
        String[] options = {"", "", ""};
        Drawable[] drawables = {context.getResources().getDrawable(R.drawable.edit_spend_item), context.getResources().getDrawable(R.drawable.delete_spent_item)};
        String title = "";
        ShowOptionsDialogs showOptionsDialogs = new ShowOptionsDialogs(context);
        showOptionsDialogs.showOptionsDialogMethod(options, title, drawables);
        JSONObject jsonSpentItem = null;
        if (amt != null) {
            if (amt.length() > 0) {
                try {
                    jsonSpentItem = amt.getJSONObject(in);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        final JSONObject finalJsonSpentItem = jsonSpentItem;
        ShowOptionsDialogs.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("update")) {
                    updateSpentAmount(finalJsonSpentItem, people);
                } else if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("delete")) {
                    deleteSpentAmount(finalJsonSpentItem, people);

                } else if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("swap")) {
                    swapSpentAmount(finalJsonSpentItem, people);

                }
            }
        });


    }

    private void swapSpentAmount(JSONObject spentitem, People people) {
        SwapSpentAmount swapSpentAmount = new SwapSpentAmount(context);
        List peopleList = getAllPeoplesName(trip);
        swapSpentAmount.swapSpentAMountDialog(context, trip, people, spentitem, peopleList);
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
                ar.add(j.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ar;
    }

    private void deleteSpentAmount(JSONObject spentItem, People people) {
        DeleteSpentAmount deleteSpentAmount = new DeleteSpentAmount(context);
        deleteSpentAmount.deleteSpentAmountDialog(context, trip, people, spentItem);
        DeleteSpentAmount.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
    }

    private void updateSpentAmount(JSONObject spentItem, People people) {
        EditSpentAmount editSpentAmount = new EditSpentAmount(context);
        editSpentAmount.editSpentDialog(context, trip, people, spentItem);
        EditSpentAmount.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
    }


    private void deletePeople(People item) {
        DeletePeople deletePeople = new DeletePeople(context);
        deletePeople.deletePeopleDialog(context, trip, item);
        DeletePeople.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPeople(People item) {

        EditPeople editPeople = new EditPeople(context);
        editPeople.editPeopleDialog(trip, item);
        EditPeople.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
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
        if (!expandableLayout.isExpanded()) {
            viewHolder.b_close.setVisibility(View.GONE);
        } else {
            viewHolder.b_close.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (data.size() > 3)
            return 3;
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView maximumamount, spentamount;
        private final TextView balanceamount;
        private final TextView amountinfo;
        private final TextView spentAmountInfo;
        public Button showmore;
        public TextView textView, listofPeolpleTextView, textviewamount;
        public LinearLayout layitem1, layitem2;
        public ImageView item1icon, item2icon;
        public TextView item1name, item2name, item1amount, item2amount;
        public Button addNewSpent;

        public RelativeLayout buttonLayout;

        LinearLayout spentAmountHeadersLayout;
        /**
         * You must use the ExpandableLinearLayout in the recycler view.
         * The ExpandableRelativeLayout doesn't work.
         */
        public ExpandableLinearLayout expandableLayout;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            maximumamount = (TextView) v.findViewById(R.id.people_maximum_amount_value);
            spentamount = (TextView) v.findViewById(R.id.people_spent_amount_value);
            balanceamount = (TextView) v.findViewById(R.id.people_balance_amount_value);
            amountinfo = (TextView) v.findViewById(R.id.info_amount);

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

            spentAmountHeadersLayout = (LinearLayout) v.findViewById(R.id.headers_for_spent_amount_layout);
            spentAmountInfo = (TextView) v.findViewById(R.id.info_spent_amount_details);


//            listofPeolpleTextView = (TextView) v.findViewById(R.id.people_list_textview);


//            listofPeolpleTextView.setMovementMethod(new ScrollingMovementMethod());

        }


    }

    public String roundOfFloat(String str, int len) {
        return String.format("%." + len + "f", str);
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from,
                                               final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}