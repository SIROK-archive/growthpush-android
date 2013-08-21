package com.growthpush.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

	public static String toString(InputStream inputStream) throws IOException {

		InputStreamReader objReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(objReader);
		StringBuilder stringBuilder = new StringBuilder();

		try {

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}

			return stringBuilder.toString();

		} catch (IOException e) {
			throw new IOException("Failed to convert InputStream to String.", e);
		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new IOException("Failed to close InputStream.", e);
				}
			}

		}

	}

}
