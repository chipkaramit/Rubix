package com.amit.rubix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by amitchipkar on 20/02/18.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splashThread = new Thread()
        {
            @Override
            public void run() {
                try{
                    sleep(2000);
                    Intent splashIntent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(splashIntent);
                    finish();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                super.run();

            }
        };
        splashThread.start();
    }
}
