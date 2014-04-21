package com.growthpush;

import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.growthpush.model.Client;
import com.growthpush.model.Tag;
import com.growthpush.utils.IOUtils;

public class Preference {

	private static final String FILE_NAME = "growthpush-preferences";
	private static final String CLIENT_KEY = "client";
	private static final String TAG_KEY = "tags";

	private static Preference instance = new Preference();

	private Context context = null;
	private JSONObject preferences = null;

	private Preference() {
		super();
	}

	public static Preference getInstance() {
		return instance;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Client fetchClient() {

		Client client = new Client();
		client.setJsonObject(fetch(CLIENT_KEY));
		return client;

	}

	public Tag fetchTag(String name) {

		JSONObject tags = fetch(TAG_KEY);
		if (tags == null)
			return null;
		if (!tags.has(name))
			return null;

		Tag tag = new Tag();
		try {
			tag.setJsonObject(tags.getJSONObject(name));
		} catch (JSONException e) {
		}

		return tag;

	}

	public synchronized void saveClient(Client client) {

		if (client == null)
			throw new IllegalArgumentException("Argument client cannot be null.");

		save(CLIENT_KEY, client.getJsonObject());

	}

	public synchronized void saveTag(Tag tag) {

		if (tag == null)
			throw new IllegalArgumentException("Argument tag cannot be null.");

		JSONObject tags = fetch(TAG_KEY);
		if (tags == null)
			tags = new JSONObject();

		try {
			tags.put(tag.getName(), tag.getJsonObject());
		} catch (JSONException e) {
		}

		save(TAG_KEY, tags);

	}

	private JSONObject fetch(String key) {

		try {
			return getPreferences().getJSONObject(key);
		} catch (JSONException e) {
			return null;
		}

	}

	private void save(String key, JSONObject jsonObject) {

		JSONObject preferences = getPreferences();
		try {
			preferences.put(key, jsonObject);
		} catch (JSONException e) {
			return;
		}

		synchronized (this) {
			try {
				FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
				fileOutputStream.write(preferences.toString().getBytes());
				fileOutputStream.flush();
			} catch (IOException e) {
			}
		}

	}

	private synchronized JSONObject getPreferences() {

		if (context == null)
			throw new IllegalStateException("Context is null.");

		if (this.preferences == null) {
			try {
				String json = IOUtils.toString(context.openFileInput(FILE_NAME));
				this.preferences = new JSONObject(json);
			} catch (IOException e) {
			} catch (JSONException e) {
			}
		}

		if (this.preferences == null)
			this.preferences = new JSONObject();

		return preferences;

	}
}
