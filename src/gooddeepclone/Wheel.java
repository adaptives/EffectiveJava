package gooddeepclone;

public class Wheel implements Cloneable {
	private int size;
	
	public Wheel(int size) {
		this.size = size;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
