package com.cse190.petcafe.ui;

import static com.cse190.petcafe.ui.PostListFragment.MY_POSTS;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.R;

public class ActivityMyBlog extends ActivityBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_postlist, content, true);

        Log.e("ActivityMyBlog", "onCreate");
        String fbID = ((ApplicationSingleton)getApplication())
                .getCurrentUser().getFacebookId();

        Fragment fragment = PostListFragment.newInstance(MY_POSTS, fbID);
        ((PostListFragment) fragment).loadFragment(ActivityMyBlog.this);
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        // Inflate the menu; this adds items to the action bar if it is present.
    //        getMenuInflater().inflate(R.menu.activity_myblog, menu);
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
