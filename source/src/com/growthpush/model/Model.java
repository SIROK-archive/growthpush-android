package com.growthpush.model;

import java.util.Map;

import org.json.JSONObject;

import com.growthpush.GrowthPush;

public class Model {

	public JSONObject post(final String api, Map<String, Object> params) {
		return GrowthPush.getInstance().getHttpClient().post("1/" + api, params);
	}

	public JSONObject put(final String api, Map<String, Object> params) {
		return GrowthPush.getInstance().getHttpClient().put("1/" + api, params);
	}

}
