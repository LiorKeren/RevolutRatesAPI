package com.example.user.myapplication.model;

import java.math.BigDecimal;

import androidx.annotation.Nullable;

/**
 * A class model for Rate item View.
 * */
public class Rate {

    private String mName;
    private BigDecimal mValue;
    private BigDecimal mTotalAmount;
    private Integer mPosition;

    public Rate(String name, BigDecimal value) {
        this.mName = name;
        this.mValue = value;
        this.mTotalAmount = value;
    }

    public String getName() {
        return mName;
    }

    public BigDecimal getValue() {
        return mValue;
    }

    public Integer getPosition(){
        return mPosition;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setValue(BigDecimal value) {
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

    public BigDecimal getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(BigDecimal value) {
        mTotalAmount = value;
    }

    public void totalAmount(BigDecimal baseValue) {
        if (baseValue.equals(BigDecimal.ZERO)){
            this.mTotalAmount = baseValue;
        }else {
            this.mTotalAmount = mValue.multiply(baseValue);
        }
    }
}
