package com.cse190.petcafe.adapter;

import com.cse190.petcafe.ObjectDrawerItem;
import com.cse190.petcafe.R;
import com.cse190.petcafe.R.id;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

	Context mContext;
	int layoutResourceId;
	ObjectDrawerItem data[] = null;

	public DrawerItemCustomAdapter(Context mContext, int layoutResourceId,
			ObjectDrawerItem[] data) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View listItem = convertView;

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		listItem = inflater.inflate(layoutResourceId, parent, false);

		ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.icon);
		TextView textViewName = (TextView) listItem.findViewById(R.id.title);

		ObjectDrawerItem folder = data[position];

		imageViewIcon.setImageResource(folder.icon);
		textViewName.setText(folder.name);

		return listItem;
	}

}