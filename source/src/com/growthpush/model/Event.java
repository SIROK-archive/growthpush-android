package com.growthpush.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.model.Model;
import com.growthpush.GrowthPush;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class Event extends Model {

	private int goalId;
	private String name;
	private long timeStamp;
	private long clientId;
	private String value;

	public Event() {
		super();
	}

	public Event(String name, String value) {
		this();
		setName(name);
		setValue(value);
	}

	public Event save(GrowthPush growthPush) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clientId", growthPush.getClient().getId());
		params.put("code", growthPush.getClient().getCode());
		params.put("name", name);
		params.put("value", value);

		// JSONObject jsonObject = post("events", params);
		// if (jsonObject != null)
		// setJsonObject(jsonObject);

		return this;

	}

	public int getGoalId() {
		return goalId;
	}

	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void setJsonObject(JSONObject jsonObject) {

		try {
			if (jsonObject.has("goalId"))
				setGoalId(jsonObject.getInt("goalId"));
			if (jsonObject.has("timestamp"))
				setTimeStamp(jsonObject.getLong("timestamp"));
			if (jsonObject.has("clientId"))
				setClientId(jsonObject.getLong("clientId"));
			if (jsonObject.has("value"))
				setValue(jsonObject.getString("value"));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

	@Override
	public JSONObject getJsonObject() {
		return null;
	}

}
