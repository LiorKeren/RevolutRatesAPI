package com.example.user.myapplication.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;


/**
 * Response Class item from Server API
 * */
public class Rates {

    @SerializedName("AUD")
    @Expose
    public float aUD;
    @SerializedName("BGN")
    @Expose
    public float bGN;
    @SerializedName("BRL")
    @Expose
    public float bRL;
    @SerializedName("CAD")
    @Expose
    public float cAD;
    @SerializedName("CHF")
    @Expose
    public float cHF;
    @SerializedName("CNY")
    @Expose
    public float cNY;
    @SerializedName("CZK")
    @Expose
    public float cZK;
    @SerializedName("DKK")
    @Expose
    public float dKK;
    @SerializedName("GBP")
    @Expose
    public float gBP;
    @SerializedName("HKD")
    @Expose
    public float hKD;
    @SerializedName("HRK")
    @Expose
    public float hRK;
    @SerializedName("HUF")
    @Expose
    public float hUF;
    @SerializedName("IDR")
    @Expose
    public float iDR;
    @SerializedName("ILS")
    @Expose
    public float iLS;
    @SerializedName("INR")
    @Expose
    public float iNR;
    @SerializedName("ISK")
    @Expose
    public float iSK;
    @SerializedName("JPY")
    @Expose
    public float jPY;
    @SerializedName("KRW")
    @Expose
    public float kRW;
    @SerializedName("MXN")
    @Expose
    public float mXN;
    @SerializedName("MYR")
    @Expose
    public float mYR;
    @SerializedName("NOK")
    @Expose
    public float nOK;
    @SerializedName("NZD")
    @Expose
    public float nZD;
    @SerializedName("PHP")
    @Expose
    public float pHP;
    @SerializedName("PLN")
    @Expose
    public float pLN;
    @SerializedName("RON")
    @Expose
    public float rON;
    @SerializedName("RUB")
    @Expose
    public float rUB;
    @SerializedName("SEK")
    @Expose
    public float sEK;
    @SerializedName("SGD")
    @Expose
    public float sGD;
    @SerializedName("THB")
    @Expose
    public float tHB;
    @SerializedName("TRY")
    @Expose
    public float tRY;
    @SerializedName("USD")
    @Expose
    public float uSD;
    @SerializedName("ZAR")
    @Expose
    public float zAR;
    @SerializedName("EUR")
    @Expose
    public float eUR;

    /**
     * Get local variable Float by Field name.
     * @param name Field name.
     * @return The local variable Float value OR 0.
     * */
    public float getFloatVariableByName(String name) {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldName = field.getAnnotation(SerializedName.class).value();
                if (name.equals(fieldName)) {
                    return field.getFloat(this);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0f;
    }
}
