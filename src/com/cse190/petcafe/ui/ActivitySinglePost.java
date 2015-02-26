package com.cse190.petcafe.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse190.petcafe.R;

public class ActivitySinglePost extends ActivityBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_single_post, content, true);

        // receive the arguments from the previous Activity
        Bundle extras = getIntent().getBundleExtra(PostListFragment.KEY_POST);
        if (extras != null) {
            try {
                JSONObject postObj = new JSONObject(
                        extras.getString(PostListFragment.KEY_POST));

                TextView authorText = (TextView) findViewById(R.id.post_author);
                authorText.setText(postObj.getString("author_id"));

                TextView tagText = (TextView) findViewById(R.id.post_tags);
                tagText.setText(postObj.getString("tag"));

                TextView bodyText = (TextView) findViewById(R.id.post_body);
                bodyText.setText(postObj.getString("body"));

                TextView titleText = (TextView) findViewById(R.id.post_title);
                titleText.setText(postObj.getString("title"));
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
}
