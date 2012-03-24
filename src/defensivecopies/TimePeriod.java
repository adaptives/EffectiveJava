package defensivecopies;

import java.util.Date;

public class TimePeriod {
	
	private Date start;
	private Date end;
	
	public TimePeriod(Date start, Date end) {
		this.start = new Date(start.getTime());
		this.end = new Date(end.getTime());
	}
	
	public Date getStart() {
		return (Date)this.start.clone();
	}
	
	public Date getEnd() {
		return (Date)this.end.clone();
	}
	
	public String toString() {
		return " Start: " + this.start + " End: " + this.end;
	}
}
