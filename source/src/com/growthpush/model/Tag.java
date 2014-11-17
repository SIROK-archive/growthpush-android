package com.growthpush.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.model.Model;
import com.growthpush.GrowthPush;

public class Tag extends Model {

	private int tagId;
	private String name;
	private long clientId;
	private String value;

	public Tag() {
		super();
	}

	public Tag(String name, String value) {
		this();
		setName(name);
		setValue(value);
	}

	public Tag save(GrowthPush growthPush) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clientId", growthPush.getClient().getId());
		params.put("code", growthPush.getClient().getCode());
		params.put("name", name);
		params.put("value", value);

		// JSONObject jsonObject = post("tags", params);
		// if (jsonObject != null)
		// setJsonObject(jsonObject);

		return this;

	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public JSONObject getJsonObject() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tagId", getTagId());
			jsonObject.put("clientId", getClientId());
			jsonObject.put("value", getValue());
		} catch (JSONException e) {
		}

		return jsonObject;

	}

	@Override
	public void setJsonObject(JSONObject jsonObject) {

		try {
			if (jsonObject.has("tagId"))
				setTagId(jsonObject.getInt("tagId"));
			if (jsonObject.has("clientId"))
				setClientId(jsonObject.getLong("clientId"));
			if (jsonObject.has("value"))
				setValue(jsonObject.getString("value"));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

}
