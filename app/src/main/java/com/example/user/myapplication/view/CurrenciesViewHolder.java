package com.example.user.myapplication.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.myapplication.databinding.ItemViewCurrencyBinding;
/**
 * Class holder for recycle item view
 * */
public class CurrenciesViewHolder extends RecyclerView.ViewHolder {
    public ItemViewCurrencyBinding bindingView;
    private Context context;

    public CurrenciesViewHolder(@NonNull ItemViewCurrencyBinding itemView, Context context) {
        super(itemView.getRoot());
        this.context = context;
        bindingView = itemView;

        }

    public Context getContext() {
        return context;
    }
}
