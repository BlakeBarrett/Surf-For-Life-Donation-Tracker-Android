package org.surfforlife.donationtracker;

import org.surfforlife.objects.Volunteer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class ApplicationLaunchActivity extends Activity {

	private static SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = this.getSharedPreferences(getString(R.string.app_name),
				Context.MODE_PRIVATE);
	}

	@Override
	public void onResume() {
		super.onResume();

		Intent intent;

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
		} else {
			intent = new Intent(this, VolunteerSelectionActivity.class);
		}

		this.startActivity(intent);
	}
}
