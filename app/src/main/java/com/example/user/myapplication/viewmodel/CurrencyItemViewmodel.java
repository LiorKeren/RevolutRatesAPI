package com.example.user.myapplication.viewmodel;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.icu.util.Currency;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.user.myapplication.App;
import com.example.user.myapplication.injection.component.DaggerViewmodelComponent;
import com.example.user.myapplication.injection.component.ViewmodelComponent;
import com.example.user.myapplication.model.Rate;
import com.example.user.myapplication.picasso.BindableFieldTarget;
import com.example.user.myapplication.R;
import com.example.user.myapplication.helpers.Const;
import com.example.user.myapplication.injection.RxDataPass;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

import javax.inject.Inject;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class CurrencyItemViewmodel extends ViewModel {
    private Rate mModel;
    private TextWatcher mTextWatcher;
    //Global variables for further use
    private Currency mCurrency;
    private BindableFieldTarget bindableFieldTarget;

    private ViewmodelComponent mViewmodelComponent;


    @Inject RxDataPass rxDataPass;

    public ObservableField<Drawable> posterImage = new ObservableField<>();
    public ObservableField<String> mCurrencyName = new ObservableField<>();
    public ObservableField<String> mCurrencyType = new ObservableField<>();
    public ObservableField<String> mCurrencyValue = new ObservableField<>();


    public CurrencyItemViewmodel(Rate model, Resources resources) {
        getmViewmodelComponent().inject(this);
        setModel(model, resources);
    }

    /**
     * Set viewmodel data by Rate mModel
     *
     * @param resources The context Resources of ViewHolder for setting Picasso image view from Flags.IO API
     * @param model The Rate mModel for setting viewmodel data
     * */
    private void setModel(Rate model, Resources resources) {
        mTextWatcher = getTextWatcherIns();
        mModel = model;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCurrency = Currency.getInstance(mModel.getName());
            mCurrencyName.set(mCurrency.getDisplayName());
        }else {
            mCurrencyName.set(mModel.getName());
        }

        // Picasso keeps a weak reference to the target so it needs to be stored in a field
        bindableFieldTarget = new BindableFieldTarget(posterImage, resources);

        mCurrencyType.set(mModel.getName());

        // If amount is round don't add the .0 value
        if (model.getTotalAmount().stripTrailingZeros().scale() <= 0){
            mCurrencyValue.set(String.valueOf(mModel.getTotalAmount().intValue()));
        }else {
            mCurrencyValue.set(mModel.getTotalAmount().toString());
        }


        String flagName = model.getName().toLowerCase();
        Picasso.get()
                .load(Const.dataSetUrl.IMAGE_BASE_START_URL
                        + flagName.substring(0,2) +
                       Const.dataSetUrl.IMAGE_BASE_END_URL
                )
                .placeholder(R.drawable.ic_autorenew_blue_24dp)
                .error(R.drawable.ic_block_black_24dp)
                .into(bindableFieldTarget);

    }

    /**
    * Send on click to currency viewmodel that holds the list
    * @param view Not in use
    * */
    public void onClick(View view) {
        rxDataPass.getListItemClickSubject().onNext(mModel.getPosition());
    }


    /**
     * A Text Watcher instance
     * */
    private TextWatcher getTextWatcherIns() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //replace empty String with 0 str
                if (s.length() == 0){
                    s = "0";
                }

                //Check if the text that the user changed is in first position inside list view.
                //Check if the value is different from the value we got inside the Rate model.
                BigDecimal sBigDecimalValue = new BigDecimal(s.toString());
                if (mModel.getPosition() == 0 && !mModel.getTotalAmount().equals(
                        sBigDecimalValue)){

                    ///If amount is round don't add the .0 value
                    mModel.setTotalAmount(sBigDecimalValue);
                    //Subscribe text to the viewmodel of an activity
                    rxDataPass.getOnTextChangedSubject().onNext(sBigDecimalValue);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    public TextWatcher getTextWatcher() {
        return mTextWatcher;
    }

    /**
     * Binding the view (from XML) Edit Text with the viewmodel Text watcher only if position is 0
     * @param editText The edit text from XML.
     * @param textWatcher The getTextWatcherIns method.
     * @param position The position of list item.
     **/
    //
    @BindingAdapter({"textChangedListener", "position"})
    public static void bindTextWatcher(EditText editText, TextWatcher textWatcher, Integer position) {
        //only the first editText in item list can change all list values
        if (position == 0) {
            editText.addTextChangedListener(textWatcher);
            editText.requestFocus();
        }else {
            editText.setEnabled(false);
        }
    }

    //DI
    private ViewmodelComponent getmViewmodelComponent() {
        if (mViewmodelComponent == null) {
            mViewmodelComponent = DaggerViewmodelComponent.builder()
                    .applicationComponent(App.get(App.getInstance()).getComponent())
                    .build();
        }
        return mViewmodelComponent;
    }

    public Rate getModel() {
        return mModel;
    }
}
