package org.surfforlife.donationtracker;

import java.util.ArrayList;
import java.util.List;

import org.surfforlife.api.SurfForLifeAPI;
import org.surfforlife.objects.Volunteer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class VolunteerSelectionActivity extends Activity {

	private static SharedPreferences prefs;
	private static ArrayList<Volunteer> volunteers;
	private static ListView volunteersDropDown;
	private static Button submitButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(getString(R.string.select_volunteers));
		checkRemembered();
		setContentView(R.layout.activity_volunteer_selection);
		initVolunteersUI();
		addClickListenerForSelection();
	}
	
	private void checkRemembered() {
		Intent intent;
		prefs = this.getSharedPreferences(getString(R.string.app_name),
				Context.MODE_PRIVATE);
		
		final Boolean remembered = prefs.getBoolean(Volunteer.REMEMBER, false);

		if (remembered) {
			intent = new Intent(this, VolunteerDonationStatusActivity.class);
			if (prefs.getInt(Volunteer.VOLUNTEER_ID, 0) != 0) {
				final int id = prefs.getInt(Volunteer.VOLUNTEER_ID, 0);
				final String name = prefs.getString(Volunteer.VOLUNTEER_NAME,
						"");
				final String url = prefs.getString(Volunteer.VOLUNTEER_URL, "");
				intent.putExtra(Volunteer.VOLUNTEER_ID, id);
				intent.putExtra(Volunteer.VOLUNTEER_NAME, name);
				intent.putExtra(Volunteer.VOLUNTEER_URL, url);
			}
			this.startActivity(intent);
		}
	}
	
	private void initVolunteersUI() {
		volunteersDropDown = (ListView) findViewById(R.id.volunteers_drop_down_list);
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
						final VolunteerAdapter adapter = new VolunteerAdapter(
								VolunteerSelectionActivity.this,
								R.layout.list_item_volunteer, volunteers);
						adapter.setDropDownViewResource(R.layout.list_item_volunteer);
						((ListView) volunteersDropDown).setAdapter(adapter);
						submitButton.setEnabled(volunteers.size() > 0);
					}
				});
			}
		}.start();
	}

	private int selectedVolunteerIndex;
	
	private void addClickListenerForSelection() {
		submitButton.setEnabled(volunteers != null);
		volunteersDropDown.setOnItemClickListener(
				new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
            	selectedVolunteerIndex = pos;
			}
		});
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = selectedVolunteerIndex;

				if (index < 0) {
					return;
				}

				final Volunteer volunteer = volunteers.get(index);
				final Intent intent = new Intent(
						VolunteerSelectionActivity.this,
						VolunteerDonationStatusActivity.class);
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

		private LayoutInflater inflater;
		private List<Volunteer> volunteers;

		public VolunteerAdapter(Context context, int textViewResourceId,
				List<Volunteer> objects) {
			super(context, textViewResourceId, objects);
			volunteers = objects;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				// inflate view
				view = inflater.inflate(R.layout.list_item_volunteer, null);
			}

			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.name = (TextView) view.findViewById(R.id.volunteer_name);
			}

			holder.name.setText(volunteers.get(position).getName());
			view.setTag(holder);

			return view;
		}

		@Override
		public View getDropDownView(final int position, final View convertView,
				final ViewGroup parent) {
			return getView(position, convertView, parent);
		}

		private class ViewHolder {
			public TextView name;
		}
	}
}
