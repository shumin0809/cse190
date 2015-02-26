package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cse190.petcafe.R;

public class ActivityMyFriendsPosts extends ActivityBase {

    private List<String> listValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_myfriendsposts, content, true);

    }

}
