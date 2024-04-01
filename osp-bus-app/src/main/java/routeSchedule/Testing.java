package routeSchedule;/*package routeSchedule;
import routeSchedule.OperatingWindow;
import routeSchedule.RouteSchedule;
import routeSchedule.PredefinedSession;

public class Testing {

	public static void main(String[] args) { */
		/*
		Encoding/decoding pieces back and forth
		OperatingTime testTime = OperatingTime.decode("0000-2359");
		OperatingTime testTimeTwo = new OperatingTime("08:30-");
		OperatingTime testTimeThree = new OperatingTime("-17:30");
		OperatingTime testTimeFour = new OperatingTime("16:50-19:20");
		OperatingDate testDate = OperatingDate.decode("103023-113024");
		OperatingDate testDateTwo = new OperatingDate("01/02/03");
		OperatingDate testDateThree = new OperatingDate(OperatingDate.Indefinite);
		
		// Example schedule: 
		HashMap<DayOfWeek, OperatingTime[]> testDailySchedule = OperatingWindow.blankDailySchedule();
		testDailySchedule.put(DayOfWeek.MONDAY, new OperatingTime[] {testTimeTwo});
		testDailySchedule.put(DayOfWeek.TUESDAY, new OperatingTime[] {testTimeThree});
		testDailySchedule.put(DayOfWeek.FRIDAY, new OperatingTime[] {testTime, testTimeFour});
		
		OperatingWindow testWindow = new OperatingWindow(
				new OperatingDate[] {testDate, testDateTwo},
				testDailySchedule);
		
		System.out.println("-------------------------------------");
		System.out.println(testWindow.toString());
		System.out.println("-------------------------------------");
		
		OperatingWindow testWindowTwo = OperatingWindow.decode("-102524,102724{M:0830-1230,1445-2100,0730-1230;TWF:-;R:-0830;}");
		System.out.println(testWindowTwo.toString());
		System.out.println("-------------------------------------");
		System.out.println(testWindow.encode());
		System.out.println(testWindowTwo.encode());
		*/
		
		/*
		Encoding/decoding windows back and forth
		String testEncodedWindow = "-102524,102724{U:1725-;M:-0230,0830-1230,1330-1400,1740-;TW:-;R:-0830;}";
		
		OperatingWindow owDecodingTest = OperatingWindow.decode(testEncodedWindow);
		System.out.println("Decoded the original string into...\n" + owDecodingTest.encode());
		System.out.println("PASSED? " + testEncodedWindow.equals(owDecodingTest.encode()));
		System.out.println("Which translates to...\n" + owDecodingTest.toString());
		OperatingWindow owEncodingTest = OperatingWindow.decode(owDecodingTest.encode());
		System.out.println("-------------------------------------");
		System.out.println("owDecodingTest has been encoded, decoded, then re-encoded into...\n" + owEncodingTest.encode());
		System.out.println("PASSED? " + testEncodedWindow.equals(owEncodingTest.encode()));
		System.out.println("Which translates to...\n" + owEncodingTest.toString());
		*/
		/*
		RouteSchedule testRS = RouteSchedule.decode("f;s{M-F:0630-1945;}|u{M-F:0700-1900;}|i{M-F:0630-1945;}");
		System.out.println(testRS.toString());
		RouteSchedule testRS2 = RouteSchedule.decode(testRS.encode());
		System.out.println(testRS2.toString());
		System.out.println("Encoding & Decoding are equal? " + testRS.toString().equals(testRS2.toString()));
		
		System.out.println(testRS.fullSchedule());
		int i = 1;
		while (i <= testRS.uniqueSchedules()) {
			System.out.println(testRS.nthSchedule(i));
			i++;
		}
	}
}*/