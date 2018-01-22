package com.geek4s.tripnotes;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;


public class ShowMorePeopleExpandRecyclerAdapter extends RecyclerView.Adapter<ShowMorePeopleExpandRecyclerAdapter.ViewHolder> {

    private final List<People> data;
    private Context context;
    SparseBooleanArray expandState = new SparseBooleanArray();
    Trip trip;

    public ShowMorePeopleExpandRecyclerAdapter(final List<People> data, Trip trip, People people) {
        this.data = data;
        this.trip = trip;
        for (int i = 0; i < data.size(); i++) {
            if (people != null)
                if (data.get(i).getName() != people.getName())
                    expandState.append(i, false);
                else
                    expandState.append(i, true);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.show_more_people_recyclerview, parent, false));
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


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
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

        holder.maximumamount.setText(item.getMaxAmount() + "");
        holder.spentamount.setText(item.getTotalAmountSpent() + "");

        float balance = item.getTotalAmountSpent() - item.getMaxAmount();

        holder.amountinfo.setBackgroundColor(Color.BLACK);
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
        holder.balanceamount.setText(Math.abs(balance) + "");


        holder.textviewamount.setText(item.getTotalAmountSpent() + "");
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_dark_background));
        holder.expandableLayout.setInterpolator(Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableLayout.setExpanded(expandState.get(position));


        holder.foramountrecycleview.setLayoutManager(new LinearLayoutManager(context));
        holder.foramountrecycleview.setAdapter(new SpentAmountForRecyclerAdapter(item.getAmountSpentJSON(), trip, item));

        int spentItemLength = item.getAmountSpentJSON().length();
        if (spentItemLength == 0) {
            holder.headersForSpentAmountLayout.setVisibility(View.GONE);
            String text = "No spent amount details available. Click <b><i> Add New Spent</i></b> to add new spent";
            holder.textviewInfoSpentAmountList.setText(Html.fromHtml(text));
            holder.textviewInfoSpentAmountList.setVisibility(View.VISIBLE);
            holder.textviewInfoSpentAmountList.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));
        }


        String text = "Spent Amount Details(" + "<b><font color=#FFFFFF>" + spentItemLength + "</font></b>" + ")";
        holder.spentamountdetailstitle.setText(Html.fromHtml(text));


        holder.addNewSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AddNewSpentAmount(context, trip, item).addNewSpentDialog();
                AddNewSpentAmount.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        ShowMorePeopleViewActivity.refresh(trip, item, context);
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
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    private List getLatestData(People item) {
        List l = new ArrayList();
        Datas datas = new Datas(context);
        datas.open();
        Trip t = datas.getTrip(trip.getName());
        if (t != null) {
            l.add(t);
        }
        JSONArray p = t.getPeoplesJSON();
        final People people = convertJSONARRAYtoLIST(t.getPeoples(), item);
        if (people != null) {
            l.add(people);
        }
        Log.i("dfdsf", l.size() + "");
        return l;
    }

    private People convertJSONARRAYtoLIST(People[] peoplesJSON, People item) {

        for (int i = 0; i < peoplesJSON.length; i++) {

            if (peoplesJSON[i].getName().toString().equalsIgnoreCase(item.getName().toString())) {
                return peoplesJSON[i];
            }
        }
        return null;
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView maximumamount, spentamount;
        private final TextView balanceamount;
        private final TextView amountinfo;
        private final RecyclerView foramountrecycleview;
        private final TextView spentamountdetailstitle;
        private final LinearLayout peopleListLayout;
        public TextView textView;
        public TextView textviewamount;
        public TextView textviewInfoSpentAmountList;
        public Button addNewSpent;
        public LinearLayout headersForSpentAmountLayout;

        public RelativeLayout buttonLayout;
        /**
         * You must use the ExpandableLinearLayout in the recycler view.
         * The ExpandableRelativeLayout doesn't work.
         */
        public ExpandableLinearLayout expandableLayout;

        public ViewHolder(View v) {
            super(v);

            foramountrecycleview = (RecyclerView) v.findViewById(R.id.show_more_people_amount_for_recyclerview);
            textView = (TextView) v.findViewById(R.id.textView);
            maximumamount = (TextView) v.findViewById(R.id.people_maximum_amount_value);
            spentamount = (TextView) v.findViewById(R.id.people_spent_amount_value);
            balanceamount = (TextView) v.findViewById(R.id.people_balance_amount_value);
            amountinfo = (TextView) v.findViewById(R.id.info_amount);

            textviewInfoSpentAmountList = (TextView) v.findViewById(R.id.show_more_people_info_spent_amount_details);

            spentamountdetailstitle = (TextView) v.findViewById(R.id.show_more_people_spent_amount_details_title);

            headersForSpentAmountLayout = (LinearLayout) v.findViewById(R.id.show_more_people_headers_for_spent_amount_layout);
            peopleListLayout = (LinearLayout) v.findViewById(R.id.show_more_people_textViewLayout);

            addNewSpent = (Button) v.findViewById(R.id.addnewspent_indiv_People);

            textviewamount = (TextView) v.findViewById(R.id.textView_amount);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);

        }


    }

    public String roundOfFloat(String str, int len) {
        return String.format("%." + len + "f", str);
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}








/*
*
*   List list = getLatestData(item);
                        People p = (People) list.get(1);
                        Trip trip = (Trip) list.get(0);
                        int spentItemLength = p.getAmountSpentJSON().length();
                        if (spentItemLength == 0) {
                            holder.headersForSpentAmountLayout.setVisibility(View.GONE);
                            String text = "No spent amount details available. Click <b><i> Add New Spent</i></b> to create new spent";
                            holder.textviewInfoSpentAmountList.setText(Html.fromHtml(text));
                            holder.textviewInfoSpentAmountList.setVisibility(View.VISIBLE);
                            holder.textviewInfoSpentAmountList.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));
                        }


                        String text = "Spent Amount Details(" + "<b><font color=#FFFFFF>" + spentItemLength + "</font></b>" + ")";
                        holder.spentamountdetailstitle.setText(Html.fromHtml(text));

                        holder.foramountrecycleview.setAdapter(new SpentAmountForRecyclerAdapter(p.getAmountSpentJSON(), trip, p));
* */