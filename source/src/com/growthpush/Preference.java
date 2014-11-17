package com.growthpush;

import org.json.JSONObject;

import com.growthpush.model.Client;

public class Preference extends com.growthbeat.Preference {

	private static final String CLIENT_KEY = "client";

	public Preference() {
		super();
	}

	public Preference(String fileName) {
		this();
		setFileName(fileName);
	}

	public Client fetchClient() {

		JSONObject clientJsonObject = get(CLIENT_KEY);
		if (clientJsonObject == null)
			return null;

		Client client = new Client();
		client.setJsonObject(clientJsonObject);

		return client;

	}

	public synchronized void saveClient(Client client) {

		if (client == null)
			throw new IllegalArgumentException("Argument client cannot be null.");

		save(CLIENT_KEY, client.getJsonObject());

	}

	public void deleteClient() {
		save(CLIENT_KEY, null);
	}

}
