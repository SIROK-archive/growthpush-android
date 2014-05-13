package com.growthpush.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthpush.GrowthPush;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class Client extends Model {

	private long id;
	private int applicationId;
	private String code;
	private String token;
	private Environment environment;
	private ClientStatus status;
	private Date created;

	public Client() {
		super();
	}

	public Client(String token, Environment environment) {
		this();
		setToken(token);
		setEnvironment(environment);
	}

	public Client save(GrowthPush growthPush) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("applicationId", growthPush.getApplicationId());
		params.put("secret", growthPush.getSecret());
		params.put("token", token);
		params.put("environment", environment.toString());
		params.put("os", "android");
		JSONObject jsonObject = post("clients", params);
		if (jsonObject != null)
			setJsonObject(jsonObject);

		return this;

	}

	public Client update() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		params.put("token", token);
		params.put("environment", environment.toString());

		JSONObject jsonObject = put("clients/" + id, params);
		setJsonObject(jsonObject);

		return this;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public ClientStatus getStatus() {
		return status;
	}

	public void setStatus(ClientStatus status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public JSONObject getJsonObject() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", getId());
			jsonObject.put("applicationId", getApplicationId());
			jsonObject.put("code", getCode());
			jsonObject.put("token", getToken());
			if (getEnvironment() != null)
				jsonObject.put("environment", getEnvironment().toString());
			if (getStatus() != null)
				jsonObject.put("status", getStatus().toString());
			if (getCreated() != null)
				jsonObject.put("created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getCreated()));
		} catch (JSONException e) {
			return null;
		}

		return jsonObject;

	}

	public void setJsonObject(JSONObject jsonObject) {

		if (jsonObject == null)
			return;

		try {
			if (jsonObject.has("id"))
				setId(jsonObject.getLong("id"));
			if (jsonObject.has("applicationId"))
				setApplicationId(jsonObject.getInt("applicationId"));
			if (jsonObject.has("code"))
				setCode(jsonObject.getString("code"));
			if (jsonObject.has("token"))
				setToken(jsonObject.getString("token"));
			if (jsonObject.has("environment"))
				setEnvironment(Environment.valueOf(jsonObject.getString("environment")));
			if (jsonObject.has("status"))
				setStatus(ClientStatus.valueOf(jsonObject.getString("status")));
			if (jsonObject.has("created")) {
				try {
					setCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("created")));
				} catch (ParseException e) {
				}
			}
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

}
