package com.growthpush.growthpushsample.test;

import com.growthpush.GrowthPush;
import com.growthpush.growthpushsample.MainActivity;
import com.growthpush.model.Environment;

public class TestTag extends TestCase<MainActivity> {

	public TestTag() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		super.setUp();

		Thread.sleep(10);

		initialize();

		GrowthPush.getInstance().initialize(getActivity().getApplicationContext(), APPLICATION_ID, APPLICATION_SECRET,
				Environment.development, true);
		GrowthPush.getInstance().register(SENDER_ID);

		waitClient(30);

	}

	public void testSetDeviceTags() {
		GrowthPush.getInstance().setDeviceTags();
	}

	public void testSetTag() {
		GrowthPush.getInstance().setTag("Payed User");
	}

	public void testSetTagWithInvalidName() {
		GrowthPush.getInstance().setTag(null);
	}

	public void testSetTagWithValu() {
		GrowthPush.getInstance().setTag("Gender", "male");
	}

}
