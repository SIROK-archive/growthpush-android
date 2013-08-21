package com.growthpush.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Error {

	private int code;
	private String message;

	public Error() {
		super();
	}

	public Error(int code, String message) {
		this();
		setCode(code);
		setMessage(message);
	}

	public Error(JSONObject jsonObject) {
		this();
		setJsonObject(jsonObject);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setJsonObject(JSONObject jsonObject) {

		try {
			if (jsonObject.has("code"))
				setCode(jsonObject.getInt("code"));
			if (jsonObject.has("message"))
				setMessage(jsonObject.getString("message"));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

}
