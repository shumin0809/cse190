package com.cse190.petcafe.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.BlogPostInformation;
import com.cse190.petcafe.Petcafe_api;
import com.cse190.petcafe.R;

public class ActivityNewPost extends ActivityBase {

    private Button mBtnSubmit;

    private Spinner  mSpinnerType;
    private Spinner  mSpinnerTag;

    private Activity mSelf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_newpost, content, true);
        mSelf = this;
        Log.e("newpost", "oncreate");

        mSpinnerType = (Spinner)  findViewById(R.id.spinner_type);
        mSpinnerTag  = (Spinner)  findViewById(R.id.spinner_tag);

        addItemsOnSpinner();
        addListenerOnButton();
    }

    // add items into spinner dynamically
    private void addItemsOnSpinner() {

        int rSpinnerItem = android.R.layout.simple_spinner_item;
        int rSpinnerDropdownItem = android.R.layout.simple_spinner_dropdown_item;

        // type dropdown menu
        String [] postTypes = getResources().getStringArray(R.array.post_types);
        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<String>(this, rSpinnerItem, postTypes);
        typeAdapter.setDropDownViewResource(rSpinnerDropdownItem);
        mSpinnerType.setAdapter(typeAdapter);

        // tag dropdown menu
        String [] postTags = getResources().getStringArray(R.array.pet_species);
        ArrayAdapter<String> tagAdapter =
                new ArrayAdapter<String>(this, rSpinnerItem, postTags);
        tagAdapter.setDropDownViewResource(rSpinnerDropdownItem);
        mSpinnerTag.setAdapter(tagAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        mSpinnerType = (Spinner) findViewById(R.id.spinner_type);
        mSpinnerTag = (Spinner) findViewById(R.id.spinner_tag);
        mBtnSubmit = (Button) findViewById(R.id.post_button);

        final EditText postTitle = (EditText) findViewById(R.id.title);
        final EditText postBody  = (EditText) findViewById(R.id.body);

        mBtnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String fbID = ((ApplicationSingleton)getApplication())
                        .getCurrentUser().getFacebookId();

                String title = postTitle.getText().toString();
                String body = postBody.getText().toString();
                String type = String.valueOf(mSpinnerType.getSelectedItem());
                String tag  = String.valueOf(mSpinnerTag.getSelectedItem());

                BlogPostInformation post = new BlogPostInformation(fbID,
                        title, type, body, 3, tag);
                new AddPostTask().execute(post);
            }
        });
    }

    private class AddPostTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            BlogPostInformation post = (BlogPostInformation) params[0];
            try {
                Petcafe_api api = new Petcafe_api();
                api.addPost(post);
            } catch (Exception e) {
                Log.e("NewPost failed", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void v) {
            mSelf.finish();
        }
    }
}
