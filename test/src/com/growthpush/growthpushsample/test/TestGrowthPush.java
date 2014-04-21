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

		Thread.sleep(10);

	}

	public void testRegisterWithDevelopment() throws Exception {

		initialize();
		GrowthPush.getInstance().initialize(getActivity().getApplicationContext(), APPLICATION_ID, APPLICATION_SECRET,
				Environment.development, true);
		GrowthPush.getInstance().register(SENDER_ID);

		waitClient(30);
		assertNotNull(GrowthPush.getInstance().getClient());
		assertEquals(Environment.development, GrowthPush.getInstance().getClient().getEnvironment());

	}

	public void testRegisterWithProduction() throws Exception {

		initialize();
		GrowthPush.getInstance().initialize(getActivity().getApplicationContext(), APPLICATION_ID, APPLICATION_SECRET,
				Environment.production, true);
		GrowthPush.getInstance().register(SENDER_ID);

		waitClient(30);
		assertNotNull(GrowthPush.getInstance().getClient());
		assertEquals(Environment.production, GrowthPush.getInstance().getClient().getEnvironment());

	}

	public void testRegisterWithInvalidSenderId() throws Exception {

		initialize();
		GrowthPush.getInstance().initialize(getActivity().getApplicationContext(), APPLICATION_ID, APPLICATION_SECRET,
				Environment.development, true);
		GrowthPush.getInstance().register("INVALID_SENDER_ID");

		waitClient(10);

		assertTrue(GrowthPush.getInstance().getClient() == null || GrowthPush.getInstance().getClient().getId() == 0);

	}

}
