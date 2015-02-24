package com.example.haha;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FindFriendsActivity extends Activity {

	private static final String TAG = "FindFriendsActivity";

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // in
																		// Meters

	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
																	// Milliseconds

	// Send My Location
	private Location location;
	private LocationManager locationManager;

	// Find Friends
	private ListView friendsList;
	private FriendsAdapter friendsAdapter;
	private ArrayList<Friend> friends = new ArrayList<Friend>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_find_friends);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());

		// send current location of current user
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		sendMyLocation("facebookUID", location.getLongitude(),
				location.getLatitude());

		// display friends that within 5 miles distance
		// friends = getFriendsFromServer(); => we will call from server
		// currently, we hard coding the friends list
		Friend friend = new Friend(R.drawable.ic_launcher, "dude", "5 miles");
		for (int i = 0; i < 4; i++)
			friends.add(friend);

		showFriends(friends);

	}

	/******** call this method to send current user location **********/
	public void sendMyLocation(String facebookUID, double longitude,
			double latitude) {

		// send(facebookUID,longitude, latitude); => send to server

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

	/****************** Display nearly friends ********************/

	private class FriendsAdapter extends ArrayAdapter<Friend> {
		public FriendsAdapter(Context context, List<Friend> friends) {
			super(context, android.R.layout.simple_list_item_1, friends);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i("FriendsAdapter", "getView");

			Friend friend = getItem(position);

			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.friend_detail, parent, false);
			}

			ImageView avatar = (ImageView) convertView
					.findViewById(R.id.profile_picture);
			TextView username = (TextView) convertView
					.findViewById(R.id.user_name);
			TextView distance = (TextView) convertView
					.findViewById(R.id.distance);

			avatar.setImageResource(friend.getProfilePicture());
			username.setText(friend.getUserName());
			distance.setText(friend.getDistance());

			return convertView;
		}

	}

	private class Friend {

		private int profile_picture;
		private String username;
		private String distance;

		public Friend(int profile_picture, String username, String distance) {
			this.profile_picture = profile_picture;
			this.username = username;
			this.distance = distance;
		}

		public int getProfilePicture() {
			return this.profile_picture;
		}

		public String getUserName() {
			return this.username;
		}

		public String getDistance() {
			return this.distance;
		}
	}

	public void showFriends(ArrayList<Friend> friends) {
		friendsList = (ListView) findViewById(R.id.friends_list);
		friendsAdapter = new FriendsAdapter(this, friends);
		friendsList.setAdapter(friendsAdapter);

		friendsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO future
			}
		});
	}

	// test sending location

	// private Button retrieveLocationButton;

	// protected void showCurrentLocation() {
	//
	// // // testing current user location method, could be removed
	// // retrieveLocationButton = (Button)
	// findViewById(R.id.retrieve_location_button);
	// // retrieveLocationButton.setOnClickListener(new OnClickListener() {
	// // @Override
	// // public void onClick(View v) {
	// // showCurrentLocation();
	// // }
	// // });
	//
	// Location location = locationManager
	// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	//
	// if (location != null) {
	//
	// String message = String.format(
	//
	// "Current Location \n Longitude: %1$s \n Latitude: %2$s",
	//
	// location.getLongitude(), location.getLatitude()
	//
	// );
	//
	// Toast.makeText(FindFriendsActivity.this, message,
	//
	// Toast.LENGTH_LONG).show();
	//
	// }
	//
	// }

}
