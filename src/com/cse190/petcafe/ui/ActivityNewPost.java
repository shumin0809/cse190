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

	private Spinner spinner;
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

		spinner = (Spinner) findViewById(R.id.spinner_type);
		List<String> list = new ArrayList<String>();
		list.add("Stories");
		list.add("Tips");
		list.add("News");
		list.add("Wiki");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

	// get the selected dropdown list value
	public void addListenerOnButton() {

		spinner = (Spinner) findViewById(R.id.spinner_type);
		btnSubmit = (Button) findViewById(R.id.post_button);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(
						ActivityNewPost.this,
						"Spinner : "
								+ String.valueOf(spinner.getSelectedItem()),
						Toast.LENGTH_SHORT).show();
			}

		});
	}

}
