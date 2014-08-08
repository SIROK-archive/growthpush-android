package com.growthpush;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthpush.model.Client;
import com.growthpush.model.Tag;

public class Preference extends com.growthbeat.Preference {

	private static final String FILE_NAME = "growthpush-preferences";
	private static final String CLIENT_KEY = "client";
	private static final String TAG_KEY = "tags";

	public Preference() {
		super();
		setFileName(FILE_NAME);
	}

	public Client fetchClient() {

		JSONObject clientJsonObject = get(CLIENT_KEY);
		if (clientJsonObject == null)
			return null;

		Client client = new Client();
		client.setJsonObject(clientJsonObject);

		return client;

	}

	public Tag fetchTag(String name) {

		JSONObject tagsJsonObject = get(TAG_KEY);
		if (tagsJsonObject == null)
			return null;
		if (!tagsJsonObject.has(name))
			return null;

		Tag tag = new Tag();
		try {
			tag.setJsonObject(tagsJsonObject.getJSONObject(name));
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

		JSONObject tags = get(TAG_KEY);
		if (tags == null)
			tags = new JSONObject();

		try {
			tags.put(tag.getName(), tag.getJsonObject());
		} catch (JSONException e) {
		}

		save(TAG_KEY, tags);

	}

	public void deleteClient() {
		save(CLIENT_KEY, null);
	}

	public void deleteTags() {
		save(TAG_KEY, null);
	}

}
