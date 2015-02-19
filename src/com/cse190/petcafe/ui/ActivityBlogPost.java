package com.cse190.petcafe.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;

import com.cse190.petcafe.R;
import com.cse190.petcafe.drawer.ActivityBase;

public class ActivityBlogPost extends ActivityBase{

 
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_blogpost, content, true); 

    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }
 
}
