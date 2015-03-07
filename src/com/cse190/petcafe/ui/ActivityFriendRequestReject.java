package com.cse190.petcafe.ui;

import java.util.ArrayList;

import com.cse190.petcafe.R;
import com.cse190.petcafe.R.id;
import com.cse190.petcafe.R.layout;
import com.cse190.petcafe.R.menu;
import com.cse190.petcafe.UserProfileInformation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActivityFriendRequestReject extends Activity {
	private ListView requestList;
	private ArrayList<UserProfileInformation> requestingUsers;
	private ArrayList<String> requestingUsersNames;
	private ArrayAdapter<String> adapter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_friend_request_reject);
		
		requestList = (ListView)findViewById(R.id.requestList);
		requestingUsers = new ArrayList<UserProfileInformation>();
		requestingUsersNames = new ArrayList<String>();
		
		adapter = new ArrayAdapter<String>(this, R.layout.listitem_myfriends, R.id.listText, requestingUsersNames);
		requestList.setAdapter(adapter);
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_friend_request_reject, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
}
