package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.R;
import com.cse190.petcafe.R.id;
import com.cse190.petcafe.R.layout;
import com.cse190.petcafe.R.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;
import com.cse190.petcafe.ChatManager;
import com.cse190.petcafe.GroupChatManagerImpl;
import com.cse190.petcafe.PrivateChatManagerImpl;
import com.cse190.petcafe.ChatAdapter;
import com.cse190.petcafe.UsersAdapter;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

public class ActivityDialog extends Activity {
    private static final String TAG = ActivityChat.class.getSimpleName();

    public static final String EXTRA_DIALOG = "dialog";
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    private EditText messageEditText;
    private ListView messagesContainer;
    private Button sendButton;
    private ProgressBar progressBar;

    private ChatManager chat;
    private ChatAdapter adapter;
    private QBDialog dialog;
    
    private ArrayList<QBChatMessage> history;
    
    private UsersAdapter usersAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actvitiy_dialog);
        initViews();
	}

	@Override
    public void onBackPressed() {
        try {
            chat.release();
        } catch (XMPPException e) {
            Log.e(TAG, "failed to release chat", e);
        }
        super.onBackPressed();
    }

    private void initViews() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageEditText = (EditText) findViewById(R.id.messageEdit);
        sendButton = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLabel);
        TextView companionLabel = (TextView) findViewById(R.id.companionLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();

        // Get chat dialog
        //
        dialog = (QBDialog)intent.getSerializableExtra(EXTRA_DIALOG);

        Integer opponentID = ((ApplicationSingleton)getApplication()).getOpponentIDForPrivateDialog(dialog);

        chat = new PrivateChatManagerImpl(this, opponentID);

        if (((ApplicationSingleton)getApplication()).getDialogsUsers().get(opponentID) != null)
        {
        	companionLabel.setText(((ApplicationSingleton)getApplication()).getDialogsUsers().get(opponentID).getLogin());
        	dialog.setType(QBDialogType.PRIVATE);
	        loadChatHistory();
        }
        else
        {
            //((ApplicationSingleton)getApplication()).addDialogsUsers(usersAdapter.getSelected());

        	QBDialog dialogToCreate = new QBDialog();
            dialogToCreate.setName("Fuck you");
            dialogToCreate.setType(QBDialogType.PRIVATE);
            dialogToCreate.setOccupantsIds(dialog.getOccupants());
            
            QBChatService.getInstance().getGroupChatManager().createDialog(dialogToCreate, new QBEntityCallbackImpl<QBDialog>() {
                @Override
                public void onSuccess(QBDialog dialog, Bundle args) {
                    // Load CHat history
                    loadChatHistory();
                }

                @Override
                public void onError(List<String> errors) {
                    //AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityDialog.class);
                    //dialog.setMessage("dialog creation errors: " + errors).create().show();
                }
            });
        }
        
        adapter = new ChatAdapter(ActivityDialog.this, new ArrayList<QBChatMessage>());
        messagesContainer.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                // Send chat message
                //
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(messageText);
                chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
                chatMessage.setDateSent(new Date().getTime()/1000);

                try {
                    chat.sendMessage(chatMessage);
                } catch (XMPPException e) {
                    Log.e(TAG, "failed to send a message", e);
                } catch (SmackException sme){
                    Log.e(TAG, "failed to send a message", sme);
                }

                messageEditText.setText("");

                showMessage(chatMessage);
            }
        });
    }

    private void loadChatHistory(){
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        customObjectRequestBuilder.sortDesc("date_sent");

        QBChatService chatService = QBChatService.getInstance();
        QBChatService.getDialogMessages(dialog, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                history = messages;

                adapter = new ChatAdapter(ActivityDialog.this, new ArrayList<QBChatMessage>());
                messagesContainer.setAdapter(adapter);

                for(int i=messages.size()-1; i>=0; --i) {
                    QBChatMessage msg = messages.get(i);
                    showMessage(msg);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityDialog.this);
                dialog.setMessage("load chat history errors: " + errors).create().show();
            }
        });
    }

    public void showMessage(QBChatMessage message) {
        adapter.add(message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                scrollDown();
            }
        });
    }

    private void scrollDown() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    public static enum Mode {PRIVATE, GROUP}
}
