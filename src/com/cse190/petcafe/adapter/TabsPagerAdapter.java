package com.cse190.petcafe.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cse190.petcafe.blog.FragmentBlogExperience;
import com.cse190.petcafe.blog.FragmentBlogNews;
import com.cse190.petcafe.blog.FragmentBlogStories;
import com.cse190.petcafe.blog.FragmentBlogWiki;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	final int PAGE_COUNT = 4;
	// Tab Titles
	private String tabtitles[] = new String[] { "Stories", "Experience", "News", "Wiki" };
	Context context;

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Stories fragment activity
			return new FragmentBlogStories();
		case 1:
			// Experience fragment activity
			return new FragmentBlogExperience();
		case 2:
			// News fragment activity
			return new FragmentBlogNews();
		case 3:
			// Wiki fragment activity
			return new FragmentBlogWiki();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
	
}