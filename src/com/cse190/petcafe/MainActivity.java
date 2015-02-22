package com.cse190.petcafe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
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
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
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
import com.cse190.petcafe.ui.ActivityChat;

public class MainActivity extends Activity {
    static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;
	
	private LoginButton loginButton;
	private UiLifecycleHelper uiHelper;
	private Boolean firstInstall;
	private String fbAccessToken;
    private QBChatService chatService;
    private GraphUser facebookUser;
    private Session fbSession;
    
    private ArrayList<String> fbFriendUIDs;
    private HashMap<String, String> fbFriendNamesWUIDs;
	
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
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
        
        QBSettings.getInstance().fastConfigInit(GlobalStrings.APP_ID, GlobalStrings.AUTH_KEY, GlobalStrings.AUTH_SECRET);
    	
        QBChatService.setDebugEnabled(true);
    	if (!QBChatService.isInitialized())
    	{
    	    QBChatService.init(this);
    	}
        chatService = QBChatService.getInstance();
        
        loginButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                	Log.i(GlobalStrings.LOGTAG, "You are now logged in");
//                	final GraphUser userToPass = user;
                	facebookUser = user;
                	
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
                } else {
                	Log.i(GlobalStrings.LOGTAG, "You are now not logged in");
                }
            }
        });
    }
    
    private void createQBSession()
    {	
    	// quickblox stuff             
    	QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() 
		{
    	    @Override
    	    public void onSuccess(QBSession session, Bundle params) {
    	    	
    	        Log.i(GlobalStrings.LOGTAG, "Successfully logged into quickblox");
    	        
    	        if (firstInstall)
    	        	signUpUserToChat();
    	        else
    	        	signInUserToQuickblox();
    	    }
    	    
    	    @Override
    	    public void onError(List<String> errors) {
    	    	Log.i("This is", "Stupid");
    	    }
		});
    	/*
        QBAuth.createSessionUsingSocialProvider(QBProvider.FACEBOOK, fbAccessToken, null, new QBEntityCallbackImpl<QBSession>() {
    	    @Override
    	    public void onSuccess(QBSession session, Bundle params) {
    	    	
    	        Log.i(GlobalStrings.LOGTAG, "Successfully logged into quickblox");
    	        
            	signInUserToQuickblox();
    	    }
    	    
    	    @Override
    	    public void onError(List<String> errors) {
    	    	Log.i("This is", "Stupid");
    	    }
    	});*/
    }
    
	private void checkChatServiceStatus(final QBUser user)
    {   
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
    
    private void signUpUserToChat()
    {
    	String username = facebookUser.getId();
    	Log.i(GlobalStrings.LOGTAG, "User information: pass " + "username " + username);
    	QBUser qbUser = null;
		qbUser = new QBUser(username, GlobalStrings.USER_PASSWORD);
		qbUser.setFullName(facebookUser.getName());
		
    	qbUser.setFacebookId(facebookUser.getId());
    	QBUsers.signUp(qbUser, new QBEntityCallbackImpl<QBUser>()
    	{
    		@Override
    		public void onSuccess(QBUser user, Bundle args)
    		{
    			Log.i(GlobalStrings.LOGTAG, "Successfully added user to quickblox");
    			signInUserToQuickblox();
    	    	/*((ApplicationSingleton)getApplication()).setCurrentUser(user);
    	        QBUser qbUser = new QBUser();
    	        
    	        qbUser.setId(user.getId());
    	        qbUser.setLogin(facebookUser.getId());
    	        qbUser.setPassword(GlobalStrings.USER_PASSWORD);
    	        
            	checkChatServiceStatus(qbUser);
            	
    	    	getAllFriendUsers();
    			goToBlog();8*/
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
/*    	    	if (firstInstall)
    	    	{
    	    		signUpUserToChat();
    	    	}
    	    	else
    	    	{*/
	    	    	((ApplicationSingleton)getApplication()).setCurrentUser(user);
	    	        QBUser qbUser = new QBUser();
	    	        
	    	        qbUser.setId(user.getId());
	    	        qbUser.setLogin(facebookUser.getId());
	    	        qbUser.setPassword(GlobalStrings.USER_PASSWORD);
	    	        
	            	checkChatServiceStatus(qbUser);
	            	
	    	    	getAllFriendUsers();
	    			goToBlog();
    	    	//}
    			
    			Log.i(GlobalStrings.LOGTAG, "Successfully signed into quickblox");
    		}
    		
    		@Override
    		public void onError(List<String> errors)
    		{
    			Log.e(GlobalStrings.LOGTAG, "Something went horribly wrong!");
    		}
    	});
    }
    
    private void getAllFriendUsers()
    {
    	new Request(
			fbSession,
			"/me/friends",
			null,
			HttpMethod.GET,
			new Request.Callback() {
				
				@Override
				public void onCompleted(Response response) {
					try {
						fbFriendUIDs = new ArrayList<String>();
						fbFriendNamesWUIDs = new HashMap<String, String>();
						JSONObject resp = new JSONObject(response.getRawResponse());
						JSONArray arr = resp.getJSONArray("data");
						for (int i = 0; i < arr.length(); ++i)
						{
							JSONObject obj = arr.getJSONObject(i);
							
							String uid = obj.getString("id");
							String name = obj.getString("name");
							
							fbFriendUIDs.add(uid);
							fbFriendNamesWUIDs.put(uid, name);
							Log.i(GlobalStrings.LOGTAG, uid);
						}
						
						getAllUsersFromChatApi();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
    	).executeAsync();
    }
    
    private void getAllUsersFromChatApi()
    {
    	List<QBUser> chatFriendsList = null;
    	
		try {
			chatFriendsList = new RetrieveFacebookFriends().execute("").get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        if (chatFriendsList != null)
        	((ApplicationSingleton)getApplication()).setFriendUsers(chatFriendsList);
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
            	fbSession = session;
            	
            	createQBSession();
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
    
    private class RetrieveFacebookFriends extends AsyncTask<String, Void, List<QBUser>>
    {

		@Override
		protected List<QBUser> doInBackground(String... params) {
	        QBPagedRequestBuilder requestBuilder = null;
	        Bundle _params = new Bundle();
	        
	        List<QBUser> chatFriendsList = null;

	        if (!fbFriendUIDs.isEmpty())
	        {
				try {
					chatFriendsList = QBUsers.getUsersByFacebookId(fbFriendUIDs, requestBuilder, _params);
				} catch (QBResponseException e) {
					e.printStackTrace();
				}
	        }
			
			return chatFriendsList;
		}
    }
}

