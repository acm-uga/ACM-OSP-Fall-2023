package routeSchedule;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Represents a range of {@code LocalDate}s during which a Route operates
 *
 * @see LocalDate
 */
public class OperatingDate {
	// Public constant so Indefinite can be passed to an OperatingDate constructor
	public static final String Indefinite = "";

	/**
	 * Valid types of date ranges an {@code OperatingDate} can represent:
	 * <ul>
	 *     <li>{@link #RANGE}</li>
	 *     <li>{@link #SINGLE}</li>
	 *     <li>{@link #INDEFINITE}</li>
	 *     <li>{@link #PRE}</li>
	 *     <li>{@link #POST}</li>
	 * </ul>
	 */
	enum Type {
		/**
		 * A unique {@code startDate} and {@code endDate} are defined and are in chronological order, representing
		 * operation within that range, inclusive
		 */
		RANGE,
		/**
		 * {@code startDate} and {@code endDate} are the same date, representing operation on a single day
		 */
		SINGLE,
		/**
		 * {@code startDate} is defined as the date at instantiation and {@code endDate} as that date plus 10 years,
		 * representing operation for the "indefinite" future
		 */
		INDEFINITE,
		/**
		 * {@code startDate} is the date at instantiation and {@code endDate} is specified, representing operation from
		 * the date of instantiation until the {@code endDate} specified ("pre-continuity")
		 */
		PRE,
		/**
		 * {@code startDate} is specified and {@code endDate} is {@code startDate} + 10 years, representing operation from
		 * {@code startDate} to the "indefinite" future ("post-continuity")
		 */
		POST};
	
	private LocalDate startDate;
	private LocalDate endDate;
	private Type type;

	/**
	 * Instantiates an {@code OperatingDate} object provided a {@code String} date (MM/DD/YY), inclusive date range
	 * (MM/DD/YY-MM/DD/YY), or {@code Indefinite} ("")
	 *
	 * @param dateString the single date, date range, or {@code Indefinite} with which to populate the new
	 * {@code OperatingDate}'s {@code startDate} and {@code endDate} fields
	 *
	 * @see OperatingDate.Type
	 * */
	public OperatingDate(String dateString) {		
		RouteSchedule.Errors error = RouteSchedule.Errors.NONE;
		
		// Parse the dateString and instantiate LocalDates depending on the type of OperatingDate detected
		if (isIndefinite(dateString)) {
			this.startDate = LocalDate.now(ZoneId.of("UTC-5")); // TODO Possible source of errors in the *very* distant future
			this.endDate = LocalDate.now(ZoneId.of("UTC-5")).plusYears(10); // Guaranteed to work until 2090 :)
			this.type = Type.INDEFINITE;
		} else if (isDateRange(dateString, false)) {
			String startString = dateString.substring(0,8);
			String endString = dateString.substring(9,17);
			
			int startMonthInt = parseMonth(startString);
			int startDayInt = parseDay(startString);
			int startYearInt = parseYear(startString) + 2000; // Enables use of conventional MM/DD/YY format
			int endMonthInt = parseMonth(endString);
			int endDayInt = parseDay(endString);
			int endYearInt = parseYear(endString) + 2000; // Enables use of conventional MM/DD/YY format
			
			/* Validate the dates those integers represent by attempting to instantiate a LocalDate
			 * object with them. If invalid, catch the error, don't set the OperatingTime's attributes, and print
			 * the error to the console. */
			try {
				LocalDate start = LocalDate.of(startYearInt, startMonthInt, startDayInt);
				LocalDate end = LocalDate.of(endYearInt, endMonthInt, endDayInt);
				
				// Finally, check that the given times are valid (either equal or chronological)
				if (start.isBefore(end) || start.equals(end)) {
					this.startDate = start;
					this.endDate = end;
					this.type = Type.RANGE;
				} else {
					error = RouteSchedule.Errors.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.Errors.FORMAT;
			}
		} else if (continuesBefore(dateString, false)) {
			String endString = dateString.substring(1,9);
			int endMonthInt = parseMonth(endString);
			int endDayInt = parseDay(endString);
			int endYearInt = parseYear(endString) + 2000; // Enables use of conventional MM/DD/YY format
			
			/* Validate the dates those integers represent by attempting to instantiate a LocalDate
			 * object with them. If invalid, catch the error, don't set the OperatingTime's attributes, and print
			 * the error to the console. */
			try {
				LocalDate start = LocalDate.now(ZoneId.of("UTC-5"));
				LocalDate end = LocalDate.of(endYearInt, endMonthInt, endDayInt);
				
				// Finally, check that the given times are valid (either equal or chronological)
				if (start.isBefore(end) || start.equals(end)) {
					this.startDate = start;
					this.endDate = end;
					this.type = Type.PRE;
				} else {
					error = RouteSchedule.Errors.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.Errors.FORMAT;
			}
			
		} else if (continuesAfter(dateString, false)) {
			String startString = dateString.substring(0,8);
			int startMonthInt = parseMonth(startString);
			int startDayInt = parseDay(startString);
			int startYearInt = parseYear(startString) + 2000; // Enables use of conventional MM/DD/YY format
			
			/* Validate the dates those integers represent by attempting to instantiate a LocalDate
			 * object with them. If invalid, catch the error, don't set the OperatingTime's attributes, and print
			 * the error to the console. */
			try {
				LocalDate start = LocalDate.of(startYearInt, startMonthInt, startDayInt);
				LocalDate end = LocalDate.of(startYearInt + 10, startMonthInt, startDayInt);
				
				// Finally, check that the given times are valid (either equal or chronological)
				if (start.isBefore(end) || start.equals(end)) {
					this.startDate = start;
					this.endDate = end;
					this.type = Type.POST;
				} else {
					error = RouteSchedule.Errors.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.Errors.FORMAT;
			}	
		// If it's no other special instance, then the encoded date is presumed to be of single type
		} else {			
			int monthInt = parseMonth(dateString);
			int dayInt = parseDay(dateString);
			int yearInt = parseYear(dateString) + 2000; // Enables use of conventional MM/DD/YY format		
			
			// Validate the date those integers represent
			try {
				LocalDate date = LocalDate.of(yearInt, monthInt, dayInt);
				
				// If it's a valid date, set startDate and endDate to that same date to indicate it's just one date
				this.startDate = date;
				this.endDate = date;
				this.type = Type.SINGLE;
			} catch (Exception e) {
				error = RouteSchedule.Errors.FORMAT;
			}
		}
		
		// If an error occurred, print the correct message
		if (error != RouteSchedule.Errors.NONE) {
			System.out.println(errorMessage(error));
		}
	}

	/**
	 * Instantiates an {@code OperatingDate} object provided start and end {@code LocalDate} objects, representing
	 * the inclusive range of dates (or single date) in which a Route has operations
	 *
	 * @param startDate the first {@code LocalDate} operations exist within or {@code null}, representing
	 * continuous operation prior to {@code endDate} if {@code endDate != null} or indefinite
	 * operation if {@code endDate == null}
	 *
	 * @param endDate the last {@code LocalDate} operations exist within or {@code null}, representing continuous
	 * operation past {@code startDate} if {@code startDate != null} or indefinite operation if
	 * {@code startDate == null}
	 *
	 * @see OperatingDate.Type
	 */
	public OperatingDate(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;

		if (startDate.equals(endDate)) {
			this.type = Type.SINGLE;
		} else if (startDate == null && endDate != null) {
			this.type = Type.PRE;
		} else if (startDate != null && endDate == null) {
			this.type = Type.POST;
		} else if (startDate == null && endDate == null) {
			this.type = Type.INDEFINITE;
		} else {
			this.type = Type.RANGE;
		}
	}
	
	// GETTERS/SETTERS
	/**
	 * Gets this {@code OperatingDate}'s {@code startDate}
	 *
	 * @return the {@code startDate} field of this {@code OperatingDate}
	 */
	protected LocalDate startDate() {
		return this.startDate;
	}

	/**
	 * Gets this {@code OperatingDate}'s {@code endDate}
	 *
	 * @return the {@code endDate} field of this {@code OperatingDate}
	 */
	protected LocalDate endDate() {
		return this.endDate;
	}

	/**
	 * Gets this {@code OperatingDate}'s {@code type}
	 *
	 * @return the {@code OperatingDate.Type} of this {@code OperatingDate}
	 */
	protected Type type() {
		return this.type;
	}
	
	// METHODS
	/**
	 * Instantiates an {@code OperatingDate} object by extracting the data contained within an encoded OperatingDate
	 * {@code String} and populating fields accordingly
	 *
	 * @param encodedDate the encoded OperatingDate {@code String} to decode
	 *
	 * @return an instantiated {@code OperatingDate} representing the data encoded in {@code encodedDate}
	 *
	 * @see "README"
	 */
	protected static OperatingDate decode(String encodedDate) {
		/* Determine what type of date is encoded and parse accordingly. Then, instantiate an OperatingDate with
		 * the args found. */
		if (isSingleDate(encodedDate, true)) {
			/* Format the date in a human-legible way so it can be passed to the OperatingDate
			* constructor */
			String dateFormatted = formatEncodedDate(encodedDate);
			
			return new OperatingDate(dateFormatted);
		} else if (continuesBefore(encodedDate, true)) {
			/* Format the date in a human-legible way so it can be passed to the OperatingDate
			* constructor */
			String startDate = encodedDate.substring(1,7);
			String startDateFormatted = "-" + formatEncodedDate(startDate);
			
			return new OperatingDate(startDateFormatted);			
		} else if (continuesAfter(encodedDate, true)) {
			/* Format the date in a human-legible way so it can be passed to the OperatingDate
			* constructor */
			String endDate = encodedDate.substring(0,6);
			String endDateFormatted = formatEncodedDate(endDate) + "-";
			
			return new OperatingDate(endDateFormatted);
		} else if (isDateRange(encodedDate, true)){
			/* Format the dates in a human-legible way so they can be passed to the OperatingDate
			* constructor */
			String startDate = encodedDate.substring(0,6);
			String endDate = encodedDate.substring(7,13);
			String startDateFormatted = formatEncodedDate(startDate);
			String endDateFormatted = formatEncodedDate(endDate);
			
			return new OperatingDate(startDateFormatted + "-" + endDateFormatted);
		} else {
			return new OperatingDate(OperatingDate.Indefinite);
		}
	}

	/**
	 * Encodes this {@code OperatingDate} object as a {@code String} capable of being decoded back into an identical
	 * {@code OperatingDate} object
	 *
	 * @return a {@code String} concisely representing this {@code OperatingDate}'s data
	 */
	protected String encode() {
		switch (this.type) {
			case INDEFINITE:
				return OperatingDate.Indefinite;
			case SINGLE:
				return stripDateString(this.startDate.toString(), true);
			case PRE:
				return "-" + stripDateString(this.endDate.toString(), true);
			case POST:
				return stripDateString(this.startDate.toString(), true) + "-";
			default:
				return stripDateString(this.startDate.toString(), true) +  '-' + stripDateString(this.endDate.toString(), true);
		}
	}

	/**
	 * Creates a textual representation of this {@code OperatingDate}'s data in a brief, but human-friendly format
	 *
	 * @return <b>If {@code this.type == Indefinite}:</b><p>"Indefinitely"</p>
	 * <b>Else If {@code this.type == null}:</b><p>"Uninitialized OperatingDate"</p>
	 * <b>Else:</b>
	 * <p>a {@code String} containing this {@code OperatingDate}'s start and end {@code LocalDate}s in MM/DD/YY
	 * format, with - placed before, after, or between the date(s) to represent continuity or a range</p>
	 */
	public String toString() {
        String result;
        // If the object is uninitialized for whatever reason, catch it early
		switch (this.type) {
			case SINGLE:
				int monthInt = this.startDate.getMonthValue();
				int dayInt = this.startDate.getDayOfMonth();
				int yearInt = this.startDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format

                result = monthInt + "/" + dayInt + "/" + yearInt;
                break;
            case PRE:
				int endOnMonthInt = this.endDate.getMonthValue();
				int endOnDayInt = this.endDate.getDayOfMonth();
				int endOnYearInt = this.endDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format

                result = "-" + endOnMonthInt + "/" + endOnDayInt + "/" + endOnYearInt;
                break;
            case POST:
				int startOnMonthInt = this.startDate.getMonthValue();
				int startOnDayInt = this.startDate.getDayOfMonth();
				int startOnYearInt = this.startDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format

                result = startOnMonthInt + "/" + startOnDayInt + "/" + startOnYearInt + "-";
                break;
            case INDEFINITE:
                result = "Indefinitely";
                break;
            case RANGE:
				int startMonthInt = this.startDate.getMonthValue();
				int startDayInt = this.startDate.getDayOfMonth();
				int startYearInt = this.startDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format
				int endMonthInt = this.endDate.getMonthValue();
				int endDayInt = this.endDate.getDayOfMonth();
				int endYearInt = this.endDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format
				
				String startOfOperations = startMonthInt + "/" + startDayInt + "/" + startYearInt;
				String endOfOperations = endMonthInt + "/" + endDayInt + "/" + endYearInt;
                result = startOfOperations + "-" + endOfOperations;
                break;
            default:
				System.out.println(errorMessage(RouteSchedule.Errors.INVALID_FIELDS));
                result = "Uninitialized OperatingDate";
                break;
        }
        return result;
    }

	/**
	 * Compares this {@code OperatingDate} to the passed {@code OperatingDate} based on {@code startDate} and
	 * {@code endDate} values
	 *
	 * @param that the {@code OperatingDate} object to compare to the invoking {@code OperatingDate}
	 *
	 * @return {@code true} if this {@code OperatingDate}'s {@code startDate} and {@code endDate} fields equal those of
	 * {@code that}
	 *
	 * @see LocalDate#equals
	 */
	protected boolean equals(OperatingDate that) {
		return this.startDate.equals(that.startDate()) && this.endDate.equals(that.endDate());
	}

	/**
	 * Determines whether a Route has operations on the given {@code date}
	 *
	 * @param date the {@code LocalDate} in which to check for operations
	 *
	 * @return {@code true} if the provided {@code date} falls between this {@code OperatingDate}'s
	 * {@code startDate} and {@code endDate}, inclusive
	 */
	protected boolean operatesOn(LocalDate date) {
		boolean isAfterStart = this.startDate.isBefore(date) || this.startDate.equals(date);
		boolean isBeforeEnd = this.endDate.isAfter(date) || this.endDate.equals(date);
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
			return "ERROR: OperatingDate Constructor: Date is invalid.";
		case CHRONOLOGICAL:
			return "ERROR: OperatingDate Constructor: Start and end dates are not chronological.";
		case INVALID_FIELDS:
			return "WARNING: OperatingDate has invalid field(s).";
		default:
			return "ERROR: OperatingDate: Unknown error occurred.";
		}
	}

	/**
	 * Determines if the passed {@code dateString} represents an indefinite-type {@code OperatingDate} by checking if it
	 * equals {@code Indefinite}
	 *
	 * @param dateString the {@code String} to check for indefiniteness
	 *
	 * @return {@code true} if {@code dateString} is equal to {@code Indefinite}
	 *
	 * @see Type#INDEFINITE
	 * @see #Indefinite
	 * @see "README"
	 */
	private static boolean isIndefinite(String dateString) {
		return dateString.equals(Indefinite);
	}

	/**
	 * Determines if the passed {@code dateString} represents a single-date-type {@code OperatingDate} by checking if it
	 * contains just one date
	 *
	 * @param dateString the {@code String} to check for a single date
	 * @param isEncoded whether the {@code dateString} passed is encoded or in "human" MM/DD/YY format
	 *
	 * @return {@code true} if just one date exists in the {@code dateString}
	 *
	 * @see Type#SINGLE
	 * @see "README"
	 */
	private static boolean isSingleDate(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 6;
		} else {
			return dateString.length() == 8;
		}
	}

	/**
	 * Determines if the passed {@code dateString} represents a pre-continuous {@code OperatingDate} by checking
	 * if it begins with the continuity symbol, '-', and contains just one date
	 * 
	 * @param dateString the {@code String} to check for pre-continuity and a single date
	 * @param isEncoded whether the {@code dateString} passed is encoded or in "human" MM/DD/YY format
	 * 
	 * @return {@code true} if just one date exists and the continuity symbol precedes it
	 *
	 * @see Type#PRE
	 * @see "README"
	 */
	private static boolean continuesBefore(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 7 && dateString.charAt(0) == '-';
		} else {
			return dateString.length() == 9 && dateString.charAt(0) == '-';
		}
	}

	/**
	 * Determines if the passed {@code dateString} represents a post-continuous {@code OperatingDate} by checking if it
	 * ends with the continuity symbol, '-', and contains just one date
	 *
	 * @param dateString the {@code String} to check for post-continuity and a single date
	 * @param isEncoded whether the {@code dateString} passed is encoded or in "human" MM/DD/YY format
	 *
	 * @return {@code true} if just one date exists and the continuity symbol follows it
	 *
	 * @see Type#POST
	 * @see "README"
	 */
	private static boolean continuesAfter(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 7 && dateString.charAt(6) == '-';
		} else {
			return dateString.length() == 9 && dateString.charAt(8) == '-';
		}
	}

	/**
	 * Determines if the passed {@code dateString} represents a range-type {@code OperatingDate} by checking if two dates
	 * exists surrounding the continuity symbol, '-'
	 *
	 * @param dateString the {@code String} to check for a date range
	 * @param isEncoded whether the {@code dateString} is encoded or in "human" MM/DD/YY format
	 *
	 * @return {@code true} if two dates exist with the continuity symbol, '-', between them
	 *
	 * @see Type#RANGE
	 * @see "README"
	 */
	private static boolean isDateRange(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 13 && dateString.charAt(6) == '-';
		} else {
			return dateString.length() == 17 && dateString.charAt(8) == '-';
		}
	}

	/**
	 * Generates an array of every {@code LocalDate} between this {@code OperatingDate}'s {@code startDate} and
	 * {@code endDate}, inclusive
	 *
	 * @return an array of every {@code LocalDate} that exists between this {@code OperatingDate}'s {@code startDate} and
	 * {@code endDate}, inclusive
	 */
	protected LocalDate[] expand() {
		int numOfDays = (int)(this.endDate.toEpochDay() - this.startDate.toEpochDay());
		LocalDate[] localDates = new LocalDate[numOfDays];
		
		int i = 0;
		while (i < numOfDays) {
			localDates[i] = LocalDate.ofEpochDay(this.endDate.toEpochDay() + i);
			i++;
		}
		
		return localDates;
	}
	
	// PARSERS
	/**
	 * Parses the month integer from {@code fullDate}
	 *
	 * @param fullDate a date {@code String} in MM/DD/YY format
	 *
	 * @return the {@code Integer} representation of MM or -1 should an error occur
	 */
	private static int parseMonth(String fullDate) {
		try {
			return Integer.parseInt(fullDate.substring(0,2));
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Parses the day integer from {@code fullDate}
	 *
	 * @param fullDate a date {@code String} in MM/DD/YY format
	 *
	 * @return the {@code Integer} representation of DD or -1 should an error occur
	 */
	private static int parseDay(String fullDate) {
		try {
			return Integer.parseInt(fullDate.substring(3,5));
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Parses the year integer from {@code fullDate}
	 *
	 * @param fullDate a date {@code String} in MM/DD/YY format
	 *
	 * @return the {@code Integer} representation of YY or -1 should an error occur
	 */
	private static int parseYear(String fullDate) {
		try {
			return Integer.parseInt(fullDate.substring(6,8));
		} catch (Exception e) {
			return -1;
		}
	}
	
	// FORMATTERS
	/**
	 * Formats the {@code encodedDate}, MMDDYY, {@code String} into MM/DD/YY
	 *
	 * @param encodedDate the encoded date {@code String} to format
	 *
	 * @return {@code encodedDate} in MM/DD/YY format
	 *
	 * @see "README"
	 */
	protected static String formatEncodedDate(String encodedDate) {
		return encodedDate.substring(0,2) + "/" + encodedDate.substring(2,4) + "/" + encodedDate.substring(4,6);
	}

	/**
	 * Formats the {@code date} {@code String} into MMDDYY, encoded form
	 *
	 * @param date the date {@code String} to format
	 * @param isLocalDateFormat whether {@code date} follows {@code LocalDate} (YYYY-MM-DD) or {@code routeSchedule}
	 * (MM/DD/YY) date formatting
	 *
	 * @return {@code date} in encoded MMDDYY form
	 *
	 * @see "README"
	 * @see LocalDate#toString()
	 */
	/**
	 * Formats the {@code date} {@code String} into MMDDYY, encoded form
	 *
	 * @param date the date {@code String} to format
	 * @param isLocalDateFormat whether {@code date} follows {@code LocalDate} (YYYY-MM-DD) or {@code routeSchedule}
	 * (MM/DD/YY) date formatting
	 *
	 * @return {@code date} in encoded MMDDYY form
	 *
	 * @see "README"
	 * @see LocalDate#toString()
	 */
	protected static String stripDateString(String date, boolean isLocalDateFormat) {
		String month, day, year;
		
		// Parse out the month, day, and year
		if (isLocalDateFormat) {
			month = date.substring(5,7);
			day = date.substring(8,10);
			year = date.substring(2, 4);
		} else {
			month = date.substring(5,7);
			year = date.substring(2, 4);
			day = date.substring(8,10);
		}
		return month + day + year;
	}
}