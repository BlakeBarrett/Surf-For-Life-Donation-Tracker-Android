package org.surfforlife.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.surfforlife.objects.Volunteer;

public class SurfForLifeAPI {
	
	private static final String SURF_FOR_LIFE_API = "https://surfforlife.org/api/v1";
	private static final String VOLUNTEERS_ENDPOINT = "/volunteers";
	private static final String VOLUNTEER_STATUS_ENDPOINT = "/volunteer/VOLUNTEER_ID/funding_status";
	
	public static ArrayList<Volunteer> getVolunteers() {
		return new ArrayList<Volunteer>();
	}
	
	/**
	 * Fetches a list of volunteers.
	 * @return JSONObject 
	  		{	
	  			volunteers[
		  			{
		  				id: 12345,
		  				name: "Jonus Grumby",
		  				page_url: "http://surfforlife.org/teams/jgrumby"
		  			},
		  			...] 
		 	}
	 */
	public static JSONObject getVolunteersJSON() {
		return getJSONForEndpoint(VOLUNTEERS_ENDPOINT);
	}
	
	/**
	 * Fetches the funding status for a specific volunteer.
	 * @param volunteer - the unique id of the volunteer in question 
	 * @return JSONObject 
	  		{	
	  			funding_goal: 2000,
		 		funding_status: 1999,
		 		currency_symbol: "$"
		 	}
	 */
	public static JSONObject getFundingStatusForVolunteer(final int volunteerId) {
		final String endpoint = VOLUNTEER_STATUS_ENDPOINT.replace("VOLUNTEER_ID", String.valueOf(volunteerId));
		return getJSONForEndpoint(endpoint);
	}
	
	private static JSONObject getJSONForEndpoint(String endpoint) {
		URL url = null;
		try {
			url = new URL(SURF_FOR_LIFE_API + endpoint);
		} catch (MalformedURLException ex) {
			return null;
		}
		final String results = doGetRequest(url);
		JSONObject resultsObj = null;
		try {
			resultsObj = new JSONObject(results);
		} catch (JSONException jse) {
		}
		return resultsObj;
	}
	
	private static String doGetRequest(URL url) {
		return "";
	}
}
