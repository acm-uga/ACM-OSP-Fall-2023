package routeSchedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
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
	
	/* Using the the sessions defined in addSessions(), creates and returns a HashMap mapping indicators as
	 * char's to names as String's. */
	
	private static HashMap<Character, String> getNamesByIndicator() {
		PredefinedSessionTemplate[] predefinedSessions = addSessions();
		HashMap<Character, String> namesByIndicator = new HashMap<>();
		
		for (PredefinedSessionTemplate predefinedSession : predefinedSessions) {
			namesByIndicator.put(predefinedSession.indicator(), predefinedSession.fullName());
		}
		
		return namesByIndicator;
	}
	
	/* Using the the sessions defined in addSessions(), creates and returns a HashMap mapping names as
	 * String's to OperatingDate arrays. */
	private static HashMap<String, OperatingDate[]> getDatesByName() {
		PredefinedSessionTemplate[] predefinedSessions = addSessions();
		HashMap<String, OperatingDate[]> datesByName = new HashMap<>();
		
		for (PredefinedSessionTemplate predefinedSession : predefinedSessions) {
			datesByName.put(predefinedSession.fullName(), datesOf(predefinedSession.encodedDates()));
		}
		
		return datesByName;
	}

	// CONSTRUCTORS
	public PredefinedSession(char indicator) {
		super(datesByIndicator(indicator));
		this.indicator = (namesByIndicator.containsKey(indicator)) ? indicator : '\u0000';
	}
	
	
	// GETTERS/SETTERS
	private char indicator() {
		return this.indicator;
	}
	
	// METHODS
	protected static Session decodeSession(String encodedSession) {
		return new PredefinedSession(encodedSession.charAt(0));
	}

	// Returns the encoded form of this predefined session, given by this PredefinedSession's indicator.
	protected String encode() {
		return String.valueOf(this.indicator);
	}

	// Returns the String name of this predefined session or "Uninitialized PredefinedSession" if no indicator exists.
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
			System.out.println(errorMessage(RouteSchedule.ERRORS.INVALID_FIELDS));
			return "Uninitialized PredefinedSession";
		}
	}
	
	// Returns a String separated by "," of all encoded OperatingDates of the specified PredefinedSession(s)
	/* CURRENTLY NOT IN USE. Intended function is to enable us to reuse previous definitions in addSessions, which
	* may not even be possible. */
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
	}
	
	// Returns true if the predefined sessions are the same type and have the same dates. False if otherwise.
	public boolean equals(Session that) {
		if (that instanceof PredefinedSession) {
			PredefinedSession ofThat = (PredefinedSession) that;
			return this.indicator == ofThat.indicator() && this.dates().equals(that.dates());
		} else {
			return false;
		}
	}
	
	// Accepts a list of encoded dates and returns a list of instantiated OperatingDate's including them.
	private static OperatingDate[] datesOf(String encodedOperatingDates) {
		String[] operatingDateStrings = RouteSchedule.parseToArray(encodedOperatingDates, RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ',');
		OperatingDate[] operatingDates = new OperatingDate[operatingDateStrings.length];
		
		int i = 0;
		for (String operatingDateString : operatingDateStrings) {
			operatingDates[i] = OperatingDate.decode(operatingDateString);
			i++;
		}
		
		return operatingDates;
	}
	
	// Accepts a list of encoded dates and returns a list of instantiated OperatingDate's excluding those within the encodedBaseDates.
	private static String datesExcluding(String encodedBaseDates, String encodedExclusions) {
		// Establish the base dates in which to later make exclusions from.
		// Parse encodedBaseDates into an array of encoded OperatingDate Strings
		String[] operatingDateStrings = RouteSchedule.parseToArray(encodedBaseDates, RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ',');
		OperatingDate[] baseDates = new OperatingDate[operatingDateStrings.length];
		
		// Decode the encoded OperatingDate Strings
		int i = 0;
		for (String operatingDateString : operatingDateStrings) {
			baseDates[i] = OperatingDate.decode(operatingDateString);
			i++;
		}
		
		// Parse the encodedExclusions into an array of encoded OperatingDate Strings
		String[] exclusionDateStrings = RouteSchedule.parseToArray(encodedExclusions, RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ',');
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
		
		/*System.out.println("--------------------------------------------------------------------------");
		System.out.println("Base dates: " + RouteSchedule.strArrayToStr(operatingDateStrings, ", "));
		System.out.println("Exc dates: " + RouteSchedule.strArrayToStr(exclusionDateStrings, ", "));
		System.out.println("BaseDates length: " + baseDates.length);
		System.out.println("ExcDates length: " + exclusionDates.length + "\n");(*/
		
		while (baseDateIndex < modifiedDates.size()) {
			base = modifiedDates.get(baseDateIndex);
			baseIsSingle = base.type() == OperatingDate.TYPE.SINGLE;
			excDateIndex = 0;
			baseUnaffected = true;
			fullyRemoved = false;
			
			/*System.out.println("~~~~~Looking at \"base\": " + base.toString() + "~~~~~");
			System.out.println("\n------------------------------------------"
					+ "\nPRE MODIFIED DATES LIST:\n"
					+ modifiedDates.toString()
					+ "\n Length is: " + modifiedDates.size()
					+ "\n Current baseIndex is: " + baseDateIndex
					+ "\n------------------------------------------\n"); */
			
			while (baseUnaffected && (excDateIndex < exclusionDates.length)) {				
				exc = exclusionDates[excDateIndex];
				affectedByStart = base.operatesOn(exc.startDate());
				affectedByEnd = base.operatesOn(exc.endDate());				
				
				//System.out.println("Looking at exclusion: " + exc.toString());
				//System.out.println("\tTemp base affected by exc start? " + affectedByStart);
				//System.out.println("\tTemp base affected by exc end? " + affectedByStart);
				
				/* If the base OperatingDate is either single and affected or is the same range as the exclusion, 
				 * consider it fully removed. */
				fullyRemoved = (baseIsSingle && (affectedByStart || affectedByEnd)) || 
						(base.startDate().equals(exc.startDate()) && base.endDate().equals(exc.endDate()));
				
				//System.out.println("\tTemp base fully removed by exc? " + fullyRemoved + "\n");
				
				/* So long as the base date hasn't been fully removed ("excluded"), apply the exclusion and add it to
				* tempModifiedDates so itself can have exclusions applied (should they exist when checked next iteration). */
				if (!fullyRemoved) {
					if (affectedByStart && !affectedByEnd) {
						modifiedDates.remove(baseDateIndex);
						modifiedDates.add(baseDateIndex, new OperatingDate(base.startDate(), exc.startDate().minusDays(1)));
						baseUnaffected = false;
						//System.out.println("\tVERDICT 1/2: Removed " + base.toString() + " from modifiedDates!");
						//System.out.println("\tVERDICT 1/2: Added " + modifiedDates.get(baseDateIndex).toString() + " to modifiedDates!");
					} else if (affectedByStart && affectedByEnd) {
						modifiedDates.remove(baseDateIndex);
						modifiedDates.add(baseDateIndex, new OperatingDate(base.startDate(), exc.startDate().minusDays(1)));
						modifiedDates.add(baseDateIndex + 1, new OperatingDate(exc.endDate().plusDays(1), base.endDate()));
						
						//System.out.println("\tVERDICT 1/3: Removed " + base.toString() + " from modifiedDates!");
						//System.out.println("\tVERDICT 2/3: Added " + modifiedDates.get(baseDateIndex).toString() + " to modifiedDates!");
						//System.out.println("\tVERDICT 3/3: Added " + modifiedDates.get(baseDateIndex + 1).toString() + " to modifiedDates!");
						baseUnaffected = false;
					} else if (!affectedByStart && affectedByEnd) {
						modifiedDates.remove(baseDateIndex);
						modifiedDates.add(baseDateIndex, new OperatingDate(exc.endDate().plusDays(1), base.endDate()));
						baseUnaffected = false;
						//System.out.println("\tVERDICT 1/2: Removed " + base.toString() + " from modifiedDates!");
						//System.out.println("\tVERDICT 2/2: Added " + modifiedDates.get(baseDateIndex).toString() + " to modifiedDates!");
					}
				} else {
					//System.out.println("\tVERDICT: Base date removed!");
				}
				excDateIndex++;
			}
			
			// If the base date is unaffected, add it to the modified dates list
			if (baseUnaffected) {
				//System.out.println("VERDICT: Kept original base!");
				baseDateIndex++;
			}
			
			/*System.out.println("\n------------------------------------------"
					+ "\nPOST MODIFIED DATES LIST:\n"
					+ modifiedDates.toString()
					+ "\n Length is: " + modifiedDates.size()
					+ "\n Current baseIndex is: " + baseDateIndex
					+ "\n------------------------------------------\n"); */
		}
		
		/* Now that we have the modified list of OperatingDates (base with exclusions applied), convert each into
		* its String representation and join it so it can be returned as a single String. */
		String[] modifiedDateStrings = new String[modifiedDates.size()];
		int modDateIndex = 0;
		for (OperatingDate modifiedDate : modifiedDates) {
			modifiedDateStrings[modDateIndex] = modifiedDate.encode();
			modDateIndex++;
		}
		
		/*System.out.println("~~SUMMARY:~~");
		System.out.println("\tBASE: " + RouteSchedule.strArrayToStr(operatingDateStrings, ","));
		System.out.println("\tEXCLUDE: " + RouteSchedule.strArrayToStr(exclusionDateStrings, ","));
		System.out.println("\tFINAL STRING: " + RouteSchedule.strArrayToStr(modifiedDateStrings, ",")); */
		
		return RouteSchedule.strArrayToStr(modifiedDateStrings, ",");
	}
	
	// Returns a predefined session's name given its indicator
	private static String nameByIndicator(char indicator) {
		return namesByIndicator.get(indicator);
	}
	
	// Returns a predefined session's dates Array given its name
	private static OperatingDate[] datesByName(String name) {	
		return datesByName.get(name);
	}
	
	// Returns a predefined session's dates Array given its indicator
	private static OperatingDate[] datesByIndicator(char indicator) {
		return datesByName.get(namesByIndicator.get(indicator));
	}
	
	// Return the correct error message String given the RouteSchedule ERROR provided as an argument.
	private static String errorMessage(RouteSchedule.ERRORS error) {
		switch (error) {
		case INVALID_FIELDS:
			return "WARNING: PredefinedSession has invalid field(s).";
		default:
			return "ERROR: PredefinedSession: Unknown error occurred.";
		}
	}
}