package com.cse190.petcafe.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.cse190.petcafe.adapter.DrawerItemCustomAdapter;
import com.cse190.petcafe.ui.ActivityFindFriends;
import com.cse190.petcafe.ui.ActivityMyFriends;
import com.cse190.petcafe.ui.ActivityNewPost;
import com.cse190.petcafe.ui.ActivityProfile;
import com.cse190.petcafe.ui.ActivitySearchPosts;
import com.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import com.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import com.nineoldandroids.view.ViewHelper;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class MainActivity extends ActionBarActivity implements ScrollTabHolder,
		ViewPager.OnPageChangeListener {

	private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();

	private KenBurnsSupportView mHeaderPicture;
	private View mHeader;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	private int mActionBarHeight;
	private int mMinHeaderHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private ImageView mHeaderLogo;

	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();

	private TypedValue mTypedValue = new TypedValue();
	private SpannableString mSpannableString;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;

	private Toolbar toolbar;
	private TextView title;
	private ImageView icon;

	private final int ACTIVITY_BLOG = 0;
	private final int ACTIVITY_PROFILE = 1;
	private final int ACTIVITY_NEWPOST = 2;
	private final int ACTIVITY_SEARCHPOSTS = 3;
	private final int ACTIVITY_MYFRIENDS = 4;
	private final int ACTIVITY_FINDFRIENDS = 5;

	public String[] mDrawerTitles;
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public LinearLayout mDrawerView;
	public CharSequence mTitle;
	public ActionBarDrawerToggle mDrawerToggle;

	protected LinearLayout fullLayout;
	protected FrameLayout actContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Paging Slider Strip
		mMinHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.min_header_height);
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);
		mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		icon = (ImageView) findViewById(R.id.icon);
		title = (TextView) findViewById(R.id.title);

		mHeaderPicture = (KenBurnsSupportView) findViewById(R.id.header_picture);
		mHeaderPicture.setResourceIds(R.drawable.pic0, R.drawable.pic1);
		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
		mHeader = findViewById(R.id.header);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(4);

		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mPagerAdapter.setTabHolderScrollingContent(this);
		mViewPager.setAdapter(mPagerAdapter);

		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip.setOnPageChangeListener(this);

		mSpannableString = new SpannableString(
				getString(R.string.actionbar_title));
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
		ViewHelper.setAlpha(getActionBarIconView(), 0f);

		getSupportActionBar().setBackgroundDrawable(null);

		// Navigation Drawer
		mDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerView = (LinearLayout) findViewById(R.id.drawer);
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];
		drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_launcher, "Blogs");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_launcher, "Profile");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_launcher, "New Post");
		drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_launcher,
				"Search Posts");
		drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_launcher,
				"My Friends");
		drawerItem[5] = new ObjectDrawerItem(R.drawable.ic_launcher,
				"Find Friends");

		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
				R.layout.listitem_drawer, drawerItem);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
	public void onPageScrollStateChanged(int arg0) {
		// nothing
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// nothing
	}

	@Override
	public void onPageSelected(int position) {
		SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter
				.getScrollTabHolders();
		ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);

		currentHolder.adjustScroll((int) (mHeader.getHeight() + ViewHelper
				.getTranslationY(mHeader)));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
		if (mViewPager.getCurrentItem() == pagePosition) {
			int scrollY = getScrollY(view);
			ViewHelper.setTranslationY(mHeader,
					Math.max(-scrollY, mMinHeaderTranslation));
			float ratio = clamp(ViewHelper.getTranslationY(mHeader)
					/ mMinHeaderTranslation, 0.0f, 1.0f);
			interpolate(mHeaderLogo, getActionBarIconView(),
					sSmoothInterpolator.getInterpolation(ratio));
			setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
		}
	}

	@Override
	public void adjustScroll(int scrollHeight) {
	}

	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHeaderHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	private void interpolate(View view1, View view2, float interpolation) {
		getOnScreenRect(mRect1, view1);
		getOnScreenRect(mRect2, view2);

		float scaleX = 1.0F + interpolation
				* (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation
				* (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left
				+ mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top
				+ mRect2.bottom - mRect1.top - mRect1.bottom));

		ViewHelper.setTranslationX(view1, translationX);
		ViewHelper.setTranslationY(view1,
				translationY - ViewHelper.getTranslationY(mHeader));
		ViewHelper.setScaleX(view1, scaleX);
		ViewHelper.setScaleY(view1, scaleY);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		return rect;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getTheme().resolveAttribute(android.R.attr.actionBarSize,
					mTypedValue, true);
		} else {
			getTheme()
					.resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
		}

		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());

		return mActionBarHeight;
	}

	// private void setTitleAlpha(float alpha) {
	// mAlphaForegroundColorSpan.setAlpha(alpha);
	// mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
	// mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	// getSupportActionBar().setTitle(mSpannableString);
	// }
	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		title.setText(mSpannableString);
	}

	// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// private ImageView getActionBarIconView() {
	//
	// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
	// return (ImageView)findViewById(android.R.id.home);
	// }
	//
	// return (ImageView)findViewById(android.support.v7.appcompat.R.id.home);
	// }
	private ImageView getActionBarIconView() {
		return icon;
	}

	private void selectItem(int position) {
		// Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
		Intent intent = null;
		switch (position) {
		case ACTIVITY_BLOG:
			// intent = new Intent(this, ActivityBlogPost.class);
			break;
		case ACTIVITY_PROFILE:
			intent = new Intent(this, ActivityProfile.class);
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
		if (intent != null) {
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerView);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public class PagerAdapter extends FragmentPagerAdapter implements
			IconTabProvider {

		private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
		private final String[] TITLES = { "Stories", "Experience", "News",
				"Wiki" };
		private int[] resources = { R.drawable.ic_stories, R.drawable.ic_tips,
				R.drawable.ic_news, R.drawable.ic_wiki };
		private ScrollTabHolder mListener;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
		}

		public void setTabHolderScrollingContent(ScrollTabHolder listener) {
			mListener = listener;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) SampleListFragment
					.newInstance(position);

			mScrollTabHolders.put(position, fragment);
			if (mListener != null) {
				fragment.setScrollTabHolder(mListener);
			}

			return fragment;
		}

		public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}

		@Override
		public int getPageIconResId(int position) {

			return resources[position];
		}

	}
}