package com.growthpush;

public class GrowthPushException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GrowthPushException() {
		super();
	}

	public GrowthPushException(String message) {
		super(message);
	}

	public GrowthPushException(Throwable throwable) {
		super(throwable);
	}

	public GrowthPushException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
