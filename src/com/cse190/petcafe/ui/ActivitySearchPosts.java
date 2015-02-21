package com.cse190.petcafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cse190.petcafe.R;

public class ActivitySearchPosts extends ActivityBase {

	private Button search;
	private EditText input;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_searchposts, content,
				true);

		input = (EditText) findViewById(R.id.input);
		search = (Button) findViewById(R.id.button1);

		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ActivitySearchPosts.this, input.getText().toString(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
