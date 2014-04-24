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

	public void testSetDeviceTags() {
		GrowthPush.getInstance().setDeviceTags();
		assertNotNull(Preference.getInstance().fetchTag("OS"));
		assertNotNull(Preference.getInstance().fetchTag("Version"));
		assertNotNull(Preference.getInstance().fetchTag("Language"));
		assertNotNull(Preference.getInstance().fetchTag("Time Zone"));
		assertNotNull(Preference.getInstance().fetchTag("Device"));
		assertNotNull(Preference.getInstance().fetchTag("Build"));
	}

	public void testSetTag() {
		assertNull(Preference.getInstance().fetchTag("Payed User"));
		GrowthPush.getInstance().setTag("Payed User");
		assertNotNull(Preference.getInstance().fetchTag("Payed User"));
	}

	public void testSetTagWithInvalidName() {
		GrowthPush.getInstance().setTag(null);
	}

	public void testSetTagWithValu() {
		assertNull(Preference.getInstance().fetchTag("Gender"));
		GrowthPush.getInstance().setTag("Gender", "male");
		assertNotNull(Preference.getInstance().fetchTag("Gender"));
		assertEquals("male", Preference.getInstance().fetchTag("Gender").getValue());
	}

}
