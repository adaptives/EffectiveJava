package etc;

import java.util.ArrayList;
import java.util.List;

public class ParamTypes {
	
	// This is an example of bad parameters, because the parameter type is
	// ArrayList, which is a very specific type
	public void badParams(ArrayList list) {
		
	}
	
	// This is an example of good parameters, because the parameter type is
	// List, which is the most appropriate general type
	public void goodParams(List list) {
		
	}
}
