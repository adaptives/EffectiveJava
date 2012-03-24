package clonewithconstructor;

public class Car extends Vehicle {

	public String model;
	
	public Car(int wheels, String model) {
		super(wheels);
		this.model = model;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
