package com.example.haha;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindFriendsActivity extends Activity {

	private static final String TAG = "SettingsActivity";

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
																		// Meters

	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
																	// Milliseconds

	protected LocationManager locationManager;

	protected Button retrieveLocationButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_find_friends);

		retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
				new MyLocationListener()
				);

		// testing method, could be removed
		retrieveLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCurrentLocation();
			}

		});

	}
	
	/******** call this method to send current user location  **********/
	public void sendMyLocation() {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		// int user_id = current_user.getId();
		
		// send(location.getLongitude(), location.getLatitude(), user_id);
	}

	protected void showCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {

			String message = String.format(

			"Current Location \n Longitude: %1$s \n Latitude: %2$s",

			location.getLongitude(), location.getLatitude()

			);

			Toast.makeText(FindFriendsActivity.this, message,

			Toast.LENGTH_LONG).show();

		}

	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {

			String message = String.format(

			"New Location \n Longitude: %1$s \n Latitude: %2$s",

			location.getLongitude(), location.getLatitude()

			);

			Toast.makeText(FindFriendsActivity.this, message, Toast.LENGTH_LONG)
					.show();

		}

		public void onStatusChanged(String s, int i, Bundle b) {

			Toast.makeText(FindFriendsActivity.this, "Provider status changed",

			Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {

			Toast.makeText(FindFriendsActivity.this,

			"Provider disabled by the user. GPS turned off",

			Toast.LENGTH_LONG).show();

		}

		public void onProviderEnabled(String s) {

			Toast.makeText(FindFriendsActivity.this,

			"Provider enabled by the user. GPS turned on",

			Toast.LENGTH_LONG).show();

		}

	}

}
