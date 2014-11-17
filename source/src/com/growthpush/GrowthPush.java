package com.growthpush;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.growthbeat.CatchableThread;
import com.growthbeat.GrowthbeatCore;
import com.growthbeat.Logger;
import com.growthbeat.Preference;
import com.growthbeat.analytics.GrowthAnalytics;
import com.growthbeat.http.GrowthbeatHttpClient;
import com.growthbeat.utils.AppUtils;
import com.growthbeat.utils.DeviceUtils;
import com.growthpush.handler.DefaultReceiveHandler;
import com.growthpush.handler.ReceiveHandler;
import com.growthpush.model.Client;
import com.growthpush.model.Environment;

public class GrowthPush {

	public static final String LOGGER_DEFAULT_TAG = "GrowthPush";
	public static final String HTTP_CLIENT_DEFAULT_BASE_URL = "https://api.growthpush.com/";
	public static final String PREFERENCE_DEFAULT_FILE_NAME = "growthpush-preferences";

	private static final GrowthPush instance = new GrowthPush();
	private final Logger logger = new Logger(LOGGER_DEFAULT_TAG);
	private final GrowthbeatHttpClient httpClient = new GrowthbeatHttpClient(HTTP_CLIENT_DEFAULT_BASE_URL);
	private final Preference preference = new Preference(PREFERENCE_DEFAULT_FILE_NAME);

	private Client client = null;
	private Semaphore semaphore = new Semaphore(1);
	private CountDownLatch latch = new CountDownLatch(1);
	private ReceiveHandler receiveHandler = new DefaultReceiveHandler();

	private Context context = null;
	private String applicationId;
	private String credentialId;
	private Environment environment = null;

	private GrowthPush() {
		super();
	}

	public static GrowthPush getInstance() {
		return instance;
	}

	public void initialize(final Context context, final String applicationId, final String credentialId, final Environment environment,
			final String senderId) {

		if (this.context != null)
			return;

		GrowthbeatCore.getInstance().initialize(context, applicationId, credentialId);

		this.context = context;
		this.applicationId = applicationId;
		this.credentialId = credentialId;
		this.environment = environment;

		// TODO set logger configuration
		this.preference.setContext(context);

		new Thread(new Runnable() {

			@Override
			public void run() {

				com.growthbeat.model.Client growthbeatClient = GrowthbeatCore.getInstance().waitClient();
				client = Client.load();

				if (client != null && client.getGrowthbeatClientId() != null
						&& !client.getGrowthbeatClientId().equals(growthbeatClient.getId()))
					GrowthPush.this.clearClient();

				GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
				try {
					String registrationId = gcm.register(senderId);
					registerClient(registrationId);
				} catch (IOException e) {
				}

			}

		}).start();

	}

	public void registerClient(final String registrationId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					semaphore.acquire();

					com.growthbeat.model.Client growthbeatClient = GrowthbeatCore.getInstance().waitClient();
					client = Client.load();
					// TODO Check applicationId
					if (client == null) {
						createClient(growthbeatClient.getId(), registrationId);
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

	private void createClient(final String growthbeatClientId, final String registrationId) {

		try {

			logger.info(String.format("Registering client... (applicationId: %d, environment: %s)", applicationId, environment));
			client = Client.create(growthbeatClientId, applicationId, credentialId, registrationId, environment);
			logger.info(String.format("Registering client success (clientId: %d)", client.getId()));

			logger.info(String
					.format("See https://growthpush.com/applications/%d/clients to check the client registration.", applicationId));
			Client.save(client);
			latch.countDown();

		} catch (GrowthPushException e) {
			logger.error(String.format("Registering client fail. %s", e.getMessage()));
		}

	}

	private void updateClient(final String registrationId) {

		try {

			logger.info(String.format("Updating client... (applicationId: %d, token: %s, environment: %s)", applicationId, registrationId,
					environment));
			client.setToken(registrationId);
			client.setEnvironment(environment);
			client = Client.update(client.getGrowthbeatClientId(), credentialId, registrationId, environment);
			logger.info(String.format("Update client success (clientId: %d)", client.getId()));

			Client.save(client);
			latch.countDown();

		} catch (GrowthPushException e) {
			logger.error(String.format("Updating client fail. %s", e.getMessage()));
		}

	}

	public void trackEvent(final String eventId) {
		trackEvent(eventId, new HashMap<String, String>());
	}

	public void trackEvent(final String eventId, Map<String, String> properties) {
		GrowthAnalytics.getInstance().trackEvent(eventId, properties);
	}

	public void setTag(final String tagId) {
		setTag(tagId, null);
	}

	public void setTag(final String tagId, final String value) {
		GrowthAnalytics.getInstance().setTag(tagId, value);
	}

	public void setDeviceTags() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (context == null)
					throw new IllegalStateException("GrowthPush is not initialized.");

				setTag("Device", DeviceUtils.getModel());
				setTag("OS", "Android " + DeviceUtils.getOsVersion());
				setTag("Language", DeviceUtils.getLanguage());
				setTag("Time Zone", DeviceUtils.getTimeZone());
				setTag("Version", AppUtils.getaAppVersion(context));
				setTag("Build", AppUtils.getAppBuild(context));

			}

		}).start();

	}

	public void setReceiveHandler(ReceiveHandler receiveHandler) {
		this.receiveHandler = receiveHandler;
	}

	public ReceiveHandler getReceiveHandler() {
		return receiveHandler;
	}

	public Logger getLogger() {
		return logger;
	}

	public GrowthbeatHttpClient getHttpClient() {
		return httpClient;
	}

	public Preference getPreference() {
		return preference;
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
		Client.clear();

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
			GrowthPush.getInstance().getLogger().warning(message);
			e.printStackTrace();
		}

	}

}
