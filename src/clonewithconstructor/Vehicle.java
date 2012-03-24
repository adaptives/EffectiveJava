package clonewithconstructor;

public class Vehicle implements Cloneable {
	private int wheels;
	
	public Vehicle(int wheels) {
		this.wheels = wheels;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return new Vehicle(this.wheels);
	}
}
