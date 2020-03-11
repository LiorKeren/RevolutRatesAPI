package com.example.user.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.user.myapplication.R;
import com.example.user.myapplication.model.Rate;
import com.example.user.myapplication.view.CurrenciesViewHolder;
import com.example.user.myapplication.viewmodel.CurrencyItemViewmodel;
import com.example.user.myapplication.databinding.ItemViewCurrencyBinding;
import java.util.List;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An adapter class for a List(Recycle) view
 * */
public class RecycleViewAdapter extends RecyclerView.Adapter<CurrenciesViewHolder> {
    private List<Rate> mRateList;
    private LayoutInflater mLayoutInflater;
    private float mPrevAlpha;

    @Override
    public CurrenciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemViewCurrencyBinding binding =
                DataBindingUtil.inflate(mLayoutInflater, R.layout.item_view_currency, parent, false);

        CurrenciesViewHolder currenciesViewHolder = new CurrenciesViewHolder(binding, parent.getContext());
        mPrevAlpha = currenciesViewHolder.itemView.getAlpha();

        return currenciesViewHolder;
    }

    @Override
    public void onBindViewHolder(CurrenciesViewHolder holder, final int position) {
        holder.itemView.setAlpha(mPrevAlpha);

        //Update Rate model position
        Rate rate = mRateList.get(position);
        rate.setPosition(position);

        //Set the viewmodel by sending the updated model
        CurrencyItemViewmodel listItemViewmodel =
                new CurrencyItemViewmodel(mRateList.get(position), holder.getContext().getResources());

        holder.bindingView.setViewmodel(listItemViewmodel);

    }

    @Override
    public int getItemCount() {
        return mRateList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public RecycleViewAdapter(List<Rate> rateList) {
        this.mRateList = rateList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //Listener to update data between recycle view and the viewmodel
    public interface RecycleAdapterListener {
        void notifyDataChange();
    }
}
