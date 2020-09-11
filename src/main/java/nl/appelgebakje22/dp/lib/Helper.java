package nl.appelgebakje22.dp.lib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Helper {

	public static Properties getDatabaseProps() {
		try (InputStream stream = Helper.class.getResourceAsStream("/db.properties")) {
			Properties result = new Properties();
			result.load(stream);
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void doSilently(Runnable runnable) {
		try {
			runnable.run();
		} catch (Throwable ignored) {
		}
	}
}