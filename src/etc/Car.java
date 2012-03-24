package etc;

public class Car {
	private int enginePower;
	private String model;
	
	public Car(String model) {
		this.model = model;
		this.enginePower = computeEnginePower(model);
	}
	
	public int computeEnginePower(String model) {
		////
		return 1;
	}
}
