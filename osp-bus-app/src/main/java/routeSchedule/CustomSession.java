package routeSchedule;

import java.util.ArrayList;

public class CustomSession extends Session {	
	public CustomSession(OperatingDate[] operatingDates) {
		super(operatingDates);
	}
	
	/* Decode the encoded session (series of dates separated by semicolons) and instantiate */
	protected static Session decodeSession(String encodedSession) {
		// Parse the encoded OperatingDate strings into an Array of OperatingDates
		String[] operatingDateStrings = RouteSchedule.parseToArray(encodedSession, 
				RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ',');
		OperatingDate[] operatingDates = new OperatingDate[operatingDateStrings.length];
		
		int i = 0;
		for (String operatingDateString : operatingDateStrings) {
			operatingDates[i] = OperatingDate.decode(operatingDateString);
			i++;
		}
		
		return new CustomSession(operatingDates);
	}
	
	/**
	 * Encodes this CustomSession object as a String capable of being decoded back into an
	 * identical, instantiated CustomSession object.
	 * 
	 * Permits storage of the CustomSession in a condensed format that is easy to understand and
	 * modify as-is.
	 * 
	 * @return A String containing encoded CustomSession data, adhering to the guidelines outlined in README
	 * 
	 * @see "README"
	 */
	public String encode() {
		/* Encode the list of OperatingDates. Take each as a string, strip its formatting, then
		* add them to a string separated by ',' if multiple exist. */
		String[] operatingDateStrings = new String[this.dates().length];
		int i = 0;
		for (OperatingDate operatingDate : this.dates()) {			
			operatingDateStrings[i] = operatingDate.encode();
			i++;
		}
		
		return RouteSchedule.strArrayToStr(operatingDateStrings, ",");
	}
	/**
	 * Creates a String representation of this CustomSession's data in an expanded,
	 * human-friendly format.
	 * 
	 * @return A String of ", "-separated OperatingDates in human-friendly MM/DD/YY
	 * format
	 */
	public String toString() {
		if (this.dates() != null) {
			String[] operatingDateStrings = new String[this.dates().length];
			int i = 0;
			for (OperatingDate operatingDate : this.dates()) {
				operatingDateStrings[i] = operatingDate.toString();
				i++;
			}
			
			return RouteSchedule.strArrayToStr(operatingDateStrings, ", ");
		} else {
			System.out.println(errorMessage(RouteSchedule.ERRORS.INVALID_FIELDS));
			return "Uninitialized CustomSession";
		}
	}
	
	/**
	 * Compares this CustomSession to the passed Session based on type and fields.
	 * 
	 * @param that  the Session object to compare to the invoking CustomSession
	 * 
	 * @return <code>true</code> if and only if <code>that</code> is of CustomSession type and contains the
	 * same OperatingDate's as the invoking CustomSession
	 */
	public boolean equals(Session that) {
		return that instanceof CustomSession && this.dates().equals(that.dates());
	}
	
	// Return the correct error message String given the RouteSchedule ERROR provided as an argument.
	private static String errorMessage(RouteSchedule.ERRORS error) {
		switch (error) {
		case INVALID_FIELDS:
			return "WARNING: CustomSessions has invalid field(s).";
		default:
			return "ERROR: CustomSession: Unknown error occurred.";
		}
	}
}