package com.cse190.petcafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse190.petcafe.R;
import com.cse190.petcafe.drawer.ActivityBase;

public class ActivityProfile extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_profile, content, true);

		// receive the arguments from the previous Activity
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}

		// assign the values to string-arguments
		String name = extras.getString("name");
		String email = extras.getString("email");
		String phone = extras.getString("phone");
		String address = extras.getString("address");
		String city = extras.getString("city");
		String state = extras.getString("state");
		String zip = extras.getString("zip");

		TextView nameText = (TextView) findViewById(R.id.prof_name);
		nameText.setText(name);
		TextView emailText = (TextView) findViewById(R.id.prof_email);
		emailText.setText(email);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.edit:
			Intent intent = new Intent(this, ActivityEditProfile.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
