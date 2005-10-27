package org.woped.core.config;

import java.util.HashMap;

public class LoggerManager {

	private static HashMap list = new HashMap();

	public static void register(ILogger logger, String loggerName) {
		list.put(loggerName, logger);
	}

	public static void debug(String logger, String msg) {
		if (list.containsKey(logger)) {
			((ILogger) list.get(logger)).debug(msg);
		}
	}

	public static void info(String logger, String msg) {
		if (list.containsKey(logger)) {
			((ILogger) list.get(logger)).info(msg);
		}
	}

	public static void warn(String logger, String msg) {
		if (list.containsKey(logger)) {
			((ILogger) list.get(logger)).warn(msg);
		}
	}

	public static void error(String logger, String msg) {
		if (list.containsKey(logger)) {
			((ILogger) list.get(logger)).error(msg);
		}
	}

	public static void fatal(String logger, String msg) {
		if (list.containsKey(logger)) {
			((ILogger) list.get(logger)).fatal(msg);
		}
	}

}
