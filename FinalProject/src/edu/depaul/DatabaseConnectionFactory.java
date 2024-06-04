package edu.depaul;

public abstract class DatabaseConnectionFactory {
    
	public static Database getDatabase() {
		
			return new MySQLDBConnection();
			
    }
}
