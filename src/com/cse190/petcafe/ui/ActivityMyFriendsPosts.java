package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cse190.petcafe.R;

public class ActivityMyFriendsPosts extends ActivityBase {

	private List<String> listValues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_myfriendsposts, content, true);

		ListView friendList = (ListView) findViewById(R.id.friendslistposts);

		listValues = new ArrayList<String>();
		listValues.add("Friend Post 1");
		listValues.add("Friend Post 2");
		listValues.add("Friend Post 3");
		listValues.add("Friend Post 4");
		listValues.add("Friend Post 5");

		// initiate the listadapter
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
				R.layout.listitem_myfriendsposts, R.id.listText, listValues);

		// assign the list adapter
		friendList.setAdapter(myAdapter);

	}
	
}
