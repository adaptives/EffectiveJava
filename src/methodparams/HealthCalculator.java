package methodparams;

public class HealthCalculator {

	public int healthMeter(ICalculationStrategy strategy,
						   int age, 
						   double weight, 
						   int height) {
		if(strategy == null) {
			throw new NullPointerException("Strategy must be non null");
		}
		if(age <= 0) {
			throw new IllegalArgumentException("Age must be greater than 0");
		}
		if(weight <= 0.0 ) {
			throw new IllegalArgumentException("Weight must be greater than 0");
		}
		if(height <= 0) {
			throw new IllegalArgumentException("Height must be greater than 0");
		}
		
		return strategy.calculate(age, weight, height);
	}
}
