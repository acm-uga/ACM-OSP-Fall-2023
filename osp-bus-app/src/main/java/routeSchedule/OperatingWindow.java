package routeSchedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * Represents a set of {@code Session}s during which a Route operates with a specific {@code DailySchedule}
 *
 * @see Session
 * @see DailySchedule
 */
public class OperatingWindow {
	private Session[] sessions; // Store the sessions (lists of Operating Dates) in which the DailySchedule applies
	private DailySchedule dailySchedule; // A list of operating times associated with DayOfWeek's

	/**
	 * Instantiates an {@code OperatingWindow} object provided a {@code String} of ","-separated, encoded {@code Session}s
	 * and an encoded {@code DailySchedule} {@code String}
	 *
	 * @param sessionsString a {@code String} of ","-separated, encoded {@code Session}s
	 * @param dailyScheduleString an encoded {@code DailySchedule} {@code String}
	 *
	 * @see "README"
	 * @see Session#encode()
	 * @see DailySchedule#encode()
	 */
	public OperatingWindow(String sessionsString, String dailyScheduleString) {		
		// Parse datesString into an array of encoded OperatingDate Strings.
		String[] sessionStrings = RouteSchedule.parseToArray(sessionsString, RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ';');
		
		// Convert all the session Strings into Sessions stored in sessions
		Session[] sessions = new Session[sessionStrings.length];
		int i = 0;
		for (String sessionString : sessionStrings) {
			sessions[i] = Session.decode(sessionString);
		}
		
		this.sessions = sessions;
		
		// Possible because the "HUMAN" format of daily schedules is the same as the "encoded" form stored in the database.
		this.dailySchedule = DailySchedule.decode(dailyScheduleString); 
	}

	/**
	 * Instantiates an {@code OperatingWindow} object provided an array of {@code Session}s and a {@code DailySchedule},
	 * representing the {@code Session}(s) in which a Route operates according to the given {@code DailySchedule}
	 *
	 * @param sessions the {@code Session}(s) that the {@code dailySchedule} applies to
	 *
	 * @param dailySchedule the {@code DailySchedule} that the Route operates according to during the {@code sessions}
	 */
	public OperatingWindow(Session[] sessions, DailySchedule dailySchedule) {
		this.sessions = sessions;
		this.dailySchedule = dailySchedule;
	}
	
	// GETTERS/SETTERS
	/**
	 * Gets this {@code OperatingWindow}'s {@code sessions}
	 *
	 * @return the {@code sessions} field of this {@code OperatingWindow}
	 */
	public Session[] sessions() {
		return this.sessions.clone();
	}

	/**
	 * Gets this {@code OperatingWindow}'s {@code dailySchedule}
	 *
	 * @return the {@code dailySchedule} field of this {@code OperatingWindow}
	 */
	public DailySchedule dailySchedule() {
		return this.dailySchedule;
	}
	
	// METHODS
	/* Takes the "encoded" String (as stored in the database) and returns an instantiated OperatingWindow using
	* the args found through parsing. */

	/**
	 * Instantiates an {@code OperatingWindow} object by extracting the data contained within an encoded OperatingWindow
	 * {@code String} and populating fields accordingly
	 *
	 * @param encodedWindow the encoded OperatingWindow {@code String} to decode
	 *
	 * @return an instantiated {@code OperatingWindow} representing the data encoded in {@code encodedWindow}
	 *
	 * @see "README"
	 */
	protected static OperatingWindow decode(String encodedWindow) {		
		// Locate '{' and '}' and separate the Session's from the DailySchedule
		int endOfDates = encodedWindow.indexOf('{');
		int endOfDailySchedule = encodedWindow.indexOf('}');
		String sessionsString = encodedWindow.substring(0, endOfDates);
		String dailyScheduleString = encodedWindow.substring(endOfDates + 1, endOfDailySchedule);
		
		// Parse the encoded Sessions String into an Array of Session Strings
		String[] sessionStrings = RouteSchedule.parseToArray(sessionsString, 
				RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ';');
		Session[] sessions = new Session[sessionStrings.length];
		
		int i = 0;
		for (String sessionString : sessionStrings) {
			sessions[i] = Session.decode(sessionString);
			i++;
		}
		
		// Parse the encoded DailySchedule String into a DailySchedule
		DailySchedule dailySchedule = DailySchedule.decode(dailyScheduleString);
		
		return new OperatingWindow(sessions, dailySchedule);
	}

	/**
	 * Encodes this {@code OperatingWindow} object as a {@code String} capable of being decoded back into an identical
	 * {@code OperatingWindow} object
	 *
	 * @return a {@code String} concisely representing this {@code OperatingWindow}'s data
	 */
	public String encode() {
		String encodedSession = "";
		
		/* Encode the list of Sessions first. Add each encoded Session to encodedSession
		 * and separate by ; */
		String[] sessionStrings = new String[this.sessions.length];
		int i = 0;
		for (Session session : this.sessions) {			
			sessionStrings[i] = session.encode();
			i++;
		}
		
		encodedSession += RouteSchedule.strArrayToStr(sessionStrings, ";");
		encodedSession += '{' + this.dailySchedule.encode() + '}';
		return encodedSession;
	}

	/**
	 * Creates a textual representation of this {@code OperatingWindow}'s data in a brief, but human-friendly format
	 *
	 * @return a {@code String} listing the {@code Session}s the Route operates within on separate lines, followed by
	 * the {@code DailySchedule} followed during those {@code Session}s
	 *
	 * @see Session#toString()
	 * @see DailySchedule#toString()
	 */
	public String toString() {
		String fullString = "";
		
		// List the dates this following dates/sessions the following daily schedule applies to
		fullString += "OPERATES ON:\n";
		String[] sessionStrings = new String[this.sessions.length];
		int i = 0;
		for (Session session : this.sessions) {
			sessionStrings[i] = session.toString();
			i++;
		}
		
		fullString += RouteSchedule.strArrayToStr(sessionStrings, "\n") + "\n\n";	
		fullString += "WITH A DAILY SCHEDULE OF:" + this.dailySchedule.toString();
		
		return fullString;
	}
	
	/**
	 * Compares this {@code OperatingWindow} to the passed {@code OperatingWindow} based on {@code sessions} and
	 * {@code dailySchedule} values
	 *
	 * @param that the {@code OperatingWindow} object to compare to the invoking {@code OperatingWindow}
	 *
	 * @return {@code true} if this {@code OperatingWindow}'s {@code sessions} and {@code dailySchedule} fields equal 
	 * those of {@code that}
	 *
	 * @see DailySchedule#equals(Object) 
	 * @see Session#equals(Session) 
	 */
	public boolean equals(OperatingWindow that) {
		return Arrays.equals(this.sessions, that.sessions) && this.dailySchedule.equals(that.dailySchedule);
	}

	/**
	 * Determines whether a Route operates on the given date at the given time (as specified in {@code dateTime})
	 *
	 * @param dateTime the {@code LocalDateTime} object containing the date and time in which to check for operation
	 *
	 * @return {@code true} if the provided date falls within any of this {@code OperatingWindow}'s {@code Session}s and
	 * the provided time falls within any {@code OperatingTime} listed for the {@code DayOfWeek} that {@code dateTime}
	 * falls on in this {@code OperatingWindow}'s {@code DailySchedule}
	 * 
	 * @see Session#operatesOn(LocalDate)
	 * @see DailySchedule#operatesOnAt(DayOfWeek, LocalTime)
	 */
	protected boolean operatesOnAt(LocalDateTime dateTime) {
		// Since this info is reused, gather it once
		LocalDate localDate = dateTime.toLocalDate();
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		LocalTime localTime = dateTime.toLocalTime();
		
		// Ensure operations exist within the previous scope before entering the next
		boolean isOperating = false;
		
		/* Check whether dateTime's date falls within any OperatingDate in any of this Window's
		* Session's */
		int i = 0;
		while (!isOperating && (i < this.sessions.length)) {
			isOperating = this.sessions[i].operatesOn(localDate);
			i++;
		}
		
		/* Now that we know there may be a time for the given date, check that
		 * there are operations on its day of week. */
		if (isOperating) isOperating = this.dailySchedule.operatesOnAt(dayOfWeek, localTime);
		
		return isOperating;
	}
}