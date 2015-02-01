package com.cse190.petcafe;

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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class ActivityBaseTab extends FragmentActivity{
	


    public String[] mDrawerTitles;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public CharSequence mTitle;
    public ActionBarDrawerToggle mDrawerToggle;
    
    protected LinearLayout fullLayout;
    protected FrameLayout actContent;
 

    @Override
	protected void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
 
        mTitle = "test";
 
        mDrawerTitles = new String[]{"Blogs", "Profile", "Post Blog" , "Find Friends", "My Friends"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
 
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
 
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
 
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }
 
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mTitle);
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
    	switch(position){
    	case 0:
    		intent = new Intent(this, ActivityBlog.class);
    		break;
    	case 1:
    		intent = new Intent(this, ActivityProfile.class);
    		break;
    	case 2:
    		intent = new Intent(this, ActivityBlogPost.class);
    		break;
    	case 3:
    		intent = new Intent(this, ActivityBlog.class);
    		break;
    	case 4:
    		intent = new Intent(this, ActivityBlog.class);
    		break;
    	default:
    		break;
    	}
        startActivity(intent);
        
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
