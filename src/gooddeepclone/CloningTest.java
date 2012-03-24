package gooddeepclone;

import java.util.ArrayList;
import java.util.List;

public class CloningTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		List<Wheel> wheels = new ArrayList<Wheel>();
		for(int i=0; i<4; i++) {
			wheels.add(new Wheel(4));
		}
		Car car = new Car(wheels, "1986 Accura");
		
		Car clonedCar = (Car)car.clone();
		
		//tests
		
		System.out.println("Checking if car and clonedCar are different instances");
		if(car == clonedCar) {
			throw new RuntimeException("car and clonedCar are the same instance");
		}
		System.out.println("checked!\n");
		
		System.out.println("Checking that car and cloned car are of the same type");
		if(car.getClass() != clonedCar.getClass()) {
			throw new RuntimeException("car and clonedCar are not of the same class");
		}
		System.out.println("checked!\n");
		
		System.out.println("Checking that clonedCar is a deep clone of car");
		for(int i=0; i<4; i++) {
			if(car.getWheels().get(i) == clonedCar.getWheels().get(i)) {
				throw new RuntimeException("Not a deep clone! Found same wheel instance.");
			}
		}
		System.out.println("checked!\n");
		
		System.out.println("We have a deep clone !");
		
	}

}
