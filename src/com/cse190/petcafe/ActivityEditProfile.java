package com.cse190.petcafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ActivityEditProfile extends ActivityBase {

	private EditText name, email, phone, address, city, state, zip;
	private Button done;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_editprofile, content, true); 

		context = this.getApplicationContext();

		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		address = (EditText) findViewById(R.id.address);
		city = (EditText) findViewById(R.id.city);
		state = (EditText) findViewById(R.id.state);
		zip = (EditText) findViewById(R.id.zip);
		
        done = (Button) findViewById(R.id.button_done);
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, ActivityProfile.class);
                myIntent.putExtra("name", name.getText().toString());
                myIntent.putExtra("email", email.getText().toString());
                myIntent.putExtra("phone", phone.getText().toString());
                myIntent.putExtra("address", address.getText().toString());
                myIntent.putExtra("city", city.getText().toString());
                myIntent.putExtra("state", state.getText().toString());
                myIntent.putExtra("zip", zip.getText().toString());
                
                startActivity(myIntent);
            }
        });


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main1, menu);
		return true;
	}

}
