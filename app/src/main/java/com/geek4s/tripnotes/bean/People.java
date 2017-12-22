package com.geek4s.tripnotes.bean;

import java.util.Map;

/**
 * Created by Rakesh on 12/22/2017.
 */

public class People {

    private String name;
    private float maxAmount;
    private Map<String, Float> amountSpent;

    public People(String name, float maxAmount, Map<String, Float> amountSpent) {
        this.name = name;
        this.maxAmount = maxAmount;
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

    public void setMaxAmount(float maxAmount) {
        this.maxAmount = maxAmount;
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
}
