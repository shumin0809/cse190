package com.cse190.petcafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.ApplicationSingleton.TrackerName;
import com.cse190.petcafe.ObjectDrawerItem;
import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.DrawerItemCustomAdapter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.cse190.petcafe.GlobalStrings;
import com.facebook.widget.ProfilePictureView;


public class ActivityBase extends ActionBarActivity {

    private static final int ACTIVITY_BLOG         = 0;
    private static final int ACTIVITY_PROFILE      = 1;
    private static final int ACTIVITY_MYBLOG       = 2;
    private static final int ACTIVITY_FRIENDSPOSTS = 3;
    private static final int ACTIVITY_SEARCHPOSTS  = 4;
    private static final int ACTIVITY_MYFRIENDS    = 5;
    private static final int ACTIVITY_FINDFRIENDS  = 6;
    private static final int ACTIVITY_CHAT = 7;


    private Intent mPendingIntent;

    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mDrawerView;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
	private ProfilePictureView profPic;
	private String fbuid;

    protected LinearLayout fullLayout;
    protected FrameLayout actContent;

    protected Tracker mTracker;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        mTracker = ((ApplicationSingleton) getApplication()).getTracker(
                TrackerName.APP_TRACKER);
        mTracker.setScreenName(this.getClass().getName());
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setupActionBar();
        setupNewPostMenu();
        setupNavDrawer();
    }

    /*
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * getMenuInflater().inflate(R.menu.toolbar_menu, menu); return true; }
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
        case R.id.new_post:
            intent = new Intent(ActivityBase.this, ActivityNewPost.class);
            startActivity(intent);
            return true;
        case R.id.action_settings:
            Toast.makeText(ActivityBase.this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void selectItem(int position) {
        // Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerView);

        switch (position) {
        case ACTIVITY_BLOG:
            mPendingIntent = new Intent(this, ActivityBlog.class);
            break;
        case ACTIVITY_PROFILE:
            mPendingIntent = new Intent(this, ActivityProfile.class);
            mPendingIntent.putExtra("fbuid", fbuid);
            mPendingIntent.putExtra("username", getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.USERNAME_CACHE_KEY, ""));
            break;
        case ACTIVITY_MYBLOG:
            mPendingIntent = new Intent(this, ActivityMyBlog.class);
            break;
        case ACTIVITY_SEARCHPOSTS:
            mPendingIntent = new Intent(this, ActivitySearchPosts.class);
            break;
        case ACTIVITY_MYFRIENDS:
            mPendingIntent = new Intent(this, ActivityMyFriends.class);
            break;
        case ACTIVITY_FINDFRIENDS:
            mPendingIntent = new Intent(this, ActivityFindFriends.class);
            break;
        case ACTIVITY_FRIENDSPOSTS:
            mPendingIntent = new Intent(this, ActivityMyFriendsPosts.class);
            break;
        case ACTIVITY_CHAT:
            mPendingIntent = new Intent(this, ActivityChat.class);
            break;
        default:
        }
    }

    protected class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent,
                View view, int position, long id) {
            selectItem(position);
        }
    }

    void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        // Disables title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void setupNewPostMenu () {
        ImageView newpost = (ImageView) findViewById(R.id.new_post);
        newpost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent newPostIntent = new Intent(ActivityBase.this,
                        ActivityNewPost.class);
                startActivity(newPostIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    void setupNavDrawer() {
		fbuid = getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, "");
		profPic = (ProfilePictureView)findViewById(R.id.activity_base_profpic);

		profPic.setCropped(true);
		profPic.setProfileId(fbuid);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerView = (LinearLayout) findViewById(R.id.drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[mDrawerTitles.length];
        for (int i = 0; i < mDrawerTitles.length; ++i) {
            drawerItem[i] = new ObjectDrawerItem(R.drawable.ic_launcher,
                    mDrawerTitles[i]);
        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
                R.layout.listitem_drawer, drawerItem);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                if (mPendingIntent != null) {
                    startActivity(mPendingIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
}
