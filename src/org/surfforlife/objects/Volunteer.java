package org.surfforlife.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Volunteer {
	
	public static final String VOLUNTEER = "volunteer";
	public static final String VOLUNTEER_ID = "id";
	public static final String VOLUNTEER_NAME = "name";
	public static final String VOLUNTEER_URL = "page_url";
	
	private int id;
	private String name;
	private String url;

	public Volunteer(JSONObject volunteer) {
		try {
			this.id = volunteer.getInt(VOLUNTEER_ID);
			this.name = volunteer.getString(VOLUNTEER_NAME);
			this.url = volunteer.getString(VOLUNTEER_URL);
		} catch (JSONException ex) {
			// don't care
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
