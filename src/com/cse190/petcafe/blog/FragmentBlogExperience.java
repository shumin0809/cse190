package com.cse190.petcafe.blog;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cse190.petcafe.R;

public class FragmentBlogExperience extends Fragment {
	private List<String> listValues;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_blog_experience,
				container, false);

		ListView blogList = (ListView) rootView.findViewById(R.id.bloglist);

		listValues = new ArrayList<String>();
		listValues.add("Experience Blog 1");
		listValues.add("Experience Blog 2");
		listValues.add("Experience Blog 3");
		listValues.add("Experience Blog 4");
		listValues.add("Experience Blog 5");

		// initiate the listadapter
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.listitem_blog_experience, R.id.listText,
				listValues);

		// assign the list adapter
		blogList.setAdapter(myAdapter);

		return rootView;
	}

}