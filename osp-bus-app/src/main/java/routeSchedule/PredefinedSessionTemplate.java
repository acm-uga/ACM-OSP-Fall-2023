package routeSchedule;

/**
 * Represents all the components of a "predefined" Session
 *
 * @param indicator the "predefined session indicator," any lowercase letter
 * @param fullName the full name of the predefined session (e.g. "Fall Semester")
 * @param encodedDates a {@code String} of encoded OperatingDates included in the predefined session
 *
 * @see PredefinedSession
 * @see "README"
 */
record PredefinedSessionTemplate(char indicator, String fullName, String encodedDates) {}