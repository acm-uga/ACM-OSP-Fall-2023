package routeSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DailySchedule {
	/* DailySchedule's of type...
	 * GENERAL contain OperatingTimes that are not mapped to specific days of the week.
	 * SPECIFIC indicate OperatingTimes are mapped to specific days of the week. */
	private enum TYPE {GENERAL, SPECIFIC};
	
	private HashMap<DayOfWeek, OperatingTime[]> dailySchedule;
	private TYPE type;
	
	public DailySchedule(HashMap<DayOfWeek, OperatingTime[]> dailySchedule, TYPE type) {
		this.dailySchedule = dailySchedule;
		this.type = type;
	}
	
	// METHODS
	
	/* Takes the encoded DailySchedule (as stored in the database) and returns an instantiated HashMap
	* of every DayOfWeek to a list of OperatingTime's. */
	protected static DailySchedule decode(String encodedDailySchedule) {
		// Initialize a HashMap
		HashMap<DayOfWeek,OperatingTime[]> decodedDailySchedule = blankDailySchedule();
		
		// Initialize variables to store start and endpoints of search
		int lookFrom = 0;
		int lookTo = 0;
		char lookingAt;
		String timesString;
		
		/* Prepare an ArrayList holding the days of the week whose times are currently being read (an arbitrary
		* amount) */
		ArrayList<DayOfWeek> currentlySetting = new ArrayList<>();
		
		// Prepare an ArrayList holding the operating time Strings for the days of the week in currentlySetting
		ArrayList<String> operatingTimeStrings = new ArrayList<>();
		
		// Prepare an ArrayList holding the OperatingTimes for the days of the week in currentlySetting
		ArrayList<OperatingTime> operatingTimes = new ArrayList<>();
		
		// Account for day indicator-less lists of times (which means operating times apply to every day)
		if (findNextDay(lookFrom, encodedDailySchedule) == -1) {
			/* Add all the operating time Strings for this iteration's set of days to the ArrayList to be instantiated
			* as OperatingTimes */
			Collections.addAll(operatingTimeStrings, 
					RouteSchedule.parseToArray(encodedDailySchedule, RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ','));
			
			// Instantiate all the operatingTimes and add them to the operatingTimes ArrayList
			for (String timeString : operatingTimeStrings) {
				operatingTimes.add(OperatingTime.decode(timeString));
			}
			
			// Now that the number of OperatingDate objects is known, convert it into an array to be mapped to each day
			OperatingTime[] knownOperatingTimes = new OperatingTime[operatingTimes.size()];
			int i = 0;
			for (OperatingTime operatingTime : operatingTimes) {
				knownOperatingTimes[i] = operatingTime;
				i++;
			}
			
			// Map known OperatingTimes to each day in currentlySetting
			for (DayOfWeek day : DayOfWeek.values()) {
				decodedDailySchedule.put(day, knownOperatingTimes);
			}
		}
		
		// Iterate through each day of the week so long as one exists
		while (findNextDay(lookFrom, encodedDailySchedule) != -1) {			
			// Identify the substring of encodedDailyTimes in which to check for encoded days
			lookFrom = findNextDay(lookFrom, encodedDailySchedule);
			lookTo = encodedDailySchedule.substring(lookFrom).indexOf(':') + encodedDailySchedule.substring(0,lookFrom).length();
			
			/* Determine whether it's a single day, multiple days, or ranges of days by peeking at
			* one character ahead of lookFrom. */
			lookingAt = encodedDailySchedule.charAt(lookFrom + 1);	
			switch (lookingAt) {
				case ':': // Single day, OperatingTimes immediately follow :
					// Add the day prior to the : to the currentlySetting ArrayList
					lookingAt = encodedDailySchedule.charAt(lookFrom);
					currentlySetting.add(toDayOfWeek(lookingAt));
					break;
				case '-': // Range of days, one more day indicator follows -
					// Add all the days that fall within the range to the currentlySetting ArrayList
					Collections.addAll(currentlySetting, daysFromRange(encodedDailySchedule.substring(lookFrom, lookTo)));
					break;
				default: // Set of days, one or more day indicators follow current char
					// Add all the days that appear in the set of days to the currentlySetting ArrayList
					Collections.addAll(currentlySetting, daysFromSet(encodedDailySchedule.substring(lookFrom, lookTo)));
			}
			
			/* Now that we know what days of the week we're setting the following OperatingTimes for, process
			* those operating times. */
			// Identify the substring of encodedDailyTimes in which to check for encoded OperatingTimes
			lookFrom = lookTo + 1;
			lookTo = encodedDailySchedule.substring(lookFrom).indexOf(';') + encodedDailySchedule.substring(0,lookFrom).length();
			timesString = encodedDailySchedule.substring(lookFrom, lookTo);
			
			/* Add all the operating time Strings for this iteration's set of days to the ArrayList to be instantiated
			* as OperatingTimes */
			Collections.addAll(operatingTimeStrings, RouteSchedule.parseToArray(timesString,
					RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ','));
			
			// Instantiate all the operatingTimes and add them to the operatingTimes ArrayList
			for (String timeString : operatingTimeStrings) {
				operatingTimes.add(OperatingTime.decode(timeString));
			}
			
			// Now that the number of OperatingDate objects is known, convert it into an array to be mapped to each day
			OperatingTime[] knownOperatingTimes = new OperatingTime[operatingTimes.size()];
			int i = 0;
			for (OperatingTime operatingTime : operatingTimes) {
				knownOperatingTimes[i] = operatingTime;
				i++;
			}
			
			// Map knownOperatingTimes to each day in currentlySetting
			for (DayOfWeek settingDay : currentlySetting) {
				decodedDailySchedule.put(settingDay, knownOperatingTimes);
			}
			
			// Clear the temporary ArrayLists before the next iteration
			currentlySetting.clear();
			operatingTimeStrings.clear();
			operatingTimes.clear();
		}
		
		return new DailySchedule(decodedDailySchedule, TYPE.SPECIFIC);
	}

	/* Takes the "encoded" String of "Daily Times" (as stored in the database) and returns an instantiated HashMap of
	 * every DayOfWeek to a list of OperatingTime objects. Given the date(s), it will find the correct DayOfWeek and map all
	 * OperatingTimes to it. */
	protected static DailySchedule decode(OperatingDate[] operatingDates, String encodedDailyTimes) {
		// Initialize a HashMap
		HashMap<DayOfWeek,OperatingTime[]> decodedDailySchedule = blankDailySchedule();
		
		// Initialize an ArrayList holding the days of the week whose times are currently being read
		ArrayList<DayOfWeek> daysToSet = new ArrayList<>();
		
		// Find the day of the week that date falls on
		for (OperatingDate date : operatingDates) {
			DayOfWeek day = date.startDate().getDayOfWeek();
			daysToSet.add(day);
		}
		
		// Parse the encodedDailyTimes String and save the OperatingTimes to the operatingTimes Array
		String[] operatingTimeStrings = RouteSchedule.parseToArray(encodedDailyTimes,
				RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ',');
		OperatingTime[] operatingTimes = new OperatingTime[operatingTimeStrings.length];
		
		int i = 0;
		for (String operatingTimeString : operatingTimeStrings) {
			operatingTimes[i] = OperatingTime.decode(operatingTimeString);
			i++;
		}
		
		// For every day in daysToSet, add operatingTimes to the correct day in decodedDailySchedule
		for (DayOfWeek dayWithTimes : daysToSet) {
			decodedDailySchedule.put(dayWithTimes, operatingTimes);
		}
		
		return new DailySchedule(decodedDailySchedule, TYPE.GENERAL);
	}

	/* Take a HashMap mapping DayOfWeek's to Array's of OperatingTime's and encode it as a String
	 * that can be decoded and used to construct a new dailySchedules HashMap */
	protected String encode() {
		HashMap<Character, String> fullDailySchedule = new HashMap<>();
		char dayIndicator;
		String operatingTimesString;
		
		/* Convert the dailySchedule HashMap<DayOfWeek,OperatingTime[]) into a HashMap of day indicator Characters
		 * and encoded OperatingTime Strings */
		int i = 0;
		for (Map.Entry<DayOfWeek,OperatingTime[]> day : this.dailySchedule.entrySet()) {
			if (day.getValue() != null) {
				// Get the day of week indicator for the current day's DayOfWeek key
				dayIndicator = toDayIndicator(day.getKey());
				String[] operatingTimeStrings = new String[day.getValue().length];
				
				// Convert the value, an array of OperatingTimes, into an array of operating time Strings
				i = 0;
				for (OperatingTime operatingTime : day.getValue()) {
					operatingTimeStrings[i] = operatingTime.encode();
					i++;
				}
				
				// Convert the array of operating time Strings to a string representing the array
				operatingTimesString = RouteSchedule.strArrayToStr(operatingTimeStrings, ",");
				fullDailySchedule.put(dayIndicator, operatingTimesString);
			}
		}
		
		// Determine which days have the same operating times
		HashMap<String, String> groupedDailySchedule = new HashMap<>();
		String dailyScheduleString;
		String dayIndicatorsString;
		String keyToReplace;
		int accountedFor = 0;
		
		/* Group days with the same list of operating times together and place into
		* condensedDailySchedule */
		for (char key : fullDailySchedule.keySet()) {
			/* Prevents processing more days than we already have to, since one
			 * iteration of the parent loop is capable of iterating through all other
			 * entries. */
			if (accountedFor < fullDailySchedule.size()) {
				dayIndicatorsString = "";
				keyToReplace = "";
				
				// Check if the daily times string already exists in condensedDailySchedule
				dailyScheduleString = fullDailySchedule.get(key);
				
				for (Map.Entry<String, String> schedule : groupedDailySchedule.entrySet()) {
					if (dailyScheduleString.equals(schedule.getValue())) {							
						if (keyToReplace.equals("")) {
							keyToReplace = schedule.getKey();
							dayIndicatorsString = keyToReplace;
							//System.out.println(String.valueOf(key)+ " is similar to " + keyToReplace);
						}
						dayIndicatorsString = dayIndicatorsString + String.valueOf(key);
						accountedFor++;
					}
				}
				
				// If this entry's daily times string does not exist in condensedDailySchedule, add it
				if (dayIndicatorsString.length() == 0) {
					//System.out.println("Added day " + String.valueOf(key));
					groupedDailySchedule.put(String.valueOf(key), dailyScheduleString);
					accountedFor++;
				/* Otherwise, remove the existing entry with the value of dailyScheduleString
				 * and replace it with a new entry containing the same value but dayIndicatorsString
				 * as the key (the "condensed" key) */
				} else {
					//System.out.println("Replace... " + keyToReplace);
					//System.out.println("...with " + dayIndicatorsString);
					groupedDailySchedule.remove(keyToReplace);
					groupedDailySchedule.put(dayIndicatorsString, dailyScheduleString);
				}
			}
		}
		
		//System.out.println("Grouped:");
		//System.out.println(groupedDailySchedule.toString());
		
		// Sort the groupedDailySchedule's keys in ascending order
		HashMap<String, String> sortedDailySchedule = new HashMap<>();
		keyToReplace = "";
		dayIndicatorsString = "";
		
		for (Map.Entry<String, String> schedule : groupedDailySchedule.entrySet()) {
			// If the current key is more than one character, sort it
			if (schedule.getKey().length() > 1) {
				i = 0;
				keyToReplace = schedule.getKey();
				dayIndicatorsString = "";
				DayOfWeek[] daysOfWeekArray = new DayOfWeek[keyToReplace.length()];
				
				// Convert every day in schedule's key to a DayOfWeek so it can be sorted
				while (i < keyToReplace.length()) {
					daysOfWeekArray[i] = toDayOfWeek(keyToReplace.charAt(i));
					i++;
				}
				
				// Sort daysArray in ascending order
				Arrays.sort(daysOfWeekArray);
				
				// Convert daysArray back into an array of characters
				i = 0;
				String[] dayIndicatorsArray = new String[daysOfWeekArray.length];
				while (i < dayIndicatorsArray.length) {
					dayIndicatorsArray[i] = String.valueOf(toDayIndicator(daysOfWeekArray[i]));
					i++;
				}
				
				// Convert the array of characters back into a string
				dayIndicatorsString = RouteSchedule.strArrayToStr(dayIndicatorsArray);
				
				/* Remove the keyToReplace entry and replace with the sorted key
				* and same value */
				sortedDailySchedule.put(dayIndicatorsString, schedule.getValue());
			} else {
				sortedDailySchedule.put(schedule.getKey(), schedule.getValue());
			}
		}
		
		//System.out.println("Sorted:");
		//System.out.println(sortedDailySchedule.toString());
		
		// Now that groupedDailySchedule's keys are sorted, condense them when possible
		HashMap<String, String> condensedDailySchedule = new HashMap<>();
		boolean daysAreConsecutive;
		DayOfWeek firstDay;
		DayOfWeek secondDay;
		keyToReplace = "";
		dayIndicatorsString = "";
		
		for (Map.Entry<String, String> schedule : sortedDailySchedule.entrySet()) {
			// If the current key is more than 2 characters, consider it for condensing
			if (schedule.getKey().length() > 2) {
				keyToReplace = schedule.getKey();
				daysAreConsecutive = true;
				i = 0;
				
				// Iterate through the characters to determine if the days are consecutive
				while (i < (keyToReplace.length() - 1) && daysAreConsecutive) {
					firstDay = toDayOfWeek(keyToReplace.charAt(i));
					secondDay = toDayOfWeek(keyToReplace.charAt(i + 1));
					
					//System.out.println(firstDay.getValue());
					//System.out.println(secondDay.getValue());
					//System.out.println("Consecutive? " + ((secondDay.getValue() - firstDay.getValue()) == 1));
					daysAreConsecutive = ((secondDay.getValue() - firstDay.getValue()) == 1);
					i++;
				}
				
				// If the days are consecutive, make them a range
				if (daysAreConsecutive) {
					dayIndicatorsString = keyToReplace.charAt(0) + "-" + keyToReplace.charAt(keyToReplace.length() - 1);
					condensedDailySchedule.put(dayIndicatorsString, schedule.getValue());
				} else {
					condensedDailySchedule.put(keyToReplace, schedule.getValue());
				}
			} else {
				condensedDailySchedule.put(schedule.getKey(), schedule.getValue());
			}
		}
		
		//System.out.println("Condensed:");
		//System.out.println(condensedDailySchedule.toString());
		
		// Convert condensedDailySchedule into a String Array
		String[] dailyScheduleStrings = new String[condensedDailySchedule.size()];
		i = 0;
		
		for (Map.Entry<String, String> schedule : condensedDailySchedule.entrySet()) {
			/* This, eventually, has the potential to be modified to remove the need for ; on the last day.
			* Doing so would require modification of OperatingWindow.decode() too. */
			dailyScheduleStrings[i] = schedule.getKey() + ":" + schedule.getValue() + ";";
			i++;
		}
		
		// Sort the array in ascending order of the first character of each element
		dailyScheduleStrings = sortByDay(dailyScheduleStrings);
		
		// Convert the sorted array into a string
		String encodedDailySchedule = RouteSchedule.strArrayToStr(dailyScheduleStrings);
		
		
		return encodedDailySchedule;
	}
	
	// Returns true if operating on the given day of the week at the given time. Else, returns false.
	protected boolean operatesOnAt(DayOfWeek day, LocalTime time) {
		boolean isOperating = false;
		
		// Ensure a list of times exists for the specified day
		if (this.dailySchedule.get(day) != null) {
			isOperating = true;
		}
		
		/* Now that we know OperatingTimes exist for the given day of week, check the correct
		 * day of week to see if the current time falls within an OperatingTime */
		if (isOperating) {
			isOperating = false;
			int maxIndex = this.dailySchedule.get(day).length;
			OperatingTime[] operatingTimes = this.dailySchedule.get(day);
			int i = 0;
			while (!isOperating && i < maxIndex) {
				isOperating = operatingTimes[i].operatesAt(time);
				i++;
			}
		}
		
		return isOperating;
	}
	
	/**
	 * Creates a blank "daily schedule" HashMap mapping every day of the week to a null "OperatingTime"
	 * array.
	 * 
	 * @return a HashMap with keys of every Java.time.DayOfWeek and corresponding null values, intended
	 * to be replaced with OperatingTime arrays
	 */
	public static HashMap<DayOfWeek,OperatingTime[]> blankDailySchedule() {
		HashMap<DayOfWeek,OperatingTime[]> emptyDailySchedule = new HashMap<>();
		
		// Add every DayOWeek to the HashMap as keys with a default null value
		emptyDailySchedule.put(DayOfWeek.SUNDAY, null);
		emptyDailySchedule.put(DayOfWeek.MONDAY, null);
		emptyDailySchedule.put(DayOfWeek.TUESDAY, null);
		emptyDailySchedule.put(DayOfWeek.WEDNESDAY, null);
		emptyDailySchedule.put(DayOfWeek.THURSDAY, null);
		emptyDailySchedule.put(DayOfWeek.FRIDAY, null);
		emptyDailySchedule.put(DayOfWeek.SATURDAY, null);
		
		return emptyDailySchedule;
	}

	/* Returns the index of the next day indicator (U,M,T,W,R,F, or S) in encodedDailyTimes
	 * given the index to start searching from (left-to-right), startAt. */
	private static int findNextDay(int startAt, String encodedDailyTimes) {
		/* Define the substring to search within as the characters from startAt to the end of 
		 * encodedDailyTimes */
		String searchWithin = encodedDailyTimes.substring(startAt);
		
		// Define valid dayIndicators (ordered to allow for binary search)
		char[] dayIndicators = new char[]{'F','M','R','S','T','U','W'};
		
		int lookingAt = 0; // The index of the character in searchWithin currentlyBeing evaluated
		boolean found = false;
		
		while (!found && lookingAt < searchWithin.length()) {
			if (Arrays.binarySearch(dayIndicators, searchWithin.charAt(lookingAt)) >= 0){
				found = true;
			} else {
				lookingAt++;
			}
		}
		
		// If a day indicator has been found, return its correct index. Otherwise, return -1.
		return (found) ? lookingAt + startAt : -1;
	}
	
	// Take the "day indicator" character and convert it to a valid DayOfWeek value
	private static DayOfWeek toDayOfWeek(char dayIndicator) {
		switch (dayIndicator) {
			case 'U':
				return DayOfWeek.SUNDAY;
			case 'M':
				return DayOfWeek.MONDAY;
			case 'T':
				return DayOfWeek.TUESDAY;
			case 'W':
				return DayOfWeek.WEDNESDAY;
			case 'R':
				return DayOfWeek.THURSDAY;
			case 'F':
				return DayOfWeek.FRIDAY;
			case 'S':
				return DayOfWeek.SATURDAY;
			default:
				System.out.println("Invalid Day of Week");
				return DayOfWeek.SUNDAY;
		}
	}
	
	// Take the DayOfWeek and convert it to a valid "day indicator" character
	private static char toDayIndicator(DayOfWeek day) {
		switch (day) {
			case SUNDAY:
				return 'U';
			case MONDAY:
				return 'M';
			case TUESDAY:
				return 'T';
			case WEDNESDAY:
				return 'W';
			case THURSDAY:
				return 'R';
			case FRIDAY:
				return 'F';
			case SATURDAY:
				return 'S';
			default:
				System.out.println("Invalid DayOfWeek");
				return (char)0;
		}
	}
	
	// Return an Array of DayOfWeeks from the first detected "day indicator" char to the last, inclusive.
	private static DayOfWeek[] daysFromRange(String dayRange) {
		// Determine the start and end day (the characters before and after the -, the centerOfRange)
		DayOfWeek startDay = toDayOfWeek(dayRange.charAt(0));
		DayOfWeek endDay = toDayOfWeek(dayRange.charAt(2));
		
		// Prepare an ArrayList to hold an arbitrary number of days included in the range
		ArrayList<DayOfWeek> includedDaysList = new ArrayList<>();
		
		/* If the day in the DayOfWeek enum is either the startDay, endDay, or some day in between,
		 * add it to includedDaysList */
		boolean included = false;
		for (DayOfWeek day : DayOfWeek.values()) { 
		    if (day == startDay) included = true;
		    if (included) includedDaysList.add(day);
		    if (day == endDay) included = false;
		}
		
		/* Now that the number of included days is known, convert includedDaysList back into
		 * an array we can return */
		DayOfWeek[] includedDays = new DayOfWeek[includedDaysList.size()];
		int i = 0;
		for (DayOfWeek day : includedDaysList) {
			includedDays[i] = day;
			i++;
		}
		
		return includedDays;
	}
	
	/* Return an Array of DayOfWeeks containing every "day indicator" char from the first in the set to the last
	* (designated as the last character before :) */
	private static DayOfWeek[] daysFromSet(String daySet) {
		int lookingAtIndex = 0;
		char lookingAtChar;
		
		// Prepare an ArrayList to hold an arbitrary number of days included in the set
		ArrayList<DayOfWeek> includedDaysList = new ArrayList<>();
		
		// Convert every day indicator char that appears before : to a DayOfWeek and add it to includedDaysList
		while (lookingAtIndex < daySet.length()) {
			lookingAtChar = daySet.charAt(lookingAtIndex);
			includedDaysList.add(toDayOfWeek(lookingAtChar));
			lookingAtIndex++;
		}
		
		/* Now that the number of included days is known, convert includedDaysList back into
		 * an array we can return */
		DayOfWeek[] includedDays = new DayOfWeek[includedDaysList.size()];
		int i = 0;
		for (DayOfWeek day : includedDaysList) {
			includedDays[i] = day;
			i++;
		}
		
		return includedDays;
	}
	
	/* Accepts an Array of String elements with leading dayIndicators and returns an array
	 * in ascending (day) order by the first indicator of each element. */
	private static String[] sortByDay(String[] unsortedArray) {
		// Create a custom, adjusted DayOfWeek enum locally to store our standard week order
		enum AdjDayOfWeek {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};
		
		// Get the first AdjDayOfWeek of every element and add them to an array
		AdjDayOfWeek[] adjDays = new AdjDayOfWeek[unsortedArray.length];
		HashMap<DayOfWeek,String> referenceMap = new HashMap<>();
		int i = 0;
		char dayIndicator;
		AdjDayOfWeek dayOfWeek;
		
		for (String day : unsortedArray) {
			dayIndicator = day.charAt(0);
			switch(dayIndicator) {
				case 'U':
					dayOfWeek = AdjDayOfWeek.SUNDAY;
					break;
				case 'M':
					dayOfWeek = AdjDayOfWeek.MONDAY;
					break;
				case 'T':
					dayOfWeek = AdjDayOfWeek.TUESDAY;
					break;
				case 'W':
					dayOfWeek = AdjDayOfWeek.WEDNESDAY;
					break;
				case 'R':
					dayOfWeek = AdjDayOfWeek.THURSDAY;
					break;
				case 'F':
					dayOfWeek = AdjDayOfWeek.FRIDAY;
					break;
				case 'S':
					dayOfWeek = AdjDayOfWeek.SATURDAY;
					break;
				default:
					dayOfWeek = AdjDayOfWeek.SUNDAY;
			}
			adjDays[i] = dayOfWeek;
			referenceMap.put(toDayOfWeek(dayIndicator), day);
			i++;
		}
		
		// Sort that array in ascending order
		Arrays.sort(adjDays);
		
		// Convert adjDays into an Array of DayOfWeek
		DayOfWeek[] days = new DayOfWeek[unsortedArray.length];
		i = 0;
		for (AdjDayOfWeek adjDay : adjDays) {
			switch (adjDay) {
				case SUNDAY:
					days[i] = DayOfWeek.SUNDAY;
					break;
				case MONDAY:
					days[i] = DayOfWeek.MONDAY;
					break;
				case TUESDAY:
					days[i] = DayOfWeek.TUESDAY;
					break;
				case WEDNESDAY:
					days[i] = DayOfWeek.WEDNESDAY;
					break;
				case THURSDAY:
					days[i] = DayOfWeek.THURSDAY;
					break;
				case FRIDAY:
					days[i] = DayOfWeek.FRIDAY;
					break;
				case SATURDAY:
					days[i] = DayOfWeek.SATURDAY;
					break;
			}
			i++;
		}
		
		// Use the HashMap to recreate the unsortedArray in ascending DayOfWeek order
		String[] sortedArray = new String[unsortedArray.length];
		i = 0;
		for (DayOfWeek key : days) {
			sortedArray[i] = referenceMap.get(key);
			i++;
		}
		
		return sortedArray;
	}
	
	/* Returns a String containing an abbreviated, human-friendly form of this DailySchedule */
	protected String fullSchedule() {
		// Use the encodedSchedule as the basis for formatting
		String encodedSchedule = this.encode();		
		return abbreviateSchedule(encodedSchedule);
	}
	
	/* Returns the number of unique schedules. A unique schedule is defined as
	 * having a different list of Operating Times for that day */
	protected int uniqueScheduleCount() {
		int count = 0;
		ArrayList<OperatingTime[]> uniqueOTs = new ArrayList<>();
		
		/* For every entry in dailySchedule, check if it already exists in the list of
		* unique Operating Times. If it doesn't, add it and increment count */
		for (Map.Entry<DayOfWeek, OperatingTime[]> day : this.dailySchedule.entrySet()) {
			if (day.getValue() != null && !uniqueOTs.contains(day.getValue())) {
				uniqueOTs.add(day.getValue());
				count++;
			}
		}
		return count;
	}
	
	/* Returns a String containing an abbreviated, human-friendly form of this DailySchedule's
	* nth unique schedule if n <= this DailySchedule's number of unique schedules. If n
	* exceeds this number, returns "Does not exist" */
	protected String nthSchedule(int n) {
		if (n <= this.uniqueScheduleCount()) {
			ArrayList<OperatingTime[]> uniqueOTs = new ArrayList<>();
			
			/* For every entry in dailySchedule, check if it already exists in the list of
			* unique Operating Times. If it doesn't, add it to uniqueOTs. */
			for (Map.Entry<DayOfWeek, OperatingTime[]> day : this.dailySchedule.entrySet()) {
				if (day.getValue() != null && !uniqueOTs.contains(day.getValue())) {
					uniqueOTs.add(day.getValue());
				}
			}
			
			/* Create the nth Daily Schedule by searching this.dailySchedule for all values
			 * with the same dailyTimes */
			OperatingTime[] dailyTimes = uniqueOTs.get(n - 1);
			HashMap<DayOfWeek, OperatingTime[]> nthScheduleMap = new HashMap<>();
			
			for (Map.Entry<DayOfWeek, OperatingTime[]> day : this.dailySchedule.entrySet()) {
				if (day.getValue() != null && day.getValue().equals(dailyTimes)) {
					nthScheduleMap.put(day.getKey(), dailyTimes);
				}
			}
			
			DailySchedule nthSchedule = new DailySchedule(nthScheduleMap, TYPE.SPECIFIC);
			
			// Create the abbreviate schedule of the nth schedule
			//TODO
			String abbreviatedSchedule = abbreviateSchedule(nthSchedule.encode());
			
			/* If the abbreviated schedule is special (M-F/weekdays or US/weekend only), change its
			* prefix */
			// Ensure only one set of day indicators exists
			boolean singleSet = false;
			if (abbreviatedSchedule.length() > 0) {
				int numOfSets = findNextDay(0, abbreviatedSchedule.substring(abbreviatedSchedule.indexOf(':')));
				if (numOfSets == -1) singleSet = true;
			}
			
			if (singleSet) {
				// Determine if the abbreviate schedule is for weekdays (M-F) or weekends (US)
				if (abbreviatedSchedule.substring(0,3).equals("M-F")) {
					abbreviatedSchedule = "Weekdays" + abbreviatedSchedule.substring(3);
				} else if (abbreviatedSchedule.substring(0,2).equals("SU")) {
					abbreviatedSchedule = "Weekends" + abbreviatedSchedule.substring(2);
				}
			}
			
			return abbreviatedSchedule;
		} else {
			return errorMessage(RouteSchedule.ERRORS.DOES_NOT_EXIST);
		}
	}
	
	// Returns a String of the abbreviated, human-friendly form of the encoded Daily Schedule String.
	private String abbreviateSchedule(String encodedSchedule) {
		String abbSchedule = ""; // Abbreviated schedule... the output
		
		// Iterate through each day of the week so long as one exists
		int lookFrom = 0;
		int lookTo = 0;
		int lookingAt = 0;
		String timesString = "";
		String time = "";
		
		while (encodedSchedule.indexOf(':') != -1) {			
			// Find the day indicators and add them to the abbSchedule String
			lookFrom = 0;
			lookTo = encodedSchedule.indexOf(':');
			boolean includeDayIndicators = this.type == TYPE.SPECIFIC;
			abbSchedule += (includeDayIndicators) ? encodedSchedule.substring(lookFrom, lookTo) + ": " : "";
			
			// Determine the start and end of the encoded times
			lookFrom = lookTo + 1;
			lookTo = encodedSchedule.indexOf(';');
			
			timesString = encodedSchedule.substring(lookFrom, lookTo);
			encodedSchedule = encodedSchedule.substring(lookTo + 1);
			
			// Parse the encoded timesString into an array of Strings
			String[] timeStrings = RouteSchedule.parseToArray(timesString, RouteSchedule.FromBeginning, RouteSchedule.ToEnd, ',');
			String[] formattedTimeStrings = new String[timeStrings.length];
			
			// Format each of those timeStrings
			lookingAt = 0;
			for (String timeString : timeStrings) {
				// If it's a time range
				if (timeString.length() == 9) {
					time = OperatingTime.formatEncodedTime(timeString.substring(0,4), true) + "-";
					time += OperatingTime.formatEncodedTime(timeString.substring(5,9), true);
					formattedTimeStrings[lookingAt] = time;
					
				// If there's a continuity
				} else if (timeString.length() == 5) {
					// Determine if it's pre- or post-continuity
					// If it's pre-continuity...
					if (timeString.charAt(0) == '-') {
						time = "-" + OperatingTime.formatEncodedTime(timeString.substring(1,5), true);
						formattedTimeStrings[lookingAt] = time;
					// If it's post-continuity...
					} else {
						time = OperatingTime.formatEncodedTime(timeString.substring(0,4), true) + "-";
						formattedTimeStrings[lookingAt] = time;
					}
				
				// If it spans the whole day (or an unknown edge case)
				} else {
					formattedTimeStrings[lookingAt] = "-";
				}
				time = "";
				lookingAt++;
			}
			
			// Add the formatted times to abbSchedule
			abbSchedule += RouteSchedule.strArrayToStr(formattedTimeStrings, ",");
			
			// If it's not the last set of times, add a ';'
			if (encodedSchedule.indexOf(':') != -1) {
				abbSchedule += "; ";
			}
		}
		
		return abbSchedule;
	}
	
	// Returns a String representing this DailySchedule in "Human" readable format
	
	/**
	 * Creates a String representation of this DailySchedule's data in an expanded,
	 * human-friendly format.
	 * 
	 * @return a 7-line String where ", "-separated OperatingTime's in HH:MM format for each day
	 * are preceded by the day of week they belong to. Every day of week (and their corresponding
	 * OperatingTime's) is on its own line.
	 */
	public String toString() {
		String fullString = "";
		/* If the DailySchedule is "Specific" (as in, it was instantiated in the context
		* of operating within a range of dates at different times for each day of the week),
		* display the times alongside the day indicators. */
		if (this.type == TYPE.SPECIFIC) {
			// Create an array here so that the days of week stay in when displayed
			DayOfWeek[] daysOfWeek = new DayOfWeek[] {
					DayOfWeek.SUNDAY,DayOfWeek.MONDAY,DayOfWeek.TUESDAY,
					DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.FRIDAY,DayOfWeek.SATURDAY};
			fullString = "";
			
			for (DayOfWeek dayOfWeek : daysOfWeek) {
			    OperatingTime[] listOfTimes = this.dailySchedule.get(dayOfWeek);
			    
			    // Print the day of week
			    fullString += "\n" + dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US) + ": ";
			    
			    // Print all the times for that day of week (if they exist)			
			    if (listOfTimes != null) {
			    	int i = 1;
			    	int numOfItems = listOfTimes.length;
			    	for (OperatingTime operatingTime : listOfTimes) {
				    	if (i < numOfItems) {
							fullString += operatingTime.toString() + ", ";
							i++;
						} else {
							fullString += operatingTime.toString();
						}
				    }
			    } else {
			    	fullString += "No times for this day.";
			    }
			}
		/* If the DailySchedule is "General" (as in it was instantiated in the context of
		* of operating at the same times "every" day (or more specifically, in relation to 
		* Sessions, the set of dates specified there)), just display the times. */
		} else if (this.type == TYPE.GENERAL){
			// Determine the number of times that are listed in this general schedule		
			int numOfTimes = 0;
			for (OperatingTime[] operatingTimesList : this.dailySchedule.values()) {
				if (operatingTimesList.length > numOfTimes) {
					numOfTimes = operatingTimesList.length;
				}
			}
			
			// Find the first day with a list of operating times and refer to that
			OperatingTime[] arrayOfTimes = new OperatingTime[numOfTimes];
			boolean timesFound = false;
			for (OperatingTime[] operatingTimesList : this.dailySchedule.values()) {
				if (!timesFound && operatingTimesList != null) {
					arrayOfTimes = operatingTimesList;
					timesFound = true;
				}
			}
			
			int i = 1;
	    	int numOfItems = arrayOfTimes.length;
			for (OperatingTime operatingTime : arrayOfTimes) {
		    	if (i < numOfItems) {
					fullString += operatingTime.toString() + ", ";
					i++;
				} else {
					fullString += operatingTime.toString();
				}
		    }
		}
		
		return fullString;
	}
	
	private static String errorMessage(RouteSchedule.ERRORS error) {
		switch (error) {
			case DOES_NOT_EXIST:
				return "Does Not Exist";
			default:
				return "Unknown Error";
		}
	}
}