package exceptions.model;

import exceptions.DataException;
import gooddeepclone.Car;

public class CarModel {

	public static Car findByFilter(String filter) throws DataException {
		Car car = null;
		//logic to get the car from the database
		if(car == null) {
			throw new DataException("Could not find Car instance with filter '" + filter + "'");
		}
		return car;
	}
}
