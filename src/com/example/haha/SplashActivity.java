package com.example.haha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity{
	
	private static final int DELAY = 1000;
	
	private LoginActivity lo;
			
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	// do something here
            	Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            	startActivity(intent);
            }
        }, DELAY);
    }

}
