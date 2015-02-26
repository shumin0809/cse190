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

import com.cse190.petcafe.MainActivity;
import com.cse190.petcafe.ObjectDrawerItem;
import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.DrawerItemCustomAdapter;

public class ActivityBase extends ActionBarActivity {

    private final int ACTIVITY_BLOG = 0;
    private final int ACTIVITY_PROFILE = 1;
    private final int ACTIVITY_MYBLOG = 2;
    private final int ACTIVITY_NEWPOST = 3;
    private final int ACTIVITY_SEARCHPOSTS = 4;
    private final int ACTIVITY_MYFRIENDS = 5;
    private final int ACTIVITY_FINDFRIENDS = 6;

    public String[] mDrawerTitles;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public LinearLayout mDrawerView;
    public CharSequence mTitle;
    public ActionBarDrawerToggle mDrawerToggle;
    public Toolbar toolbar;
    public ImageView newpost;

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

        // New Post Menu Item
        newpost = (ImageView) findViewById(R.id.new_post);
        newpost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent newPostIntent = new Intent(ActivityBase.this, ActivityNewPost.class);
                startActivity(newPostIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerView = (LinearLayout) findViewById(R.id.drawer);

        String [] drawerItemNames = {
                "Blogs", "Profile", "My Blog", "New Post", "Search Posts",
                "My Friends", "Find Friends"};

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[drawerItemNames.length];
        for (int i = 0; i < drawerItemNames.length; ++i) {
            drawerItem[i] = new ObjectDrawerItem(R.drawable.ic_launcher, drawerItemNames[i]);
        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
                R.layout.listitem_drawer, drawerItem);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
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
        Intent intent = null;
        switch (position) {
        case ACTIVITY_BLOG:
            intent = new Intent(this, MainActivity.class);
            break;
        case ACTIVITY_PROFILE:
            intent = new Intent(this, ActivityProfile.class);
            break;
        case ACTIVITY_MYBLOG:
            intent = new Intent(this, ActivityMyBlog.class);
            break;
        case ACTIVITY_NEWPOST:
            intent = new Intent(this, ActivityNewPost.class);
            break;
        case ACTIVITY_SEARCHPOSTS:
            intent = new Intent(this, ActivitySearchPosts.class);
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
