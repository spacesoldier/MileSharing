package com.soloway.city.milesharing.activity;

import com.soloway.city.milesharing.api.UserSession;

import android.app.Application;
import android.view.View;

public class MyApplication extends Application {
    public View viewToClick;
    public UserSession mainSession;
    
    @Override
    public void onCreate()
    {
      super.onCreate();
      
      viewToClick = null;
    }
}
