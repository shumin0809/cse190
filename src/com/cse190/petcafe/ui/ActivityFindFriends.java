package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cse190.petcafe.R;

public class ActivityFindFriends extends ActivityBase {

	// private List<String> listValues;

	private Button find;
	private EditText name;
	private EditText location;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_findfriends, content,
				true);
		/*
		 * ListView friendList = (ListView) findViewById(R.id.friendslist);
		 * 
		 * listValues = new ArrayList<String>(); listValues.add("New Friend 1");
		 * listValues.add("New Friend 2"); listValues.add("New Friend 3");
		 * listValues.add("New Friend 4"); listValues.add("New Friend 5");
		 * 
		 * // initiate the listadapter ArrayAdapter<String> myAdapter = new
		 * ArrayAdapter<String>(this, R.layout.listitem_findfriends,
		 * R.id.listText, listValues);
		 * 
		 * // assign the list adapter friendList.setAdapter(myAdapter);
		 */

		name = (EditText) findViewById(R.id.name);
		location = (EditText) findViewById(R.id.location);
		find = (Button) findViewById(R.id.button1);

		find.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(
						ActivityFindFriends.this,
						name.getText().toString()
								+ " " + location.getText().toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
