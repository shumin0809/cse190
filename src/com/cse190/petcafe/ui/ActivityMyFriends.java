package com.cse190.petcafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cse190.petcafe.ApplicationSingleton;
import com.cse190.petcafe.R;
import com.cse190.petcafe.ui.ActivityBase;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.model.QBUser;

public class ActivityMyFriends extends ActivityBase {

    private List<String> listValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_myfriends, content, true);

        ListView friendList = (ListView) findViewById(R.id.friendslist);

        listValues = new ArrayList<String>();
        listValues.add("Dong Sung Chang");
        listValues.add("David Nguyen");
        listValues.add("Michael Chang");

        // initiate the listadapter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                R.layout.listitem_myfriends, R.id.listText, listValues);

        // assign the list adapter
        friendList.setAdapter(myAdapter);

        friendList.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // create a new chat dialog;;;;;
                // this is hardcode. Need to change this later ====
                String fbuid = "";
                if (position == 0)
                    fbuid = "1542339792686537";
                else if (position == 1)
                    fbuid = "10202613710098479";
                else
                    fbuid = "10153670628264152";

                ApplicationSingleton data = (ApplicationSingleton)getApplication();
                QBUser currUser = data.getCurrentUser();
                QBUser selectedUser = data.getFriendUsers().get(fbuid);

                ArrayList<QBUser> newDialogUsers = new ArrayList<QBUser>();
                newDialogUsers.add(currUser);
                newDialogUsers.add(selectedUser);

                ((ApplicationSingleton)getApplication()).addDialogsUsers(newDialogUsers);

                QBDialog newDialog = new QBDialog();
                newDialog.setName(usersListToChatName(newDialogUsers));
                newDialog.setType(QBDialogType.PRIVATE);
                newDialog.setOccupantsIds(getUserIds(newDialogUsers));

                QBChatService.getInstance().getPrivateChatManager().createDialog(selectedUser.getId(), new QBEntityCallbackImpl<QBDialog>() {
                    @Override
                    public void onSuccess(QBDialog dialog, Bundle args) {
                        startNewChat(dialog);
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityMyFriends.this);
                        dialog.setMessage("dialog creation errors: " + errors).create().show();
                    }
                });
            }
        });
    }

    private void startNewChat(QBDialog dialog)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ActivityDialog.EXTRA_DIALOG, dialog);

        Intent i = new Intent(this.getBaseContext(), ActivityDialog.class);
        i.putExtras(bundle);
        startActivity(i);

        /*
        Intent i = new Intent(getContext(), ActivityChat.class);
        if (position == 0)
        {
            i.putExtra("name", "Dong Sung Chang");
            i.putExtra("uid", 2359813);
            i.putExtra("originClass", "MyFriends");
        }
        else
        {
            i.putExtra("name", "Michael Chang");
            i.putExtra("uid", 2353102);
            i.putExtra("originClass", "MyFriends");
        }
        startActivity(i);
        */
    }

    private String usersListToChatName(List<QBUser> users){
        String chatName = "";
        for(QBUser user : users){
            String prefix = chatName.equals("") ? "" : ", ";
            chatName = chatName + prefix + user.getLogin();
        }
        return chatName;
    }

    public static ArrayList<Integer> getUserIds(List<QBUser> users){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for(QBUser user : users){
            ids.add(user.getId());
        }
        return ids;
    }
}
