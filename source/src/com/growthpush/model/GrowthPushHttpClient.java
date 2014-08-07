package com.growthpush.model;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.GrowthbeatException;
import com.growthbeat.http.BaseHttpClient;
import com.growthbeat.http.HttpRequest;
import com.growthbeat.http.HttpResponse;
import com.growthbeat.model.Error;
import com.growthbeat.utils.HttpUtils;
import com.growthpush.GrowthPushException;

public class GrowthPushHttpClient extends BaseHttpClient {

	public GrowthPushHttpClient() {
		super();
	}

	public JSONObject post(String api, Map<String, Object> params) {
		return request("POST", api, params);
	}

	public JSONObject put(String api, Map<String, Object> params) {
		return request("PUT", api, params);
	}

	protected JSONObject request(String method, String api, Map<String, Object> params) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(HttpUtils.makeNameValuePairs(params), HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new GrowthPushException("Failed to encode request body.", e);
		}
		HttpRequest httpRequest = new HttpRequest().withMethod(method).withPath(api).withParameters(params).withHeaders(headers)
				.withEntity(entity);
		HttpResponse httpResponse = super.request(httpRequest);
		return fetchJSONObject(httpResponse);
	}

	private JSONObject fetchJSONObject(HttpResponse httpResponse) {

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(httpResponse.getBody());
		} catch (JSONException e) {
			throw new GrowthbeatException("Failed to parse response JSON. " + e.getMessage(), e);
		}

		if (httpResponse.getStatus() < 200 || httpResponse.getStatus() >= 300) {
			Error error = new Error(jsonObject);
			throw new GrowthbeatException(error.getMessage());
		}

		return jsonObject;

	}

}
