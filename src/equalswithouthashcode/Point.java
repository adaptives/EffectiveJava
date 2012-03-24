package equalswithouthashcode;

import java.util.HashSet;
import java.util.Set;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		Point other = (Point)o;
		return this.x == other.x && this.y == other.y;
	}
	
	@Override
	public int hashCode() {
		return x + y;
	}
		
	public static void main(String[] args) {
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,0);
		
		Set<Point> points = new HashSet<Point>();
		points.add(p1);
		points.add(p2);
		
		System.out.println("p1 and p2 are equal - " + (p1.equals(p2)));
		System.out.println("The set contains " + points.size() + " points");	
	}

}

