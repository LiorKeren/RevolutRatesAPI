package com.example.user.myapplication;

import android.app.Application;
import android.content.Context;

import com.example.user.myapplication.injection.component.ApplicationComponent;
import com.example.user.myapplication.injection.component.DaggerApplicationComponent;
import com.example.user.myapplication.injection.module.ApplicationModule;

public class App extends Application {

  protected ApplicationComponent applicationComponent;
  private static App instance;
  private static Context context;


  public static App get(Context context) {
    return (App) context.getApplicationContext();
  }

  public App() {
    instance = this;
  }

  public static App getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    App.context = getApplicationContext();
    //Dagger DI create
    applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    applicationComponent.inject(this);
  }

  public ApplicationComponent getComponent(){
    return applicationComponent;
  }
}
