package routeSchedule;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class RouteSchedule {
	protected static enum ERRORS {CHRONOLOGICAL, FORMAT, OVERLAP, NOT_OPERATING, DOES_NOT_EXIST, INVALID_FIELDS, NONE};
	
	// ArrayList of operating windows
	private OperatingWindow[] schedule;
	protected static final char FromBeginning = '\u0000'; // Used in parseToArray
	protected static final char ToEnd = '\u0000'; // Used in parseToArray
	
	// Construct a RouteSchedule given a list of OperatingWindows
	public RouteSchedule(OperatingWindow[] schedule) {
		this.schedule = schedule;
	}
	
	// METHODS
	// Provided an encoded RouteSchedule String, return an instantiated RouteSchedule
	public static RouteSchedule decode(String encodedRouteSchedule) {
		// Parse the encodedRouteSchedule into encoded Operating Windows
		String[] operatingWindowStrings = parseToArray(encodedRouteSchedule, FromBeginning, ToEnd, '|');
		OperatingWindow[] operatingWindows = new OperatingWindow[operatingWindowStrings.length];
		int i = 0;
		for (String encodedOperatingWindow : operatingWindowStrings) {
			System.out.println(encodedOperatingWindow);
			operatingWindows[i] = OperatingWindow.decode(encodedOperatingWindow);
			i++;
		}
		
		return new RouteSchedule(operatingWindows);
	}
	
	// Encode this schedule object into a String

	/* Permits storage of the {@code CustomSession} in a condensed format that is easy to understand and
	 * modify as-is. */
	public String encode() {
		int itemNum = 1;
		int numOfOWs = this.schedule.length;
		String encodedRouteSchedule = "";
		for (OperatingWindow operatingWindow : this.schedule) {
			encodedRouteSchedule += operatingWindow.encode();
			if (itemNum < numOfOWs) encodedRouteSchedule += "|";
			itemNum++;
		}
		
		return encodedRouteSchedule;
	}
	
	// Return the OperatingWindow's data as a String in "Human" format
	public String toString() {
		String line = "=====================================";
		String fullString = line + "\n";
		int itemNum = 1;
		int numOfOWs = this.schedule.length;
		
		for (OperatingWindow operatingWindow : this.schedule) {
			fullString += operatingWindow.toString();
			if (itemNum < numOfOWs) fullString += "\n" + "-------------------------------------\n";
			itemNum++;
		}
		
		return fullString + "\n" + line;
	}
	
	/* Returns true if the passed LocalDateTime falls within an OperatingTime on the current day within
	 * one of this RouteSchedule's OperatingWindow's. */
	public boolean isOperatingOn(LocalDateTime dateTime) {
		int i = 0;
		int maxIndex = this.schedule.length;
		boolean found = false;
		while (!found && i < maxIndex) {
			found = this.schedule[i].operatesOnAt(dateTime);
			i++;
		}
		
		return found;
	}
	
	/* Shorthand for isOperatingOn(). Returns true if the current LocalDateTime falls within an OperatingTime
	 * on the current day within one of this RouteSchedule's OperatingWindow's. */
	public boolean isOperating() {
		return this.isOperatingOn(LocalDateTime.now(ZoneId.of("UTC-5"))); // Always adjust to EST
	}
	
	/* Returns a String containing the full Daily Schedule in abbreviated human format
	 * for the given dateTime if the Route operates on the dateTime. Otherwise, return
	 * "Not Operating" */
	public String fullScheduleOn(LocalDateTime dateTime) {
		if (isOperatingOn(dateTime)) {
			/* Since we know the route is operating on the given dateTime, determine
			 * which one is currently in effect. */
			OperatingWindow owInEffect = operatingWithinOn(dateTime);
			return owInEffect.dailySchedule().fullSchedule();
		} else {
			return errorMessage(ERRORS.NOT_OPERATING);
		}
	}
	
	/* Shorthand for fullScheduleOn(). Returns a String containing the full Daily Schedule in abbreviated
	 * human format for the current date and time if the Route is operating. Otherwise, return "Not 
	 * Operating" */
	public String fullSchedule() {
		return fullScheduleOn(LocalDateTime.now(ZoneId.of("UTC-5")));	
	}
	
	/* Returns a String containing the nth unique Daily Schedule in abbreviated human
	 * format. If n exceeds the number of unique Daily Schedules, "Does not exist" is
	 * returned. */
	public String nthScheduleOn(LocalDateTime dateTime, int n) {
		OperatingWindow owInEffect = operatingWithinOn(dateTime);
		if (owInEffect != null) {
			return owInEffect.dailySchedule().nthSchedule(n);
		}
		return errorMessage(ERRORS.NOT_OPERATING);
	}
	
	/* Shorthand for nthScheduleOn(). Returns a String containing the nth unique Daily Schedule in abbreviated
	 * human format for the current date and time. If n exceeds the number of unique Daily Schedules, "Does not
	 * exist" is returned. */
	public String nthSchedule(int n) {
		return nthScheduleOn(LocalDateTime.now(ZoneId.of("UTC-5")) , n);
	}
	
	/* Returns a String containing the main/primary unique Daily Schedule in abbreviated,
	 * human format if one exists on that date and time. If the route is not operating,
	 * returns "Not Operating." */
	public String mainScheduleOn(LocalDateTime dateTime) {
		return nthScheduleOn(dateTime, 1);
	}
	
	/* Returns a String containing the main/primary unique Daily Schedule in abbreviated,
	 * human format if one exists on the date and time of instantiation. If the route is not
	 * operating, returns "Not Operating." */
	public String mainSchedule() {
		return nthSchedule(1);
	}
	
	/* Returns a String containing the alt/secondary unique Daily Schedule in abbreviated,
	 * human format if one exists on that date and time. If the route is not operating,
	 * returns "Not Operating." If no alt schedule exists, return "Does Not Exist." */
	public String altScheduleOn(LocalDateTime dateTime) {
		return nthScheduleOn(dateTime, 2);
	}
	
	/* Returns a String containing the alt/secondary unique Daily Schedule in abbreviated,
	 * human format if one exists on the date and time of instantiation. If the route is not
	 * operating, returns "Not Operating." If no alt schedule exists, return "Does Not Exist." */
	public String altSchedule() {
		return nthSchedule(2);
	}
	
	public int uniqueSchedulesOn(LocalDateTime dateTime) {
		OperatingWindow owInEffect = operatingWithinOn(dateTime);
		if (owInEffect != null) {
			return owInEffect.dailySchedule().uniqueScheduleCount();
		}
		return 0;
	}
	
	public int uniqueSchedules() {
		return uniqueSchedulesOn(LocalDateTime.now(ZoneId.of("UTC-5")));
	}
	
	// Returns the OperatingWindow currently in effect given the specified date and time.
	private OperatingWindow operatingWithinOn(LocalDateTime dateTime) {
		int i = 0;
		int maxIndex = this.schedule.length;
		OperatingWindow found = null;
		
		// Iterate through this RouteSchedule's OperatingWindow's until the current one in effect is found
		while (i < maxIndex && found == null) {
			if (this.schedule[i].operatesOnAt(dateTime)) {
				found = this.schedule[i];
			} else {
				i++;
			}
		}
		
		return found;
	}
	
	// TODO method to get an encoded RouteSchedule through the terminal?
	
	private static String errorMessage(ERRORS error) {
		switch (error) {
			case NOT_OPERATING:
				return "Not Operating";
			default:
				return "Unknown Error";
		}
	}
	
	
	// PARSERS
	/* Parse the provided arrayAsString into an array of Strings. The start of the array is indicated by
	 * the startOfArray char and the end by endOfArray (exclusive). Array elements are separated by itemSeparator. If
	 * startOfArray and/or endOfArray is 0, the arrayAsString is assumed to begin and end at the first
	 * and last character respectively. */
	protected static String[] parseToArray(String fullString, char startOfArray, char endOfArray, char itemSeparator) {
		// Determine what part of fullString contains the array to parse
		int startIndex = 0;
		int endIndex = fullString.length();
		if (startOfArray != FromBeginning) {
			startIndex = fullString.indexOf(startOfArray) + 1;
		}
		if (endOfArray != ToEnd) {
			endIndex = fullString.indexOf(endOfArray);
		}
		String arrayString = fullString.substring(startIndex, endIndex);
		
		// Prepare an ArrayList to hold the parsed values
		ArrayList<String> parsedItems = new ArrayList<>();
		
		// If arrayString only contains one array element, just parse that. Otherwise, iterate through and parse all items.
		if (arrayString.indexOf(itemSeparator) == -1) {
			parsedItems.add(arrayString);
		} else {
			int endOfItem;
			String item;
			boolean itemsLeft = true;
			while (itemsLeft) {
				// If this isn't the last item...
				if (arrayString.indexOf(itemSeparator) != -1) {
					// Determine the end of the item by finding the next itemSeparator
					endOfItem = arrayString.indexOf(itemSeparator);
					item = arrayString.substring(0, endOfItem);
					parsedItems.add(item);
					
					// Set the arrayString to the remaining items
					arrayString = arrayString.substring(endOfItem + 1);
				// If this is the last item...
				} else {
					// Set the end of the item to the last character of arrayString
					endOfItem = arrayString.length();
					item = arrayString.substring(0, endOfItem);
					parsedItems.add(item);
					itemsLeft = false;
				}				
			}
		}
		
		// Now that the number of items is known, convert parsedItems back into an array we can return
		String[] items = new String[parsedItems.size()];
		int i = 0;
		for (String element : parsedItems) {
			items[i] = element;
			i++;
		}
		
		return items;
	}
	
	// FORMATTERS
	/* Returns a String representation of a String array with inner items separated
	* by itemSeparator */
	protected static String strArrayToStr(String[] array, String itemSeparator) {
		int i = 1;
		int numOfItems = array.length;
		String arrayAsString = "";
		
		/* Concatenate every item in array to arrayAsString, appending itemSeparator if the current item isn't
		* the last */
		for (String item : array) {
			arrayAsString += item;
			if (i < numOfItems) {
				arrayAsString += itemSeparator;
			}
			i++;
		}
		
		return arrayAsString;
	}
	
	// Returns a String representation of a String array with no separation of items
	protected static String strArrayToStr(String[] array) {
		String arrayAsString = "";
		
		// Concatenate every item in array to arrayAsString, with no item separator
		for (String item : array) {
			arrayAsString += item;
		}
		
		return arrayAsString;
	}
}