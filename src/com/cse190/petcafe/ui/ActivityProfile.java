package com.cse190.petcafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse190.petcafe.MainActivity;
import com.cse190.petcafe.R;

public class ActivityProfile extends ActivityBase {

	private final int ACTIVITY_BLOG = 0;
	private final int ACTIVITY_PROFILE = 1;
	private final int ACTIVITY_NEWPOST = 2;
	private final int ACTIVITY_MYFRIENDS = 3;
	private final int ACTIVITY_FINDFRIENDS = 4;

	private Button edit_prof;

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

		edit_prof = (Button) findViewById(R.id.edit_profile);

		edit_prof.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent editProfIntent = new Intent(ActivityProfile.this,
						ActivityEditProfile.class);
				startActivity(editProfIntent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
		});

		/*
		 * toolbar.setOnMenuItemClickListener(new
		 * Toolbar.OnMenuItemClickListener() {
		 * 
		 * @Override public boolean onMenuItemClick(MenuItem menuItem) { Intent
		 * intent = new Intent(ActivityProfile.this, ActivityEditProfile.class);
		 * switch (menuItem.getItemId()) { case R.id.new_post:
		 * Toast.makeText(ActivityProfile.this, "Post Blog",
		 * Toast.LENGTH_SHORT).show(); intent = new Intent(ActivityProfile.this,
		 * ActivityEditProfile.class); return true; case R.id.edit_profile:
		 * Toast.makeText(ActivityProfile.this, "Edit Profile",
		 * Toast.LENGTH_SHORT).show(); intent = new Intent(ActivityProfile.this,
		 * ActivityEditProfile.class); return true; default: break; }
		 * startActivity(intent);
		 * 
		 * return false; } });
		 */

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.toolbar_profilemenu, menu); return true;
	 * }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.new_post:
			intent = new Intent(ActivityProfile.this, ActivityNewPost.class);
			startActivity(intent);
			return true;
		case R.id.edit_profile:
			intent = new Intent(ActivityProfile.this, ActivityEditProfile.class);
			startActivity(intent);
			return true;
		case R.id.action_settings:
			Toast.makeText(ActivityProfile.this, "Settings", Toast.LENGTH_SHORT)
					.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
