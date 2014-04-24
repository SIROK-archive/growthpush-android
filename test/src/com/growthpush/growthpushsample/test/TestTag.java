package com.growthpush.growthpushsample.test;

import com.growthpush.GrowthPush;
import com.growthpush.Preference;
import com.growthpush.growthpushsample.MainActivity;
import com.growthpush.model.Environment;

public class TestTag extends TestCase<MainActivity> {

	public TestTag() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		super.setUp();

		initialize();

		GrowthPush.getInstance().initialize(getActivity().getApplicationContext(), APPLICATION_ID, APPLICATION_SECRET,
				Environment.development, true);
		GrowthPush.getInstance().register(SENDER_ID);

		waitClient();

	}

	public void testSetDeviceTags() throws InterruptedException {
		GrowthPush.getInstance().setDeviceTags();
		Thread.sleep(2000);
		assertNotNull(Preference.getInstance().fetchTag("OS"));
		assertNotNull(Preference.getInstance().fetchTag("Version"));
		assertNotNull(Preference.getInstance().fetchTag("Language"));
		assertNotNull(Preference.getInstance().fetchTag("Time Zone"));
		assertNotNull(Preference.getInstance().fetchTag("Device"));
		assertNotNull(Preference.getInstance().fetchTag("Build"));
	}

	public void testSetTag() throws InterruptedException {
		assertNull(Preference.getInstance().fetchTag("Payed User"));
		GrowthPush.getInstance().setTag("Payed User");
		Thread.sleep(2000);
		assertNotNull(Preference.getInstance().fetchTag("Payed User"));
	}

	public void testSetTagWithInvalidName() throws InterruptedException {
		GrowthPush.getInstance().setTag(null);
		Thread.sleep(1000);
	}

	public void testSetTagWithValue() throws InterruptedException {
		assertNull(Preference.getInstance().fetchTag("Gender"));
		GrowthPush.getInstance().setTag("Gender", "male");
		Thread.sleep(2000);
		assertNotNull(Preference.getInstance().fetchTag("Gender"));
		assertEquals("male", Preference.getInstance().fetchTag("Gender").getValue());
	}

}
