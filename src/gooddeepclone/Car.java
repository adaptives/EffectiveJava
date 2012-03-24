package gooddeepclone;

import java.util.ArrayList;
import java.util.List;

public class Car implements Cloneable {
	
	private List<Wheel> wheels;
	private String model;
	
	public Car(List<Wheel> wheels, String model) {
		this.wheels = wheels;
		this.model = model;
	}
	
	public List<Wheel> getWheels() {
		return this.wheels;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Car clonedCar = (Car)super.clone();
		//Even though we cannot be 100% sure that the List type is ArrayList
		//since it could have been changed by some other class
		List<Wheel> clonedCarOriginalWheels = clonedCar.getWheels();
		clonedCar.wheels = new ArrayList<Wheel>();
		
		for(int i=0; i<clonedCarOriginalWheels.size(); i++) {
			clonedCar.wheels.add((Wheel)clonedCarOriginalWheels.get(i).clone());
		}
		return clonedCar;
	}
	
}
