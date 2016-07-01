package de.tmtomcat.em.webclient;

import java.util.Random;

public class ErrorMessage {
	static private String[] messages = {
			"The input has to be a positive integer", "mach´s noamoi" };

	public static String get() {
		if (messages.length <= 0)
			return null;

		return messages[new Random().nextInt(messages.length)];
	}

}
