package com.cse190.petcafe;

import java.io.IOException;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.qb.gson.Gson;
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
import com.quickblox.messages.QBMessages;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.cse190.petcafe.GlobalStrings;
import com.cse190.petcafe.ui.ActivityBlog;
import com.cse190.petcafe.ui.ActivityChat;

public class MainActivity extends Activity {
    static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private LoginButton loginButton;
	private UiLifecycleHelper uiHelper;
	private Boolean firstInstall;
	private String fbAccessToken;
    private QBChatService chatService;
    private GraphUser facebookUser;
    private Session fbSession;
    
    private ArrayList<String> friendUIDs;
    private HashMap<String, String> friendWUIDs;
    
    private ProgressDialog progressBar;
    private NetworkHandler networkHandler;
    private GoogleCloudMessaging gcm;
    private String gcmRegId;
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = new ProgressDialog(this);
        networkHandler = NetworkHandler.getInstance();

    	SharedPreferences localCache = getSharedPreferences(GlobalStrings.PREFNAME, 0);
    	firstInstall = false;
    	instance = this;
    	/*
    	if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != ConnectionResult.SUCCESS)
    	{
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("You need to have Google Play Services installed to use this application!").create().show();
            dialog.setOnCancelListener(new OnCancelListener(){
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
            });
            return;
    	}*/
    	
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
                	
                	UserProfileInformation profile = new UserProfileInformation(user.getId(), user.getName(), "", "", 0.0, 0.0, "");
                	UserProfileInformation myProfile = networkHandler.getUser(profile);

                	if (myProfile == null)
                	{
                		networkHandler.addUser(profile);
                	}
                } else {
                	Log.i(GlobalStrings.LOGTAG, "You are now not logged in");
                }
            }
        });
    }
    
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                    	gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    gcmRegId = gcm.register(GlobalStrings.PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + gcmRegId;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    Handler h = new Handler(getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            subscribeToPushNotifications(gcmRegId);
                        }
                    });

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(gcmRegId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(GlobalStrings.LOGTAG, msg + "\n");
            }
        }.execute(null, null, null);
    }
    
    private void subscribeToPushNotifications(String regId) {
        //Create push token with  Registration Id for Android
        //
        Log.d(GlobalStrings.LOGTAG, "subscribing...");

        String deviceId;

        final TelephonyManager mTelephony = (TelephonyManager) getSystemService(
                Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId(); //*** use for mobiles
        } else {
            deviceId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID); //*** use for tablets
        }

        QBMessages.subscribeToPushNotificationsTask(regId, deviceId, QBEnvironment.DEVELOPMENT, new QBEntityCallbackImpl<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> qbSubscriptions, Bundle bundle) {
                Log.d(GlobalStrings.LOGTAG, "subscribed");
            }

            @Override
            public void onError(List<String> strings) {

            }
        });
    }
    
    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = ((ApplicationSingleton) getApplication()).getAppVersion();
        Log.i(GlobalStrings.LOGTAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    private String getRegistrationId() {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(GlobalStrings.LOGTAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = ((ApplicationSingleton) getApplication()).getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(GlobalStrings.LOGTAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    
    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
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
    	
		boolean isUserOnQuickblox = false;
		try {
			isUserOnQuickblox = new ValidateUserOnQuickblox().execute(username).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!isUserOnQuickblox)
		{
	    	QBUsers.signUp(qbUser, new QBEntityCallbackImpl<QBUser>()
	    	{
	    		@Override
	    		public void onSuccess(QBUser user, Bundle args)
	    		{
	    			Log.i(GlobalStrings.LOGTAG, "Successfully added user to quickblox");
	    			signInUserToQuickblox();
	    		}
	    		
	    		@Override
	    		public void onError(List<String> errors)
	    		{
	    			Log.e(GlobalStrings.LOGTAG, "Something went horribly wrong!");
	    		}
	    	});
		}
		else
		{
			signInUserToQuickblox();
		}
    }
    
    private void signInUserToQuickblox()
    {
    	Log.i(GlobalStrings.LOGTAG, fbAccessToken);
    	QBUsers.signInUsingSocialProvider(QBProvider.FACEBOOK, fbAccessToken, null, new QBEntityCallbackImpl<QBUser>()
    	{
    		@Override
    		public void onSuccess(QBUser user, Bundle args)
    		{
	    	    ((ApplicationSingleton)getApplication()).setCurrentUser(user);
	    	    QBUser qbUser = new QBUser();
	    	        
	    	    qbUser.setId(user.getId());
	    	    qbUser.setLogin(facebookUser.getId());
	    	    qbUser.setPassword(GlobalStrings.USER_PASSWORD);
	    	        
	    	    checkChatServiceStatus(qbUser);
	            	
	    	    getAllFriendUsers();
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
						ArrayList<String> fbFriendUIDs = new ArrayList<String>();
						HashMap<String, String> fbFriendNamesWUIDs = new HashMap<String, String>();
						
						friendUIDs = new ArrayList<String>();
						friendWUIDs = new HashMap<String, String>();
						
						JSONObject resp = new JSONObject(response.getRawResponse());
						JSONArray arr = resp.getJSONArray("data");
						for (int i = 0; i < arr.length(); ++i)
						{
							JSONObject obj = arr.getJSONObject(i);
							
							String uid = obj.getString("id");
							String name = obj.getString("name");
							
							fbFriendUIDs.add(uid);
							fbFriendNamesWUIDs.put(uid, name);
							
							FriendInformation me = new FriendInformation(getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, ""), getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.USERNAME_CACHE_KEY, ""), "1");
							FriendInformation friend = new FriendInformation(uid,name,"1");
							
							networkHandler.addFriend(me, friend);
							Log.i(GlobalStrings.LOGTAG, uid);
						}
						
						UserProfileInformation user = new UserProfileInformation(getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, ""), getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.USERNAME_CACHE_KEY, ""), "", "", 0, 0, "");
						ArrayList<UserProfileInformation> friends = networkHandler.getFriends(user);
						
						for (UserProfileInformation fUser : friends)
						{
							friendUIDs.add(fUser.getFacebookUID());
							friendWUIDs.put(fUser.getFacebookUID(), fUser.getUserName());
						}
						
						SharedPreferences localCache = getSharedPreferences(GlobalStrings.PREFNAME, 0);
	                	SharedPreferences.Editor prefEditor = localCache.edit();
	                	
	                	Gson gson = new Gson();
	                	String json = gson.toJson(friends);
	                	prefEditor.putString(GlobalStrings.FRIENDS_LIST_CACHE_KEY, json);
	                	prefEditor.commit();
						
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
	        getAllChatDialogs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        if (chatFriendsList != null)
        	((ApplicationSingleton)getApplication()).setFriendUsers(chatFriendsList);
    }
    
    private void getAllChatDialogs()
    {
        QBRequestGetBuilder builder = new QBRequestGetBuilder();
        builder.setPagesLimit(100);
        
        QBChatService.getChatDialogs(null, builder, new QBEntityCallbackImpl<ArrayList<QBDialog>>()
        {
            @Override
            public void onSuccess(final ArrayList<QBDialog> dialogs, Bundle args)
            {
                List<Integer> userIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs)
                {
                    userIDs.addAll(dialog.getOccupants());
                }
                
                QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
                requestBuilder.setPage(1);
                requestBuilder.setPerPage(userIDs.size());
                //
                QBUsers.getUsersByIDs(userIDs, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle params) {

                        ((ApplicationSingleton)getApplication()).setDialogsUsers(users);
            			goToBlog();
                        // build list view
                        //
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("get occupants errors: " + errors).create().show();
                    }

                });
            }
            
            @Override
            public void onError(List<String> errors)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("get dialogs errors: " + errors).create().show();
            }
        });
    }

    
    private void goToBlog()
    {
    	Intent i = null;
    	if (firstInstall)
    		i = new Intent(this, ActivityinitialSetup.class);	
    	else
    		i = new Intent(this, ActivityBlog.class);
    	startActivity(i);
    	progressBar.dismiss();
    	finish();
    }
    
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            if (state.isOpened()) {
                progressBar.setMessage("Logging in...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setIndeterminate(true);
                progressBar.show();
                
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
    
    private class RetrieveFacebookFriends extends AsyncTask<String, Void, List<QBUser>>
    {
		@Override
		protected List<QBUser> doInBackground(String... params) {
	        QBPagedRequestBuilder requestBuilder = null;
	        Bundle _params = new Bundle();
	        
	        List<QBUser> chatFriendsList = null;

	        if (!friendUIDs.isEmpty())
	        {
				try {
					chatFriendsList = QBUsers.getUsersByFacebookId(friendUIDs, requestBuilder, _params);
				} catch (QBResponseException e) {
					e.printStackTrace();
				}
	        }
			return chatFriendsList;
		}
    }
    
    private class ValidateUserOnQuickblox extends AsyncTask<String, Void, Boolean>
    {

		@Override
		protected Boolean doInBackground(String... params) {
			String username = params[0];
			QBUser user = null;
			try {
				user = QBUsers.getUserByFacebookId(username);
			} catch (QBResponseException e) {
				e.printStackTrace();
			}
			if (user == null)
				return false;
			else
				return true;
		}
    }
    
    private static MainActivity instance;

	public static MainActivity getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public void retrieveMessage(String message) {
		
	}
}

