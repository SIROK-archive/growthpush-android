package com.growthpush;

public class Thread extends java.lang.Thread {

	public Thread() {
		super();
		initializeUncaughtExceptionHandler();
	}

	public Thread(Runnable runnable) {
		super(runnable);
		initializeUncaughtExceptionHandler();
	}

	public Thread(Runnable runnable, String threadName) {
		super(runnable, threadName);
		initializeUncaughtExceptionHandler();
	}

	private void initializeUncaughtExceptionHandler() {

		setUncaughtExceptionHandler(new java.lang.Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(java.lang.Thread thread, Throwable e) {
				String message = "Uncaught Exception: " + e.getClass().getName();
				if (e.getMessage() != null)
					message += "; " + e.getMessage();
				GrowthPush.getInstance().getLogger().warning(message);
			}
		});

	}

}
