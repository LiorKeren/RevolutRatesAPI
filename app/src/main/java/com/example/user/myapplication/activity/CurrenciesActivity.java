package com.example.user.myapplication.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.user.myapplication.App;
import com.example.user.myapplication.R;
import com.example.user.myapplication.adapter.RecycleViewAdapter;
import com.example.user.myapplication.databinding.ActivityCurrenciesBinding;
import com.example.user.myapplication.injection.component.ActivityComponent;
import com.example.user.myapplication.injection.component.DaggerActivityComponent;
import com.example.user.myapplication.injection.module.ActivityModule;
import com.example.user.myapplication.viewmodel.CurrenciesActivityViewmodel;


public class CurrenciesActivity extends AppCompatActivity implements RecycleViewAdapter.RecycleAdapterListener{

    private ActivityComponent mActivityComponent;
    private RecycleViewAdapter mRecycleViewAdapter;
    private RecyclerView mRecyclerView;
    private ActivityCurrenciesBinding mBinding;
    private CurrenciesActivityViewmodel mViewmodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DI
        getActivityComponent().inject(this);

        // Obtain the ViewModel component.
        mViewmodel = ViewModelProviders.of(this)
                .get(CurrenciesActivityViewmodel.class);


        // Inflate view and obtain an instance of the Binding class.
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_currencies);

        // Assign the component to a property in the Binding class.
        mBinding.setViewmodel(mViewmodel);

        mViewmodel.setListener(this);

        getLifecycle().addObserver(mViewmodel);
        mBinding.setLifecycleOwner(this);

        initRecyclerView();
    }

    /**
     * Activity component for Injectors
     * */
    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(App.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

    /**
    *  Init Recycle Grid View
    * */
    private void initRecyclerView() {
        mRecyclerView = mBinding.recyclerView;
        mRecyclerView.setHasFixedSize(true);


        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecycleViewAdapter = new RecycleViewAdapter(mViewmodel.getRateList());
        mRecycleViewAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mRecycleViewAdapter);
    }

    /**
     * Notify recycle adapter from viewmodel listener
     * */
    @Override
    public void notifyDataChange() {
        mRecycleViewAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Using clear will clear all, but can accept new disposable
        mViewmodel.getDisposables().clear();
    }

    @Override
    protected void onPause() {
        //Close API connection
        mViewmodel.getDisposables().clear();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Reconnect to API
        mViewmodel.getCurrenciesFromApi();
        super.onResume();
    }
}
