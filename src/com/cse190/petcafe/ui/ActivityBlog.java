package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.MainActivity;
import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.TabsPagerAdapter;
import com.cse190.petcafe.drawer.ActivityBaseTab;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

@SuppressWarnings("deprecation")
public class ActivityBlog extends ActivityBaseTab implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Stories", "Experiences", "News", "Wiki"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_blog, content, true); 

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
    	getAllChatDialogs();
	}
	
    private void getAllChatDialogs()
    {
		QBRequestGetBuilder builder = new QBRequestGetBuilder();
		builder.setPagesLimit(100);
		
		QBChatService.getChatDialogs(null, builder, new QBEntityCallbackImpl<ArrayList<QBDialog>>()
		{
			@Override
			public void onSuccess(final ArrayList<QBDialog> dialogs, Bundle args)
			{
				List<Integer> userIDs = new ArrayList<Integer>();
				for (QBDialog dialog : dialogs)
				{
					userIDs.addAll(dialog.getOccupants());
				}
				
                QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
                requestBuilder.setPage(1);
                requestBuilder.setPerPage(userIDs.size());
                //
                QBUsers.getUsersByIDs(userIDs, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle params) {

                        ((ApplicationSingleton)getApplication()).setDialogsUsers(users);

                        // build list view
                        //
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityBlog.this);
                        dialog.setMessage("get occupants errors: " + errors).create().show();
                    }

                });
			}
			
			@Override
			public void onError(List<String> errors)
			{
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityBlog.this);
                dialog.setMessage("get dialogs errors: " + errors).create().show();
			}
		});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main1, menu);
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
	}
	
    // On Main Screen, when back is pressed exit app
    @Override
    public void onBackPressed() {
        //your code when back button pressed
    	finish();
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
