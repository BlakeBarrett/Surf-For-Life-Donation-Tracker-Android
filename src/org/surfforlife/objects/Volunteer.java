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

	public Volunteer(final JSONObject volunteer) {
		try {
			this.id = volunteer.getInt(VOLUNTEER_ID);
			this.name = volunteer.getString(VOLUNTEER_NAME);
			this.url = volunteer.getString(VOLUNTEER_URL);
		} catch (JSONException ex) {
			// don't care
		}
	}
	
	public Volunteer(final int id, final String name, final String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	public int getId() {
		return id;
	}
	public void setId(final int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(final String url) {
		this.url = url;
	}
}
