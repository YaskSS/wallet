package com.example.yass.wallet;

import android.app.Application;
import android.content.Context;

/**
 * Created by yass on 10/2/17.
 */

public class App extends Application {
    private static Context context;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;

    }

    public static Context getContext() {
        return context;
    }
}
