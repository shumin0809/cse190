package com.cse190.petcafe.drawer;

//Drawer base for for all fragment activities extending it
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.DrawerItemCustomAdapter;
import com.cse190.petcafe.ui.ActivityBlog;
import com.cse190.petcafe.ui.ActivityBlogPost;
import com.cse190.petcafe.ui.ActivityFindFriends;
import com.cse190.petcafe.ui.ActivityMyFriends;
import com.cse190.petcafe.ui.ActivityProfile;

@SuppressWarnings("deprecation")
public class ActivityBaseTab extends FragmentActivity {
	private final int ACTIVITY_BLOG = 0;
	private final int ACTIVITY_PROFILE = 1;
	private final int ACTIVITY_POSTBLOG = 2;
	private final int ACTIVITY_MYFRIENDS = 3;
	private final int ACTIVITY_FINDFRIENDS = 4;

	public String[] mDrawerTitles;
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public CharSequence mTitle;
	public ActionBarDrawerToggle mDrawerToggle;

	protected LinearLayout fullLayout;
	protected FrameLayout actContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		mDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];

		drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_blog, "Blogs");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_profile, "Profile");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_postblog,
				"Post Blog");
		drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_myfriends,
				"My Friends");
		drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_findfriends,
				"Find Friends");

		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
				R.layout.listitem_drawer, drawerItem);

		mDrawerList.setAdapter(adapter);
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main1, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		switch (item.getItemId()) {
		case R.id.post_blog:
			Intent intent = new Intent(this, ActivityBlogPost.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Swaps fragments in the main content view
	 */
	private void selectItem(int position) {
		// Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
		Intent intent = null;
		switch (position) {
		case ACTIVITY_BLOG:
			intent = new Intent(this, ActivityBlog.class);
			break;
		case ACTIVITY_PROFILE:
			intent = new Intent(this, ActivityProfile.class);
			break;
		case ACTIVITY_POSTBLOG:
			intent = new Intent(this, ActivityBlogPost.class);
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
		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		// getActionBar().setTitle(mTitle);
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
