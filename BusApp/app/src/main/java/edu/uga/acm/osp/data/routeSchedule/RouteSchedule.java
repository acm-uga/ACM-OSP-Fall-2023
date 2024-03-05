package edu.uga.acm.osp.data.routeSchedule;
import java.time.*;
import java.util.ArrayList;

/**
 * Represents every moment in which a Route is supposed to be operating
 */
public class RouteSchedule {
	/**
	 * Types of catchable errors
	 * <ul>
	 *     <li>{@link #CHRONOLOGICAL}</li>
	 *     <li>{@link #FORMAT}</li>
	 *     <li>{@link #NOT_OPERATING}</li>
	 *     <li>{@link #DOES_NOT_EXIST}</li>
	 *     <li>{@link #INVALID_FIELDS}</li>
	 *     <li>{@link #NONE}</li>
	 * </ul>
	 */
	protected static enum Errors {
		/** The provided fields are not in chronological order ({@code startX} is not before {@code endX}) */
		CHRONOLOGICAL,
		/** The provided {@code String} is malformed */
		FORMAT,
		/** The Route is not operating at the given moment */
		NOT_OPERATING,
		/** Such time or date does not exist */
		DOES_NOT_EXIST,
		/** The object's fields are invalid or unable to be interpreted */
		INVALID_FIELDS,
		/** The default state of an error. No error has occurred */
		NONE
	};

	// Array of operating windows
	private OperatingWindow[] operatingWindows;
	public static final char FROM_BEGINNING = '\u0000'; // Used in parseToArray
	public static final char TO_END = '\u0000'; // Used in parseToArray

	/**
	 * Instantiates a {@code RouteSchedule} object provided an array of {@code OperatingWindow}s
	 *
	 * @param operatingWindows the array of {@code OperatingWindow} objects with which to populate the new
	 * {@code RouteSchedule}'s {@code operatingWindows} field
	 */
	public RouteSchedule(OperatingWindow[] operatingWindows) {
		this.operatingWindows = operatingWindows;
	}
	
	// METHODS
	/**
	 * Decodes the encoded {@code RouteSchedule} and instantiates one given the extracted data
	 *
	 * @param encodedRouteSchedule the encoded {@code RouteSchedule} {@code String} to decode
	 *
	 * @return an instantiated {@code RouteSchedule} representing the data encoded in {@code encodedRouteSchedule}
	 *
	 * @see "README"
	 */
	public static RouteSchedule decode(String encodedRouteSchedule) {
		// Parse the encodedRouteSchedule into encoded Operating Windows
		String[] operatingWindowStrings = parseToArray(encodedRouteSchedule, FROM_BEGINNING, TO_END, '|');
		OperatingWindow[] operatingWindows = new OperatingWindow[operatingWindowStrings.length];
		int i = 0;
		for (String encodedOperatingWindow : operatingWindowStrings) {
			System.out.println(encodedOperatingWindow);
			operatingWindows[i] = OperatingWindow.decode(encodedOperatingWindow);
			i++;
		}
		
		return new RouteSchedule(operatingWindows);
	}

	/**
	 * Encodes this {@code RouteSchedule} object as a {@code String} capable of being decoded back into an
	 * identical {@code RouteSchedule} object
	 *
	 * @return a {@code String} concisely representing this {@code RouteSchedule}'s data
	 *
	 * @see "README"
	 */
	public String encode() {
		int itemNum = 1;
		int numOfOWs = this.operatingWindows.length;
		String encodedRouteSchedule = "";
		for (OperatingWindow operatingWindow : this.operatingWindows) {
			encodedRouteSchedule += operatingWindow.encode();
			if (itemNum < numOfOWs) encodedRouteSchedule += "|";
			itemNum++;
		}
		
		return encodedRouteSchedule;
	}

	/**
	 * Creates a textual representation of this {@code RouteSchedule}'s data in a brief but human-friendly format
	 *
	 * @return a {@code String} of every {@code OperatingWindow} separated by dashed lines
	 *
	 * @see OperatingWindow#toString()
	 */
	public String toString() {
		String line = "=====================================";
		String fullString = line + "\n";
		int itemNum = 1;
		int numOfOWs = this.operatingWindows.length;
		
		for (OperatingWindow operatingWindow : this.operatingWindows) {
			fullString += operatingWindow.toString();
			if (itemNum < numOfOWs) fullString += "\n" + "-------------------------------------\n";
			itemNum++;
		}
		
		return fullString + "\n" + line;
	}

	/**
	 * Determines whether a Route operates on the given date at the given time (as specified in {@code dateTime})
	 *
	 * @param dateTime the {@code LocalDateTime} object containing the date and time in which to check for operation
	 *
	 * @return {@code true} if the provided date falls within any of this {@code RouteSchedule}'s {@code OperatingWindow}s'
	 * {@code Session}s and the provided time falls within any {@code OperatingTime} listed for the {@code DayOfWeek} that
	 * {@code dateTime} falls on in the {@code OperatingWindow}s' {@code DailySchedule}s
	 *
	 * @see OperatingWindow#operatesOnAt(LocalDateTime) 
	 */
	public boolean isOperatingOn(LocalDateTime dateTime) {
		int i = 0;
		int maxIndex = this.operatingWindows.length;
		boolean found = false;
		while (!found && i < maxIndex) {
			found = this.operatingWindows[i].operatesOnAt(dateTime);
			i++;
		}
		
		return found;
	}

	/**
	 * Determines whether a Route is operating at the moment of invocation
	 *
	 * @return {@code true} if the current date falls within any of this {@code RouteSchedule}'s {@code OperatingWindow}s'
	 * {@code Session}s and the current time falls within any {@code OperatingTime} listed for the current {@code DayOfWeek}
	 * in the {@code OperatingWindow}s' {@code DailySchedule}s
	 */
	public boolean isOperatingNow() {
		return this.isOperatingOn(LocalDateTime.now(ZoneId.of("UTC-5"))); // Always adjust to EST
	}

	/**
	 * Creates an abbreviated, textual representation of the {@code DailySchedule} data in effect on {@code dateTime}
	 * in the form of one or more lines representing each unique set of OperatingTimes that exist per set of "day indicators"
	 * <p></p>
	 * Includes each unique set of OperatingTimes per set of "day indicators" on a separate line
	 *
	 * @param dateTime the date and time with which to find the {@code DailySchedule} in effect
	 *
	 * @return a {@code String} containing all unique each unique set of {@code OperatingTime}s that exist per set of
	 * "day indicators" within the {@code DailySchedule} in effect at {@code dateTime}
	 *
	 * @see DailySchedule#fullSchedule()
	 */
	public String fullScheduleOn(LocalDateTime dateTime) {
		if (isOperatingOn(dateTime)) {
			/* Since we know the route is operating on the given dateTime, determine
			 * which one is currently in effect. */
			OperatingWindow owInEffect = operatingWithinOn(dateTime);
			return owInEffect.dailySchedule().fullSchedule();
		} else {
			return errorMessage(Errors.NOT_OPERATING);
		}
	}

	/**
	 * Creates an abbreviated, textual representation of the {@code DailySchedule} data currently in effect in the form
	 * of one or more lines representing each unique set of OperatingTimes that exist per set of "day indicators"
	 * <p></p>
	 * Includes each unique set of OperatingTimes per set of "day indicators" on a separate line
	 *
	 * @return a {@code String} containing all unique each unique set of {@code OperatingTime}s that exist per set of
	 * "day indicators" within the {@code DailySchedule} currently in effect
	 *
	 * @see DailySchedule#fullSchedule()
	 */
	public String fullSchedule() {
		return fullScheduleOn(LocalDateTime.now(ZoneId.of("UTC-5")));	
	}

	/**
	 * Determines the {@code n}th unique set of {@code OperatingTime}s in the {@code DailySchedule} in effect at
	 * {@code dateTime} and generates an abbreviated, textual representation of them and the "day indicators" they apply to
	 * <p></p>
	 * Invoking this method with the same {@code dateTime} and {@code n} value will always produce the same response.
	 *
	 * @param dateTime the date and time with which to find the {@code DailySchedule} in effect
	 * @param n the unique set of {@code OperatingTime}s to represent (1 is first)
	 *
	 * @return <b>If {@code n <= this.uniqueScheduleCount()}:</b>
	 * <p>The abbreviated, textual representation of the {@code n}th unique set of {@code OperatingTime}s and their
	 * corresponding "day indicators" (or "Weekends" and "Weekdays" when applicable)</p>
	 * <b>If the Route is not operating on {@code dateTime}:</b>
	 * <P>"Not Operating"</P>
	 * <b>Else:</b> "Does not exist"
	 *
	 * @see DailySchedule#uniqueScheduleCount()
	 * @see DailySchedule#nthSchedule(int) 
	 */
	public String nthScheduleOn(LocalDateTime dateTime, int n) {
		OperatingWindow owInEffect = operatingWithinOn(dateTime);
		if (owInEffect != null) {
			return owInEffect.dailySchedule().nthSchedule(n);
		}
		return errorMessage(Errors.NOT_OPERATING);
	}

	/**
	 * Determines the {@code n}th unique set of {@code OperatingTime}s in the {@code DailySchedule} currently in effect
	 * and generates an abbreviated, textual representation of them and the "day indicators" they apply to
	 * <p></p>
	 * Invoking this method with the same {@code n} value will always produce the same response.
	 *
	 * @param n the unique set of {@code OperatingTime}s to represent (1 is first)
	 *
	 * @return <b>If {@code n <= this.uniqueScheduleCount()}:</b>
	 * <p>The abbreviated, textual representation of the {@code n}th unique set of {@code OperatingTime}s and their
	 * corresponding "day indicators" (or "Weekends" and "Weekdays" when applicable)</p>
	 * <b>If the Route is not currently operating:</b>
	 * <P>"Not Operating"</P>
	 * <b>Else:</b> "Does not exist"
	 *
	 * @see DailySchedule#uniqueScheduleCount()
	 * @see DailySchedule#nthSchedule(int)
	 */
	public String nthSchedule(int n) {
		return nthScheduleOn(LocalDateTime.now(ZoneId.of("UTC-5")) , n);
	}

	/**
	 * Determines the first unique set of {@code OperatingTime}s in the {@code DailySchedule} in effect at
	 * {@code dateTime} and generates an abbreviated, textual representation of them and the "day indicators" they apply to
	 *
	 * @param dateTime the date and time with which to find the {@code DailySchedule} in effect
	 *
	 * @return <b>If the Route is operating on {@code dateTime}:</b>
	 * <p>The abbreviated, textual representation of the {@code n}th unique set of {@code OperatingTime}s and their
	 * corresponding "day indicators" (or "Weekends" and "Weekdays" when applicable)</p>
	 * <b>Else:</b>
	 * <P>"Not Operating"</P>
	 *
	 * @see DailySchedule#uniqueScheduleCount()
	 * @see DailySchedule#nthSchedule(int)
	 */
	public String mainScheduleOn(LocalDateTime dateTime) {
		return nthScheduleOn(dateTime, 1);
	}

	/**
	 * Determines the first unique set of {@code OperatingTime}s in the {@code DailySchedule} currently in effect and
	 * generates an abbreviated, textual representation of them and the "day indicators" they apply to
	 *
	 * @return <b>If the Route is currently operating:</b>
	 * <p>The abbreviated, textual representation of the {@code n}th unique set of {@code OperatingTime}s and their
	 * corresponding "day indicators" (or "Weekends" and "Weekdays" when applicable)</p>
	 * <b>Else:</b>
	 * <P>"Not Operating"</P>
	 *
	 * @see DailySchedule#uniqueScheduleCount()
	 * @see DailySchedule#nthSchedule(int)
	 */
	public String mainSchedule() {
		return nthSchedule(1);
	}

	/**
	 * Determines the second unique set of {@code OperatingTime}s in the {@code DailySchedule} in effect at
	 * {@code dateTime} and generates an abbreviated, textual representation of them and the "day indicators" they apply to
	 *
	 * @param dateTime the date and time with which to find the {@code DailySchedule} in effect
	 *
	 * @return <b>If the Route is operating on {@code dateTime}:</b>
	 * <p>The abbreviated, textual representation of the {@code n}th unique set of {@code OperatingTime}s and their
	 * corresponding "day indicators" (or "Weekends" and "Weekdays" when applicable)</p>
	 * <b>Else:</b>
	 * <P>"Not Operating"</P>
	 *
	 * @see DailySchedule#uniqueScheduleCount()
	 * @see DailySchedule#nthSchedule(int)
	 */
	public String altScheduleOn(LocalDateTime dateTime) {
		return nthScheduleOn(dateTime, 2);
	}

	/**
	 * Determines the second unique set of {@code OperatingTime}s in the {@code DailySchedule} currently in effect and
	 * generates an abbreviated, textual representation of them and the "day indicators" they apply to
	 *
	 * @return <b>If the Route is currently operating:</b>
	 * <p>The abbreviated, textual representation of the {@code n}th unique set of {@code OperatingTime}s and their
	 * corresponding "day indicators" (or "Weekends" and "Weekdays" when applicable)</p>
	 * <b>Else:</b>
	 * <P>"Not Operating"</P>
	 *
	 * @see DailySchedule#uniqueScheduleCount()
	 * @see DailySchedule#nthSchedule(int)
	 */
	public String altSchedule() {
		return nthSchedule(2);
	}

	/**
	 * Determines the number of unique {@code OperatingTime} arrays that exist in the {@code DailySchedule} in effect
	 * on/at {@code dateTime}
	 *
	 * @param dateTime the date and time with which to find the number of unique schedules on/at
	 *
	 * @return the number of unique {@code OperatingTime} arrays (as determined by elements) that exist in the
	 * {@code dailySchedule} {@code HashMap} of the {@code DailyScedule} in effect on/at {@code dateTime}
	 */
	public int uniqueSchedulesOn(LocalDateTime dateTime) {
		OperatingWindow owInEffect = operatingWithinOn(dateTime);
		if (owInEffect != null) {
			return owInEffect.dailySchedule().uniqueScheduleCount();
		}
		return 0;
	}

	/**
	 * Determines the number of unique {@code OperatingTime} arrays that exist in the {@code DailySchedule} currently in
	 * effect
	 *
	 * @return the number of unique {@code OperatingTime} arrays (as determined by elements) that exist in the
	 * {@code dailySchedule} {@code HashMap} of the {@code DailyScedule} currently in effect
	 */
	public int uniqueSchedules() {
		return uniqueSchedulesOn(LocalDateTime.now(ZoneId.of("UTC-5")));
	}
	
	// Returns the OperatingWindow currently in effect given the specified date and time.

	/**
	 * Determines the {@code OperatingWindow} currently in effect on/at {@code dateTime}
	 *
	 * @param dateTime the date and time with which to find the {@code OperatingWindow} in effect
	 *
	 * @return the {@code OperatingWindow} in effect on/at {@code dateTime}
	 */
	private OperatingWindow operatingWithinOn(LocalDateTime dateTime) {
		int i = 0;
		int maxIndex = this.operatingWindows.length;
		OperatingWindow found = null;
		
		// Iterate through this RouteSchedule's OperatingWindow's until the current one in effect is found
		while (i < maxIndex && found == null) {
			if (this.operatingWindows[i].operatesOnAt(dateTime)) {
				found = this.operatingWindows[i];
			} else {
				i++;
			}
		}
		
		return found;
	}
	
	// TODO method to get an encoded RouteSchedule through the terminal?

	/**
	 * Provides a standardized error message to print to the console given the provided error
	 *
	 * @param error the type of error whose message needs to be retrieved
	 *
	 * @return the correct error message given the passed error
	 */
	private static String errorMessage(Errors error) {
		switch (error) {
			case NOT_OPERATING:
				return "Not Operating";
			default:
				return "Unknown Error";
		}
	}
	
	// PARSERS
	/**
	 * Parses the provided {@code fullString} into an array of {@code String}s, beginning parsing at {@code startOfArray},
	 * ending parsing at {@code endOfArray}, and locating elements with {@code itemSeparator}
	 *
	 * @param fullString the {@code itemSeparator}-delimited {@code String} to parse into separate {@code String} elements
	 * @param startOfArray the character to begin parsing at (the character immediately before the first element)
	 * @param endOfArray the character to end parsing at (the character immediately after the last element)
	 * @param itemSeparator the character delimiting each array element
	 *
	 * @return an array of {@code String}s derived from the {@code itemSeparated}-separated substrings of {@code fullString}
	 */
	protected static String[] parseToArray(String fullString, char startOfArray, char endOfArray, char itemSeparator) {
		// Determine what part of fullString contains the array to parse
		int startIndex = 0;
		int endIndex = fullString.length();
		if (startOfArray != FROM_BEGINNING) {
			startIndex = fullString.indexOf(startOfArray) + 1;
		}
		if (endOfArray != TO_END) {
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
	/**
	 * Generates a {@code String} representation of a {@code String} array with elements separated by {@code itemSeparator}
	 *
	 * @param array the array of {@code String}s to represent as a single {@code String}
	 * @param itemSeparator the delimiting character to add between each element in the final {@code String}
	 *
	 * @return a {@code String} containing the elements of {@code array} separated by {@code itemSeparator}
	 */
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

	/**
	 * Generates a {@code String} representation of a {@code String} array without item delimiters
	 *
	 * @param array the array of {@code String}s to represent as a single {@code String}
	 *
	 * @return a {@code String} containing the elements of {@code array} without item delimiters
	 */
	protected static String strArrayToStr(String[] array) {
		String arrayAsString = "";
		
		// Concatenate every item in array to arrayAsString, with no item separator
		for (String item : array) {
			arrayAsString += item;
		}
		
		return arrayAsString;
	}
}