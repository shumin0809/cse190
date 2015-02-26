package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
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

    private Button search;
    private EditText input;
    private Spinner spinnerType;
    private Spinner spinnerTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_searchposts, content,
                true);

        input = (EditText) findViewById(R.id.search_input);
        search = (Button) findViewById(R.id.search_button);

        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitySearchPosts.this, input.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        addItemsOnSpinner();
        addListenerOnButton();
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        spinnerType = (Spinner) findViewById(R.id.search_spinner_type);
        List<String> listType = new ArrayList<String>();
        listType.add("All");
        listType.add("Stories");
        listType.add("Tips");
        listType.add("News");
        listType.add("Wiki");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listType);
        typeAdapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        spinnerTag = (Spinner) findViewById(R.id.search_spinner_tag);
        List<String> listTag = new ArrayList<String>();
        listTag.add("All");
        listTag.add("Dog");
        listTag.add("Cat");
        listTag.add("Pig");
        listTag.add("Rabbit");
        listTag.add("Other");
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTag);
        tagAdapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTag.setAdapter(tagAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinnerType = (Spinner) findViewById(R.id.search_spinner_type);
        spinnerTag = (Spinner) findViewById(R.id.search_spinner_tag);
        search = (Button) findViewById(R.id.search_button);

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(
                        ActivitySearchPosts.this,
                        "Spinner : "
                                + String.valueOf(spinnerType.getSelectedItem()),
                                Toast.LENGTH_SHORT).show();
                Toast.makeText(
                        ActivitySearchPosts.this,
                        "Spinner : "
                                + String.valueOf(spinnerTag.getSelectedItem()),
                                Toast.LENGTH_SHORT).show();
            }

        });
    }
}
