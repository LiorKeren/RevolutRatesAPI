package com.example.user.myapplication.injection.component;

import android.content.Context;

import com.example.user.myapplication.App;
import com.example.user.myapplication.injection.DataApi;
import com.example.user.myapplication.injection.annotations.ApplicationContext;
import com.example.user.myapplication.injection.module.ApplicationModule;
import com.example.user.myapplication.injection.RxDataPass;

import javax.inject.Singleton;

import dagger.Component;



@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App app);

    @ApplicationContext
    Context getContext();

    DataApi getDataApi();

    RxDataPass getRxDataPass();
}
