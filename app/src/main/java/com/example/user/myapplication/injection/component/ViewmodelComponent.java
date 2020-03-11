package com.example.user.myapplication.injection.component;

import com.example.user.myapplication.injection.annotations.PerActivity;
import com.example.user.myapplication.injection.module.ViewmodelModule;
import com.example.user.myapplication.viewmodel.CurrenciesActivityViewmodel;
import com.example.user.myapplication.viewmodel.CurrencyItemViewmodel;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ViewmodelModule.class)
public interface ViewmodelComponent {
   void inject(CurrenciesActivityViewmodel currenciesActivityViewmodel);
   void inject(CurrencyItemViewmodel currencyItemViewmodel);
}
