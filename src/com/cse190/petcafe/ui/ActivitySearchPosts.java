package com.cse190.petcafe.ui;

import static com.cse190.petcafe.ui.PostListFragment.FILTERED_POSTS;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cse190.petcafe.R;

public class ActivitySearchPosts extends ActivityBase {

    //private Button   mBtnSearch;
    private EditText mInput;
    private Spinner  mSpinnerType;
    private Spinner  mSpinnerTag;
    private ImageView searchIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_searchposts, content, true);

        mInput       = (EditText) findViewById(R.id.search_input);
        //mBtnSearch   = (Button)   findViewById(R.id.search_button);
        mSpinnerType = (Spinner)  findViewById(R.id.search_spinner_type);
        mSpinnerTag  = (Spinner)  findViewById(R.id.search_spinner_tag);

        searchIcon = (ImageView) findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitySearchPosts.this,
                        mInput.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        addItemsOnSpinner();
        addListenerOnButton();
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
        searchIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mInput.getText().toString();
                String type = String.valueOf(mSpinnerType.getSelectedItem());
                String tag  = String.valueOf(mSpinnerTag.getSelectedItem());

                Fragment fragment = PostListFragment
                        .newInstance(FILTERED_POSTS, searchText, tag, type);
                ((PostListFragment) fragment).loadFragment(ActivitySearchPosts.this);
            }
        });
    }
}
