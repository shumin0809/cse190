package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cse190.petcafe.R;
import com.cse190.petcafe.drawer.ActivityBase;

public class ActivityMyFriends extends ActivityBase {

	private List<String> listValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
				
		friendList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(view.getContext(), ActivityChat.class);
				i.putExtra("name", "Dong Sung Chang");
				i.putExtra("uid", 2359813);
				i.putExtra("originClass", "MyFriends");
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
