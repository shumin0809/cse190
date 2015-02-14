package com.cse190.petcafe;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

public final class NetworkHandler extends Application {
	private static final NetworkHandler instance = new NetworkHandler();
	private static final Petcafe_api api = new Petcafe_api();
	
	public static NetworkHandler getInstance() { return instance; }
	
	public NetworkHandler()
	{ }
	
	public void addUser(UserProfileInformation user) 
	{
		new AddUserTask().execute(user);
	}
	
	public UserProfileInformation getUser(UserProfileInformation user)
	{
		UserProfileInformation profile = null;
		try
		{
			profile = new GetUserTask().execute(user).get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(GlobalStrings.LOGTAG, e.toString());
		}
		return profile;
	}
	
	private class AddUserTask extends AsyncTask<Object, Void, String>
	{
		@Override
		protected String doInBackground(Object... params) {
        	UserProfileInformation profile = (UserProfileInformation)params[0];
        	try {
				JSONArray ja = api.getUser(profile);
				
				if (ja.isNull(0))
				{
	        		api.addUser(profile);
	        		Log.i(GlobalStrings.LOGTAG, "Added User to Server");
				}
				Log.i(GlobalStrings.LOGTAG, "We have the user now!");
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
			}
			return "It worked!";			
		}
		
        @Override 
        protected void onPostExecute(String result) 
        {
            Log.d("MyAsyncTask", "Received result: " + result);
        }
	}
	
	private class GetUserTask extends AsyncTask<Object, Void, UserProfileInformation>
	{

		@Override
		protected UserProfileInformation doInBackground(Object... params) {
        	UserProfileInformation profile = (UserProfileInformation)params[0];
        	UserProfileInformation result = null;
        	
        	try {
        		JSONArray ja = api.getUser(profile);
        		
        		if (!ja.isNull(0))
        		{
        			JSONObject jo = ja.getJSONObject(0);
        			
        			result = new UserProfileInformation(jo.getString("fb_id"), jo.getString("name"), jo.getString("first_lang"), jo.getString("second_lang"), jo.getDouble("latitude"), jo.getDouble("longitude"), jo.getString("status"));
        		}
        	}
        	catch (JSONException e)
        	{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
        	}
        	
			return result;
		}
		
		@Override
		protected void onPostExecute(UserProfileInformation result)
		{
			
		}
	}
}
