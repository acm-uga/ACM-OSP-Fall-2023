package routeSchedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class OperatingWindow {
	private Session[] sessions; // Store the sessions (lists of Operating Dates) in which the DailySchedule applies
	private DailySchedule dailySchedule; // A list of operating times associated with DayOfWeek's
	
	// Accepts an encodedSession and an encoded DailySchedule as Strings and instantatiate an OperatingWindow from them.
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
	
	// Accepts a pre-instatiated Session array and DailySchedule to instantiate an OperatingWindow
	public OperatingWindow(Session[] sessions, DailySchedule dailySchedule) {
		this.sessions = sessions;
		this.dailySchedule = dailySchedule;
	}
	
	// GETTERS/SETTERS
	public Session[] sessions() {
		return this.sessions.clone();
	}
	
	public DailySchedule dailySchedule() {
		return this.dailySchedule;
	}
	
	// METHODS
	/* Takes the "encoded" String (as stored in the database) and returns an instantiated OperatingWindow using
	* the args found through parsing. */
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
	
	// Encode this OperatingWindow as a String capable of being decoded back into an OperatingWindow object
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
	
	// Return the OperatingWindow's data as a String in "Human" format
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
	
	// Returns true if this has the same DailySchedule and Session array as that
	public boolean equals(OperatingWindow that) {
		return this.sessions.equals(that.sessions)&& this.dailySchedule.equals(that.dailySchedule); 
	}
	
	/* Returns true if the this OperatingWindow is in effect at the specified date in dateTime
	 * AND is operating at the specified time given the time and day of week in dateTime. */
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