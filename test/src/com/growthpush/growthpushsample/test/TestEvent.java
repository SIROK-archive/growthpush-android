package com.growthpush.growthpushsample.test;

import com.growthpush.GrowthPush;
import com.growthpush.growthpushsample.MainActivity;
import com.growthpush.model.Environment;

public class TestEvent extends TestCase<MainActivity> {

	public TestEvent() {
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

	public void testTrackEvent() throws InterruptedException {
		GrowthPush.getInstance().trackEvent("Launch");
		Thread.sleep(1000);
	}

	public void testTrackEventWithInvalidName() throws InterruptedException {
		GrowthPush.getInstance().trackEvent(null);
		Thread.sleep(1000);
	}

	public void testTrackEventWithValue() throws InterruptedException {
		GrowthPush.getInstance().trackEvent("Payment", String.valueOf(500));
		Thread.sleep(1000);
	}

}
