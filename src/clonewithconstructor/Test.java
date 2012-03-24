package clonewithconstructor;

public class Test {

	public static void main(String args[]) throws Exception {
		Car car = new Car(4, "Maruti");
		Object clonedCar = car.clone();
		boolean isSame = car.getClass() == clonedCar.getClass();
		System.out.println("Is car.getClass() == clonedCar.getClass() : " + isSame);
	}
}
