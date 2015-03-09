package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smackx.xdata.FormField.Option;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.FriendInformation;
import com.cse190.petcafe.GlobalStrings;
import com.cse190.petcafe.NetworkHandler;
import com.cse190.petcafe.R;
import com.cse190.petcafe.UserProfileInformation;
import com.cse190.petcafe.ui.ActivityBase;
import com.qb.gson.Gson;
import com.qb.gson.reflect.TypeToken;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.model.QBUser;

public class ActivityMyFriends extends ActivityBase {

	private ArrayList<UserProfileInformation> friends;
	private List<String> listValues;
	private ListView friendList;
	private String facebookUID;
	private FriendsAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_myfriends, content, true);

		facebookUID = getSharedPreferences(GlobalStrings.PREFNAME, 0)
				.getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, "");

		friendList = (ListView) findViewById(R.id.friendslist);
		Gson gson = new Gson();
		String json = getSharedPreferences(GlobalStrings.PREFNAME, 0)
				.getString(GlobalStrings.FRIENDS_LIST_CACHE_KEY, "");
		friends = gson.fromJson(json,
				new TypeToken<ArrayList<UserProfileInformation>>() {
				}.getType());

		// listValues = new ArrayList<String>();
		// //
		// for (UserProfileInformation user : friends) {
		// listValues.add(user.getUserName());
		// }
		// Log.d("testing friends", listValues.toString());

		/*
		 * listValues.add("Dong Sung Chang"); listValues.add("David Nguyen");
		 * listValues.add("Michael Chang");
		 */

		// initiate the listadapter
		myAdapter = new FriendsAdapter(this, friends);

		// assign the list adapter
		friendList.setAdapter(myAdapter);

		friendList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// create a new chat dialog;;;;;
				// this is hardcode. Need to change this later ====
				// String fbuid = "";
				// if (position == 0)
				// fbuid = "1542339792686537";
				// else if (position == 1)
				// fbuid = "10202613710098479";
				// else
				// fbuid = "10153670628264152";
				/*
				 * Toast.makeText(getApplicationContext(), "Friend: " +
				 * listValues.get(position).toString(), Toast.LENGTH_LONG)
				 * .show();
				 */

				AlertDialog.Builder dialog = new AlertDialog.Builder(
						ActivityMyFriends.this);
				String[] profOptions = { "Profile", "Chat" };
				final ArrayAdapter<String> dialogAdapter = new ArrayAdapter<String>(
						ActivityMyFriends.this,
						android.R.layout.simple_list_item_1, profOptions);
				dialog.setTitle("Choose an option");
				dialog.setNeutralButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.setNegativeButton("Delete Friend",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String o_id = ((UserProfileInformation) friendList
										.getItemAtPosition(position))
										.getFacebookUID();
								FriendInformation friend = new FriendInformation(
										o_id, null, null);
								FriendInformation me = new FriendInformation(
										facebookUID, null, null);
								NetworkHandler.getInstance().deleteFriend(me,
										friend);
								Toast.makeText(
										getApplicationContext(),
										"Successfully delete "
												+ ((UserProfileInformation) friendList
														.getItemAtPosition(position))
														.getUserName(),
										Toast.LENGTH_LONG).show();
								friends.remove(position);
								myAdapter.notifyDataSetChanged();
							}
						});
				dialog.setAdapter(dialogAdapter,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									Intent intent = new Intent(
											ActivityMyFriends.this,
											ActivityProfile.class);
									intent.putExtra("username",
											listValues.get(position));
									intent.putExtra("fbuid",
											friends.get(position)
													.getFacebookUID());
									startActivity(intent);
									overridePendingTransition(R.anim.fade_in,
											R.anim.fade_out);
								} else if (which == 1) {
									String fbuid = friends.get(position)
											.getFacebookUID();

									ApplicationSingleton data = (ApplicationSingleton) getApplication();
									QBUser currUser = data.getCurrentUser();
									QBUser selectedUser = data.getFriendUsers()
											.get(fbuid);

									ArrayList<QBUser> newDialogUsers = new ArrayList<QBUser>();
									newDialogUsers.add(currUser);
									newDialogUsers.add(selectedUser);

									((ApplicationSingleton) getApplication())
											.addDialogsUsers(newDialogUsers);

									QBDialog newDialog = new QBDialog();
									newDialog
											.setName(usersListToChatName(newDialogUsers));
									newDialog.setType(QBDialogType.PRIVATE);
									newDialog
											.setOccupantsIds(getUserIds(newDialogUsers));

									QBChatService
											.getInstance()
											.getPrivateChatManager()
											.createDialog(
													selectedUser.getId(),
													new QBEntityCallbackImpl<QBDialog>() {
														@Override
														public void onSuccess(
																QBDialog dialog,
																Bundle args) {
															startNewChat(dialog);
														}

														@Override
														public void onError(
																List<String> errors) {
															AlertDialog.Builder dialog = new AlertDialog.Builder(
																	ActivityMyFriends.this);
															dialog.setMessage(
																	"dialog creation errors: "
																			+ errors)
																	.create()
																	.show();
														}
													});
								}
							}
						});
				dialog.show();
			}
		});
	}

	private void startNewChat(QBDialog dialog) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ActivityDialog.EXTRA_DIALOG, dialog);

		Intent i = new Intent(this.getBaseContext(), ActivityDialog.class);
		i.putExtras(bundle);
		startActivity(i);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private String usersListToChatName(List<QBUser> users) {
		String chatName = "";
		for (QBUser user : users) {
			String prefix = chatName.equals("") ? "" : ", ";
			chatName = chatName + prefix + user.getLogin();
		}
		return chatName;
	}

	public static ArrayList<Integer> getUserIds(List<QBUser> users) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (QBUser user : users) {
			ids.add(user.getId());
		}
		return ids;
	}

	private class FriendsAdapter extends ArrayAdapter<UserProfileInformation> {
		public FriendsAdapter(Context context,
				List<UserProfileInformation> friends) {
			super(context, android.R.layout.simple_list_item_1, friends);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i("MyFriendsAdapter", "getView");

			UserProfileInformation friend = getItem(position);

			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.listitem_myfriends, parent, false);
			}

			TextView username = (TextView) convertView
					.findViewById(R.id.listText);

			username.setText(friend.getUserName());

			return convertView;
		}

	}
}
