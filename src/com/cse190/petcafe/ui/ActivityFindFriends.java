package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse190.petcafe.BlogPostInformation;
import com.cse190.petcafe.GlobalStrings;
import com.cse190.petcafe.MainActivity;
import com.cse190.petcafe.Petcafe_api;
import com.cse190.petcafe.R;
import com.cse190.petcafe.UserProfileInformation;

/*
 * 1. Find People around you
 * 2. Add people to your friend lists
 */

public class ActivityFindFriends extends ActivityBase {

	private static final String TAG = "ActivityFindFriends";

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // in
																		// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
																	// Milliseconds

	// Send My Location
	private LocationManager locationManager;

	// Find Friends
	private ListView friendsList;
	private FriendsAdapter friendsAdapter;
	// private ArrayList<UserProfileInformation> friends = new
	// ArrayList<UserProfileInformation>();
	private UserProfileInformation user = new UserProfileInformation();

	private final Petcafe_api api = new Petcafe_api();
	private JSONArray mNearPeople;
	private JSONArray mPerson;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);

		getLayoutInflater().inflate(R.layout.activity_findfriends, content,
				true);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());

		String facebookUID = getSharedPreferences(GlobalStrings.PREFNAME, 0)
				.getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, "");

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		user.setFacebookUID(facebookUID);
		user.setLatitude(location.getLatitude());
		user.setLongitude(location.getLongitude());
		new UpdateLocationTask().execute(user);
		new GetNearPeopleTask().execute(user);

		// // testing current user location method, could be removed
		// retrieveLocationButton = (Button)
		// findViewById(R.id.retrieve_location_button);
		// retrieveLocationButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// showCurrentLocation();
		// }
		// });
	}

	private class UpdateLocationTask extends AsyncTask<Object, Void, Void> {
		@Override
		protected Void doInBackground(Object... params) {
			UserProfileInformation user = (UserProfileInformation) params[0];
			try {
				api.modifyUser(user);
				Log.d(TAG, "current user location: "
						+ api.getUser(user).toString());
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return null;
		}
	}

	private class GetNearPeopleTask extends
			AsyncTask<Object, Void, ArrayList<UserProfileInformation>> {
		@Override
		protected ArrayList<UserProfileInformation> doInBackground(
				Object... params) {
			UserProfileInformation user = (UserProfileInformation) params[0];
			ArrayList<UserProfileInformation> friends = new ArrayList<UserProfileInformation>();
			try {
				mNearPeople = api.getNearPeople(user);
				
				Log.d(TAG, "user is: "
						+ api.getUser(user).toString());
				Log.d(TAG, "near friends are"+ mNearPeople.toString());
				for (int i = 0; i < mNearPeople.length(); i++) {
					UserProfileInformation friend = new UserProfileInformation();
					friend.setUserName(mNearPeople.getJSONObject(i).getString(
							"name"));
					friend.setStatus(mNearPeople.getJSONObject(i).getString(
							"status"));
					friends.add(friend);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return friends;
		}

		@Override
		protected void onPostExecute(ArrayList<UserProfileInformation> friends) {
			showFriends(user, friends);
		}
	}

	/******** call this method to send current user location **********/
	// click on a friend in friends list
	public void showFriends(final UserProfileInformation user,
			ArrayList<UserProfileInformation> friends) {
		friendsList = (ListView) findViewById(R.id.friends_list);
		friendsAdapter = new FriendsAdapter(this, friends);
		friendsList.setAdapter(friendsAdapter);

		friendsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO future
				UserProfileInformation friend = (UserProfileInformation) friendsList
						.getItemAtPosition(position);
				openAlert(user, friend);
			}
		});
	}

	public void addFriend(int facebookUID) {
		// send facebookUID request
		// something like
		// sendFriendRequest(facebookUID);
	}

	private void openAlert(UserProfileInformation user,
			UserProfileInformation friend) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ActivityFindFriends.this);

		alertDialogBuilder.setTitle("Add Friend?");

		alertDialogBuilder.setMessage("Do you want to add "
				+ friend.getUserName() + " ?");

		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// addFriend(facebookUID);
					}

				});

		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}

				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {

			String message = String.format(

			"New Location \n Longitude: %1$s \n Latitude: %2$s",

			location.getLongitude(), location.getLatitude()

			);

			Toast.makeText(ActivityFindFriends.this, message, Toast.LENGTH_LONG)
					.show();

		}

		public void onStatusChanged(String s, int i, Bundle b) {

			Toast.makeText(ActivityFindFriends.this, "Provider status changed",

			Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {

			Toast.makeText(ActivityFindFriends.this,

			"Provider disabled by the user. GPS turned off",

			Toast.LENGTH_LONG).show();

		}

		public void onProviderEnabled(String s) {

			Toast.makeText(ActivityFindFriends.this,

			"Provider enabled by the user. GPS turned on",

			Toast.LENGTH_LONG).show();

		}
	}

	/****************** Display nearly friends ********************/

	private class FriendsAdapter extends ArrayAdapter<UserProfileInformation> {
		public FriendsAdapter(Context context,
				List<UserProfileInformation> friends) {
			super(context, android.R.layout.simple_list_item_1, friends);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i("FriendsAdapter", "getView");

			UserProfileInformation friend = getItem(position);

			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.list_item_friends, parent, false);
			}

			ImageView avatar = (ImageView) convertView
					.findViewById(R.id.profile_picture);
			TextView username = (TextView) convertView
					.findViewById(R.id.user_name);
			TextView distance = (TextView) convertView
					.findViewById(R.id.distance);

			avatar.setImageResource(R.drawable.ic_launcher);
			username.setText(friend.getUserName());
			distance.setText(friend.getStatus());

			return convertView;
		}

	}

	// test sending location, could comment out below

	private Button retrieveLocationButton;

	protected void showCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {

			String message = String.format(

			"Current Location \n Longitude: %1$s \n Latitude: %2$s",

			location.getLongitude(), location.getLatitude()

			);

			Toast.makeText(ActivityFindFriends.this, message,

			Toast.LENGTH_LONG).show();

		}

	}
}
