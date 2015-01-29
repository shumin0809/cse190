package com.cse190.petcafe;

import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;

public class ActivityMain extends ActivityBase{

 
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main1, content, true); 

    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }
    
    // On Main Screen, when back is pressed exit app
    @Override
    public void onBackPressed() {
        //your code when back button pressed
    	finish();
    }
 
}
