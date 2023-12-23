package routeSchedule;

import java.util.Arrays;

/**
 * Represents a unique set of {@code OperatingDate}s during which a Route operates, not already defined in a
 * {@code PredefinedSession}
 *
 * @see OperatingDate
 */
public class CustomSession extends Session {
	/**
	 * Instantiates a {@code CustomSession} object provided an array of {@code OperatingDate}s
	 *
	 * @param operatingDates the array of {@code OperatingDate} objects with which to populate the new
	 * {@code CustomSession}'s {@code dates} field
	 */
	public CustomSession(OperatingDate[] operatingDates) {
		super(operatingDates);
	}

	/**
	 * Decodes the encoded {@code CustomSession} and instantiates one given the extracted data
	 *
	 * @param encodedSession the encoded {@code CustomSession} {@code String} to decode
	 *
	 * @return an instantiated {@code CustomSession} representing the data encoded in {@code encodedSession}
	 *
	 * @see "README"
	 */
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
	 * Encodes this {@code CustomSession} object as a {@code String} capable of being decoded back into an
	 * identical {@code CustomSession} object
	 *
	 * @return a {@code String} concisely representing this {@code CustomSession}'s data
	 *
	 * @see "README"
	 */
	protected String encode() {
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
	 * Creates a textual representation of this {@code CustomSession}'s data in a brief but human-friendly format
	 * 
	 * @return <b>If {@code this.dates == null}</b><p>"Uninitialized CustomSession"</p>
	 * <b>Else:</b>
	 * <p>a {@code String} of ", "-separated {@code OperatingDate}s in MM/DD/YY format</p>
	 *
	 * @see OperatingDate#toString()
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
			System.out.println(errorMessage(RouteSchedule.Errors.INVALID_FIELDS));
			return "Uninitialized CustomSession";
		}
	}
	
	/**
	 * Compares this {@code CustomSession} to the passed {@code Session} based on type and fields
	 * 
	 * @param that the {@code Session} object to compare to the invoking {@code CustomSession}
	 *
	 * @return {@code true} if and only if {@code that} is of {@code CustomSession} type and contains the same
	 * {@code OperatingDate}s in {@code operatingDate}s as the invoking {@code CustomSession}
	 * 
	 * @see OperatingDate#equals(OperatingDate) 
	 */
	public boolean equals(Session that) {
		return that instanceof CustomSession && Arrays.equals(this.dates(), that.dates());
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
			return "WARNING: CustomSessions has invalid field(s).";
		default:
			return "ERROR: CustomSession: Unknown error occurred.";
		}
	}
}