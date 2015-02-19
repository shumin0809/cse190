package com.cse190.petcafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cse190.petcafe.MainActivity;
import com.cse190.petcafe.ObjectDrawerItem;
import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.DrawerItemCustomAdapter;

public class ActivityBase extends ActionBarActivity {

	private final int ACTIVITY_BLOG = 0;
	private final int ACTIVITY_PROFILE = 1;
	private final int ACTIVITY_NEWPOST = 2;
	private final int ACTIVITY_MYFRIENDS = 3;
	private final int ACTIVITY_FINDFRIENDS = 4;

	public String[] mDrawerTitles;
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public LinearLayout mDrawerView;
	public CharSequence mTitle;
	public ActionBarDrawerToggle mDrawerToggle;
	public Toolbar toolbar;

	protected LinearLayout fullLayout;
	protected FrameLayout actContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		// Action Bar Toolbar
		toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		setSupportActionBar(toolbar);
		toolbar.setLogo(R.drawable.ic_launcher);
		// Disables title
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		// toolbar.inflateMenu(R.menu.toolbar_menu);

		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {

				switch (menuItem.getItemId()) {
				case R.id.post_blog:
					Toast.makeText(ActivityBase.this, "Post Blog",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}

				return false;
			}
		});

		// Navigation Drawer

		mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
		mDrawerView = (LinearLayout) findViewById(R.id.drawer);
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];
		drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_launcher, "Blogs");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_launcher, "Profile");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_launcher, "New Post");
		drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_launcher,
				"My Friends");
		drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_launcher,
				"Find Friends");

		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
				R.layout.listitem_drawer, drawerItem);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	// this works
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		return true;
	}

	private void selectItem(int position) {
		// Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
		Intent intent = null;
		switch (position) {
		case ACTIVITY_BLOG:
			intent = new Intent(this, MainActivity.class);
			break;
		case ACTIVITY_PROFILE:
			intent = new Intent(this, ActivityProfile.class);
			break;
		case ACTIVITY_NEWPOST:
			intent = new Intent(this, ActivityNewPost.class);
			break;
		case ACTIVITY_MYFRIENDS:
			intent = new Intent(this, ActivityMyFriends.class);
			break;
		case ACTIVITY_FINDFRIENDS:
			intent = new Intent(this, ActivityFindFriends.class);
			break;
		default:
			break;
		}
		startActivity(intent);

		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerView);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

}
