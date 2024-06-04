package edu.depaul;

import java.sql.Connection;
import java.sql.SQLException;


public interface Database {
	Connection connect() throws SQLException;
}
