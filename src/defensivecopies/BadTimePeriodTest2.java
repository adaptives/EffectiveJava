package defensivecopies;

import java.util.Date;

public class BadTimePeriodTest2 {
	
	public static void main(String args[]) {
		Date maliciousStart = new Date();
		Date endDate = new Date();
		BadTimePeriod timePeriod = new BadTimePeriod(maliciousStart, endDate);
		System.out.println("Time Period: " + timePeriod);
		maliciousStart.setYear(80);
		System.out.println("Time Period: " + timePeriod);
	}
}
