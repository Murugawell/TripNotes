package com.geek4s.tripnotes.bean;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Rakesh on 12/22/2017.
 */

public class Trip {

    private String name;
    private People[] peoples;
    private String from;
    private String to;
    private float estimateAmount;
    private long time;

    public Trip(String name, People[] peoples, float estimateAmount, long time, String from, String to) {
        this.name = name;
        this.peoples = peoples;
        this.estimateAmount = estimateAmount;
        this.time = time;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public People[] getPeoples() {
        return peoples;
    }

    public void setPeoples(People[] peoples) {
        this.peoples = peoples;
    }

    public float getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(float estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getAmountSpent() {
        float total = 0.0f;
        for (People people : peoples) {
            total += people.getTotalAmountSpent();
        }
        return total;
    }

    public JSONObject getTripJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("estimatedamount", estimateAmount);
            jsonObject.put("from", from);
            jsonObject.put("to", to);
            JSONArray jsonArray = getPeoplesJSON();
            jsonObject.put("people", jsonArray);
            jsonObject.put("time", time);
        }
        catch (Exception e) {}
        return jsonObject;
    }

    public JSONArray getPeoplesJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            for (People people : peoples) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", people.getName());
                jsonObject.put("maxamount", people.getMaxAmount());
                jsonObject.put("isShared", people.getIsShared());
                jsonObject.put("amountSpent", people.getAmountSpentJSON());
                jsonArray.put(jsonObject);
            }
        }
        catch (Exception e) {}
        return jsonArray;
    }

    public int totalPeople() {
        return peoples.length;
    }

    public float maxAmountUnsharedPeople() {
        float amount = 0;
        for (People people : peoples) {
            if (people.getIsShared() == false)
                amount += people.getMaxAmount();
        }
        return amount;
    }

    public int totalPeopleSharing() {
        int i = 0;
        for (People people : peoples) {
            if (people.getIsShared())
                i++;
        }
        return i;
    }

    public int totalPeopleNotSharing() {
        int i = 0;
        for (People people : peoples) {
            if (people.getIsShared() == false)
                i++;
        }
        return i;
    }

    public People getPeople(String name) {
        People people = null;
        for (People people1 : peoples)
            if (people1.getName().equals(name))
                people = people1;
        return people;
    }
}
