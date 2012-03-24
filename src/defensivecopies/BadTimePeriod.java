package defensivecopies;

import java.util.Date;

public class BadTimePeriod {
	
	private Date start;
	private Date end;
	
	public BadTimePeriod(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public Date getStart() {
		return this.start;
	}
	
	public Date getEnd() {
		return this.end;
	}
	
	@Override
	public String toString() {
		return "Start: " + this.start + " End: " + this.end;
	}
}
