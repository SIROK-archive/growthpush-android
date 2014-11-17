package com.growthpush.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.model.Model;
import com.growthbeat.utils.DateUtils;
import com.growthpush.GrowthPush;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class Client extends Model {

	private long id;
	private String growthbeatClientId;
	private int applicationId;
	private String code;
	private String token;
	private Environment environment;
	private ClientStatus status;
	private Date created;

	public Client() {
		super();
	}

	public Client(JSONObject jsonObject) {
		super();
		setJsonObject(jsonObject);
	}

	public static Client save(String clientId, String applicationId, String credentialId, String token, Environment environment) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clientId", clientId);
		params.put("applicationId", applicationId);
		params.put("credentialId", credentialId);
		params.put("token", token);
		params.put("environment", environment.toString());
		params.put("os", "android");
		JSONObject jsonObject = GrowthPush.getInstance().getHttpClient().post("3/clients", params);

		return new Client(jsonObject);

	}

	public static Client update(String clientId, String credentialId, String token, Environment environment) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("credentialId", credentialId);
		params.put("token", token);
		params.put("environment", environment.toString());

		JSONObject jsonObject = GrowthPush.getInstance().getHttpClient().put("clients/" + clientId, params);

		return new Client(jsonObject);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGrowthbeatClientId() {
		return growthbeatClientId;
	}

	public void setGrowthbeatClientId(String growthbeatClientId) {
		this.growthbeatClientId = growthbeatClientId;
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

	@Override
	public JSONObject getJsonObject() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", getId());
			jsonObject.put("growthbeatClientId", getGrowthbeatClientId());
			jsonObject.put("applicationId", getApplicationId());
			jsonObject.put("code", getCode());
			jsonObject.put("token", getToken());
			if (getEnvironment() != null)
				jsonObject.put("environment", getEnvironment().toString());
			if (getStatus() != null)
				jsonObject.put("status", getStatus().toString());
			if (getCreated() != null)
				jsonObject.put("created", DateUtils.formatToDateTimeString(getCreated()));
		} catch (JSONException e) {
			return null;
		}

		return jsonObject;

	}

	@Override
	public void setJsonObject(JSONObject jsonObject) {

		if (jsonObject == null)
			return;

		try {
			if (jsonObject.has("id"))
				setId(jsonObject.getLong("id"));
			if (jsonObject.has("growthbeatClientId"))
				setGrowthbeatClientId(jsonObject.getString("growthbeatClientId"));
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
			if (jsonObject.has("created"))
				setCreated(DateUtils.parseFromDateTimeString(jsonObject.getString("created")));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

}
