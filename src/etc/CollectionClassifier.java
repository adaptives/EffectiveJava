package etc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionClassifier {

	public static String classify(Set s) {
		return "Set";
	}
	
	public static String classify(List l) {
		return "List";
	}
	
	public static String classify(Collection c) {
		return "Unknown Collection";
	}

	public static void main(String[] args) {
		Collection[] tests = new Collection[] {
			new HashSet(),
			// A Set
			new ArrayList(),
			// A List
			new HashMap().values() // Neither Set nor List
		};
		
		for (int i = 0; i < tests.length; i++) {
			System.out.println(classify(tests[i]));
		}
	}
}
