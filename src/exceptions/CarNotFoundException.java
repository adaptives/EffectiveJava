package exceptions;

public class CarNotFoundException extends Exception {
	
	public CarNotFoundException() {
		super();
	}
	
	public CarNotFoundException(String msg) {
		super(msg);
	}
	
	public CarNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public CarNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
