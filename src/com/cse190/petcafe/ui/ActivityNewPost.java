package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cse190.petcafe.R;

public class ActivityNewPost extends ActivityBase {

    private Spinner spinnerType;
    private Spinner spinnerTag;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_newpost, content, true);

        addItemsOnSpinner();
        addListenerOnButton();

    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        spinnerType = (Spinner) findViewById(R.id.spinner_type);
        List<String> listType = new ArrayList<String>();
        listType.add("Stories");
        listType.add("Tips");
        listType.add("News");
        listType.add("Wiki");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listType);
        typeAdapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        spinnerTag = (Spinner) findViewById(R.id.spinner_tag);
        List<String> listTag = new ArrayList<String>();
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

        spinnerType = (Spinner) findViewById(R.id.spinner_type);
        spinnerTag = (Spinner) findViewById(R.id.spinner_tag);
        btnSubmit = (Button) findViewById(R.id.post_button);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(
                        ActivityNewPost.this,
                        "Spinner : "
                                + String.valueOf(spinnerType.getSelectedItem()),
                                Toast.LENGTH_SHORT).show();
                Toast.makeText(
                        ActivityNewPost.this,
                        "Spinner : "
                                + String.valueOf(spinnerTag.getSelectedItem()),
                                Toast.LENGTH_SHORT).show();
            }

        });
    }

}
