package exceptions;

import exceptions.application.RacingApplication;
import gooddeepclone.Car;

public class ExceptionsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Car c = null;
		try {
			c = RacingApplication.getCarForPerson(0);
		} catch(CarNotFoundException cnfe) { 
			//notice that we did not catch the superclass Exception
			cnfe.printStackTrace();
		}
	}
}
