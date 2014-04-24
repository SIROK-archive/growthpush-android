package com.growthpush.growthpushsample.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.growthpush.GrowthPush;
import com.growthpush.Preference;

public class TestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

	protected static final int APPLICATION_ID = 1071;
	protected static final String APPLICATION_SECRET = "Ou3DgCwmMS2tBocWXGKSnRUUTyVA078n";
	protected static final String SENDER_ID = "955057365401";

	public TestCase(Class activityClass) {
		super(activityClass);
	}

	protected void initialize() throws Exception {

		initializePreference();
		initializeGrowthPush();

	}

	protected void initializePreference() throws Exception {

		Field fileNameField = Preference.class.getDeclaredField("FILE_NAME");
		fileNameField.setAccessible(true);
		String fileName = (String) fileNameField.get(null);
		getActivity().getFileStreamPath(fileName).delete();

		Constructor<Preference> preferenceConstructor = Preference.class.getDeclaredConstructor();
		preferenceConstructor.setAccessible(true);
		Preference preference = preferenceConstructor.newInstance();

		Field instanceField = Preference.class.getDeclaredField("instance");
		instanceField.setAccessible(true);
		instanceField.set(null, preference);

	}

	protected void initializeGrowthPush() throws Exception {

		Constructor<GrowthPush> growthPushConstructor = GrowthPush.class.getDeclaredConstructor();
		growthPushConstructor.setAccessible(true);
		GrowthPush growthPush = growthPushConstructor.newInstance();

		Field instanceField = GrowthPush.class.getDeclaredField("instance");
		instanceField.setAccessible(true);
		instanceField.set(null, growthPush);

	}

	protected void waitClient() throws Exception {
		waitClient(30);
	}

	protected void waitClient(int second) throws Exception {

		for (int i = 0; i < second; i++) {
			if (GrowthPush.getInstance().getClient() != null)
				return;
			Thread.sleep(1000);
		}

		throw new RuntimeException("wating client timeout");

	}

}
