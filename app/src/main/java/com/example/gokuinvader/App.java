package com.example.gokuinvader;

import android.app.Application;

import com.example.gokuinvader.Utils.SharedPreferencesManager;
import com.example.gokuinvader.Utils.SignalManager;
import com.example.gokuinvader.Utils.SoundManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
        SignalManager.init(this);
    }

}
