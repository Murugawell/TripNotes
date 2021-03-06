package com.geek4s.tripnotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class FoldingCellListAdapter extends ArrayAdapter<Trip> {

    private int OPENEDCELL = -1;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private RecyclerView recyclerView;
    public Context context;
    private TripPeopleExpandRecyclerAdapter peopleAdapter;
    private List<People> peopleList;
    private Trip temptrip;

    public FoldingCellListAdapter(Context context, List<Trip> objects) {
        super(context, 0, objects);
        this.context = context;
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
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        // get item for selected view
        final Trip[] item = {getItem(position)};
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
//            Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(context);
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            // front side
            viewHolder.f_estimatedAmount = (TextView) cell.findViewById(R.id.title_estimateamount);
            viewHolder.f_time = (TextView) cell.findViewById(R.id.title_time_label);
            viewHolder.f_day = (TextView) cell.findViewById(R.id.title_day_label);
            viewHolder.f_date = (TextView) cell.findViewById(R.id.title_date_label);
            viewHolder.f_fromAddress = (TextView) cell.findViewById(R.id.title_from_address);
            viewHolder.f_toAddress = (TextView) cell.findViewById(R.id.title_to_address);
            viewHolder.f_peopleCount = (TextView) cell.findViewById(R.id.title_people_count);
            viewHolder.f_tripAmount = (TextView) cell.findViewById(R.id.title_amount);
            viewHolder.f_triptitle = (TextView) cell.findViewById(R.id.cell_title_trip_name_textview);
            viewHolder.f_peopleLayout = (LinearLayout) cell.findViewById(R.id.cell_title_people_layout);
            // back side
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
            viewHolder.b_showAllPeople = (Button) cell.findViewById(R.id.content_show_all_people);
            viewHolder.b_close = (ImageView) cell.findViewById(R.id.content_header_title_left_icon_imageview);
            viewHolder.b_edit_trip = (ImageButton) cell.findViewById(R.id.content_header_edit_trip_main);
            viewHolder.b_showallpeoplelistimgbtn = (ImageButton) cell.findViewById(R.id.content_people_show_all_imagebtn);
            viewHolder.b_peopleLayout = (LinearLayout) cell.findViewById(R.id.content_people_layout);
            viewHolder.b_estimatedamountLayout = (LinearLayout) cell.findViewById(R.id.content_estimated_amount_layout);
            viewHolder.b_spentamountLayout = (LinearLayout) cell.findViewById(R.id.content_spent_amount_layout);
            viewHolder.b_peopleInfo = (TextView) cell.findViewById(R.id.content_people_info_textview);
            viewHolder.b_fromtoaddresscard = (CardView) cell.findViewById(R.id.cell_content_address_card);

            viewHolder.f_trip_open_icon = (ImageView) cell.findViewById(R.id.cell_title_open_icon);


            viewHolder.b_edit_trip.setVisibility(View.GONE);


//            viewHolder.b_addPeople.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
//            int[] state = new int[]{android.R.attr.state_window_focused, android.R.attr.state_focused};
//

            final FoldingCell finalCell = cell;

//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                viewHolder.b_close.setBackground(context.getResources().getDrawable(R.drawable.fold_icon));
//            }

            // fold cell back side close button click
            viewHolder.b_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        new Animation().hideShowElement(viewHolder.b_close, viewHolder.b_close, false);
                        finalCell.toggle(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // open cell front side triptitle  click
            viewHolder.f_trip_open_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        finalCell.toggle(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // cell long click
//            cell.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    Toast.makeText(context, "Long", Toast.LENGTH_LONG).show();
//                    return false;
//                }
//            });

            // back side add people button click
            viewHolder.b_addPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AddNewPeople(context, viewHolder.b_triptitle.getText().toString()).addPeopleDialog();
                    AddNewPeople.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (AddNewPeople.resultAlertAction.equalsIgnoreCase("confirm")) {
/*
                                MainActivity.getTripListFromDB(context);
                                MainActivity.showListOfTrips(context);*/
                                item[0] = getTripUpdated(item[0]);
                                temptrip = item[0];
                                peopleList = getAllPeople(item[0]);
                                checkNoPeopleMes(viewHolder, item[0]);
                                peopleAdapter = new TripPeopleExpandRecyclerAdapter(peopleList, item[0], viewHolder);
                                recyclerView.setAdapter(peopleAdapter);
                            }

                        }
                    });
                }
            });


            // recyclerview for list of peoples
            recyclerView = (RecyclerView) cell.findViewById(R.id.recyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(context));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            peopleList = convertJSONARRAYtoLIST(item[0].getPeoples());

            peopleAdapter = new TripPeopleExpandRecyclerAdapter(peopleList, item[0], viewHolder);
            recyclerView.setAdapter(peopleAdapter);

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
            viewHolder.f_triptitle.setText(item[0].getName());
            viewHolder.f_estimatedAmount.setText(item[0].getEstimateAmount() + "");
            viewHolder.f_date.setText(getDateTime(item[0].getTime(), "date"));
            viewHolder.f_time.setText(getDateTime(item[0].getTime(), "time"));
            viewHolder.f_day.setText(getDateTime(item[0].getTime(), "day"));
            viewHolder.f_fromAddress.setText(item[0].getFrom());
            viewHolder.f_toAddress.setText(item[0].getTo());
            viewHolder.f_peopleCount.setText(item[0].getPeoples().length + "");
            viewHolder.f_tripAmount.setText(item[0].getAmountSpent() + "");

            // back side
            viewHolder.b_triptitle.setText(item[0].getName());
            viewHolder.b_estimatedAmount.setText(item[0].getEstimateAmount() + "");
            viewHolder.b_peopleCount.setText(item[0].getPeoples().length + "");
            viewHolder.b_tripAmount.setText(item[0].getAmountSpent() + "");
            viewHolder.b_date.setText(getDateTime(item[0].getTime(), "date"));
            viewHolder.b_time.setText(getDateTime(item[0].getTime(), "time"));
            viewHolder.b_day.setText(getDateTime(item[0].getTime(), "day"));
            viewHolder.b_fromAddress.setText(item[0].getFrom());
            viewHolder.b_toAddress.setText(item[0].getTo());

            checkNoPeopleMes(viewHolder, item[0]);

//            viewHolder.b_close.setBackgroundColor(context.getResources().getColor(R.color.background1));

            // back title long press to delete
            viewHolder.b_triptitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DeleteTrip deleteTrip = new DeleteTrip(context);
                    deleteTrip.deleteTripDialog(context, item[0]);
                    DeleteTrip.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                        }
                    });
                    return false;
                }
            });

            // front title long press to delete
            viewHolder.f_triptitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    try {

                        String[] options = {"", ""};
                        String title = "";
                        Drawable[] drawables = {context.getResources().getDrawable(R.drawable.ic_edit), context.getResources().getDrawable(R.drawable.ic_delete)};

                        ShowOptionsDialogs showOptionsDialogs = new ShowOptionsDialogs(context);
                        showOptionsDialogs.showOptionsDialogMethod(options, title, drawables);

                        ShowOptionsDialogs.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("update")) {
                                    editTrip(context, item[0], "all");
                                } else if (ShowOptionsDialogs.selectedOption.equalsIgnoreCase("delete")) {
                                    deleteTrip(context, item[0]);
                                }
                            }
                        });

                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });


            temptrip = item[0];
            // show all people , back side people count onclick
            viewHolder.b_peopleCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowMorePeopleViewActivity.trip = temptrip;
                    Log.i("add", temptrip.toString());
                    Intent in = new Intent(context, ShowMorePeopleViewActivity.class);
                    context.startActivity(in);

                }
            });

            // show all people , front side people count layout onclick
            viewHolder.f_peopleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        ShowMorePeopleViewActivity.trip = temptrip;
                        Intent in = new Intent(context, ShowMorePeopleViewActivity.class);
                        getContext().startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            viewHolder.f_peopleLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        ShowMorePeopleViewActivity.trip = temptrip;
                        Intent in = new Intent(context, ShowMorePeopleViewActivity.class);
                        getContext().startActivity(in);
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }

            });
            //show all people
            viewHolder.b_showAllPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ShowMorePeopleViewActivity.trip = temptrip;
                        Intent in = new Intent(context, ShowMorePeopleViewActivity.class);
                        getContext().startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            viewHolder.b_estimatedamountLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    editTrip(context, item[0], "estimatedamount");
                    return false;
                }
            });


            viewHolder.f_fromAddress.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    editTrip(context, item[0], "fromto");
                    return false;
                }
            });

            viewHolder.f_toAddress.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    editTrip(context, item[0], "fromto");
                    return false;
                }
            });

            viewHolder.f_estimatedAmount.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    editTrip(context, item[0], "estimatedamount");
                    return false;
                }
            });


            viewHolder.b_showallpeoplelistimgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowMorePeopleViewActivity.trip = temptrip;
                    Intent in = new Intent(context, ShowMorePeopleViewActivity.class);
                    context.startActivity(in);

                }
            });


            viewHolder.b_fromtoaddresscard.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    editTrip(context, item[0], "fromto");
                    return false;
                }
            });


        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

        return cell;
    }

    private void checkNoPeopleMes(ViewHolder viewHolder, Trip item) {
        viewHolder.b_peopleInfo.setTextColor(Color.BLACK);
        if (item.getPeoples().length == 0) {
            String txt = "No people available. Click <b><i> Add People</i></b> to add new people in <b>" + item.getName() + "</b> trip";
            viewHolder.b_peopleInfo.setText(Html.fromHtml(txt));
            viewHolder.b_peopleInfo.setBackgroundColor(context.getResources().getColor(R.color.btnRequest));
        } else {
            viewHolder.b_peopleInfo.setVisibility(View.GONE);
        }
        viewHolder.b_showAllPeople.setVisibility(View.GONE);
        // show all people button ,only if more than 3
        if (item.getPeoples().length > 3) {
            viewHolder.b_showAllPeople.setVisibility(View.VISIBLE);
        }

    }

    private List<People> getAllPeople(Trip item) {
        Datas datas = new Datas(context);
        datas.open();
        Trip t = datas.getTrip(item.getName().toString());
        if (t != null) {
            if (t.getPeoples() != null) {
                return convertJSONARRAYtoLIST(t.getPeoples());
            }
        }
        datas.close();
        return new ArrayList<>();
    }

    private Trip getTripUpdated(Trip t) {
        Datas datas = new Datas(context);
        datas.open();
        Trip trip = datas.getTrip(t.getName().toString());
        if (t != null) {
            return trip;
        }
        datas.close();
        return null;
    }

    private void deleteTrip(final Context context, Trip item) {

        DeleteTrip deleteTrip = new DeleteTrip(context);
        deleteTrip.deleteTripDialog(context, item);
        DeleteTrip.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                MainActivity.getTripListFromDB(context);
                MainActivity.showListOfTrips(context);
            }
        });
    }

    private void editTrip(final Context context, Trip item, String option) {
        EditTrip editTrip = new EditTrip(context, item);
        editTrip.editTripDialog(item, option);
        EditTrip.alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!EditTrip.isCancel) {
                    MainActivity.getTripListFromDB(context);
                    MainActivity.showListOfTrips(context);
                }
            }
        });
    }

    private List<People> convertJSONARRAYtoLIST(People[] peoplesJSON) {
        List<People> list = new ArrayList<>();
        for (int i = 0; i < peoplesJSON.length; i++) {
            list.add(i, peoplesJSON[i]);
        }
        return list;
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
    public static class ViewHolder {
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
        Button b_showAllPeople;
        ImageView b_close;
        ImageButton b_showallpeoplelistimgbtn;
        ImageButton b_edit_trip;
        LinearLayout b_peopleLayout;
        LinearLayout b_estimatedamountLayout;
        LinearLayout b_spentamountLayout;
        LinearLayout f_peopleLayout;
        TextView b_peopleInfo;
        CardView b_fromtoaddresscard;
        ImageView f_trip_open_icon;
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


