package equalsandhashcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestEqualsHashCode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Wheel> wheels1 = new ArrayList<Wheel>();
		for(int i=0; i<4;i ++) {
			wheels1.add(new Wheel(10));
		}
		Car car1 = new Car("1986 Accura", wheels1);
		
		List<Wheel> wheels2 = new ArrayList<Wheel>();
		for(int i=0; i<4;i ++) {
			wheels2.add(new Wheel(10));
		}
		Car car2 = new Car("1986 Accura", wheels2);
		
		Set<Car> carSet = new HashSet<Car>();
		carSet.add(car1);
		carSet.add(car2);
		
		System.out.println("Number of cars in the set : " + carSet.size());
	}

}
