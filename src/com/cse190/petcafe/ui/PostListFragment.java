package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
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

import com.cse190.petcafe.GlobalStrings;
import com.cse190.petcafe.Petcafe_api;
import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.ListViewAdapter;

public class PostListFragment extends ScrollTabHolderFragment implements
        OnScrollListener {

    // Types of post list
    public final static String KEY_POST_TYPE = "posttype";
    public final static int MY_POSTS       = 0;
    public final static int FRIENDS_POSTS  = 1;
    public final static int FILTERED_POSTS = 2;
    public final static int TABBED_POSTS   = 3;

    private static final String KEY_ARG = "args";

    private static final String [] POST_TYPES = {"Stories", "Tips", "News", "Wiki"};

    private static final Map<String, Integer> POST_RESOURCES;
    static {
        Map<String, Integer> resMap = new HashMap<String, Integer>();
        resMap.put("cat", R.drawable.noun_1836);
        resMap.put("dog", R.drawable.noun_2015);
        POST_RESOURCES = Collections.unmodifiableMap(resMap);
    }

    private final Petcafe_api api = new Petcafe_api();

    private ListView mListView;
    private ArrayList<ListViewItem> mListItems;

    private int mPosition;
    private int mPostListType;
    private String [] mRequestArgs;
    private int mOffset;

    /**
     * @param position
     *            the tab position on the main post page
     * @return Fragment of a post list of a type
     */
    public static Fragment newInstance(int postListType, int position) {
        PostListFragment f = new PostListFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_POST_TYPE, postListType);
        b.putStringArray(KEY_ARG, new String[]{POST_TYPES[position]});
        f.setArguments(b);
        return f;
    }

    /**
     * @return Fragment of a post list from search result
     */
    public static Fragment newInstance(int postListType,
            String searchText, String tag, String type) {
        PostListFragment f = new PostListFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_POST_TYPE, postListType);
        b.putStringArray(KEY_ARG, new String[]{searchText, tag, type});
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostListType = getArguments().getInt(KEY_POST_TYPE);
        mRequestArgs = getArguments().getStringArray("args");
        mOffset = 0;

        mListItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();

        new GetPostListTask().execute(mPostListType, mRequestArgs);
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
            public void onItemClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                // TODO Auto-generated method stub
                Log.d("listitem", "clicked");
                int itemPosition = position;
                // String itemValue = (String)
                // mListView.getItemAtPosition(position);
                // Log.d("2", "2");
                Intent intent;
                switch (itemPosition) {
                case 0:
                    break;
                default:
                    Log.d("item", "Now position:" + itemPosition);
                    // bundle...
                    intent = new Intent(getActivity(), ActivitySinglePost.class);
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

    private class GetPostListTask extends AsyncTask<Object, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            // start spinning icon
        }

        @Override
        protected JSONArray doInBackground(Object... params) {
            int arrIndex = 0;
            JSONArray postArr = null;
            int postListType = (Integer)params[arrIndex++];
            String [] args = (String [])params[arrIndex++];
            try {
                if (postListType == MY_POSTS) {
                    postArr = api.getMyPost(args[0]);
                } else if (postListType == FRIENDS_POSTS) {
                    postArr = api.getFriendsPost(args[0]);
                } else if (postListType == FILTERED_POSTS) {
                    arrIndex = 0;
                    String title = args[arrIndex++];
                    String tag   = args[arrIndex++];
                    String type  = args[arrIndex++];
                    postArr = api.getPostByFilters(title, tag, type);
                } else { // (postListType == TABBED_POSTS) || default
                    String type  = args[0];
                    postArr = api.getPostByFilters(null, null, type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(GlobalStrings.LOGTAG, e.toString());
            }
            return postArr;
        }

        @Override
        protected void onPostExecute (JSONArray postArr) {
            // turn off spinning icon

            // errors
            if (postArr == null) return;

            // update UI
            Resources resources = getResources();
            for (int i = 0; i < postArr.length(); i++) {
                try {
                    JSONObject postObj = postArr.getJSONObject(i);
                    int petIconRes = POST_RESOURCES.get(postObj.getString("tag"));
                    mListItems.add(new ListViewItem(resources.getDrawable(petIconRes),
                            getString(R.string.dog), postObj.getString("body")));
                } catch (JSONException e) {
                    Log.e(GlobalStrings.LOGTAG, e.toString());
                }
            }
        }
    }
}
