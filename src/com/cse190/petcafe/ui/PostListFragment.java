package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.GlobalStrings;
import com.cse190.petcafe.Petcafe_api;
import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.ListViewAdapter;
import com.quickblox.users.model.QBUser;

public class PostListFragment
        extends ScrollTabHolderFragment implements OnScrollListener {

    // Types of post list
    public final static int MY_POSTS       = 0;
    public final static int FRIENDS_POSTS  = 1;
    public final static int FILTERED_POSTS = 2;
    public final static int TABBED_POSTS   = 3;

    // keys for bundles
    public final static String KEY_POST_TYPE = "posttype";
    private static final String KEY_POSTLIST = "postlist";
    private static final String KEY_POST = "post";

    private static final String [] POST_TYPES = {"Stories", "Tips", "News", "Wiki"};

    private static final Map<String, Integer> POST_RESOURCES;
    static {
        Map<String, Integer> resMap = new HashMap<String, Integer>();
        resMap.put("Cat",    R.drawable.noun_1836);
        resMap.put("Dog",    R.drawable.noun_2015);
        resMap.put("Rabbit", R.drawable.noun_2015);
        resMap.put("Pig",    R.drawable.noun_2015);
        resMap.put("All",    R.drawable.noun_2015);
        POST_RESOURCES = Collections.unmodifiableMap(resMap);
    }

    private final Petcafe_api api = new Petcafe_api();

    private ListView mListView;
    private ArrayList<ListViewItem> mListItems;

    private int mPosition;
    private int mPostListType;
    private String [] mRequestArgs;
    private int mOffset;
    private JSONArray mPostJArr;
    private ListViewAdapter mPostListAdapter;

    private ProgressDialog mProgressDialog;

    /**
     * @param position
     *            the tab position on the main post page
     * @return Fragment of a post list of a type
     */
    public static Fragment newInstance(int postListType, int position) {
        PostListFragment f = new PostListFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_POST_TYPE, postListType);
        b.putStringArray(KEY_POSTLIST, new String[]{POST_TYPES[position]});
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
        b.putStringArray(KEY_POSTLIST, new String[]{searchText, tag, type});
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostListType = getArguments().getInt(KEY_POST_TYPE);
        mRequestArgs = getArguments().getStringArray(KEY_POSTLIST);
        mOffset = 0;
        mListItems = new ArrayList<ListViewItem>();

        new GetPostListTask().execute(mPostListType, mRequestArgs);
        Log.e("PostListFragment", "onCreate");
        //setListAdapter(new ListViewAdapter(getActivity(), mListItems));
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
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Log.e("item", "Now position:" + position);
                // String itemValue = (String)
                Object o = mListView.getItemAtPosition(position);
                Log.e("mListView Object", o.toString());

                switch (position) {
                case 0:
                    break;
                default:
                    Intent intent = new Intent(getActivity(), ActivitySinglePost.class);
                    try {
                        // bundle containg post info
                        Bundle b = new Bundle();
                        b.putString(KEY_POST,
                                mPostJArr.getJSONObject(position - 1).toString());
                        intent.putExtra(KEY_POST, b);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
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
        mPostListAdapter = new ListViewAdapter(getActivity(), mListItems);
        mListView.setAdapter(mPostListAdapter);
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
            mScrollTabHolder.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount, mPosition);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // nothing
    }

    private class GetPostListTask extends AsyncTask<Object, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            // start spinning icon
            mProgressDialog = ProgressDialog.show(getActivity(), "Loading", "Wait...");
        }

        @Override
        protected JSONArray doInBackground(Object... params) {
            Log.e("PostListAsync", "doInBackground");
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
            Log.e("GetPostAsync", "onPostExecute");
            mProgressDialog.dismiss(); // turn off spinning icon
            updatePostList(postArr);   // update UI
        }
    }

    // -----------------------------------------------------------
    // Helper Functions
    // -----------------------------------------------------------
    private void updatePostList(JSONArray postArr) {
        if (postArr == null) return;
        // store post information
        mPostJArr = postArr;
        // fill up UI list
        Resources resources = getResources();
        for (int i = 0; i < postArr.length(); i++) {
            try {
                JSONObject postObj = postArr.getJSONObject(i);

                Drawable petIconRes = resources.getDrawable(
                        POST_RESOURCES.get(postObj.getString("tag")));
                String title = postObj.getString("title");
                String body = postObj.getString("body").substring(0, 50) + "...";

                mListItems.add(new ListViewItem(petIconRes, title, body));
            } catch (JSONException e) {
                Log.e(GlobalStrings.LOGTAG, e.toString());
            }
        }
        // update UI
        mPostListAdapter.notifyDataSetChanged();
    }

}
