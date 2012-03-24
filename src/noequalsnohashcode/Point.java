package noequalsnohashcode;

import java.util.HashSet;
import java.util.Set;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static void main(String args[]) {
		Point p1 = new Point(1, 2);
		Point p2 = new Point(1, 2);
		Set<Point> set = new HashSet<Point>();
		set.add(p1);
		set.add(p2);
		System.out.println("size of set : " + set.size());
	}
}
