package com.example.user.myapplication.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;
import com.example.user.myapplication.App;
import com.example.user.myapplication.helpers.Const;
import com.example.user.myapplication.model.Rate;
import com.example.user.myapplication.model.response.Currency;
import com.example.user.myapplication.adapter.RecycleViewAdapter;
import com.example.user.myapplication.injection.DataApi;
import com.example.user.myapplication.injection.component.DaggerViewmodelComponent;
import com.example.user.myapplication.injection.component.ViewmodelComponent;
import com.example.user.myapplication.injection.RxDataPass;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


public class CurrenciesActivityViewmodel extends ViewModel implements LifecycleObserver {
    private ViewmodelComponent mViewmodelComponent;
    private RecycleViewAdapter.RecycleAdapterListener mListener;
    //base value start with 1
    private BigDecimal mBaseValue = new BigDecimal(1);
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject DataApi dataApi;
    @Inject RxDataPass rxDataPass;


    private List<Rate> mRateList = new ArrayList<>();

    /**
     * Set recycle view mListener to notify the activity when data set changed.
     * @param recycleAdapterListener The activity recycle view mListener.
     * */
    public void setListener(RecycleViewAdapter.RecycleAdapterListener recycleAdapterListener){
        mListener = recycleAdapterListener;
    }

    /**
     * viewmodel constructor.
     * Inject DI.
     * */
    @SuppressLint("CheckResult")
    public CurrenciesActivityViewmodel() {
        getmViewmodelComponent().inject(this);
    }

    /**
     * Update the list view items when the user change value of first item view EditText
     * @param inputText The text from user input
     * */
    private void updateListViewByUserInput(BigDecimal inputText) {
            Rate tempRate = mRateList.get(0);
            tempRate.setTotalAmount(inputText);

            mBaseValue =
                    tempRate.getTotalAmount().divide(tempRate.getValue());
            for (int i = 1; i < mRateList.size(); i++) {
                mRateList.get(i).totalAmount(mBaseValue);
            }
            mListener.notifyDataChange();
    }

    /**
     * Move list item to top of list.
     * Called when item clicked.
     * @param position The position in List
     * */
    private void onListItemClicked(Integer position) {
        Rate rate = mRateList.get(position);
        mRateList.remove(rate);
        mRateList.add(0, rate);
        mListener.notifyDataChange();
    }

    /**
     * Inside the method we subscribe the OnItemClick to method :onListItemClicked
     * And subscribe when User write in first EditText in list to method :updateListViewByUserInput
     * Lunch an API call.
     * Load data from api with observable and add him to disposables.
     * */
    @SuppressLint("CheckResult")
    public void loadViewmodelDisposables(){


        mDisposables.add(rxDataPass.getListItemClickSubject().subscribe(this::onListItemClicked, throwable -> {
            if (throwable != null && throwable.getMessage() != null) {
                Log.w("error OnItemClick", throwable.getMessage());
            }
        }));

        mDisposables.add(rxDataPass.getOnTextChangedSubject().subscribe(this::updateListViewByUserInput, throwable -> {
            if (throwable != null  && throwable.getMessage() != null) {
                Log.w("error OnTextChanged", throwable.getMessage());
            }
        }));

        //  get Currencies From Api
        mDisposables.add(dataApi.getAPIService()
                    .getCurrencies(dataApi.getQueriesForApi(Const.dataSetUrl.CURRENCY_BASE))
                        .subscribeOn(Schedulers.io())
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                        .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> {
                            if (e instanceof HttpException) {
                                Toast.makeText(App.getInstance().getApplicationContext(),  e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                })
                        .subscribe(this::loadCurrenciesSuccess, this::loadCurrenciesFailed)
        );
    }

    /**
     * Method called after failed api call that prints a Log
     * */
    private void loadCurrenciesFailed(Throwable throwable) {
        if (throwable != null  && throwable.getMessage() != null) {
            Log.w("API error ", throwable.getMessage());
        }
    }

    /**
     * Method after success api loading.
     * We take the response And extract A Rate List from it by iterating Response Class field.
     * Also the response contains a base value and we also Convert him to a Rate Class object.
     * @param currency A class API Response
     * */
    private synchronized void loadCurrenciesSuccess(Currency currency) {
        //handle first item in list if its not the base currency
        if (!mRateList.isEmpty() &&
                currency.rates.getFloatVariableByName(mRateList.get(0).getName()) != 0f){

            mBaseValue =
                    mRateList.get(0).getTotalAmount().divide(
                            BigDecimal.valueOf(currency.rates.getFloatVariableByName(mRateList.get(0).getName())));
        }

        //handle base currency in list
        Rate baseRate = new Rate(Const.dataSetUrl.CURRENCY_BASE, mBaseValue);
        if (mRateList.contains(baseRate)) {
            mRateList.get(mRateList.indexOf(baseRate)).totalAmount(baseRate.getValue());
        } else {
            mRateList.add(baseRate);
        }

        //handle all currency rates in list
        Field[] fields = currency.rates.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String name = field.getAnnotation(SerializedName.class).value();
                if (!Const.dataSetUrl.CURRENCY_BASE.equals(name) &&
                             !mRateList.get(0).getName().equals(name)){
                    //Convert api float to BigDecimal
                    BigDecimal value = new BigDecimal(field.getFloat(currency.rates));
                    Rate rate = new Rate(name, value);
                    if (mRateList.contains(rate)) {
                        mRateList.get(mRateList.indexOf(rate)).setValue(rate.getValue());
                        mRateList.get(mRateList.indexOf(rate)).totalAmount(mBaseValue);

                    } else {
                        mRateList.add(rate);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //  This below toast is for UI Debugging check before sending project
//        Toast.makeText(App.getInstance().getApplicationContext(), String.valueOf(currency.rates.aUD), Toast.LENGTH_LONG).show();

        //Notify Listener
        mListener.notifyDataChange();
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


    /**
     * Clear all disposables(unsubscribe All threads)
     * */
    @Override
    protected void onCleared() {
        // Using clear will clear all, but can accept new disposable
        mDisposables.clear();
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        mDisposables.dispose();
        super.onCleared();
    }


    public List<Rate> getRateList() {
        return mRateList;
    }

    public CompositeDisposable getDisposables() {
        return mDisposables;
    }
}
