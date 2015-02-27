package com.cse190.petcafe.ui;

import static com.cse190.petcafe.ui.PostListFragment.FILTERED_POSTS;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cse190.petcafe.R;

public class ActivitySearchPosts extends ActivityBase {

    private Button   mBtnSearch;
    private EditText mInput;
    private Spinner  mSpinnerType;
    private Spinner  mSpinnerTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_searchposts, content, true);

        mInput       = (EditText) findViewById(R.id.search_input);
        mBtnSearch   = (Button)   findViewById(R.id.search_button);
        mSpinnerType = (Spinner)  findViewById(R.id.search_spinner_type);
        mSpinnerTag  = (Spinner)  findViewById(R.id.search_spinner_tag);

        mBtnSearch.setOnClickListener(new OnClickListener() {
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
        String [] postTypes = getResources().getStringArray(R.array.post_type_list);
        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<String>(this, rSpinnerItem, postTypes);
        typeAdapter.setDropDownViewResource(rSpinnerDropdownItem);
        mSpinnerType.setAdapter(typeAdapter);

        // tag dropdown menu
        String [] postTags = getResources().getStringArray(R.array.post_tag_list);
        ArrayAdapter<String> tagAdapter =
                new ArrayAdapter<String>(this, rSpinnerItem, postTags);
        tagAdapter.setDropDownViewResource(rSpinnerDropdownItem);
        mSpinnerTag.setAdapter(tagAdapter);
    }

    // get the selected dropdown list value
    private void addListenerOnButton() {
        mBtnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mInput.getText().toString();
                String type = String.valueOf(mSpinnerType.getSelectedItem());
                String tag  = String.valueOf(mSpinnerTag.getSelectedItem());

                Fragment fragment = (Fragment) PostListFragment
                        .newInstance(FILTERED_POSTS, searchText, tag, type);
                ((PostListFragment) fragment).loadFragment(ActivitySearchPosts.this);
            }
        });
    }
}
