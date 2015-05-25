# Growth Push SDK for Android

[Growth Push](https://growthpush.com/) is push notification and analysis platform for mobile apps.

## Usage 

1. Install [Growthbeat Core SDK](https://github.com/SIROK/growthbeat-core-android).

1. Add growthpush.jar, google-play-services.jar and android-support-v4.jar into libs directory in your project.

1. Write initialization code.

	```java
	GrowthPush.getInstance().initialize(getApplicationContext(), "APPLICATION_ID", "CREDENTIAL_ID",
	BuildConfig.DEBUG ? Environment.development : Environment.production, "SENDER_ID");
	```

	GrowthPush instance will get GCM registration id, send it to server. You can get APPLICATION_ID and CREDENTIAL_ID on web site of GrowthPush and SENDER_ID on Google API Console.

1. (Optional) If you would like to use analytics platform or segment notification, track events or set tags with following code.

	```objc
	GrowthPush.getInstance().trackEvent("NAME", "VALUE");
	GrowthPush.getInstance().setTag("NAME", "VALUE");
	```

## Growthbeat Full SDK

You can use Growthbeat SDK instead of this SDK. Growthbeat is growth hack tool for mobile apps. You can use full functions include Growth Push when you use the following SDK.

* [Growthbeat SDK for iOS](https://github.com/SIROK/growthbeat-ios/)
* [Growthbeat SDK for Android](https://github.com/SIROK/growthbeat-android/)

## License

Apache License, Version 2.0


