package com.example.user.myapplication.injection.module;

import androidx.lifecycle.ViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class ViewmodelModule {
    private ViewModel mViewmodel;

    public ViewmodelModule(ViewModel viewmodel) {
        mViewmodel = viewmodel;
    }

    @Provides
    ViewModel provideViewmodel() {
        return mViewmodel;
    }
}
