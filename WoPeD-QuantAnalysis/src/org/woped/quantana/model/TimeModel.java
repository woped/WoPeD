package org.woped.quantana.model;

public class TimeModel {
	
	// Anm.: 1 Jahr = 360 Tage, 1 Monat = 30 Tage !!!
	
	public static final int TM_SECOND 	= 0;
	public static final int TM_MINUTE 	= 1;
	public static final int TM_HOUR 	= 2;
	public static final int TM_DAY 		= 3;
	public static final int TM_WEEK		= 4;
	public static final int TM_MONTH 	= 5;
	public static final int TM_YEAR 	= 6;
	
	private static final double TM_MIN_TO_SEC 		= 60;
	private static final double TM_HOUR_TO_MIN 		= 60;
	private static final double TM_DAY_TO_HOUR 		= 24;
	private static final double TM_WEEK_TO_DAY		= 7;
	private static final double TM_MONTH_TO_DAY 	= 30; // siehe Anm.
	private static final double TM_YEAR_TO_MONTH 	= 12;
	
	private int stdUnit = 2;
	private double stdUnitMultiple = 1.0;
	
	private double cvMinToSec = TM_MIN_TO_SEC;
	private double cvHourToMin = TM_HOUR_TO_MIN;
	private double cvDayToHour = TM_DAY_TO_HOUR;
	private double cvWeekToDay = TM_WEEK_TO_DAY;
	private double cvMonthToDay = TM_MONTH_TO_DAY;
	private double cvYearToMonth = TM_YEAR_TO_MONTH;
	
	private double cvHourToSec;
	private double cvDayToSec;
	private double cvWeekToSec;
	private double cvMonthToSec;
	private double cvYearToSec;
	
	private double cvDayToMin;
	private double cvWeekToMin;
	private double cvMonthToMin;
	private double cvYearToMin;
	
	private double cvWeekToHour;
	private double cvMonthToHour;
	private double cvYearToHour;
	
	private double cvMonthToWeek;
	private double cvYearToWeek;
	
	private double cvYearToDay;
	
	public TimeModel(int unit, double multiple){
		stdUnit = unit;
		stdUnitMultiple = multiple;
		
		init();
	}
	
	public TimeModel(int unit, double multiple, double[] cvValues){
		// Explizite Angabe der Umrechnungsfaktoren für
		// spezifische Anwendungen
		
		// Das Array entält genau 5 Werte für aufsteigende Folge
		// von Umrechnungsfaktoren von der größeren zur direkt
		// folgenden kleineren Einheit
		
		stdUnit = unit;
		stdUnitMultiple = multiple;
		
		cvMinToSec = cvValues[0];
		cvHourToMin = cvValues[1];
		cvDayToHour = cvValues[2];
		cvWeekToDay = cvValues[3];
		cvMonthToDay = cvValues[4];
		cvYearToMonth = cvValues[5];
		
		init();
	}

	public int getStdUnit() {
		return stdUnit;
	}

	public void setStdUnit(int stdUnit) {
		this.stdUnit = stdUnit;
	}

	public double getStdUnitMultiple() {
		return stdUnitMultiple;
	}

	public void setStdUnitMultiple(double stdUnitMultiple) {
		this.stdUnitMultiple = stdUnitMultiple;
	}
	
	// converts given time to standard time
	public double cv(int u, double m){
		double val = 0.0;

		if (u != stdUnit){
			switch (u){
			case 0:		// u is TM_SECOND
				switch (stdUnit){
				case 2:		// stdUnit is TM_HOUR
					val = m / cvHourToSec;
					break;
				case 3:		// stdUnit is TM_DAY
					val = m / cvDayToSec;
					break;
				case 4:		// stdUnit is TM_WEEK
					val = m / cvWeekToSec;
					break;
				case 5:		// stdUnit is TM_MONTH
					val = m / cvMonthToSec;
					break;
				case 6:		// stdUnit is TM_YEAR
					val = m / cvYearToSec;
					break;
				default:	// stdUnit is TM_MINUTE
					val = m / cvMinToSec;
				}
				break;

			case 2:		// u is TM_HOUR
				switch (stdUnit){
				case 0:		// stdUnit is TM_SECOND
					val = m * cvHourToSec;
					break;
				case 3:		// stdUnit is TM_DAY
					val = m / cvDayToHour;
					break;
				case 4:		// stdUnit is TM_WEEK
					val = m / cvWeekToHour;
					break;
				case 5:		// stdUnit is TM_MONTH
					val = m / cvMonthToHour;
					break;
				case 6:		// stdUnit is TM_YEAR
					val = m / cvYearToHour;
					break;
				default:	// stdUnit is TM_MINUTE
					val = m * cvHourToMin;
				}
				break;

			case 3:		// u is TM_DAY
				switch (stdUnit){
				case 0:		// stdUnit is TM_SECOND
					val = m * cvDayToSec;
					break;
				case 2:		// stdUnit is TM_HOUR
					val = m * cvDayToHour;
					break;
				case 4:		// stdUnit is TM_WEEK
					val = m / cvWeekToDay;
					break;
				case 5:		// stdUnit is TM_MONTH
					val = m / cvMonthToDay;
					break;
				case 6:		// stdUnit is TM_YEAR
					val = m / cvYearToDay;
					break;
				default:	// stdUnit is TM_MINUTE
					val = m * cvDayToMin;
				}
				break;

			case 4:		// u is TM_WEEK
				switch (stdUnit){
				case 0:		// stdUnit is TM_SECOND
					val = m * cvWeekToSec;
					break;
				case 2:		// stdUnit is TM_HOUR
					val = m * cvWeekToHour;
					break;
				case 3:		// stdUnit is TM_DAY
					val = m * cvWeekToDay;
					break;
				case 5:		// stdUnit is TM_MONTH
					val = m / cvMonthToWeek;
					break;
				case 6:		// stdUnit is TM_YEAR
					val = m / cvYearToWeek;
					break;
				default:	// stdUnit is TM_MINUTE
					val = m * cvWeekToMin;
				}
				break;

			case 5:		// u is TM_MONTH
				switch (stdUnit){
				case 0:		// stdUnit is TM_SECOND
					val = m * cvMonthToSec;
					break;
				case 2:		// stdUnit is TM_HOUR
					val = m * cvMonthToHour;
					break;
				case 3:		// stdUnit is TM_DAY
					val = m * cvMonthToDay;
					break;
				case 4:		// stdUnit is TM_WEEK
					val = m * cvMonthToWeek;
					break;
				case 6:		// stdUnit is TM_YEAR
					val = m / cvYearToMonth;
					break;
				default:	// stdUnit is TM_MINUTE
					val = m * cvMonthToMin;
				}
				break;

			case 6:		// u is TM_YEAR
				switch (stdUnit){
				case 0:		// stdUnit is TM_SECOND
					val = m * cvYearToSec;
					break;
				case 2:		// stdUnit is TM_HOUR
					val = m * cvYearToHour;
					break;
				case 3:		// stdUnit is TM_DAY
					val = m * cvYearToDay;
					break;
				case 4:		// stdUnit is TM_WEEK
					val = m * cvYearToWeek;
					break;
				case 5:		// stdUnit is TM_MONTH
					val = m * cvYearToMonth;
					break;
				default:	// stdUnit is TM_MINUTE
					val = m * cvYearToMin;
				}
				break;

			default:	// u is TM_MINUTE
				switch (stdUnit){
				case 0:		// stdUnit is TM_SECOND
					val = m * cvMinToSec;
					break;
				case 2:		// stdUnit is TM_HOUR
					val = m / cvHourToMin;
					break;
				case 3:		// stdUnit is TM_DAY
					val = m / cvDayToMin;
					break;
				case 4:		// stdUnit is TM_WEEK
					val = m / cvWeekToMin;
					break;
				case 5:		// stdUnit is TM_MONTH
					val = m / cvMonthToMin;
					break;
				case 6:		// stdUnit is TM_YEAR
					val = m / cvYearToMin;
				}
			}

			return val / stdUnitMultiple;
		} else {
			return m / stdUnitMultiple;
		}
	}
	
	public double cv(int u, double m, double orgFactor){
		return (cv(u,m)*orgFactor);
	}

	public void setCvDayToHour(double cvDayToHour) {
		this.cvDayToHour = cvDayToHour;
		init();
	}

	public void setCvHourToMin(double cvHourToMin) {
		this.cvHourToMin = cvHourToMin;
		init();
	}

	public void setCvMinToSec(double cvMinToSec) {
		this.cvMinToSec = cvMinToSec;
		init();
	}

	public void setCvMonthToDay(double cvMonthToDay) {
		this.cvMonthToDay = cvMonthToDay;
		init();
	}

	public void setCvYearToMonth(double cvYearToMonth) {
		this.cvYearToMonth = cvYearToMonth;
		init();
	}

	public void setCvWeekToDay(double cvWeekToDay) {
		this.cvWeekToDay = cvWeekToDay;
		init();
	}
	
	private void init(){
		cvHourToSec = cvHourToMin * cvMinToSec;
		cvDayToSec = cvDayToHour * cvHourToSec;
		cvMonthToSec = cvMonthToDay * cvDayToSec;
		cvYearToSec = cvYearToMonth * cvMonthToSec;
		cvDayToMin = cvDayToHour * cvHourToMin;
		cvMonthToMin = cvMonthToDay * cvDayToMin;
		cvYearToMin = cvYearToMonth * cvMonthToMin;
		cvMonthToHour = cvMonthToDay * cvDayToHour;
		cvYearToHour = cvYearToMonth * cvMonthToHour;
		cvYearToDay = cvYearToMonth * cvMonthToDay;
		
		cvWeekToSec = cvWeekToDay * cvDayToSec;
		cvWeekToMin = cvWeekToDay * cvDayToMin;
		cvWeekToHour = cvWeekToDay * cvDayToHour;
		cvMonthToWeek = cvMonthToDay / cvWeekToDay;
		cvYearToWeek = cvYearToMonth * cvMonthToWeek;
	}
}
