package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cse190.petcafe.R;

public class ActivityMyFriends extends ActivityBase {

	private List<String> listValues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_myfriends, content, true);

		ListView friendList = (ListView) findViewById(R.id.friendslist);

		listValues = new ArrayList<String>();
		listValues.add("Daniel");
		listValues.add("Michael");
		listValues.add("Shumin");
		listValues.add("Scott");
		listValues.add("Lucia");

		// initiate the listadapter
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
				R.layout.listitem_myfriends, R.id.listText, listValues);

		// assign the list adapter
		friendList.setAdapter(myAdapter);

	}
	
}
