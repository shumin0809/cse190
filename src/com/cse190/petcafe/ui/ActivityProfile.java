package com.cse190.petcafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cse190.petcafe.R;

public class ActivityProfile extends ActivityBase {

	@Override
	public void onCreate(Bundle savedInstanceState) {
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

		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				Intent intent = null;
				switch (menuItem.getItemId()) {
				case R.id.post_blog:
					Toast.makeText(ActivityProfile.this, "Post Blog",
							Toast.LENGTH_SHORT).show();
					break;
				case R.id.edit_profile:
					Toast.makeText(ActivityProfile.this, "Edit Profile",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(ActivityProfile.this,
							ActivityEditProfile.class);
					break;
				default:
					break;
				}
				if (intent != null) {
					startActivity(intent);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
				return false;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_profilemenu, menu);
		return true;
	}
}
