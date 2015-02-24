package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cse190.petcafe.R;
import com.cse190.petcafe.R.id;
import com.cse190.petcafe.R.layout;
import com.cse190.petcafe.R.menu;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.adapter.ChatDialogAdapter;
import com.cse190.petcafe.ui.ActivityBase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActivityChat extends ActivityBase {
	private ListView chatDialogsList;

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_activity_chat, content, true);
		
		Intent i = getIntent();
		String origin = i.getStringExtra("originClass");

		chatDialogsList = (ListView)findViewById(R.id.chatLists);
		
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

                        // build list view
                        //
                        buildListView(dialogs);
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityChat.this);
                        dialog.setMessage("get occupants errors: " + errors).create().show();
                    }

                });
			}
			
			@Override
			public void onError(List<String> errors)
			{
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityChat.this);
                dialog.setMessage("get dialogs errors: " + errors).create().show();
			}
		});
	}
	
    private void buildListView(List<QBDialog> dialogs){
        final ChatDialogAdapter adapter = new ChatDialogAdapter(dialogs, ActivityChat.this);
        chatDialogsList.setAdapter(adapter);

        // choose dialog
        //
        /*
        chatDialogsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBDialog selectedDialog = (QBDialog)adapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable(ChatActivity.EXTRA_DIALOG, (QBDialog)adapter.getItem(position));

                // group
                if(selectedDialog.getType().equals(QBDialogType.GROUP)){
                    bundle.putSerializable(ChatActivity.EXTRA_MODE, ChatActivity.Mode.GROUP);

                // private
                } else {
                    bundle.putSerializable(ChatActivity.EXTRA_MODE, ChatActivity.Mode.PRIVATE);
                }

                // Open chat activity
                //
                ChatActivity.start(DialogsActivity.this, bundle);
            }
        });*/
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chat, menu);
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
