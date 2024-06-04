package edu.depaul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLDBConnection implements Database {
	
    private static final String url = "jdbc:mysql://127.0.0.1:3306/crm";
    private static final String username = "root";
    private static final String password = "lmarupeddepaul";

    public Connection connect() throws SQLException {
			try {
				return DriverManager.getConnection(url, username, password);				
			} catch (SQLException e) {		       
				LoggingSystem.logSevere("Unable to establish MySQL DB Connection. Invalid Credentials!");
				return null;
			}						
    }    
}