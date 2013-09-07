package org.surfforlife.objects;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class FundingStatus {
	
	public static final String STATUS = "funding_status";
	public static final String GOAL = "funding_goal";
	public static final String LOCALE = "locale";
	
	private int status;
	private int goal;
	private Locale locale;

	public FundingStatus(final JSONObject status) {
		try {
			final int progress = status.getInt(STATUS);
			final int goal = status.getInt(GOAL);
			String localeString = "en_US";
			if (status.has(LOCALE)) {
				localeString = status.get(LOCALE).toString();
			}
			
			final FundingStatus temp = new FundingStatus(progress, goal, localeString);
			
			this.status = temp.status;
			this.goal = temp.goal;
			this.locale = temp.locale;
		} catch (JSONException ex) {
		}
	}
	
	public FundingStatus(final int status, final int goal, final String locale) {
		this.status = status;
		this.goal = goal;
		final String[] langCountry = locale.split("_");
		this.locale = new Locale(langCountry[0], langCountry[1]);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
