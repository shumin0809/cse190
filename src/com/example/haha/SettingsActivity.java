package com.example.haha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private static final String TAG = "SettingsActivity";

	private EditText userEmailView;
	private EditText nameView;
	private EditText zip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);

		userEmailView = (EditText) findViewById(R.id.settings_user_email);
		nameView = (EditText) findViewById(R.id.settings_username);
		zip = (EditText) findViewById(R.id.settings_zip);

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save:
			saveData();
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void saveData() {
		Toast.makeText(getApplicationContext(), "Information Saved",
				Toast.LENGTH_LONG).show();
		
        try {
            String newEmail = userEmailView.getText().toString();
            String newUserName = nameView.getText().toString();
            String newZip = zip.getText().toString();
            
            // save data here

        } catch (Exception exception) {
            Log.e("Error: ", exception.toString());
            displayError("Error");
        }
	}
	
    private void displayError(String errorDetail) {
        Toast.makeText(this, errorDetail, Toast.LENGTH_SHORT).show();
    }

}
