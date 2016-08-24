package org.woped.core.utilities;

import java.util.HashMap;

public class LoggerManager
{

    private static HashMap<String, Object> list = new HashMap<>();

    public static void register(Object logger, String loggerName)
    {
        if (logger instanceof ILogger)
        {
            list.put(loggerName, logger);
        }
    }

    public static void debug(String logger, String msg)
    {
        if (list.containsKey(logger))
        {
            ((ILogger) list.get(logger)).debug(msg);
        }
    }

    public static void info(String logger, String msg)
    {
        if (list.containsKey(logger))
        {
            ((ILogger) list.get(logger)).info(msg);
        }
    }

    public static void warn(String logger, String msg)
    {
        if (list.containsKey(logger))
        {
            ((ILogger) list.get(logger)).warn(msg);
        }
    }

    public static void error(String logger, String msg)
    {
        if (list.containsKey(logger))
        {
            ((ILogger) list.get(logger)).error(msg);
        }
    }

    public static void fatal(String logger, String msg)
    {
        if (list.containsKey(logger))
        {
            ((ILogger) list.get(logger)).fatal(msg);
        }
    }

    public static void resetForTesting(){
        list = new HashMap<>();
    }

}
