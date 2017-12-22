package com.geek4s.tripnotes.bean;

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
}
