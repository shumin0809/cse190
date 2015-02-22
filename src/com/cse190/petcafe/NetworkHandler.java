package com.cse190.petcafe;

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
	
	public NetworkHandler() { }
	
	/**
	 ************** *************** *************** **************
	 ************** *************** *************** **************
	 ************** Public Network Hanlding Methods **************
	 ************** *************** *************** **************
	 ************** *************** *************** **************
	 * @param user
	 */
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
	
	public void modifyUser(UserProfileInformation user)
	{
		new ModifyUserTask().execute(user);
	}
	
	public void deleteUser(UserProfileInformation user)
	{
		new DeleteUserTask().execute(user);
	}
	
	public void addFriend(FriendInformation myself, FriendInformation other)
	{
		new AddFriendTask().execute(myself, other);
	}
	
	public void deleteFriend(FriendInformation myself, FriendInformation other)
	{
		new DeleteFriendTask().execute(myself, other);
	}
	
	public void addPet(PetInformation pet)
	{
		new AddPetTask().execute(pet);
	}
	
	public void deletePet(PetInformation pet)
	{
		new DeletePetTask().execute(pet);
	}
	
	public PetInformation getPet(PetInformation pet)
	{
		PetInformation result = null;
		try
		{
			result = new GetPetTask().execute(pet).get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(GlobalStrings.LOGTAG, e.toString());
		}
		return result; 
	}
	
	/**
	 ************** ************** ************** **************
	 ************** ************** ************** **************
	 ************** ASYNC TASK CLASS DECLARATIONS **************
	 ************** ************** ************** ************** 
	 ************** ************** ************** **************
	 * @author michaelchang
	 */
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
	}
	
	private class ModifyUserTask extends AsyncTask<Object, Void, String>
	{
		@Override
		protected String doInBackground(Object... params)
		{
			UserProfileInformation profile = (UserProfileInformation)params[0];
			
			try
			{
				api.modifyUser(profile);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
			}
			
			return "It worked!";
		}
	}
	
	private class DeleteUserTask extends AsyncTask<Object, Void, String>
	{
		@Override
		protected String doInBackground(Object... params)
		{
			UserProfileInformation profile = (UserProfileInformation)params[0];
			try
			{
				api.deleteUser(profile);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
			}
			
			return "It worked!";
		}
	}

	private class AddFriendTask extends AsyncTask<Object, Void, String>
	{
		@Override
		protected String doInBackground(Object... params)
		{
			FriendInformation myself = (FriendInformation)params[0];
			FriendInformation other = (FriendInformation)params[1];
			
			try{
				api.addFriend(myself, other, false);
				
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
			}
			
			return "It worked!";
		}
	}

	private class DeleteFriendTask extends AsyncTask<Object, Void, String>
	{
		@Override
		protected String doInBackground(Object... params)
		{
			FriendInformation myself = (FriendInformation)params[0];
			FriendInformation other = (FriendInformation)params[1];
			
			try
			{
				api.deleteFriend(myself, other);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
			}
			return "It worked";
		}
	}

	private class AddPetTask extends AsyncTask<Object, Void, String>
	{
		@Override
		protected String doInBackground(Object... params)
		{
			PetInformation pet = (PetInformation)params[0];
			
			try
			{
				api.addPet(pet);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
			}
			
			return "It works!";
		}
	}

	private class DeletePetTask extends AsyncTask<Object, Void, String>
	{
		@Override
		protected String doInBackground(Object... params)
		{
			PetInformation pet = (PetInformation)params[0];
			
			try
			{
				api.deletePet(pet);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());				
			}
			
			return "It worked!";
		}
	}

	private class GetPetTask extends AsyncTask<Object, Void, PetInformation>
	{
		@Override
		protected PetInformation doInBackground(Object... params)
		{
			PetInformation pet = (PetInformation)params[0];
			PetInformation result = null;
			
			try
			{
				JSONArray ja = api.getPet(pet);
				
				if (!ja.isNull(0))
				{
					JSONObject jo = ja.getJSONObject(0);
					result = new PetInformation(jo.getString("name"), jo.getString("species"), jo.getString("breed"), jo.getString("gender"), jo.getInt("age"), jo.getString("description"), jo.getString("fb_id"));
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				Log.e(GlobalStrings.LOGTAG, e.toString());
			}
			
			return result;
		}
	}
}