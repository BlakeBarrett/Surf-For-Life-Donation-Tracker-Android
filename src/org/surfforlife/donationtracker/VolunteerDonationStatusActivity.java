package org.surfforlife.donationtracker;

import java.text.NumberFormat;
import java.util.Locale;

import org.surfforlife.api.SocialAPI;
import org.surfforlife.api.SurfForLifeAPI;
import org.surfforlife.objects.FundingStatus;
import org.surfforlife.objects.Volunteer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class VolunteerDonationStatusActivity extends Activity {

	private int volunteerId;
	private String volunteerName;
	private String volunteerUrl;
	private String socialMessage;

	private static CheckBox rememberVolunteer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volunteer_donation_status);

		getVolunteerInfo();
		setVolunteerTitle();
		getStatusForVolunteer();
		setupRememberMeCheckBox();
		setSocialSharingButtonClickListeners();
	}

	private void getVolunteerInfo() {
		volunteerId = getIntent().getExtras().getInt(Volunteer.VOLUNTEER_ID);
		volunteerName = getIntent().getExtras().getString(
				Volunteer.VOLUNTEER_NAME);
		volunteerUrl = getIntent().getExtras().getString(
				Volunteer.VOLUNTEER_URL);
	}

	private void saveVolunteerForNextTime(final Boolean remember) {
		SharedPreferences.Editor editor = this.getSharedPreferences(
				getString(R.string.app_name), Context.MODE_PRIVATE).edit();
		editor.putBoolean(Volunteer.REMEMBER, remember);
		editor.putInt(Volunteer.VOLUNTEER_ID, volunteerId);
		editor.putString(Volunteer.VOLUNTEER_NAME, volunteerName);
		editor.putString(Volunteer.VOLUNTEER_URL, volunteerUrl);
		editor.commit();
	}

	private void setVolunteerTitle() {
		final TextView text = (TextView) findViewById(R.id.fundraising_status_title);
		// TODO: i18n
		text.setText("Fundraising status for " + volunteerName + ":");
		socialMessage = "Donate to me bitches! " + volunteerUrl;
	}

	private void getStatusForVolunteer() {
		new Thread() {
			public void run() {
				final FundingStatus fundingStatus = SurfForLifeAPI
						.getFundingStatusForVolunteer(volunteerId);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setStatusForVolunteer(fundingStatus);
					}
				});
			}
		}.start();
	}

	private void setStatusForVolunteer(final FundingStatus fundingStatus) {
		final int status = fundingStatus.getStatus();
		final int goal = fundingStatus.getGoal();
		final Locale locale = fundingStatus.getLocale();
		final TextView statusText = (TextView) findViewById(R.id.fundraising_status_text);
		final String fundingStatusString = getCurrencyFormattedString(status,
				locale) + " of " + getCurrencyFormattedString(goal, locale);
		statusText.setText(fundingStatusString);
	}

	private String getCurrencyFormattedString(final int value,
			final Locale locale) {
		final NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		return format.format(value);
	}

	private void setupRememberMeCheckBox() {
		final Boolean checked = getSharedPreferences(
				getString(R.string.app_name), Context.MODE_PRIVATE).getBoolean(
				Volunteer.REMEMBER, false);
		rememberVolunteer = (CheckBox) findViewById(R.id.remember_selected_volunteer_checkbox);
		rememberVolunteer.setChecked(checked);
		rememberVolunteer
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						saveVolunteerForNextTime(isChecked);
					}
				});
	}

	private void setSocialSharingButtonClickListeners() {
		((Button) findViewById(R.id.share_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SocialAPI.shareOnFacebook(socialMessage);
					}
				});

		((Button) findViewById(R.id.tweet_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SocialAPI.tweet(socialMessage);
					}
				});

		((Button) findViewById(R.id.volunteer_profile_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(volunteerUrl));
						startActivity(intent);
					}
				});
	}
}
