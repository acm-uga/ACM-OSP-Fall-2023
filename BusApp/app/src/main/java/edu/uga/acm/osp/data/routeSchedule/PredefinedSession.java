package edu.uga.acm.osp.data.routeSchedule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Represents a named set of {@code OperatingDate}s during which a Route operates
 *
 * @see OperatingDate
 */
public class PredefinedSession extends Session {
	private static HashMap<Character, String> namesByIndicator = new HashMap<>();
	private static HashMap<String, OperatingDate[]> datesByName = new HashMap<>();
	
	// Initialize the HashMaps that allow us to add Predefined Sessions below...
	static {
		namesByIndicator = getNamesByIndicator();
		datesByName = getDatesByName();
	}
	
	// Holds which predefined session this specific instance of PredefinedSession is
	private char indicator;
	
	// Contains all currently known "No Service" dates/date ranges as a String of comma-separated, encoded OperatingTime's
	private static final String NOSERVICE = "090423,102823-102923,112323-112423,122523-122923,010124,011524,052724,061924";
	
	// DEFINE SESSIONS BELOW...
	/**
	 * Registers the {@code PredefinedSessionTemplate}s instantiated in the method body
	 *
	 * @return an array containing the newly-defined "Predefined Sessions" ({@code PredefinedSessionTemplate})
	 */
	private static PredefinedSessionTemplate[] addSessions() {
		return new PredefinedSessionTemplate[]{
		
		/* How to define a new PredefinedSession:
		 * 
		 * new PredefinedSessionTemplate(A, B, C),
		 * 
		 * Where...
		 * A is the unique, lowercase char used to represent the session
		 * B is the name String used to represent the session 
		 * C is the list of encoded comma-separated OperatingTime's as a single String
		*/
				
		new PredefinedSessionTemplate(
				'f', "Fall Semester", 
				datesExcluding("081623-121323", NOSERVICE)),
		
		new PredefinedSessionTemplate(
				's', "Spring Semester", 
				datesExcluding("010824-050724", NOSERVICE)),
		
		new PredefinedSessionTemplate(
				'm', "May Session", 
				datesExcluding("051424-060524", NOSERVICE)),
		
		new PredefinedSessionTemplate(
				'u', "Summer Session", 
				datesExcluding("060824-080424", NOSERVICE)),
		
		new PredefinedSessionTemplate(
				'i', "Intersession", 
				datesExcluding("080723-081523,102723,112223,121423-122223,010224-010524,030424-030824,050824-051324", NOSERVICE)),
		
		new PredefinedSessionTemplate(
				'g', "Game Days", 
				datesExcluding("090223,090923,091623,092323,093023,100723,101423,102823,110423,111123,111823,112523,120223", NOSERVICE))
		};
	}

	/**
	 * Maps the "predefined session indicators" of each "Predefined Session" added through {@link #addSessions()} to
	 * their corresponding, full names
	 *
	 * @return a {@code HashMap} mapping the "predefined session indicators" of each "Predefined Session" to their
	 * corresponding full names
	 */
	private static HashMap<Character, String> getNamesByIndicator() {
		PredefinedSessionTemplate[] predefinedSessions = addSessions();
		HashMap<Character, String> namesByIndicator = new HashMap<>();
		
		for (PredefinedSessionTemplate predefinedSession : predefinedSessions) {
			namesByIndicator.put(predefinedSession.indicator(), predefinedSession.fullName());
		}
		
		return namesByIndicator;
	}

	/**
	 * Maps the names of each "Predefined Session" added through {@link #addSessions()} to their corresponding
	 * {@code OperatingDate} arrays
	 *
	 * @return a {@code HashMap} mapping the names of each "Predefined Session" to their corresponding {@code OperatingDate}
	 * arrays
	 */
	private static HashMap<String, OperatingDate[]> getDatesByName() {
		PredefinedSessionTemplate[] predefinedSessions = addSessions();
		HashMap<String, OperatingDate[]> datesByName = new HashMap<>();
		
		for (PredefinedSessionTemplate predefinedSession : predefinedSessions) {
			datesByName.put(predefinedSession.fullName(), datesOf(predefinedSession.encodedDates()));
		}
		
		return datesByName;
	}

	// CONSTRUCTORS

	/**
	 * Instantiates a {@code PredefinedSession} object provided a valid "predefined session indicator"
	 *
	 * @param indicator the "predefined session indicator" {@code Character} of the "Predefined Session" to
	 * instantiate
	 */
	public PredefinedSession(char indicator) {
		super(datesByIndicator(indicator));
		this.indicator = (namesByIndicator.containsKey(indicator)) ? indicator : '\u0000';
	}
	
	
	// GETTERS/SETTERS

	/**
	 * Gets this {@code PredefinedSession}'s "predefined session indicator"
	 *
	 * @return the {@code OperatingDate.Type} of this {@code OperatingDate}
	 */
	private char indicator() {
		return this.indicator;
	}
	
	// METHODS
	/**
	 * Instantiates an {@code PredefinedSession} object by matching {@code encodedSession} to a valid "predefined
	 * session indicator"
	 *
	 * @param encodedSession the encoded PredefinedSession {@code String} to decode (a single-character string)
	 *
	 * @return an instantiated {@code PredefinedSession} containing the {@code OperatingDate}s represented by
	 * {@code encodedSession} (the "predefined session indicator")
	 *
	 * @see "README"
	 */
	protected static Session decodeSession(String encodedSession) {
		return new PredefinedSession(encodedSession.charAt(0));
	}

	/**
	 * Encodes this {@code PredefinedSession} object as a {@code String} capable of being decoded back into an identical
	 * {@code PredefinedSession} object
	 *
	 * @return a {@code String} containing the "predefined session indicator" that represents this {@code PredefinedSession}'s
	 * data
	 */
	protected String encode() {
		return String.valueOf(this.indicator);
	}

	/**
	 * Creates a textual representation of this {@code PredefinedSession}'s data in a brief but human-friendly format
	 *
	 * @return <b>If {@code this.dates == null}</b><p>"Uninitialized PredefinedSession"</p>
	 * <b>Else:</b>
	 * <p>the name of this {@code PredefinedSession} followed by the {@code OperatingDate}s it represents in MM/DD/YY
	 * format, separated by ", "</p>
	 *
	 * @see OperatingDate#toString()
	 */
	public String toString() {
		if (this.indicator != '\u0000') {
			String fullString = nameByIndicator(this.indicator) + " (";
			String[] operatingDateStrings = new String[this.dates().length];
			int i = 0;
			for (OperatingDate operatingDate : this.dates()) {
				operatingDateStrings[i] = operatingDate.toString();
				i++;
			}
			fullString += RouteSchedule.strArrayToStr(operatingDateStrings, ", ") + ")";
			return  fullString;
		} else {
			System.out.println(errorMessage(RouteSchedule.Errors.INVALID_FIELDS));
			return "Uninitialized PredefinedSession";
		}
	}
	
	// Returns a String separated by "," of all encoded OperatingDates of the specified PredefinedSession(s)
	/* CURRENTLY NOT IN USE. Intended function is to enable us to reuse previous definitions in addSessions, which
	* may not even be possible.
	private static String dateString(String indicators) {
		char[] sessionIndicators = indicators.toCharArray();
		
		ArrayList<OperatingDate> operatingDatesList = new ArrayList<OperatingDate>();
		for (char indicator : sessionIndicators) {
			Collections.addAll(operatingDatesList, datesByIndicator(indicator));
		}
		
		// Now that the number of OperatingDates is known, create an Array so they can be encoded
		String[] operatingDateStrings = new String[operatingDatesList.size()];
		
		int i = 0;
		for (OperatingDate operatingDate : operatingDatesList) {
			operatingDateStrings[i] = operatingDate.encode();
			i++;
		}
		
		// Convert the array into a String to return
		return RouteSchedule.strArrayToStr(operatingDateStrings, ", ");
	} */

	/**
	 * Compares this {@code PredefinedSession} to the passed {@code Session} based on type and fields
	 *
	 * @param that the {@code Session} object to compare to the invoking {@code PredefinedSession}
	 *
	 * @return {@code true} if and only if {@code that} is of {@code PredefinedSession} type and contains the same
	 * {@code OperatingDate}s in {@code operatingDate}s and {@code indicator} as the invoking {@code PredefinedSession}
	 *
	 * @see OperatingDate#equals(OperatingDate)
	 */
	public boolean equals(Session that) {
		if (that instanceof PredefinedSession) {
			PredefinedSession ofThat = (PredefinedSession) that;
			return this.indicator == ofThat.indicator() && Arrays.equals(this.dates(), that.dates());
		} else {
			return false;
		}
	}

	/**
	 * Generates an array of {@code OperatingDate}s by decoding the ","-separated {@code String} of encoded OperatingDates
	 *
	 * @param encodedOperatingDates the {@code String} of ","-separated, encoded OperatingDates to decode
	 *
	 * @return an array of instantiated {@code OperatingDate}s representing the data encoded in {@code encodedOperatingDates}
	 *
	 * @see "README"
	 */
	private static OperatingDate[] datesOf(String encodedOperatingDates) {
		String[] operatingDateStrings = RouteSchedule.parseToArray(encodedOperatingDates, RouteSchedule.FROM_BEGINNING, RouteSchedule.TO_END, ',');
		OperatingDate[] operatingDates = new OperatingDate[operatingDateStrings.length];
		
		int i = 0;
		for (String operatingDateString : operatingDateStrings) {
			operatingDates[i] = OperatingDate.decode(operatingDateString);
			i++;
		}
		
		return operatingDates;
	}

	/**
	 * Generates a {@code String} of ","-separated, encoded OperatingDates that exist after excluding the dates and
	 * date ranges specified in {@code encodedExclusions} from the dates and ranges specified in {@code encodedBaseDates}
	 * <p></p>
	 * Particularly useful for applying "No Service" dates to sets of OperatingDates en-masse.
	 *
	 * @param encodedBaseDates a ","-separated {@code String} of encoded OperatingDates to apply the exclusions to
	 * @param encodedExclusions a ","-separated {@code String} of encoded OperatingDates to exclude from the {@code encodedBaseDates}
	 *
	 * @return a ","-separated {@code String} of encoded OperatingDates produced from excluding the {@code encodedExclusions}
	 * from the {@code encodedBaseDates}
	 *
	 * @see "README"
	 */
	private static String datesExcluding(String encodedBaseDates, String encodedExclusions) {
		// Establish the base dates in which to later make exclusions from.
		// Parse encodedBaseDates into an array of encoded OperatingDate Strings
		String[] operatingDateStrings = RouteSchedule.parseToArray(encodedBaseDates, RouteSchedule.FROM_BEGINNING, RouteSchedule.TO_END, ',');
		OperatingDate[] baseDates = new OperatingDate[operatingDateStrings.length];
		
		// Decode the encoded OperatingDate Strings
		int i = 0;
		for (String operatingDateString : operatingDateStrings) {
			baseDates[i] = OperatingDate.decode(operatingDateString);
			i++;
		}
		
		// Parse the encodedExclusions into an array of encoded OperatingDate Strings
		String[] exclusionDateStrings = RouteSchedule.parseToArray(encodedExclusions, RouteSchedule.FROM_BEGINNING, RouteSchedule.TO_END, ',');
		OperatingDate[] exclusionDates = new OperatingDate[exclusionDateStrings.length];
		
		// Decode the encoded OperatingDate Strings
		i = 0;
		for (String exclusionDateString : exclusionDateStrings) {
			exclusionDates[i] = OperatingDate.decode(exclusionDateString);
			i++;
		}
		
		// Apply the exclusions
		boolean affectedByStart, affectedByEnd, baseIsSingle, baseUnaffected, fullyRemoved;
		int baseDateIndex = 0, excDateIndex = 0;
		OperatingDate base, exc;
		ArrayList<OperatingDate> modifiedDates = new ArrayList<>();
		Collections.addAll(modifiedDates, baseDates);
		
		while (baseDateIndex < modifiedDates.size()) {
			base = modifiedDates.get(baseDateIndex);
			baseIsSingle = base.type() == OperatingDate.Type.SINGLE;
			excDateIndex = 0;
			baseUnaffected = true;

            while (baseUnaffected && (excDateIndex < exclusionDates.length)) {
				exc = exclusionDates[excDateIndex];
				affectedByStart = base.operatesOn(exc.startDate());
				affectedByEnd = base.operatesOn(exc.endDate());
				
				/* If the base OperatingDate is either single and affected or is the same range as the exclusion, 
				 * consider it fully removed. */
				fullyRemoved = (baseIsSingle && (affectedByStart || affectedByEnd)) || 
						(base.startDate().equals(exc.startDate()) && base.endDate().equals(exc.endDate()));

				/* So long as the base date hasn't been fully removed ("excluded"), apply the exclusion and add it to
				* tempModifiedDates so itself can have exclusions applied (should they exist when checked next iteration). */
				if (!fullyRemoved) {
					if (affectedByStart && !affectedByEnd) {
						modifiedDates.remove(baseDateIndex);
						modifiedDates.add(baseDateIndex, new OperatingDate(base.startDate(), exc.startDate().minusDays(1)));

						baseUnaffected = false;
					} else if (affectedByStart && affectedByEnd) {
						modifiedDates.remove(baseDateIndex);
						modifiedDates.add(baseDateIndex, new OperatingDate(base.startDate(), exc.startDate().minusDays(1)));
						modifiedDates.add(baseDateIndex + 1, new OperatingDate(exc.endDate().plusDays(1), base.endDate()));

						baseUnaffected = false;
					} else if (!affectedByStart && affectedByEnd) {
						modifiedDates.remove(baseDateIndex);
						modifiedDates.add(baseDateIndex, new OperatingDate(exc.endDate().plusDays(1), base.endDate()));

						baseUnaffected = false;
					}
				}
				excDateIndex++;
			}
			
			// If the base date is unaffected, add it to the modified dates list
			if (baseUnaffected) {
				baseDateIndex++;
			}
		}
		
		/* Now that we have the modified list of OperatingDates (base with exclusions applied), convert each into
		* its String representation and join it so it can be returned as a single String. */
		String[] modifiedDateStrings = new String[modifiedDates.size()];
		int modDateIndex = 0;
		for (OperatingDate modifiedDate : modifiedDates) {
			modifiedDateStrings[modDateIndex] = modifiedDate.encode();
			modDateIndex++;
		}

		return RouteSchedule.strArrayToStr(modifiedDateStrings, ",");
	}

	/**
	 * Determines a {@code PredefinedSession}'s name given its "predefined session indicator"
	 *
	 * @param indicator a "predefined session indicator"
	 *
	 * @return the name of the "Predefined Session" represented by that {@code indicator}
	 *
	 * @see #addSessions()
	 */
	private static String nameByIndicator(char indicator) {
		return namesByIndicator.get(indicator);
	}

	/**
	 * Finds a {@code PredefinedSession}'s {@code OperatingDate}s given its name
	 *
	 * @param name the name of a registered PredefinedSession
	 *
	 * @return the {@code OperatingDate}s array of the PredefinedSession named {@code name}
	 *
	 * @see #addSessions()
	 */
	private static OperatingDate[] datesByName(String name) {	
		return datesByName.get(name);
	}

	/**
	 * Finds a {@code PredefinedSession}'s {@code OperatingDate}s array given its "predefined session indicator"
	 *
	 * @param indicator a "predefined session indicator"
	 *
	 * @return the {@code OperatingDate}s array of the PredefinedSession whose indicator is {@code indicator}
	 */
	private static OperatingDate[] datesByIndicator(char indicator) {
		return datesByName.get(namesByIndicator.get(indicator));
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
		case INVALID_FIELDS:
			return "WARNING: PredefinedSession has invalid field(s).";
		default:
			return "ERROR: PredefinedSession: Unknown error occurred.";
		}
	}
}