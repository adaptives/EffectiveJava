package defensivecopies;

import java.util.Date;

public class TimePeriodTest {
	
	public static void main(String args[]) {
		Date startDate = new Date();
		Date endDate = new Date();
		TimePeriod timePeriod = new TimePeriod(startDate, endDate);
		System.out.println("Time Period: " + timePeriod);
		startDate.setYear(80);
		System.out.println("Time Period: " + timePeriod);
		Date timePeriodStartDate = timePeriod.getStart();
		timePeriodStartDate.setYear(80);
		System.out.println("Time Period: " + timePeriod);
	}
}
