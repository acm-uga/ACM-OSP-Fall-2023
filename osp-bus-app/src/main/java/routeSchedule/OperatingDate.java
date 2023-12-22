package routeSchedule;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Represents a range of {@code java.util.LocalDate}s during which a Route operates
 */
public class OperatingDate {
	// Public constant so Indefinite can be passed to an OperatingDate constructor
	public static final String Indefinite = "";
	
	/* OperatingDate's of TYPE...
	 * RANGE contain unique, chronological startDate and endDate's that have been specified at instantiation.
	 * SINGLE contain a startDate and endDate fields with the same LocalDate, specified at instantiation.
	 * INDEFINITE contain a startDate equal to the LocalDate at instantiation and an endDate equal to startDate
	 * plus 10 years.
	 * PRE contain a startDate equal to the LocalDate at instantiation and the endDate specified at instantiation.
	 * POST contain a startDate equal to the LocalDate specified at instantiation and an endDate equal to startDate
	 * plus 10 years.
	 */
	enum TYPE {RANGE,SINGLE,INDEFINITE,PRE,POST};
	
	private LocalDate startDate;
	private LocalDate endDate;
	private TYPE type;
	
	// Accepts a single, valid date String in MM/DD/YY or MM/DD/YY-MM/DD/YY ("HUMAN") format OR Indefinite ("Indefinite").

	/**
	 * Instantiates a
	 *
	 * @param dateString
	 */
	public OperatingDate(String dateString) {		
		RouteSchedule.ERRORS error = RouteSchedule.ERRORS.NONE;
		
		// Parse the dateString and instantiate LocalDates depending on the type of OperatingDate detected
		if (isIndefinite(dateString)) {
			this.startDate = LocalDate.now(ZoneId.of("UTC-5")); // TODO Possible source of errors in the *very* distant future
			this.endDate = LocalDate.now(ZoneId.of("UTC-5")).plusYears(10); // Guaranteed to work until 2090 :)
			this.type = TYPE.INDEFINITE;
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
					this.type = TYPE.RANGE;
				} else {
					error = RouteSchedule.ERRORS.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.ERRORS.FORMAT;
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
					this.type = TYPE.PRE;
				} else {
					error = RouteSchedule.ERRORS.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.ERRORS.FORMAT;
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
					this.type = TYPE.POST;
				} else {
					error = RouteSchedule.ERRORS.CHRONOLOGICAL;
				}
			} catch (Exception e) {
				error = RouteSchedule.ERRORS.FORMAT;
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
				this.type = TYPE.SINGLE;
			} catch (Exception e) {
				error = RouteSchedule.ERRORS.FORMAT;
			}
		}
		
		// If an error occurred, print the correct message
		if (error != RouteSchedule.ERRORS.NONE) {
			System.out.println(errorMessage(error));
		}
	}
	
	// Accepts a start and end LocalDate object (passing null as an arg to either indicates pre- or post-continuity
	public OperatingDate(LocalDate startDate, LocalDate endDate) {		
		this.startDate = startDate;
		this.endDate = endDate;
		
		if (startDate.equals(endDate)) {
			this.type = TYPE.SINGLE;
		} else {
			this.type = TYPE.RANGE;
		}
	}
	
	// GETTERS/SETTERS
	public LocalDate startDate() {
		return this.startDate;
	}
	
	public LocalDate endDate() {
		return this.endDate;
	}
	
	public TYPE type() {
		return this.type;
	}
	
	// METHODS
	/* Takes the "encoded" String (as stored in the database) and returns an instantiated OperatingDate using
	* the args found through parsing. */
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
	
	// Encode this OperatingDate as a String capable of being decoded back into an OperatingDate object
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
	
	// Return the operating time range as a String in MM/DD/YY-MM/DD/YY ("HUMAN") format
	public String toString() {
		// If the object is uninitialized for whatever reason, catch it early
		switch (this.type) {
			case SINGLE:
				int monthInt = this.startDate.getMonthValue();
				int dayInt = this.startDate.getDayOfMonth();
				int yearInt = this.startDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format
				
				String operationDate = monthInt + "/" + dayInt + "/" + yearInt;
				return operationDate;
			case PRE:
				int endOnMonthInt = this.endDate.getMonthValue();
				int endOnDayInt = this.endDate.getDayOfMonth();
				int endOnYearInt = this.endDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format
				
				String endOperationsOn = endOnMonthInt + "/" + endOnDayInt + "/" + endOnYearInt;
				return "-" + endOperationsOn;
			case POST:
				int startOnMonthInt = this.startDate.getMonthValue();
				int startOnDayInt = this.startDate.getDayOfMonth();
				int startOnYearInt = this.startDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format
				
				String startOperationsOn = startOnMonthInt + "/" + startOnDayInt + "/" + startOnYearInt;
				return startOperationsOn + "-";
			case INDEFINITE:
				return "Indefinitely";
			case RANGE:
				int startMonthInt = this.startDate.getMonthValue();
				int startDayInt = this.startDate.getDayOfMonth();
				int startYearInt = this.startDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format
				int endMonthInt = this.endDate.getMonthValue();
				int endDayInt = this.endDate.getDayOfMonth();
				int endYearInt = this.endDate.getYear() - 2000; // Enables use of conventional MM/DD/YY format
				
				String startOfOperations = startMonthInt + "/" + startDayInt + "/" + startYearInt;
				String endOfOperations = endMonthInt + "/" + endDayInt + "/" + endYearInt;
				return startOfOperations + "-" + endOfOperations;
			default:
				errorMessage(RouteSchedule.ERRORS.INVALID_FIELDS);
				return "Uninitialized OperatingDate";
		}
	}
	
	// Return true if this has the same attribute values as that
	protected boolean equals(OperatingDate that) {
		return this.startDate.equals(that.startDate()) && this.endDate().equals(that.endDate());
	}
	
	// Return true if the passed LocalDate falls within this startDate and endDate
	protected boolean operatesOn(LocalDate date) {
		boolean isAfterStart = this.startDate.isBefore(date) || this.startDate.equals(date);
		boolean isBeforeEnd = this.endDate.isAfter(date) || this.endDate.equals(date);
		return isAfterStart && isBeforeEnd;
	}
	
	// Return the correct error message String given the RouteSchedule ERROR provided as an argument.
	private static String errorMessage(RouteSchedule.ERRORS error) {
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
	
	/* Returns true if dateString is indefinite (which can be evaluated the same regardless
	* of whether dateString is encoded or not */
	private static boolean isIndefinite(String dateString) {
		return dateString.equals(Indefinite);
	}
	
	// If the passed timeString begins with a '-', return true
	private static boolean isSingleDate(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 6;
		} else {
			return dateString.length() == 8;
		}
	}
	
	// If the passed timeString begins with a '-', return true
	private static boolean continuesBefore(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 7 && dateString.charAt(0) == '-';
		} else {
			return dateString.length() == 9 && dateString.charAt(0) == '-';
		}
	}
	
	// If the passed timeString ends with a '-', return true
	private static boolean continuesAfter(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 7 && dateString.charAt(6) == '-';
		} else {
			return dateString.length() == 9 && dateString.charAt(8) == '-';
		}
	}
	
	// If the passed timeString contains '-' between two HH:MM-formatted times, return true
	private static boolean isDateRange(String dateString, boolean isEncoded) {
		if (isEncoded) {
			return dateString.length() == 13 && dateString.charAt(6) == '-';
		} else {
			return dateString.length() == 17 && dateString.charAt(8) == '-';
		}
	}
	
	/* Returns an array of LocalDate's consisting of every LocalDate between this startDate and endDate
	* (inclusive) */
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
	/* Parse the month from the fullDate String into an int (given it's in MM/DD/YY format).
	* If the string cannot be parsed into an int, return the sentinel -1, which will print a format error
	* in the constructor */
	private static int parseMonth(String fullDate) {
		try {
			return Integer.parseInt(fullDate.substring(0,2));
		} catch (Exception e) {
			return -1;
		}
	}
	
	/* Parse the day from the fullDate String into an int (given it's in MM/DD/YY format).
	* If the string cannot be parsed into an int, return the sentinel -1, which will print a format error
	* in the constructor */
	private static int parseDay(String fullDate) {
		try {
			return Integer.parseInt(fullDate.substring(3,5));
		} catch (Exception e) {
			return -1;
		}
	}
	
	/* Parse the year from the fullDate String into an int (given it's in MM/DD/YY format).
	* If the string cannot be parsed into an int, return the sentinel -1, which will print a format error
	* in the constructor */
	private static int parseYear(String fullDate) {
		try {
			return Integer.parseInt(fullDate.substring(6,8));
		} catch (Exception e) {
			return -1;
		}
	}
	
	// FORMATTERS
	/* Formats the encoded date MMDDYY into human-readable MM/DD/YY, which is accepted by
	 * the OperatingDate constructor */
	protected static String formatEncodedDate(String encodedDate) {
		return encodedDate.substring(0,2) + "/" + encodedDate.substring(2,4) + "/" + encodedDate.substring(4,6);
	}
	
	/* Whether date isLocalDateFormat (YYYY-MM-DD) or OperatingDate.toString() (MM/DD/YY - "HUMAN") format,
	 * return MMDDYY. */
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