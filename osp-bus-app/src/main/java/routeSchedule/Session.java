package routeSchedule;

import java.time.LocalDate;

public abstract class Session {
	private OperatingDate[] dates;

	public abstract String encode();
	public abstract String toString();
	public abstract boolean equals(Session that);
	
	protected Session(OperatingDate[] operatingDates) {
		this.dates = operatingDates;
	}
	
	// GETTERS/SETTERS
	protected OperatingDate[] dates() {
		return this.dates.clone();
	}
	
	protected void setDates(OperatingDate[] dates) {
		this.dates = dates;
	}
	
	// METHODS
	/* Accepts an encoded Session String and returns an instantiated Session of the valid type. The
	 * decoding algorithm to attempt is based on whether or not the encoded Session String appears
	 * "custom." */
	protected static Session decode(String encodedSession) {
		if (isCustom(encodedSession)) {
			return CustomSession.decodeSession(encodedSession);
		} else {
			return PredefinedSession.decodeSession(encodedSession);
		}
	}
	
	// Returns true if the given date falls within an OperatingDate in this Session.
	protected boolean operatesOn(LocalDate date) {
		if (this.dates != null) {
			boolean isOperating = false;
			int maxIndex = this.dates.length;
			int i = 0;
			
			/* Iterate through every OperatingDate in this Session's dates until either all have been
			 * checked or an OperatingDate in which date falls within is found. */
			while (!isOperating && (i < maxIndex)) {
				isOperating = this.dates[i].operatesOn(date);
				i++;
			}
			
			return isOperating;
		} else {
			return false;
		}
	}

	/* Returns true if the length of the encodedSession String is 1, a single character. This isn't a
	 * foolproof validation method, but it expedites decision statements and any invalid custom session
	 * indicators will be caught during decoding. */
	protected static boolean isCustom(String encodedSession) {
		return encodedSession.length() != 1;
	}
	
	/* Returns true if the length of the encodedSession String > 1. This isn't foolproof, but it expedites
	 * decision statements and a malformatted string will be caught during instantiation. */
	protected static boolean isPredefined(String encodedSession) {
		return encodedSession.length() == 1;
	}
}