package com.cse190.petcafe.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import com.cse190.petcafe.R;

public class ActivityNewPost extends ActivityBase {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_newpost, content, true);
	}

}
