package com.example.user.myapplication.injection.component;

import com.example.user.myapplication.activity.CurrenciesActivity;
import com.example.user.myapplication.injection.annotations.PerActivity;
import com.example.user.myapplication.injection.module.ActivityModule;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(CurrenciesActivity currenciesActivity);
}
