package com.cse190.petcafe;

import java.util.ArrayList;

import com.cse190.petcafe.ui.ActivityBlog;
import com.facebook.widget.ProfilePictureView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ActivityinitialSetup extends Activity {
	
	private EditText firstLanguageInput;
	private EditText secondLanguageInput;
	private EditText statusInput;
	private EditText emailInput;
	private EditText phoneNumberInput;
	private Button addPetButton;
	private ListView petList;
	private ProfilePictureView profPic;
	private TextView userNameLabel;
	private Button doneButton;
	private Button skipButton;
	
	private String fbuid;
	private String username;
	
	private ArrayList<PetInformation> pets;
	private ArrayList<String> petNames;
	private ArrayAdapter<String> petListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activityinitial_setup);
		
		fbuid = getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, "");
		username = getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.USERNAME_CACHE_KEY, "");
		
		firstLanguageInput = (EditText)findViewById(R.id.firstLanguageInput);
		secondLanguageInput = (EditText)findViewById(R.id.secondLanguageInput);
		statusInput = (EditText)findViewById(R.id.statusInput);
		emailInput = (EditText)findViewById(R.id.emailInput);
		phoneNumberInput = (EditText)findViewById(R.id.phoneNumberInput);
		addPetButton = (Button)findViewById(R.id.addPetButton);
		petList = (ListView)findViewById(R.id.petList);
		profPic = (ProfilePictureView)findViewById(R.id.profile_pic);
		userNameLabel = (TextView)findViewById(R.id.user_name_label);
		doneButton = (Button)findViewById(R.id.doneButton);
		skipButton = (Button)findViewById(R.id.skipSetUpButton);
		
		profPic.setCropped(true);
		profPic.setProfileId(fbuid);
		
		userNameLabel.setText(username);
		
		petNames = new ArrayList<String>();
		
		petListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, petNames);
		petList.setAdapter(petListAdapter);

		addPetButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				showAddPetDialog();
			}
		});
		
		doneButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				performDone();
			}
		});
		
		skipButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				performSkip();
			}
		});
		
		pets = new ArrayList<PetInformation>();
	}
	
	private void showAddPetDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    LayoutInflater inflater = this.getLayoutInflater();
	    final View view = inflater.inflate(R.layout.add_pet_alert_dialog, null);
	    final Spinner speciesList = (Spinner) view.findViewById(R.id.petSpeciesInput);
	    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.pet_species, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    speciesList.setAdapter(adapter);
	    
	    final Spinner genderList = (Spinner) view.findViewById(R.id.petGenderInput);
	    final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
	            R.array.genders, android.R.layout.simple_spinner_item);
	    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    genderList.setAdapter(adapter2);
	    
	    dialog.setView(view)
	    	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   String name = ((EditText)view.findViewById(R.id.petNameInput)).getText().toString();
	            	   String species = adapter.getItem(speciesList.getSelectedItemPosition()).toString();
	            	   String breed = ((EditText)view.findViewById(R.id.petBreedInput)).getText().toString();
	            	   String gender = adapter2.getItem(genderList.getSelectedItemPosition()).toString();
	            	   int age = Integer.valueOf(((EditText)view.findViewById(R.id.petAgeInput)).getText().toString());
	            	   String description = ((EditText)view.findViewById(R.id.petDescriptionInput)).getText().toString();
	            	   
	            	   String fbuid = getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, "");
 
	            	   PetInformation pet = new PetInformation(name, species, breed, gender, age, description, fbuid);
	            	   pets.add(pet);
	            	   petNames.add(name);
	            	   petListAdapter.notifyDataSetChanged();
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	               }
	           });      
	    dialog.create();
	    dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activityinitial_setup, menu);
		return true;
	}
	
	private void performDone()
	{
		// TODO: update the server
		NetworkHandler handler = NetworkHandler.getInstance();

		// gotta change latitude, longitude, status
		UserProfileInformation profile = new UserProfileInformation(fbuid, username, firstLanguageInput.getText().toString(), 
				secondLanguageInput.getText().toString(), 0.0, 0.0, statusInput.getText().toString(), 
				emailInput.getText().toString(), phoneNumberInput.getText().toString());
		handler.modifyUser(profile);
		for (PetInformation pet : pets)
		{
			handler.addPet(pet);
		}
		
		Intent i = new Intent(getBaseContext(), ActivityBlog.class);
		startActivity(i);
		finish();
	}

	private void performSkip()
	{
		// TODO: do when skipping the initial setup
		Intent i = new Intent(getBaseContext(), ActivityBlog.class);
		startActivity(i);
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}
}
