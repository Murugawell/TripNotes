package com.geek4s.tripnotes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.geek4s.tripnotes.bean.JSON;
import com.geek4s.tripnotes.bean.People;
import com.geek4s.tripnotes.bean.Trip;

import org.json.JSONArray;
import org.json.JSONObject;

public class Datas {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATA = "data";

    private static final String DATABASE_NAME = "tripdb";
    private static final String DATABASE_TABLE = "tripTable";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT NOT NULL," + KEY_DATA + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public Datas(Context c) {
        ourContext = c;
    }

    public Datas open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public boolean isOpen() {
        return ourDatabase.isOpen();
    }

    public void close() {
        ourHelper.close();
    }

    public long deleteTrip(String name) {
        Boolean b = check(name);
        if (b)
            ourDatabase.execSQL("delete from " + DATABASE_TABLE + " where name='" + name + "';");
        else
            return 100;
        return 1;
    }

    public long createTrip(String name, String data) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_DATA, data);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public boolean check(String ch) {
        String[] col = new String[]{KEY_NAME};
        Cursor c;
        c = ourDatabase.query(DATABASE_TABLE, col, null, null, null, null, null);
        int i = c.getColumnIndex(KEY_NAME);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (ch.equalsIgnoreCase(c.getString(i)))
                return true;
        }
        return false;
    }

    public Trip getTrip(String name) {
        Trip result = null;
        try {
            String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_DATA};
            Cursor c;
            c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
            int itype = c.getColumnIndex(KEY_NAME);
            int iword = c.getColumnIndex(KEY_DATA);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                if (c.getString(itype).equalsIgnoreCase(name))
                    result = solveTrip(c.getString(itype), c.getString(iword));
            }
        } catch (Exception e) {
        }
        return result;
    }

    public ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> trips = new ArrayList<Trip>();
        try {
            String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_DATA};
            Cursor c;
            c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
            int itype = c.getColumnIndex(KEY_NAME);
            int iword = c.getColumnIndex(KEY_DATA);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String data = c.getString(iword);
                Trip trip = solveTrip(c.getString(itype), data);
                if (trip != null)
                    trips.add(trip);
            }
        } catch (Exception e) {
        }
        return trips;
    }

    private Trip solveTrip(String name, String string) {
        Trip trip = null;
        try {
            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject == null)
                return trip;
            JSONArray peopleArray = jsonObject.getJSONArray(JSON.Trip.people);
            People[] peoples = getPeoples(peopleArray);
            float estimateAmount = (float) jsonObject.getDouble(JSON.Trip.estimatedAmount);
            long time = jsonObject.getLong(JSON.Trip.time);
            String from = jsonObject.getString(JSON.Trip.from);
            String to = jsonObject.getString(JSON.Trip.to);
            if (from.length() <= 0) {
                from = "From";
            }
            if (to.length() <= 0) {
                to = "To";
            }
            trip = new Trip(name, peoples, estimateAmount, time, from, to);
        } catch (Exception e) {
        }
        return trip;
    }

    private People[] getPeoples(JSONArray peopleArray) {
        if (peopleArray == null)
            return null;
        People[] peoples = new People[peopleArray.length()];
        try {
            for (int i = 0; i < peopleArray.length(); i++) {
                JSONObject jsonObject = peopleArray.getJSONObject(i);
                String name = jsonObject.getString(JSON.People.name);
                Map<String, Float> map = getAmounts(jsonObject.getJSONArray(JSON.People.amountSpent));
                float maxAmount = (float) jsonObject.getDouble(JSON.People.maxAmount);
                boolean isShared = jsonObject.getBoolean(JSON.People.isShared);
                peoples[i] = new People(name, maxAmount, isShared, map);
            }
        } catch (Exception e) {
        }
        return peoples;
    }

    private Map<String, Float> getAmounts(JSONArray amountSpent) {
        if (amountSpent == null)
            return null;
        Map<String, Float> map = new HashMap<String, Float>();
        try {
            for (int i = 0; i < amountSpent.length(); i++) {
                JSONObject jsonObject = amountSpent.getJSONObject(i);
                String amountFor = jsonObject.getString(JSON.Amount.amountFor);
                float amount = (float) jsonObject.getDouble(JSON.Amount.amount);
                map.put(amountFor, amount);
            }
        } catch (Exception e) {
        }
        return map;
    }

    public long updateTrip(int id, String name, String data) {
        String condition = KEY_ROWID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_DATA, data);
        return ourDatabase.update(DATABASE_TABLE, cv, condition, null);
    }

}
