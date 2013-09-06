package org.surfforlife.donationtracker;

import org.json.JSONObject;
import org.surfforlife.api.SocialAPI;
import org.surfforlife.api.SurfForLifeAPI;
import org.surfforlife.objects.Volunteer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VolunteerDonationStatusActivity extends Activity {

	private int volunteerId;
	private String volunteerName;
	private String volunteerUrl;
	private String socialMessage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volunteer_donation_status);
		
		getVolunteerInfo();
		setVolunteerTitle();
		getStatusForVolunteer();
		setSocialSharingButtonClickListeners();
	}
	
	private void getVolunteerInfo() {
		volunteerId = getIntent().getExtras().getInt(Volunteer.VOLUNTEER_ID);
		volunteerName = getIntent().getExtras().getString(Volunteer.VOLUNTEER_NAME);
		volunteerUrl = getIntent().getExtras().getString(Volunteer.VOLUNTEER_URL);
	}
	
	private void setVolunteerTitle() {
		TextView text = (TextView) findViewById(R.id.fundraising_status_title);
		// TODO: i18n
		text.setText("Fundraising status for " + volunteerName + ":");
		socialMessage = "Donate to me bitches! " + volunteerUrl;
	}
	
	private void getStatusForVolunteer() {
		new Thread() {
			@SuppressWarnings("unused")
			public void run() {
				JSONObject fundingStatusJSON = SurfForLifeAPI.getFundingStatusForVolunteer(volunteerId);
			}
		}.start();
	}
	
	private void setSocialSharingButtonClickListeners() {
		((Button) findViewById(R.id.share_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SocialAPI.shareOnFacebook(socialMessage);
			}
		});
		
		((Button) findViewById(R.id.tweet_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SocialAPI.tweet(socialMessage);
			}
		});
		
	}
}
