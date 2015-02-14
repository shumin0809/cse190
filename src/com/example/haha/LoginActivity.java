package com.example.haha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoginActivity extends Activity{
	
    private static final String TAG = "LoginActivity";
    private static final int DELAY = 1000;

    private static final String ONBOARDING_TAG = "ONBOARDING_TAG";
    private static final String LOGIN_TAG = "LOGIN_TAG";
    private static final String RESET_PASS_TAG = "RESET_PASS_TAG";
    private static final String SIGNUP_TAG = "SIGNUP_TAG";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_login);
    	
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	// do something here
            	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            	startActivity(intent);
            }
        }, DELAY);
    }

}
