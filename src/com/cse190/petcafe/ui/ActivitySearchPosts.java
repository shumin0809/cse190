package com.cse190.petcafe.ui;

import static com.cse190.petcafe.ApplicationSingleton.GA_ACTION_BTN;
import static com.cse190.petcafe.ApplicationSingleton.GA_CATEGORY_UI;
import static com.cse190.petcafe.ui.PostListFragment.FILTERED_POSTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.ApplicationSingleton.TrackerName;
import com.cse190.petcafe.R;
import com.google.android.gms.analytics.HitBuilders;

public class ActivitySearchPosts extends ActivityBase {

    private EditText  mInput;
    private Spinner   mSpinnerType;
    private Spinner   mSpinnerTag;
    private ImageView mSearchIcon;

    private Map<String, String> mSearchHit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_searchposts, content, true);

        mInput       = (EditText) findViewById(R.id.search_input);
        mSpinnerType = (Spinner)  findViewById(R.id.search_spinner_type);
        mSpinnerTag  = (Spinner)  findViewById(R.id.search_spinner_tag);
        mSearchIcon = (ImageView) findViewById(R.id.search_icon);

        addItemsOnSpinner();
        addListenerOnButton();

        mTracker = ((ApplicationSingleton) getApplication()).getTracker(
                TrackerName.APP_TRACKER);

        mSearchHit = new HitBuilders.EventBuilder()
                .setCategory(GA_CATEGORY_UI)
                .setAction(GA_ACTION_BTN)
                .setLabel("Search")
                .build();
    }

    // add items into spinner dynamically
    private void addItemsOnSpinner() {

        int rSpinnerItem = android.R.layout.simple_spinner_item;
        int rSpinnerDropdownItem = android.R.layout.simple_spinner_dropdown_item;

        // type dropdown menu
        ArrayList<String> postTypes = new ArrayList<String>(Arrays.asList(
                getResources().getStringArray(R.array.post_types)));
        postTypes.add(0, "All");

        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<String>(this, rSpinnerItem, postTypes);
        typeAdapter.setDropDownViewResource(rSpinnerDropdownItem);
        mSpinnerType.setAdapter(typeAdapter);

        // tag dropdown menu
        ArrayList<String> postTags = new ArrayList<String>(Arrays.asList(
                getResources().getStringArray(R.array.pet_species)));
        postTags.add(0, "All");

        ArrayAdapter<String> tagAdapter =
                new ArrayAdapter<String>(this, rSpinnerItem, postTags);
        tagAdapter.setDropDownViewResource(rSpinnerDropdownItem);
        mSpinnerTag.setAdapter(tagAdapter);
    }

    // get the selected dropdown list value
    private void addListenerOnButton() {
        mSearchIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mInput.getText().toString();
                String type = String.valueOf(mSpinnerType.getSelectedItem());
                String tag  = String.valueOf(mSpinnerTag.getSelectedItem());

                Fragment fragment = PostListFragment
                        .newInstance(FILTERED_POSTS, searchText, tag, type);
                ((PostListFragment) fragment).loadFragment(ActivitySearchPosts.this);

                mTracker.send(mSearchHit);
            }
        });
    }
}
