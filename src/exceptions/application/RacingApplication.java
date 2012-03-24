package exceptions.application;

import exceptions.CarNotFoundException;
import exceptions.DataException;
import exceptions.model.CarModel;
import gooddeepclone.Car;

public class RacingApplication {

	public static Car getCarForPerson(int personId) throws CarNotFoundException {
		String filter = buildFilterForPeronCar(personId);
		Car car = null;
		try {
			car = CarModel.findByFilter(filter);
		} catch(DataException de) {
			String msg = "Could not find Car for person id='" + personId + "'";
			throw new CarNotFoundException(msg, de);
		}
		return car;
	}

	private static String buildFilterForPeronCar(int personId) {
		String filter = "";
		//logic to create the filter expression
		return filter;
	}
	
	
}
