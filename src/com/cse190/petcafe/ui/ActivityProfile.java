package com.cse190.petcafe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cse190.petcafe.R;

public class ActivityProfile extends ActivityBase {
	private ListView proflist;
	private ListView petlist;

	public Integer[] imgid = { R.drawable.ic_email, R.drawable.ic_phone,
			R.drawable.ic_address };
	
	String[] itemname = { "David@ucsd.edu", "(555) 555-5555", "9450 Gilman Dr." };
	
	String[] petname = { "Rudolph", "Clifford"};
	
	public Integer[] petimgid = { R.drawable.ic_pet, R.drawable.ic_pet};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup content = (ViewGroup) findViewById(R.id.content_frame);
		getLayoutInflater().inflate(R.layout.activity_profile, content, true);
		// Profile Info
		proflist = (ListView) findViewById(R.id.infolist);
		ProfileListAdapter profadapter = new ProfileListAdapter(this, itemname, imgid);
		proflist.setAdapter(profadapter);
		
		// Pet info
		petlist = (ListView) findViewById(R.id.petlist);
		PetListAdapter petadapter = new PetListAdapter(this, petname, petimgid);
		petlist.setAdapter(petadapter);
		
		// New Post Menu Item
		newpost = (ImageView) findViewById(R.id.new_post);
		newpost.setImageResource(R.drawable.ic_editprof);
		newpost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent newPostIntent = new Intent(ActivityProfile.this, ActivityEditProfile.class);
				startActivity(newPostIntent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				
			}
		});
	}

	public class ProfileListAdapter extends ArrayAdapter<String> {

		private final Activity context;
		private final String[] itemname;
		private final Integer[] imgid;

		public ProfileListAdapter(Activity context, String[] itemname,
				Integer[] imgid) {
			super(context, R.layout.listitem_profile, itemname);
			// TODO Auto-generated constructor stub

			this.context = context;
			this.itemname = itemname;
			this.imgid = imgid;
		}
		
		 @Override
		  public boolean isEnabled (int position) {
		    return false;
		  }

		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.listitem_profile, null,
					true);

			TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

			txtTitle.setText(itemname[position]);
			imageView.setImageResource(imgid[position]);
			return rowView;
		};
	}
	
	public class PetListAdapter extends ArrayAdapter<String> {

		private final Activity context;
		private final String[] itemname;
		private final Integer[] imgid;

		public PetListAdapter(Activity context, String[] itemname,
				Integer[] imgid) {
			super(context, R.layout.listitem_profile, itemname);
			// TODO Auto-generated constructor stub

			this.context = context;
			this.itemname = itemname;
			this.imgid = imgid;
		}
		
		 @Override
		  public boolean isEnabled (int position) {
		    return false;
		  }

		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.listitem_pet, null,
					true);

			TextView txtTitle = (TextView) rowView.findViewById(R.id.petname);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
			TextView gender = (TextView) rowView.findViewById(R.id.petgender);
			TextView age = (TextView) rowView.findViewById(R.id.petage);
			TextView type = (TextView) rowView.findViewById(R.id.pettype);

			txtTitle.setText(petname[position]);
			imageView.setImageResource(petimgid[position]);
			gender.setText("M");
			age.setText("999");
			type.setText("Lion");
			return rowView;
		};
	}

}
