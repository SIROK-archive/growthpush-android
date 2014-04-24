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

	public void testTrackEvent() {
		GrowthPush.getInstance().trackEvent("Launch");
	}

	public void testTrackEventWithInvalidName() {
		GrowthPush.getInstance().trackEvent(null);
	}

	public void testTrackEventWithValue() {
		GrowthPush.getInstance().trackEvent("Payment", String.valueOf(500));
	}

}
