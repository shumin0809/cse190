package com.cse190.petcafe;

import java.util.Arrays;
import java.util.List;

import org.jivesoftware.smack.SmackException;
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
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import android.app.Activity;
import android.app.AlertDialog;
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
    static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;
	
	private LoginButton loginButton;
	private UiLifecycleHelper uiHelper;
	private Boolean firstInstall;
	private String fbAccessToken;
    private QBChatService chatService;
	
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
                	                	
                	// quickblox stuff
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
                	
                	//final QBUser qbUser = new QBUser(GlobalStrings.USER_LOGIN, GlobalStrings.USER_PASSWORD);
                	//checkChatServiceStatus(qbUser);
                } else {
                	Log.i(GlobalStrings.LOGTAG, "You are now not logged in");
                }
            }
        });
    }
    
    @SuppressWarnings("rawtypes")
	private void checkChatServiceStatus(final QBUser user)
    {
    	if (!QBChatService.isInitialized())
    	{
    	    QBChatService.init(this);
    	}
        chatService = QBChatService.getInstance();
        
        chatService.login(user, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {

                // Start sending presences
                //
                try {
                    chatService.startAutoSendPresence(AUTO_PRESENCE_INTERVAL_IN_SECONDS);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
            }
        });
    }
    
    private void signUpUserToChat(GraphUser user)
    {
    	String username = user.getId();
    	Log.i(GlobalStrings.LOGTAG, "User information: pass " + "username " + username);
    	QBUser qbUser = null;
		try {
			qbUser = new QBUser(username, BaseService.getBaseService().getToken());
		} catch (BaseServiceException e) {
			e.printStackTrace();
			Log.e(GlobalStrings.LOGTAG, e.toString());
		}
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


