package com.cse190.petcafe.ui;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse190.petcafe.NetworkHandler;
import com.cse190.petcafe.R;
import com.cse190.petcafe.UserProfileInformation;

public class ActivityViewPost extends ActivityBase {

    private static final Map<String, Integer> BKGRND_RES;
    static {
        Map<String, Integer> resMap = new HashMap<String, Integer>();
        resMap.put("News",    R.drawable.background_news);
        resMap.put("Stories", R.drawable.background_stories);
        resMap.put("Wiki",    R.drawable.background_wiki);
        resMap.put("Tips",    R.drawable.background_tips);
        BKGRND_RES = Collections.unmodifiableMap(resMap);
    }

    private View mLoadingIcon;
    private UserProfileInformation mUserProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_viewpost, content, true);

        mLoadingIcon = findViewById(R.id.loadingPanel);

        // receive the arguments from the previous Activity
        Bundle extras = getIntent().getBundleExtra(PostListFragment.KEY_POST);
        if (extras != null) {
            try {
                final JSONObject postObj = new JSONObject(
                        extras.getString(PostListFragment.KEY_POST));
                // background
                Resources res = getResources();
                LinearLayout layoutBkgrnd = (LinearLayout) findViewById(R.id.viewpost_background);
                int backgroundResID = BKGRND_RES.get(postObj.getString("type"));
                layoutBkgrnd.setBackground(res.getDrawable(backgroundResID));
                // title
                TextView titleText = (TextView) findViewById(R.id.post_title);
                titleText.setText(postObj.getString("title"));
                // tag
                TextView tagText = (TextView) findViewById(R.id.post_tags);
                tagText.setText("Tag:" + postObj.getString("tag"));
                // body paragraph
                TextView bodyText = (TextView) findViewById(R.id.post_body);
                bodyText.setText(postObj.getString("body"));
                // profile pic
                new GetFBProfilePic().execute(postObj.getString("author_id"));
                // author name
                TextView authorText = (TextView) findViewById(R.id.post_author);

                mUserProfile = new UserProfileInformation();
                mUserProfile.setFacebookUID(postObj.getString("author_id"));
                mUserProfile = NetworkHandler.getInstance().getUser(mUserProfile);

                authorText.setText(mUserProfile.getUserName());
            } catch (JSONException e) {
                Log.e("ViewPost onCreate Error", e.toString());
            }
        }
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        // Inflate the menu; this adds items to the action bar if it is present.
    //        getMenuInflater().inflate(R.menu.activity_single_post, menu);
    //        return true;
    //    }

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

    private class GetFBProfilePic extends AsyncTask<Object, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Object... params) {
            String authorId = (String) params[0];
            Bitmap image = null;
            try {
                URL imgUrl = new URL("https://graph.facebook.com/"
                        + authorId + "/picture?type=large");
                InputStream in = (InputStream) imgUrl.getContent();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Cannot download image", e.toString());
            }
            return image;
        }

        @Override
        protected void onPostExecute (Bitmap image) {
            if (image != null) {
                ImageView profilePic = (ImageView) findViewById(R.id.prof_pic);
                profilePic.setImageBitmap(image);
            }
        }
    }
}
