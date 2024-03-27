package routeSchedule;

import java.time.LocalDate;

/**
 * Represents a set of {@code OperatingDate}s during which a Route operates
 */
public abstract class Session {
	private OperatingDate[] dates;

	/**
	 * Encodes this {@code Session} object as a {@code String} capable of being decoded back into an
	 * identical {@code Session} object using the appropriate encoding algorithm
	 *
	 * @return a {@code String} concisely representing this {@code Session}'s data
	 *
	 * @see "README"
	 */
	protected abstract String encode();

	/**
	 * Creates a textual representation of this {@code Sessions}'s data in a brief but human-friendly format using the
	 * appropriate conversion algorithm
	 *
	 * @return a {@code String} representing this {@code Session}'s data
	 *
	 * @see OperatingWindow#toString()
	 */
	public abstract String toString();

	/**
	 * Compares this {@code Session} to the passed {@code Session} based on equality of type, {@code dates} fields, and
	 * other appropriate fields in subclasses
	 *
	 * @param that the {@code Session} object to compare to the invoking {@code Session}
	 *
	 * @return {@code true} if and only if {@code that} {@code Session} is the same type as the invoking {@code Session},
	 * the {@code dates} fields are equal, and other appropriate subclass fields are equal
	 */
	public abstract boolean equals(Session that);

	/**
	 * Instantiates a {@code Session} object provided an array of {@code OperatingDate}s
	 *
	 * @param operatingDates the array of {@code OperatingDate} objects with which to populate the new
	 * {@code Session}'s {@code dates} field
	 */
	protected Session(OperatingDate[] operatingDates) {
		this.dates = operatingDates;
	}
	
	// GETTERS/SETTERS
	/**
	 * Gets this {@code Session}'s {@code dates}
	 *
	 * @return the array of {@code OperatingDate}s in this {@code Session}'s {@code dates} field
	 */
	protected OperatingDate[] dates() {
		return this.dates.clone();
	}
	
	// METHODS
	/**
	 * Instantiates a {@code Session} object by extracting the data contained within an encoded Session {@code String}
	 * using the appropriate decoding algorithm and populating fields accordingly
	 *
	 * @param encodedSession the encoded Session {@code String} to decode
	 *
	 * @return an instantiated {@code Session} representing the data encoded in {@code encodedSession}
	 *
	 * @see "README"
	 * @see CustomSession#decodeSession(String)
	 * @see PredefinedSession#decodeSession(String)
	 */
	protected static Session decode(String encodedSession) {
		if (isCustom(encodedSession)) {
			return CustomSession.decodeSession(encodedSession);
		} else {
			return PredefinedSession.decodeSession(encodedSession);
		}
	}

	/**
	 * Determines whether a Route has operations on the given {@code date}
	 *
	 * @param date the {@code LocalDate} in which to check for operations
	 *
	 * @return {@code true} if the provided {@code date} falls between any of this {@code Session}'s
	 * {@code OperatingDate}s' {@code startDate} and {@code endDate}, inclusive
	 */
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

	/**
	 * Determines whether the {@code encodedSession} is custom by checking that it's not a single character
	 *
	 * @param encodedSession the encoded Session {@code String} to check
	 *
	 * @return {@code true} if {@code encodedSession} is not a single character, which implies the {@code encodedSession}
	 * represents a {@code CustomSession} (the validity of which, however, is not guaranteed)
	 */
	protected static boolean isCustom(String encodedSession) {
		return encodedSession.length() != 1;
	}

	/**
	 * Determines whether the {@code encodedSession} is custom by checking that it's a single character
	 *
	 * @param encodedSession the encoded Session {@code String} to check
	 *
	 * @return {@code true} if {@code encodedSession} is a single character, which implies the {@code encodedSession}
	 * represents a {@code PredefinedSession} (the validity of which, however, is not guaranteed)
	 */
	protected static boolean isPredefined(String encodedSession) {
		return encodedSession.length() == 1;
	}
}