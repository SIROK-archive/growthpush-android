package com.growthpush.growthpushsample.test;

import com.growthpush.GrowthPush;
import com.growthpush.growthpushsample.MainActivity;
import com.growthpush.model.Environment;

public class TestGrowthPush extends TestCase<MainActivity> {

	public TestGrowthPush() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		super.setUp();

		initialize();

		GrowthPush.getInstance().initialize(getActivity().getApplicationContext(), APPLICATION_ID, APPLICATION_SECRET,
				Environment.development, true);

	}

	public void testRegister() {
		GrowthPush.getInstance().register(SENDER_ID);
	}

	public void testRegisterWithInvalidSenderId() {
		GrowthPush.getInstance().register("INVALID_SENDER_ID");
	}

	public void testTrackEvent() {
		GrowthPush.getInstance().trackEvent("Launch");
	}

	public void testTrackEventWithInvalidName() {
		GrowthPush.getInstance().trackEvent(null);
	}

	public void testTrackEventWithValue() {
		GrowthPush.getInstance().trackEvent("Payment", String.valueOf(500));
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
