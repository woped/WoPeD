/*
 * Created on 28.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.starter.utilities;

import org.apache.log4j.Logger;
import org.woped.core.utilities.ILogger;

/**
 * @author lai
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class WopedLogger implements ILogger
{

    Logger log4jLogger = null;

    public WopedLogger(Logger log4jLogger)
    {
        this.log4jLogger = log4jLogger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.core.utilities.ILogger#debug(java.lang.String)
     */
    public void debug(String msg)
    {
        log4jLogger.debug(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.core.utilities.ILogger#info(java.lang.String)
     */
    public void info(String msg)
    {
        log4jLogger.info(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.core.utilities.ILogger#warn(java.lang.String)
     */
    public void warn(String msg)
    {
        log4jLogger.warn(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.core.utilities.ILogger#error(java.lang.String)
     */
    public void error(String msg)
    {
        log4jLogger.error(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.core.utilities.ILogger#fatal(java.lang.String)
     */
    public void fatal(String msg)
    {
        log4jLogger.fatal(msg);
    }
}
