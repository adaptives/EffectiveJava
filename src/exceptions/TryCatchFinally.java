package exceptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TryCatchFinally {

	
	public static void main(String args[]) {
		Connection c = null;
		try {
			//register the driver
			Class.forName("org.hsqldb.jdbcDriver");
			//get a connection
			c = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
			
			System.out.println("Created Connection");
			
			//ADD A RECORD
			String addSql = "INSERT INTO EMAIL_LIST (name, email) VALUES ('%s', '%s');";
			Statement s = c.createStatement();
			s.executeUpdate(String.format(addSql, "Joe", "joe@somewhere.com"));
			
			listAllRecords(c);
			
		} catch(ClassNotFoundException cnfe) {
			System.out.println("Caught Exception : " + cnfe.getMessage());
		} catch(SQLException sqle) {			
			System.out.println("Caught Exception : " + sqle.getMessage());
		} finally { //close the connection			
			try {
				if(c != null) {c.close();}
			} catch(SQLException sqle) {
				System.out.println("Caught Exception");
			}
		}
	}
	
	private static void createSchema(Connection c) throws SQLException {
		String sql = "CREATE TABLE EMAIL_LIST (" +
				     "    NAME VARCHAR(64)," +
				     "    EMAIL VARCHAR(256) PRIMARY KEY)";
		Statement stmt = c.createStatement();
		stmt.executeUpdate(sql);		
	}

	public static void listAllRecords(Connection c) throws SQLException {
		System.out.println("Listing all records");
	    // RETRIEVE ALL RECORDS
		// create a statement
		String fetchSql = "SELECT * from EMAIL_LIST;";
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery(fetchSql);
		while(rs.next()) {
			String name = rs.getString("name");
			String email = rs.getString("email");
			System.out.println("Name: " + name + " Email: " + email);
		}
	}
	
	//Notice the varargs syntax... varargs if present have to be at the end of the parameter list
	public static void listByEmail(Connection c, String... emails) throws SQLException {
		System.out.println("Finding record for email '" + emails + "'");
		// create a prepared statement
		String sql = "SELECT (name) from EMAIL_LIST WHERE EMAIL = ?;";
		PreparedStatement ps = c.prepareStatement(sql);
		for(String email : emails) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String name = rs.getString("name");
				System.out.println("Name: " + name);
			}
		}
	}
}