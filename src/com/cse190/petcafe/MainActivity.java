package com.cse190.petcafe;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cse190.petcafe.GlobalStrings;
import com.cse190.petcafe.ui.ActivityBlog;

public class MainActivity extends Activity {
	
	private LoginButton loginButton;
	private UiLifecycleHelper uiHelper;
	private Boolean firstInstall;
	private String fbAccessToken;
	private String fbAccessTokenSecret;
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	SharedPreferences localCache = getSharedPreferences(GlobalStrings.PREFNAME, 0);
    	firstInstall = false;
    	
    	if (localCache.getBoolean(GlobalStrings.FIRST_INSTALL_KEY, true))
    	{
    		SharedPreferences.Editor prefEditor = localCache.edit();
    		prefEditor.putBoolean(GlobalStrings.FIRST_INSTALL_KEY, false);
    		prefEditor.commit();
    		firstInstall = true;
    	}

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
    	
        loginButton = (LoginButton)findViewById(R.id.fbLoginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        loginButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                	Log.i(GlobalStrings.LOGTAG, "You are now logged in");
                	final GraphUser userToPass = user;
                	
                	SharedPreferences localCache = getSharedPreferences(GlobalStrings.PREFNAME, 0);
                	SharedPreferences.Editor prefEditor = localCache.edit();
                	
                	prefEditor.putString(GlobalStrings.USERNAME_CACHE_KEY, user.getName());
                	prefEditor.putString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, user.getId());
                	
                	prefEditor.commit();
                	
                	// invoke GET method to server to see if user already exists.
                	// If user not exist invoke POST method to server
                	
                	UserProfileInformation profile = new UserProfileInformation(user.getId(), user.getName(), "Korean", "Belgian", 0.0, 0.0, "FML");
                	UserProfileInformation myProfile = NetworkHandler.getInstance().getUser(profile);

                	if (myProfile == null)
                	{
                    	NetworkHandler.getInstance().addUser(profile);
                	}
                	                	
                    QBSettings.getInstance().fastConfigInit(GlobalStrings.APP_ID, GlobalStrings.AUTH_KEY, GlobalStrings.AUTH_SECRET);
                	QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
                		
                	    @Override
                	    public void onSuccess(QBSession session, Bundle params) {
                	        Log.i(GlobalStrings.LOGTAG, "Successfully logged into quickblox");
                        	checkQuickbloxAuth(userToPass);
                	    }
                	    
                	    @Override
                	    public void onError(List<String> errors) {
                	 
                	    }
                	});
                	
                } else {
                	Log.i(GlobalStrings.LOGTAG, "You are now not logged in");
                }
            }
        });
    }
    
    private void signUpUserToChat(GraphUser user)
    {
    	String passwordHash = String.valueOf(user.getId().hashCode());
    	String username = user.getId();
    	Log.i(GlobalStrings.LOGTAG, "User information: pass " + passwordHash + "username " + username);
    	final QBUser qbUser = new QBUser(username, passwordHash);
    	qbUser.setFacebookId(user.getId());
    	QBUsers.signUp(qbUser, new QBEntityCallbackImpl<QBUser>()
    	{
    		@Override
    		public void onSuccess(QBUser user, Bundle args)
    		{
    			signInUserToQuickblox();
    			Log.i(GlobalStrings.LOGTAG, "Successfully added user to quickblox");
    		}
    		
    		@Override
    		public void onError(List<String> errors)
    		{
    			Log.e(GlobalStrings.LOGTAG, "Something went horribly wrong!");
    		}
    	});
    }
    
    private void signInUserToQuickblox()
    {
    	Log.i(GlobalStrings.LOGTAG, fbAccessToken);
    	QBUsers.signInUsingSocialProvider(QBProvider.FACEBOOK, fbAccessToken, null, new QBEntityCallbackImpl<QBUser>()
    	{
    		@Override
    		public void onSuccess(QBUser user, Bundle args)
    		{
    			goToBlog();
    			Log.i(GlobalStrings.LOGTAG, "Successfully signed into quickblox");
    		}
    		
    		@Override
    		public void onError(List<String> errors)
    		{
    			Log.e(GlobalStrings.LOGTAG, "Something went horribly wrong!");
    		}
    	});
       }
    
    private void checkQuickbloxAuth(GraphUser user)
    {    	
    	if (firstInstall)
    	{
    		signUpUserToChat(user);
    	}
    	else
    	{
    		signInUserToQuickblox();
    	}
    }
    
    private void goToBlog()
    {
    	Intent i = null;
    	if (firstInstall)
    		i = new Intent(this, ActivityinitialSetup.class);	
    	else
    		i = new Intent(this, ActivityBlog.class);
    	startActivity(i);
    	finish();
    }
    
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            if (state.isOpened()) {
            	fbAccessToken = session.getAccessToken();
                Log.i(GlobalStrings.LOGTAG, "Facebook session opened");
            } else if (state.isClosed()) {
                Log.i(GlobalStrings.LOGTAG, "Facebook session closed");
            }
        }
    };
    
    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
        uiHelper.onResume();
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        uiHelper.onPause();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	uiHelper.onDestroy();
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


