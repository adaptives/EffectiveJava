package defensivecopies;

import java.util.Date;

public class BadTimePeriodTest1 {
	
	public static void main(String args[]) {
		Date start = new Date();
		Date endDate = new Date();
		BadTimePeriod timePeriod = new BadTimePeriod(start, endDate);
		System.out.println("Time Period: " + timePeriod);
		Date timePeriodStartDate = timePeriod.getStart();
		timePeriodStartDate.setYear(80);
		System.out.println("Time Period: " + timePeriod);
	}
}
