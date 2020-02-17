package com.example.user.myapplication.model;

import androidx.annotation.Nullable;

/**
 * A class model for Rate item View.
 * */
public class Rate {

    private String mName;
    private Float mValue;
    private Float mTotalAmount;
    private Integer mPosition;

    public Rate(String name, Float value) {
        this.mName = name;
        this.mValue = value;
        this.mTotalAmount = value;
    }

    public String getName() {
        return mName;
    }

    public Float getValue() {
        return mValue;
    }

    public Integer getPosition(){
        return mPosition;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setValue(Float value) {
        this.mValue = value;
    }

    public void setPosition(Integer position) {
        this.mPosition = position;
    }

    /**
     * Override the equal method by jus comparing the name string
     * @param obj A Rate object.
     * */
    @Override
    public boolean equals(@Nullable Object obj) {
        return mName.equals(((Rate) obj).getName());
    }

    public Float getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(Float value) {
        mTotalAmount = value;
    }

    public void totalAmount(Float baseValue) {
        this.mTotalAmount = mValue * baseValue;
    }
}
