package com.cse190.petcafe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cse190.petcafe.blog.FragmentBlogExperience;
import com.cse190.petcafe.blog.FragmentBlogNews;
import com.cse190.petcafe.blog.FragmentBlogStories;
import com.cse190.petcafe.blog.FragmentBlogWiki;

public class TabsPagerAdapter extends FragmentPagerAdapter {

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
		return 4;
	}

}