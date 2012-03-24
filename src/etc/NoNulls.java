package etc;

import java.util.ArrayList;
import java.util.List;

import equalswithouthashcode.Point;

public class NoNulls {

	public static class CouldNotComputeException extends Exception {
		
	}
	
	public Point computerPoint(int i)  throws CouldNotComputeException {
		Point p = null;
		//computer the point
		//...
		if(p == null) {
			throw new CouldNotComputeException();
		}
		return p;
	}
	
	public List computePoints(int i) {
		List retVal = new ArrayList();
		//computer points
		return retVal;
	}
}
