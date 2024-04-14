package edu.uga.acm.osp.data.routeSchedule;
import java.time.LocalTime;

/**
 * Represents a range of {@code LocalTime}s during which a Route operates
 *
 * @see LocalTime
 */
public class OperatingTime {	
	/**
	 * Valid types of time ranges an {@code OperatingTime} can represent:
	 * <ul>
	 *     <li>{@link #RANGE}</li>
	 *     <li>{@link #PRE}</li>
	 *     <li>{@link #POST}</li>
	 *     <li>{@link #SPANS_DAY}</li>
	 * </ul>
	 */
	protected enum Type {
		/**
		 * A unique {@code startTime} and {@code endTime} are defined and are in chronological order, representing
		 * operation within that time range, inclusive
		 */
		RANGE,
		/**
		 * {@code startTime} is 12:00am and {@code endTime} is specified, representing operation that begins on a prior
		 * day and continues until {@code endTime} today ("pre-continuity")
		 */
		PRE,
		/**
		 * {@code startTime} is specified and {@code endTime} is 11:59pm, representing operation that begins today at
		 * {@code startTime} and continues until {@code endTime} someday after ("post-continuity")
		 */
		POST,
		/**
		 * {@code startTime} is 12:00am and {@code endTime} is 11:59pm, representing operation that begins on a prior
		 * day, continues throughout today, and ends someday after
		 */
		SPANS_DAY};
	
	private LocalTime startTime;
	private LocalTime endTime;
	private Type type;

	/**
	 * Instantiates an {@code OperatingTime} object provided a {@code String} time (HH:MM) (with or without continuity),
	 * inclusive time range (HH:MM-HH:MM), or the continuity symbol, '-'
	 *
	 * @param timeString the single time, time range, or continuity symbol with which to populate the new
	 * {@code OperatingTime}'s {@code startTime} and {@code endTime} fields
	 */
	public OperatingTime(String timeString) {
		RouteSchedule.Errors error = RouteSchedule.Errors.NONE;
		
		// Parse and check appropriately given the specified values
		/* If no start "time"/String is specified, only parse the endTime and modify the
		 * endTime attribute */
		if (spansDay(timeString)) {
			this.startTime = LocalTime.of(0, 0);
			this.endTime = LocalTime.of(23, 59);
			this.type = Type.SPANS_DAY;
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
				this.type = Type.PRE;
			} catch (Exception e) {
				error = RouteSchedule.Errors.FORMAT;
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
					this.type = Type.RANGE;
				} else {
					error = RouteSchedule.Errors.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.Errors.FORMAT;
			}
			
		/* If no end "time"/String is specified, only parse the startTime and modify the
		 * startTime attribute */
		} else if (continuesAfter(timeString, false)) {
			String startString = timeString.substring(0, 5);

			// Parse each String into integers
			int startHourInt = parseHour(startString);
			int startMinuteInt = parseMinute(startString);

			/* Validate that those integers fall in the range of 24-hour format (00:00 and 23:59)
			 * by attempting to instantiate a LocalTime object with the given hour and minute.
			 * If invalid, catch the error, don't set the OperatingTime's attributes, and print
			 * the error to the console. */
			try {
                this.startTime = LocalTime.of(startHourInt, startMinuteInt);
				this.endTime = LocalTime.of(23, 59);
				this.type = Type.POST;
			} catch (Exception e) {
				error = RouteSchedule.Errors.FORMAT;
			}
		// If the timeString is just a continuity symbol, treat it as such
		} else if (spansDay(timeString)) {
			this.startTime = LocalTime.of(0,0);
			this.endTime = LocalTime.of(23,59);
			this.type = Type.SPANS_DAY;
		} // If neither a start nor end "time"/String is specified (passed as null), do nothing

		// If an error occurred, print the correct message
		if (error != RouteSchedule.Errors.NONE) {
			System.out.println(errorMessage(error));
		}
	}

	/**
	 * Instantiates an {@code OperatingTime} object provided start and end {@code LocalTime} objects, representing
	 * the inclusive time range (or single time) in which a Route is operating
	 *
	 * @param startTime the first {@code LocalTime} operations exist at or {@code null}, representing
	 * continuous operation prior to {@code endTime} if {@code endTime != null} or operation spanning the day if
	 * {@code endTime == null}
	 *
	 * @param endTime the last {@code LocalTime} operations exist within or {@code null}, representing continuous
	 * operation past {@code startTime} if {@code startTime != null} or operation spanning hte day if
	 * {@code startTime == null}
	 *
	 * @see Type
	 */
	public OperatingTime(LocalTime startTime, LocalTime endTime) {
		// Determine the type of operating time based on the start and end times
		if (startTime == null && endTime != null) {
			this.startTime = LocalTime.of(0,0);
			this.endTime = endTime;
			this.type = Type.PRE;
		} else if (startTime != null && endTime == null) {
			this.startTime = startTime;
			this.endTime = LocalTime.of(23,59);
		} else if ((startTime != null && endTime != null) && (startTime.equals(LocalTime.of(0, 0)) && endTime.equals(LocalTime.of(23, 59)))) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.type = Type.SPANS_DAY;
		} else if ((startTime != null && endTime != null) && startTime.isBefore(endTime)){
			this.startTime = startTime;
			this.endTime = endTime;
			this.type = Type.RANGE;
		} else {
			System.out.println(errorMessage(RouteSchedule.Errors.INVALID_FIELDS));
		}
	}

	/**
	 * Instantiates an {@code OperatingTime} object provided a type of continuity and a {@code LocalTime} that continuity
	 * applies to
	 *
	 * @param continuity the type of continuity desired ({@code Type.PRE} or {@code Type.POST})
	 * @param toOrFromTime the time the continuity applies to
	 *
	 * @see Type#PRE
	 * @see Type#POST
	 * @see "README"
	 */
	public OperatingTime(Type continuity, LocalTime toOrFromTime) {
		if (continuity == Type.PRE) {
			this.endTime = toOrFromTime;
			this.type = Type.PRE;
		} else if (continuity == Type.POST) {
			this.startTime = toOrFromTime;
			this.type = Type.POST;
		}
	}
	
	// GETTERS/SETTERS
	/**
	 * Gets this {@code OperatingTime}'s {@code startTime}
	 *
	 * @return the {@code startTime} field of this {@code OperatingTime}
	 */
	public LocalTime startTime() {
		return this.startTime;
	}

	/**
	 * Gets this {@code OperatingTime}'s {@code startTime}
	 *
	 * @return the {@code startTime} field of this {@code OperatingTime}
	 */
	public LocalTime endTime() {
		return this.endTime;
	}

	/**
	 * Gets this {@code OperatingTime}'s {@code startTime}
	 *
	 * @return the {@code startTime} field of this {@code OperatingTime}
	 */
	public Type type() {
		return this.type;
	}
	
	// METHODS
	/**
	 * Instantiates an {@code OperatingTime} object by extracting the data contained within an encoded OperatingTime
	 * {@code String} and populating fields accordingly
	 *
	 * @param encodedTime the encoded OperatingTime {@code String} to decode
	 *
	 * @return an instantiated {@code OperatingTime} representing the data encoded in {@code encodedTime}
	 *
	 * @see "README"
	 */
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
            boolean preContinuity = continuesBefore(encodedTime, true);
			
			/* Determine if it's a case of pre- or post-continuity and format the time so it can be passed
			 * to the OperatingTime constructor. */
			if (preContinuity) {
				String timeFormatted = formatEncodedTime(encodedTime.substring(1,5), false);
				return new OperatingTime("-" + timeFormatted);
			} else {
				String timeFormatted = formatEncodedTime(encodedTime.substring(0,4), false);
				return new OperatingTime(timeFormatted + "-");
			}
		}
	}

	/**
	 * Encodes this {@code OperatingTime} object as a {@code String} capable of being decoded back into an identical
	 * {@code OperatingTime} object
	 *
	 * @return a {@code String} concisely representing this {@code OperatingTime}'s data
	 */
	public String encode() {
		// Determine this.type and output the correct encoded string
		switch(this.type) {
			case PRE:
				return '-' + stripTimeString(this.endTime.toString());
			case POST:
				return stripTimeString(this.startTime.toString()) + '-';
			case SPANS_DAY:
				return "-";
			default:
				return stripTimeString(this.startTime.toString()) + '-' + stripTimeString(this.endTime.toString());
		}
	}

	/**
	 * Creates a textual representation of this {@code OperatingTime}'s data in a brief, but human-friendly format
	 *
	 * @return <b>If {@code this.type == null}:</b><p>"Uninitialized OperatingTime"</p>
	 * <b>Else:</b>
	 * <p>a {@code String} containing this {@code OperatingTime}'s start and end {@code LocalTime}s in 24-Hour HH:MM
	 * format, with - placed before, after, or between the time(s) to represent continuity or a range</p>
	 */
	public String toString() {
		// Return this object's properties in a formatted String
		switch (this.type) {
			case PRE:
				return "-" + this.endTime.toString();
			case POST:
				return this.startTime.toString() + "-";
			case SPANS_DAY:
				return "-";
			case RANGE:
				return this.startTime.toString() + "-" + this.endTime.toString();
			default:
				System.out.println(errorMessage(RouteSchedule.Errors.INVALID_FIELDS));
				return "Uninitialized OperatingTime";
		}
	}

	/**
	 * Compares this {@code OperatingTime} to the passed {@code OperatingTime} based on {@code startTime} and
	 * {@code endTime} values
	 *
	 * @param that the {@code OperatingTime} object to compare to the invoking {@code OperatingTime}
	 *
	 * @return {@code true} if this {@code OperatingTime}'s {@code startTime} and {@code endTime} fields equal those of
	 * {@code that}
	 *
	 * @see LocalTime#equals
	 */
	public boolean equals(OperatingTime that) {
		boolean timesAreSame = this.startTime.equals(that.startTime) && this.endTime.equals(that.endTime);
		boolean typesAreSame = this.type == that.type;
		return timesAreSame && typesAreSame;
	}

	/**
	 * Determines whether a Route has operations at the given {@code time}
	 *
	 * @param time the {@code LocalTime} in which to check for operations
	 *
	 * @return {@code true} if the provided {@code time} falls between this {@code OperatingTime}'s
	 * {@code startTime} and {@code endTime}, inclusive
	 */
	protected boolean operatesAt(LocalTime time) {
		boolean isAfterStart = this.startTime.isBefore(time);
		boolean isBeforeEnd = this.endTime.plusSeconds(59).isAfter(time); // Allow checks to be inclusive
		
		return isAfterStart && isBeforeEnd;
	}

	/**
	 * Provides a standardized error message to print to the console given the provided error
	 *
	 * @param error the type of error whose message needs to be retrieved
	 *
	 * @return the correct error message given the passed error
	 */
	private static String errorMessage(RouteSchedule.Errors error) {
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

	/**
	 * Determines if the passed {@code timeString} represents a pre-continuous {@code OperatingTime} by checking
	 * if it begins with the continuity symbol, '-', and contains just one time
	 *
	 * @param timeString the {@code String} to check for pre-continuity and a single time
	 * @param isEncoded whether the {@code timeString} passed is encoded or in "human" HH:MM format
	 *
	 * @return {@code true} if just one time exists and the continuity symbol precedes it
	 *
	 * @see Type#PRE
	 * @see "README"
	 */
	private static boolean continuesBefore(String timeString, boolean isEncoded) {
		if (isEncoded) {
			return timeString.length() == 5 && timeString.charAt(0) == '-';
		} else {
			return timeString.length() == 6 && timeString.charAt(0) == '-';
		}
	}

	/**
	 * Determines if the passed {@code timeString} represents a post-continuous {@code OperatingTime} by checking if it
	 * ends with the continuity symbol, '-', and contains just one time
	 *
	 * @param timeString the {@code String} to check for post-continuity and a single time
	 * @param isEncoded whether the {@code timeString} passed is encoded or in "human" HH:MM format
	 *
	 * @return {@code true} if just one time exists and the continuity symbol follows it
	 *
	 * @see Type#POST
	 * @see "README"
	 */
	private static boolean continuesAfter(String timeString, boolean isEncoded) {
		if (isEncoded) {
			return timeString.length() == 5 && timeString.charAt(4) == '-';
		} else {
			return timeString.length() == 6 && timeString.charAt(5) == '-';
		}
	}

	/**
	 * Determines if the passed {@code timeString} represents a range-type {@code OperatingTime} by checking if two times
	 * exists surrounding the continuity symbol, '-'
	 *
	 * @param timeString the {@code String} to check for a time range
	 * @param isEncoded whether the {@code timeString} is encoded or in "human" HH:MM format
	 *
	 * @return {@code true} if two times exist with the continuity symbol, '-', between them
	 *
	 * @see Type#RANGE
	 * @see "README"
	 */
	private static boolean isTimeRange(String timeString, boolean isEncoded) {
		if (isEncoded) {
			return timeString.length() == 9 && timeString.charAt(4) == '-';
		} else {
			return timeString.length() == 11 && timeString.charAt(5) == '-';
		}
	}

	/**
	 * Determines if the passed {@code timeString} represents a range-type {@code OperatingTime} by checking if two times
	 * exists surrounding the continuity symbol, '-'
	 *
	 * @param timeString the {@code String} to check for a time range
	 *
	 * @return {@code true} if two times exist with the continuity symbol, '-', between them
	 *
	 * @see OperatingDate.Type#RANGE
	 * @see "README"
	 */
	private static boolean spansDay(String timeString) {
		return timeString.length() == 1 && timeString.charAt(0) == '-';
	}
	
	// PARSERS
	/**
	 * Parses the hour integer from {@code fullTime}
	 *
	 * @param fullTime a time {@code String} in HH:MM format
	 *
	 * @return the {@code Integer} representation of HH or -1 should an error occur
	 */
	private static int parseHour(String fullTime) {
		try {
			return Integer.parseInt(fullTime.substring(0,2));
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Parses the minute integer from {@code fullTime}
	 *
	 * @param fullTime a time {@code String} in HH:MM format
	 *
	 * @return the {@code Integer} representation of MM or -1 should an error occur
	 */
	private static int parseMinute(String fullTime) {
		try {
			return Integer.parseInt(fullTime.substring(3,5));
		} catch (Exception e) {
			return -1;
		}
	}
	
	// FORMATTERS
	/**
	 * Formats the {@code encodedDate}, HHMM, {@code String} into (H)H:MMa/pm
	 *
	 * @param encodedTime the encoded time {@code String} to format
	 *
	 * @return {@code encodedDate} in (H)H:MMa/pm format
	 *
	 * @see "README"
	 */
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

	/**
	 * Formats the {@code time} {@code String} into HHMM, encoded form
	 *
	 * @param time the date {@code String} to format
	 *
	 * @return {@code date} in encoded HHMM form
	 *
	 * @see "README"
	 */
	protected static String stripTimeString(String time) {
		return time.substring(0,2) + time.substring(3,5);
	}
}