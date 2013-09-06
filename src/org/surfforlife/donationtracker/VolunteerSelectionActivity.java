package org.surfforlife.donationtracker;

import java.util.ArrayList;
import java.util.List;

import org.surfforlife.api.SurfForLifeAPI;
import org.surfforlife.objects.Volunteer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class VolunteerSelectionActivity extends Activity {
	
	private static ArrayList<Volunteer> volunteers;
	private static Spinner volunteersDropDown;
	private static Button submitButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volunteer_selection);
		initVolunteersUI();
		addClickListenerForSelection();
	}
	
	private void initVolunteersUI() {
		volunteersDropDown = (Spinner)findViewById(R.id.volunteers_drop_down_list);
		submitButton = ((Button) findViewById(R.id.get_volunteers_submit_button));
		submitButton.setEnabled(volunteers != null);
		new Thread() {
			public void run() {
				// API request on new thread
				volunteers = SurfForLifeAPI.getVolunteers();
				// populate the UI back on the UI thread.
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final VolunteerAdapter adapter = new VolunteerAdapter(VolunteerSelectionActivity.this, R.layout.list_item_volunteer, volunteers);
						volunteersDropDown.setAdapter(adapter);
						submitButton.setEnabled(volunteers.size() > 0);
					}
				});
			}
		}.start();
	}
	
	private void addClickListenerForSelection() {
		submitButton.setEnabled(volunteers != null);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = volunteersDropDown.getSelectedItemPosition();
				
				if (index < 0) {
					return;
				}
				
				Volunteer volunteer = volunteers.get(index);
				Intent intent = new Intent(VolunteerSelectionActivity.this, VolunteerDonationStatusActivity.class);
				intent.putExtra(Volunteer.VOLUNTEER_ID, volunteer.getId());
				intent.putExtra(Volunteer.VOLUNTEER_NAME, volunteer.getName());
				intent.putExtra(Volunteer.VOLUNTEER_URL, volunteer.getUrl());
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.volunteer_donation_status, menu);
		return true;
	}

	private class VolunteerAdapter extends ArrayAdapter<Volunteer> {
		
		List<Volunteer> volunteers;
		
		public VolunteerAdapter(Context context, 
				int textViewResourceId, List<Volunteer> objects) {
			super(context, textViewResourceId, objects);
			volunteers = objects;
		}
		
		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent){
			View view = convertView;
			if (view == null) {
				// inflate view
				view = ((LayoutInflater)getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.list_item_volunteer, null);
			}
			
			TextView name = (TextView)view.findViewById(R.id.volunteer_name);
			name.setText(volunteers.get(position).getName());
			
			return view;
		}

	}
}
