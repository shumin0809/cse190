package com.cse190.petcafe;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cse190.petcafe.adapter.ListViewAdapter;
import com.cse190.petcafe.ui.ActivitySinglePost;

public class SampleListFragment extends ScrollTabHolderFragment implements
OnScrollListener {

    private static final String ARG_POSITION = "position";

    private ListView mListView;
    private ArrayList<ListViewItem> mListItems;

    private int mPosition;

    public static Fragment newInstance(int position) {
        SampleListFragment f = new SampleListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    // MEOW: Here list out all posts!
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // MEOW: mPosition is current page
        mPosition = getArguments().getInt(ARG_POSITION);

        mListItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();

        //for (int i = 1; i <= 100; i++) {
        //mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_1836), getString(R.string.dog),
                getString(R.string.dog_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        mListItems.add(new ListViewItem(resources
                .getDrawable(R.drawable.noun_2015), getString(R.string.cat),
                getString(R.string.cat_description)));
        // }
        // setListAdapter(new ListViewAdapter(getActivity(), mListItems));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blog_list, null);

        mListView = (ListView) v.findViewById(R.id.listView);

        View placeHolderView = inflater.inflate(
                R.layout.view_header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long arg3) {
                // TODO Auto-generated method stub
                Log.d("listitem","clicked");
                int itemPosition     = position;
                //String  itemValue    = (String) mListView.getItemAtPosition(position);
                //Log.d("2", "2");
                Intent intent;
                switch (itemPosition) {
                case 0:
                    break;
                default:
                    Log.d("item","Now position:"+itemPosition);
                    //bundle...
                    intent= new Intent(getActivity(), ActivitySinglePost.class);
                    startActivity(intent);
                    break;
                }
            }

        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnScrollListener(this);
        // mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
        // R.layout.list_item, android.R.id.text1, mListItems));
        mListView.setAdapter(new ListViewAdapter(getActivity(), mListItems));
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        mListView.setSelectionFromTop(1, scrollHeight);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (mScrollTabHolder != null)
            mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount, mPosition);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // nothing
    }

}
