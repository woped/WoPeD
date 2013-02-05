package org.woped.quantana;

import org.woped.gui.translations.Messages;

public class Constants {
	public final static String QUANTANA_LOGGER = "QUANTANA_LOGGER";
	
	public static final String TIME_SECOND	= Messages.getString("Transition.Properties.Seconds");
	public static final String TIME_MINUTE	= Messages.getString("Transition.Properties.Minutes");
	public static final String TIME_HOUR	= Messages.getString("Transition.Properties.Hours");
	public static final String TIME_DAY		= Messages.getString("Transition.Properties.Days");
	public static final String TIME_WEEK	= Messages.getString("Transition.Properties.Weeks");
	public static final String TIME_MONTH	= Messages.getString("Transition.Properties.Months");
	public static final String TIME_YEAR	= Messages.getString("Transition.Properties.Years");
	public static final String[] TIMEUNITS = {Constants.TIME_SECOND, Constants.TIME_MINUTE, 
			Constants.TIME_HOUR, Constants.TIME_DAY, Constants.TIME_WEEK, 
			Constants.TIME_MONTH, Constants.TIME_YEAR};
}
