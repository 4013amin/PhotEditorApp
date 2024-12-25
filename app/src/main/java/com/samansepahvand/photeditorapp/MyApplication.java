package com.samansepahvand.photeditorapp;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class MyApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        fontAssign();

    }
    private void fontAssign(){


        ViewPump.init(ViewPump.builder()
               .addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder()
               .setDefaultFontPath("fonts/iran_sans.ttf")
                       .setFontAttrId(R.attr.fontPath)
                       .build()
               ))
                .build());

    }
}
