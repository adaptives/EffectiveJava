package exceptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CatchingHighLevelException {
	
	public static void main(String args[]) {
		
	}
	
	public InputStream open(String fname) {
		InputStream is = null;
		try {
			is = new FileInputStream(fname);
		} catch(FileNotFoundException fnfe) {
			//do nothing
		} catch(IOException ioe) {
			//do nothing
		}
		return is;
	}
}
