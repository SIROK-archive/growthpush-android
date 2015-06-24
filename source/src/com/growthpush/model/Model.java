package com.growthpush.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.growthpush.GrowthPush;
import com.growthpush.GrowthPushException;
import com.growthpush.utils.IOUtils;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class Model {

	private final HttpClient httpClient = new DefaultHttpClient();
	private int TIMEOUT = 5 * 60 * 1000;

	public HashMap<HttpUriRequest, JSONObject> results = new HashMap<HttpUriRequest, JSONObject>();

	public Model() {

		super();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);

	}

	public JSONObject post(final String api, Map<String, Object> params) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : params.entrySet())
			parameters.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));

		HttpPost post = new HttpPost(GrowthPush.BASE_URL + "1/" + api);
		post.setHeader("Accept", "application/json");
		try {
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}

		return request(post);

	}

	public JSONObject put(final String api, Map<String, Object> params) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : params.entrySet())
			parameters.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));

		HttpPut put = new HttpPut(GrowthPush.BASE_URL + "1/" + api);
		put.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		try {
			put.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}

		return request(put);

	}

	private JSONObject request(final HttpUriRequest httpRequest) {

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
		} catch (IOException e) {
			throw new GrowthPushException("Feiled to execute HTTP request. " + e.getMessage(), e);
		}

		JSONObject jsonObject = null;
		try {
			InputStream inputStream = httpResponse.getEntity().getContent();
			String json = IOUtils.toString(inputStream);
			jsonObject = new JSONObject(json);
		} catch (IOException e) {
			throw new GrowthPushException("Failed to read HTTP response. " + e.getMessage(), e);
		} catch (JSONException e) {
			throw new GrowthPushException("Failed to parse response JSON. " + e.getMessage(), e);
		} finally {
			try {
				httpResponse.getEntity().consumeContent();
			} catch (IOException e) {
				throw new GrowthPushException("Failed to close connection. " + e.getMessage(), e);
			}
		}

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode < 200 || statusCode >= 300) {
			Error error = new Error(jsonObject);
			throw new GrowthPushException(error.getMessage());
		}

		return jsonObject;

	}

}
