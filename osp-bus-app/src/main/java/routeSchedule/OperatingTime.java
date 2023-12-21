package routeSchedule;
import java.time.LocalTime;

public class OperatingTime {	
	/* Outlines the types of operating times: a range of times, a time with pre-continuity,
	 * post-continuity, or all day. */
	protected enum TYPE {RANGE,PRE,POST,SPANSDAY};
	
	private LocalTime startTime;
	private LocalTime endTime;
	private TYPE type;
	
	/* Accepts start and end times as a of 24-hour HH:MM-HH:MM ("HUMAN") format in EST and stores
	* them as LocalTime instances. Arguments must be between 00:00 and 23:59 or null to indicate
	* the operating window spans before/after the current day */
	public OperatingTime(String timeString) {
		RouteSchedule.ERRORS error = RouteSchedule.ERRORS.NONE;
		
		// Parse and check appropriately given the specified values
		/* If no start "time"/String is specified, only parse the endTime and modify the
		 * endTime attribute */
		if (spansDay(timeString)) {
			this.startTime = LocalTime.of(0, 0);
			this.endTime = LocalTime.of(23, 59);
			this.type = TYPE.SPANSDAY;
		} else if (continuesBefore(timeString, false)) {
			String endString = timeString.substring(1,6);
			
			// Parse each String into integers
			int endHourInt = parseHour(endString);
			int endMinuteInt = parseMinute(endString);
			
			/* Validate that those integers fall in the range of 24-hour format (00:00 and 23:59)
			 * by attempting to instantiate a LocalTime object with the given hour and minute.
			 * If invalid, catch the error, don't set the OperatingTime's attributes, and print
			 * the error to the console. */
			try {
				LocalTime end = LocalTime.of(endHourInt, endMinuteInt);
				this.startTime = LocalTime.of(0,0);
				this.endTime = end;
				this.type = TYPE.PRE;
			} catch (Exception e) {
				error = RouteSchedule.ERRORS.FORMAT;
			}
			
		/* If both a start and end "time"/String is specified, parse both and modify both
		 * attributes */
		} else if (isTimeRange(timeString, false)) {
			String startString = timeString.substring(0,5);
			String endString = timeString.substring(6,11);
			
			
			// Parse each String into integers
			int startHourInt = parseHour(startString);
			int startMinuteInt = parseMinute(startString);
			int endHourInt = parseHour(endString);
			int endMinuteInt = parseMinute(endString);
			
			/* Validate that those integers fall in the range of 24-hour format (00:00 and 23:59)
			 * by attempting to instantiate a LocalTime object with the given hour and minute.
			 * If invalid, catch the error, don't set the OperatingTime's attributes, and print
			 * the error to the console. */
			// Validate that those integers fall in the range of 24-hour format (00:00 and 23:59)
			try {
				LocalTime start = LocalTime.of(startHourInt, startMinuteInt);
				LocalTime end = LocalTime.of(endHourInt, endMinuteInt);
				
				// Finally, check that the given times are valid (either equal or chronological)
				if (start.isBefore(end) || start.equals(end)) {
					this.startTime = start;
					this.endTime = end;
					this.type = TYPE.RANGE;
				} else {
					error = RouteSchedule.ERRORS.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.ERRORS.FORMAT;
			}
			
		/* If no end "time"/String is specified, only parse the startTime and modify the
		 * startTime attribute */
		} else if (continuesAfter(timeString, false)) {
			String startString = timeString.substring(0,5);
			
			// Parse each String into integers
			int startHourInt = parseHour(startString);
			int startMinuteInt = parseMinute(startString);
			
			/* Validate that those integers fall in the range of 24-hour format (00:00 and 23:59)
			 * by attempting to instantiate a LocalTime object with the given hour and minute.
			 * If invalid, catch the error, don't set the OperatingTime's attributes, and print
			 * the error to the console. */
			try {
				LocalTime start = LocalTime.of(startHourInt, startMinuteInt);
				this.startTime = start;
				this.endTime = LocalTime.of(23, 59);
				this.type = TYPE.POST;
			} catch (Exception e) {
				error = RouteSchedule.ERRORS.FORMAT;
			}
		} // If neither a start or end "time"/String is specified (passed as null), do nothing

		// If an error occurred, print the correct message
		if (error != RouteSchedule.ERRORS.NONE) {
			System.out.println(errorMessage(error));
		}
	}
	
	/* Accepts a start and end LocalTime object (permits instantiation of "SPANSDAY" or
	 * "RANGE"-type OperatingTimes) */
	public OperatingTime(LocalTime startTime, LocalTime endTime) {		
		this.startTime = startTime;
		this.endTime = endTime;
		
		// Determine the type of operating time based on the start and end times
		if (startTime.equals(LocalTime.of(0, 0)) && endTime.equals(LocalTime.of(23, 59))) {
			this.type = TYPE.SPANSDAY;
		} else {
			this.type = TYPE.RANGE;
		}
	}
	
	/* Accepts a toOrFromTime LocalTime object that will set this.endTime = toOrFromTime
	 * should continuity == TYPE.PRE or this.startTime = toOrFromTime should continuity == 
	 * TYPE.POST */
	public OperatingTime(TYPE continuity, LocalTime toOrFromTime) {
		if (continuity == TYPE.PRE) {
			this.endTime = toOrFromTime;
			this.type = TYPE.PRE;
		} else if (continuity == TYPE.POST) {
			this.startTime = toOrFromTime;
			this.type = TYPE.POST;
		}
	}
	
	// GETTERS/SETTERS
	public LocalTime startTime() {
		return this.startTime;
	}
	
	public LocalTime endTime() {
		return this.endTime;
	}
	
	public TYPE type() {
		return this.type;
	}
	
	// METHODS
	/* Accepts an "encoded" time String (as would be stored in the database) and instantiates an OperatingTime
	 * object using the args found through parsing. Format is either -HHMM, HHMM-, or HHMM-HHMM and uses 24-Hour time. */
	protected static OperatingTime decode(String encodedTime) {
		/* Determine whether the encodedTimeRange has any continuities, a fully-defined range, or
		 * spans the whole day (1 character) and parse accordingly. */
		// If the encodedTime is 9 characters, it is assumed to be a fully-defined range in HHMM-HHMM format
		if (encodedTime.length() == 9) {
			/* Parse the encodedTimeRange into its component times and format them so
			 * they can be passed to the OperatingTime constructor. */
			String startTime = encodedTime.substring(0,4);
			String endTime = encodedTime.substring(5,9);
			
			String startFormatted = formatEncodedTime(startTime, false);
			String endFormatted = formatEncodedTime(endTime, false);
			
			// Parse each component time String into integers
			return new OperatingTime(startFormatted + "-" + endFormatted);
			
		// If the encodedTime is 1 character, it is assumed to span the whole day, indicated by -
		} else if (encodedTime.length() == 1) {
			return new OperatingTime("-");
		
		// If neither of these special cases are met, then there is a continuity
		} else {
			String time = encodedTime;
			boolean preContinuity = continuesBefore(time, true);
			
			/* Determine if it's a case of pre- or post-continuity and format the time so it can be passed
			 * to the OperatingTime constructor. */
			if (preContinuity) {
				String timeFormatted = formatEncodedTime(time.substring(1,5), false);
				return new OperatingTime("-" + timeFormatted);
			} else {
				String timeFormatted = formatEncodedTime(time.substring(0,4), false);
				return new OperatingTime(timeFormatted + "-");
			}
		}
	}
	
	// Encodes this OperatingTime as a string capable of being decoded back into an OperatingTime object
	public String encode() {
		// Determine this.type and output the correct encoded string
		switch(this.type) {
			case PRE:
				return '-' + stripTimeString(this.endTime.toString());
			case POST:
				return stripTimeString(this.startTime.toString()) + '-';
			case SPANSDAY:
				return "-";
			default:
				return stripTimeString(this.startTime.toString()) + '-' + stripTimeString(this.endTime.toString());
		}
	}
	
	// Return the operating time range as a String in 24-Hour HH:MM-HH:MM ("Human") format
	public String toString() {
		// Return this object's properties in a fr
		switch (this.type) {
			case PRE:
				return "-" + this.endTime.toString();
			case POST:
				return this.startTime.toString() + "-";
			case SPANSDAY:
				return "-";
			case RANGE:
				return this.startTime.toString() + "-" + this.endTime.toString();
			default:
				errorMessage(RouteSchedule.ERRORS.INVALID_FIELDS);
				return "Uninitialized OperatingTime";
		}
	}
	
	// Returns true if this has the same attribute values as that
	public boolean equals(OperatingTime that) {
		boolean timesAreSame = this.startTime.equals(that.startTime) && this.endTime.equals(that.endTime);
		boolean typesAreSame = this.type == that.type;
		return timesAreSame && typesAreSame;
	}
	
	// Return true if the passed LocalDate falls within this startDate and endDate
	public boolean operatesAt(LocalTime time) {		
		boolean isAfterStart = this.startTime.isBefore(time);
		boolean isBeforeEnd = this.endTime.plusSeconds(59).isAfter(time); // Allow checks to be inclusive
		
		return isAfterStart && isBeforeEnd;
	}
	
	// Return the correct error message String given the RouteSchedule ERROR provided as an argument.
	private static String errorMessage(RouteSchedule.ERRORS error) {
		switch (error) {
			case FORMAT:
				return "ERROR: OperatingTime Constructor: Time is invalid.";
			case CHRONOLOGICAL:
				return "ERROR: OperatingTime Constructor: Start and end times are not chronological.";
			case INVALID_FIELDS:
				return "WARNING: OperatingTime has invalid field(s).";
			default:
				return "ERROR: OperatingTime: Unknown error occurred.";
		}
	}
	
	// If the passed timeString begins with a '-', return true
	private static boolean continuesBefore(String timeString, boolean isEncoded) {
		if (isEncoded) {
			return timeString.length() == 5 && timeString.charAt(0) == '-';
		} else {
			return timeString.length() == 6 && timeString.charAt(0) == '-';
		}
	}
	
	// If the passed timeString ends with a '-', return true
	private static boolean continuesAfter(String timeString, boolean isEncoded) {
		if (isEncoded) {
			return timeString.length() == 5 && timeString.charAt(4) == '-';
		} else {
			return timeString.length() == 6 && timeString.charAt(5) == '-';
		}
	}
	
	// If the passed timeString contains '-' between two HH:MM-formatted times, return true
	private static boolean isTimeRange(String timeString, boolean isEncoded) {
		if (isEncoded) {
			return timeString.length() == 9 && timeString.charAt(4) == '-';
		} else {
			return timeString.length() == 11 && timeString.charAt(5) == '-';
		}
	}
	
	// PARSERS
	/* Parse the hour from the fullTime String into an int (given it's in 24-hour HH:MM format).
	* If the string cannot be parsed into an int, return the sentinel -1, which will print a format error
	* in the constructor */
	private static int parseHour(String fullTime) {
		try {
			return Integer.parseInt(fullTime.substring(0,2));
		} catch (Exception e) {
			return -1;
		}
	}
	
	/* Parse the minute from the fullTime String into an int (given it's in 24-hour HH:MM format).
	* If the string cannot be parsed into an int, return the sentinel -1, which will print a format error
	* in the constructor */
	private static int parseMinute(String fullTime) {
		try {
			return Integer.parseInt(fullTime.substring(3,5));
		} catch (Exception e) {
			return -1;
		}
	}
	
	// If the passed timeString is simply '-', return true
	private static boolean spansDay(String timeString) {
		return timeString.length() == 1 && timeString.charAt(0) == '-';
	}
	
	// FORMATTERS
	/* Returns the encoded time (HHMM) formatted into human-readable HH:MM if twelveHourFormat is false or
	 * (H)H:MMam/pm (12-hour format) if twelveHourFormat is true. */
	protected static String formatEncodedTime(String encodedTime, boolean twelveHourFormat) {
		// If the desired return type is 12-hour format...
		if (twelveHourFormat) {
			String twentyFourHourStr = encodedTime.substring(0,2);
			String minuteStr = encodedTime.substring(2,4);
			String timeStr = "";
			int twentyFourHourInt = Integer.parseInt(twentyFourHourStr);
			
			// Translate encodedTime, which is in 24-hour format, into 12-hour format
			if (twentyFourHourInt > 12) {
				timeStr = String.valueOf(twentyFourHourInt - 12);
				timeStr += ":" + minuteStr + "pm";
			} else {
				timeStr = twentyFourHourStr.charAt(1) + ":" + minuteStr + "am";
			}
			
			return timeStr;
		// Otherwise, assume 24-hour format is desired...
		} else {
			return encodedTime.substring(0,2) + ":" + encodedTime.substring(2,4);
		}
	}
	
	// Takes a human-readable HH:MM time and returns it in encoded HHMM format
	protected static String stripTimeString(String time) {
		return time.substring(0,2) + time.substring(3,5);
	}
}