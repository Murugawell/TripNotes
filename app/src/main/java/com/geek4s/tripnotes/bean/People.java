package com.geek4s.tripnotes.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rakesh on 12/22/2017.
 */

public class People {

    private String name;
    private float maxAmount;
    private boolean isShared;
    private Map<String, Float> amountSpent;

    public People(String name, float maxAmount, boolean isShared, Map<String, Float> amountSpent) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.isShared = isShared;
        this.amountSpent = amountSpent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMaxAmount() {
        return maxAmount;
    }

    public boolean getIsShared() {
        return isShared;
    }

    public Map<String, Float> getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(Map<String, Float> amountSpent) {
        this.amountSpent = amountSpent;
    }

    public float getTotalAmountSpent() {
        float total = 0.0f;
        for (String key: amountSpent.keySet()) {
            total += amountSpent.get(key);
        }
        return total;
    }

    public JSONArray getAmountSpentJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            Set<String> keys = amountSpent.keySet();
            for (String key : keys) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("for", key);
                jsonObject.put("amount", amountSpent.get(key));
                jsonArray.put(jsonObject);
            }
        }
        catch (Exception e) {
            Log.e("err", e.toString());
        }
        return jsonArray;
    }
}
