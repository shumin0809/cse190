package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

		friendList.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
			    Toast.makeText(getApplicationContext(),
			      "Friend: " + listValues.get(position).toString(), Toast.LENGTH_LONG)
			      .show();
			    Intent intent = new Intent(ActivityMyFriends.this, ActivityMyFriendsPosts.class);
			    startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			  }
			}); 


	}
	
	

}
