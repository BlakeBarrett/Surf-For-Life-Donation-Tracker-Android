package org.surfforlife.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.surfforlife.objects.FundingStatus;
import org.surfforlife.objects.Volunteer;

public class SurfForLifeAPI {

	private static final String SURF_FOR_LIFE_API = "https://surfforlife.org/api/v1";
	private static final String VOLUNTEERS_ENDPOINT = "/volunteers";
	private static final String VOLUNTEER_STATUS_ENDPOINT = "/volunteer/VOLUNTEER_ID/funding_status";

	public static ArrayList<Volunteer> getVolunteers() {
		ArrayList<Volunteer> volunteersList = new ArrayList<Volunteer>();
		try {
			JSONObject volunteersJSON = getVolunteersJSON();
			JSONArray volunteersArr = volunteersJSON.getJSONArray("volunteers");
			for (int i = 0; i < volunteersArr.length(); i++) {
				JSONObject currentVolunteerJSON = (JSONObject) volunteersArr.get(i);
				Volunteer currentVolunteer = new Volunteer(currentVolunteerJSON);
				volunteersList.add(currentVolunteer);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return volunteersList;
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
	private static JSONObject getVolunteersJSON() {
		return getJSONForEndpoint(VOLUNTEERS_ENDPOINT);
	}

	/**
	 * Fetches the funding status for a specific volunteer.
	 * @param volunteer - the unique id of the volunteer in question 
	 * @return JSONObject 
	  		{	
	  			funding_goal: 2000,
		 		funding_status: 1999,
		 		locale: "en_US"
		 	}
	 */
	public static FundingStatus getFundingStatusForVolunteer(final int volunteerId) {
		final String endpoint = VOLUNTEER_STATUS_ENDPOINT.replace("VOLUNTEER_ID", String.valueOf(volunteerId));
		JSONObject statusJSON = getJSONForEndpoint(endpoint);
		return new FundingStatus(statusJSON);
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

	static String doGetRequest(URL url) {
		String result = "";
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			final String streamData = readStream(connection.getInputStream());
			if (streamData != null) {
				result = streamData;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// HACK!!!
			if (url.toString().equals(SURF_FOR_LIFE_API + VOLUNTEERS_ENDPOINT)) {
				result = "{volunteers:[{" +
						"id: 12345, name: 'Jonus Grumby', page_url: 'http://hippovszombies.com'},{" +
						"id: 12346, name: 'Blake Barrett', page_url: 'http://fb.com/schjlatah'}" +
						"]}";
			} else {
				result = "{funding_goal: 2000, funding_status: 1750, locale: 'en_US'}";
			}
		}
		return result;
	}
	
	private static String readStream(InputStream in) {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result.toString();
	}
}
