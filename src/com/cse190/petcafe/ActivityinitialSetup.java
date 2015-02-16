package com.cse190.petcafe;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

@SuppressLint("InflateParams")
public class ActivityinitialSetup extends Activity {
	
	private EditText firstLanguageInput;
	private EditText secondLanguageInput;
	private EditText statusInput;
	private Button addPetButton;
	private ListView petList;
	
	private ArrayList<PetInformation> pets;
	private ArrayList<String> petNames;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activityinitial_setup);
		
		firstLanguageInput = (EditText)findViewById(R.id.firstLanguageInput);
		secondLanguageInput = (EditText)findViewById(R.id.secondLanguageInput);
		statusInput = (EditText)findViewById(R.id.statusInput);
		addPetButton = (Button)findViewById(R.id.addPetButton);
		petList = (ListView)findViewById(R.id.petList);
		petNames = new ArrayList<String>();
		
		adapter = new ArrayAdapter<String>(this, R.layout.listitem_initialsetup, petNames);
		petList.setAdapter(adapter);

		addPetButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showAddPetDialog();
				
			}
		});
		
		pets = new ArrayList<PetInformation>();
	}
	
	private void showAddPetDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    LayoutInflater inflater = this.getLayoutInflater();
	    final View view = inflater.inflate(R.layout.add_pet_alert_dialog, null);
	    
	    dialog.setView(view)
	    	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   String name = ((EditText)view.findViewById(R.id.petNameInput)).getText().toString();
	            	   String species = ((EditText)view.findViewById(R.id.petSpeciesInput)).getText().toString();
	            	   String breed = ((EditText)view.findViewById(R.id.petBreedInput)).getText().toString();
	            	   String gender = ((EditText)view.findViewById(R.id.petGenderInput)).getText().toString();
	            	   int age = Integer.valueOf(((EditText)view.findViewById(R.id.petAgeInput)).getText().toString());
	            	   String description = ((EditText)view.findViewById(R.id.petDescriptionInput)).getText().toString();
	            	   
	            	   String fbuid = getSharedPreferences(GlobalStrings.PREFNAME, 0).getString(GlobalStrings.FACEBOOK_ID_CACHE_KEY, "");
 
	            	   PetInformation pet = new PetInformation(name, species, breed, gender, age, description, fbuid);
	            	   pets.add(pet);
	            	   petNames.add(name);
	            	   adapter.notifyDataSetChanged();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.done_button) 
		{
			// TODO: do when done editing bullshit
			
			return true;
		}
		else if (id == R.id.skip_button)
		{
			// TODO: do when skipping the initial setup
		}
		return super.onOptionsItemSelected(item);
	}
}
