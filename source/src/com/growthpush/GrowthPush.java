package com.growthpush;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.growthbeat.CatchableThread;
import com.growthbeat.Logger;
import com.growthpush.handler.DefaultReceiveHandler;
import com.growthpush.handler.ReceiveHandler;
import com.growthpush.model.Client;
import com.growthpush.model.Environment;
import com.growthpush.model.Event;
import com.growthpush.model.Tag;
import com.growthpush.utils.DeviceUtils;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class GrowthPush {

	public static final String BASE_URL = "https://api.growthpush.com/";

	private static final GrowthPush instance = new GrowthPush();
	private static final Logger logger = new Logger("Growth Push");

	private Client client = null;
	private Semaphore semaphore = new Semaphore(1);
	private CountDownLatch latch = new CountDownLatch(1);
	private ReceiveHandler receiveHandler = new DefaultReceiveHandler();

	private Context context = null;
	private int applicationId;
	private String secret;
	private Environment environment = null;

	private GrowthPush() {
		super();
	}

	public static GrowthPush getInstance() {
		return instance;
	}

	public GrowthPush initialize(Context context, int applicationId, String secret) {
		return initialize(context, applicationId, secret, Environment.production, false);
	}

	public GrowthPush initialize(Context context, int applicationId, String secret, Environment environment) {
		return initialize(context, applicationId, secret, environment, false);
	}

	public GrowthPush initialize(Context context, int applicationId, String secret, Environment environment, boolean debug) {

		if (this.context != null)
			return this;

		this.context = context;
		this.applicationId = applicationId;
		this.secret = secret;
		this.environment = environment;

		this.logger.setSilent(debug);
		Preference.getInstance().setContext(context);

		client = Preference.getInstance().fetchClient();
		if (client != null && client.getApplicationId() != applicationId)
			this.clearClient();

		return this;

	}

	public GrowthPush register(final String senderId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (context == null)
					throw new IllegalStateException("GrowthPush is not initialized.");

				GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
				try {
					String registrationId = gcm.register(senderId);
					registerClient(registrationId);
				} catch (IOException e) {
				}

			}

		}).start();

		return this;

	}

	public void registerClient(final String registrationId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					semaphore.acquire();

					client = Preference.getInstance().fetchClient();
					if (client == null || client.getApplicationId() != applicationId) {
						createClient(registrationId);
						return;
					}

					if ((registrationId != null && !registrationId.equals(client.getToken())) || environment != client.getEnvironment()) {
						updateClient(registrationId);
						return;
					}

					logger.info("Client already registered.");
					latch.countDown();

				} catch (InterruptedException e) {
				} finally {
					semaphore.release();
				}

			}

		}).start();

	}

	private void createClient(final String registrationId) {

		try {

			logger.info(String.format("Registering client... (applicationId: %d, environment: %s)", applicationId, environment));
			GrowthPush.this.client = new Client(registrationId, environment).save(GrowthPush.this);
			logger.info(String.format("Registering client success (clientId: %d)", GrowthPush.this.client.getId()));

			logger.info(String
					.format("See https://growthpush.com/applications/%d/clients to check the client registration.", applicationId));
			Preference.getInstance().saveClient(GrowthPush.this.client);
			latch.countDown();

		} catch (GrowthPushException e) {
			logger.error(String.format("Registering client fail. %s", e.getMessage()));
		}

	}

	private void updateClient(final String registrationId) {

		try {

			logger.info(String.format("Updating client... (applicationId: %d, token: %s, environment: %s)", applicationId, registrationId,
					environment));
			GrowthPush.this.client.setToken(registrationId);
			GrowthPush.this.client.setEnvironment(environment);
			GrowthPush.this.client = GrowthPush.this.client.update();
			logger.info(String.format("Update client success (clientId: %d)", GrowthPush.this.client.getId()));

			Preference.getInstance().saveClient(GrowthPush.this.client);
			latch.countDown();

		} catch (GrowthPushException e) {
			logger.error(String.format("Updating client fail. %s", e.getMessage()));
		}

	}

	public void trackEvent(final String name) {
		trackEvent(name, null);
	}

	public void trackEvent(final String name, final String value) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (name == null) {
					logger.warning("Event name cannot be null.");
					return;
				}

				waitClientRegistration();

				logger.info(String.format("Sending event ... (name: %s)", name));
				try {
					Event event = new Event(name, value).save(GrowthPush.this);
					logger.info(String.format("Sending event success. (timestamp: %s)", event.getTimeStamp()));
				} catch (GrowthPushException e) {
					logger.error(String.format("Sending event fail. %s", e.getMessage()));
				}

			}

		}).start();

	}

	public void setTag(final String name) {
		setTag(name, null);
	}

	public void setTag(final String name, final String value) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (name == null) {
					logger.warning("Tag name cannot be null.");
					return;
				}

				Tag tag = Preference.getInstance().fetchTag(name);
				if (tag != null && value.equalsIgnoreCase(tag.getValue()))
					return;

				waitClientRegistration();

				logger.info(String.format("Sending tag... (key: %s, value: %s)", name, value));
				try {
					Tag createdTag = new Tag(name, value).save(GrowthPush.this);
					logger.info(String.format("Sending tag success"));
					Preference.getInstance().saveTag(createdTag);
				} catch (GrowthPushException e) {
					logger.error(String.format("Sending tag fail. %s", e.getMessage()));
				}

			}

		}).start();

	}

	public void setDeviceTags() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (context == null)
					throw new IllegalStateException("GrowthPush is not initialized.");

				setTag("Device", DeviceUtils.getDevice());
				setTag("OS", DeviceUtils.getOs());
				setTag("Language", DeviceUtils.getLanguage());
				setTag("Time Zone", DeviceUtils.getTimeZone());
				setTag("Version", DeviceUtils.getVersion(context));
				setTag("Build", DeviceUtils.getBuild(context));

			}

		}).start();

	}

	public void setReceiveHandler(ReceiveHandler receiveHandler) {
		this.receiveHandler = receiveHandler;
	}

	public ReceiveHandler getReceiveHandler() {
		return receiveHandler;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public String getSecret() {
		return secret;
	}

	public Logger getLogger() {
		return logger;
	}

	public Client getClient() {
		return client;
	}

	private void waitClientRegistration() {

		if (client == null) {
			try {
				latch.await();
			} catch (InterruptedException e) {
			}
		}

	}

	private void clearClient() {

		this.client = null;
		Preference.getInstance().deleteClient();
		Preference.getInstance().deleteTags();

	}

	private static class Thread extends CatchableThread {

		public Thread(Runnable runnable) {
			super(runnable);
		}

		@Override
		public void uncaughtException(java.lang.Thread thread, Throwable e) {
			String message = "Uncaught Exception: " + e.getClass().getName();
			if (e.getMessage() != null)
				message += "; " + e.getMessage();
			logger.warning(message);
		}

	}

}
