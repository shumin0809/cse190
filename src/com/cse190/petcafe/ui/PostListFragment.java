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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cse190.petcafe.GlobalStrings;
import com.cse190.petcafe.Petcafe_api;
import com.cse190.petcafe.R;
import com.cse190.petcafe.adapter.ListViewAdapter;

public class PostListFragment
        extends ScrollTabHolderFragment implements OnScrollListener {

    // Types of post list
    public final static int MY_POSTS       = 0;
    public final static int FRIENDS_POSTS  = 1;
    public final static int FILTERED_POSTS = 2;
    public final static int TABBED_POSTS   = 3;

    // keys for bundles
    public final static String KEY_POST      = "post";
    public final static String KEY_POST_TYPE = "posttype";
    public final static String KEY_POSTLIST  = "postlist";

    public static final Map<String, Integer> POST_RESOURCES;
    static {
        Map<String, Integer> resMap = new HashMap<String, Integer>();
        resMap.put("Cat",    R.drawable.icon_cat);
        resMap.put("Dog",    R.drawable.icon_dog);
        resMap.put("Rabbit", R.drawable.icon_rabbit);
        resMap.put("Pig",    R.drawable.icon_pig);
        resMap.put("All",    R.drawable.icon_all);
        resMap.put("Other",  R.drawable.icon_other);
        POST_RESOURCES = Collections.unmodifiableMap(resMap);
    }

    private final Petcafe_api api = new Petcafe_api();

    private ListView mListView;
    private ArrayList<ListViewItem> mListItems;

    private View mLoadingIcon;
    private int mPosition;
    private int mPostListType;
    private String [] mRequestArgs;
    private int mOffset;
    private JSONArray mPostJArr;
    private ListViewAdapter mPostListAdapter;

    /**
     * @param position
     *            the tab position on the main post page
     * @return Fragment of a post list of a type
     */
    public static Fragment newInstance(int postListType, String postType) {
        PostListFragment f = new PostListFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_POST_TYPE, postListType);
        b.putStringArray(KEY_POSTLIST, new String[]{postType});
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
        mRequestArgs  = getArguments().getStringArray(KEY_POSTLIST);
        mOffset = 0;
        mListItems = new ArrayList<ListViewItem>();
        Log.e("PostListFragment", "onCreate");
        //setListAdapter(new ListViewAdapter(getActivity(), mListItems));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blog_list, null);

        mListView = (ListView) v.findViewById(R.id.listView);

        // extra space cushion for the header in Main Post List
        if (mPostListType == TABBED_POSTS) {
            View placeHolderView = inflater.inflate(
                    R.layout.view_header_placeholder, mListView, false);
            mListView.addHeaderView(placeHolderView);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                if (position == 0 && mPostListType == TABBED_POSTS) {
                    // ignore, this is the background header
                    // Tabbed_Posts has header at position 0

                } else {
                    int objIndex = mPostListType == TABBED_POSTS
                            ? position - 1 : position;
                    Intent intent = new Intent(getActivity(), ActivityViewPost.class);
                    try {
                        // bundle containg post info
                        Bundle b = new Bundle();
                        b.putString(KEY_POST,
                                mPostJArr.getJSONObject(objIndex).toString());
                        intent.putExtra(KEY_POST, b);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            }
        });

        mLoadingIcon = v.findViewById(R.id.loadingPanel);
        new GetPostListTask().execute(mPostListType, mRequestArgs);

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
        protected void onPreExecute () {
            mLoadingIcon.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(Object... params) {
            Log.e("PostListFragment", "fetching from DB");
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
            updatePostList(postArr);   // update UI
            mLoadingIcon.setVisibility(View.GONE);
        }
    }

    // -----------------------------------------------------------
    // Helper Functions
    // -----------------------------------------------------------
    private void updatePostList(JSONArray postArr) {
        if (postArr == null) {
            Toast.makeText(getActivity(), "Network error! Please check your connection.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // store post information
        mPostJArr = postArr;
        // fill up UI list
        mListItems.clear();
        Resources resources = getResources();
        for (int i = 0; i < postArr.length(); i++) {
            try {
                JSONObject postObj = postArr.getJSONObject(i);

                Drawable petIconRes = resources.getDrawable(
                        POST_RESOURCES.get(postObj.getString("tag")));
                String title = postObj.getString("title");
                String body = postObj.getString("body");
                body = body.substring(0, Math.min(body.length(), 150)) + "...";
                mListItems.add(new ListViewItem(petIconRes, title, body));

            } catch (JSONException e) {
                Log.e(GlobalStrings.LOGTAG, e.toString());
            }
        }
        // update UI
        mPostListAdapter.notifyDataSetChanged();
    }

    /**
     * Helper method for reusing this fragment class to list posts
     * @param fa - the activity that needs this fragment
     * remember to add FrameLayout
     */
    public void loadFragment (FragmentActivity fa) {
        FragmentManager fragmentManager = fa.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.postlist_fragment, this);
        fragmentTransaction.commit();
    }

}
