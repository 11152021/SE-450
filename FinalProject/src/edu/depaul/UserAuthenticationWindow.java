package edu.depaul;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class UserAuthenticationWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private List<String> cartItems = new ArrayList<>();
    private JList<String> productList;
    
    public UserAuthenticationWindow() {
        super("User Authentication Window");
      
        // Initialize components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        
        // Set layout
        setLayout(new GridLayout(3, 3));
        
        add(new JLabel("Username:"));
        add(usernameField);
        
        add(new JLabel("Password:"));        
        add(passwordField);
        
        add(loginButton);
        
        
        // Add action listeners Buttons
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                try {
					if (AuthenticateExistingUsers(username, password)) {
					    JOptionPane.showMessageDialog(UserAuthenticationWindow.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
					    LoggingSystem.logInfo("User Loggedin Successfully! Opening User Account Window");
					    openAccountWindow(username);
					    loginButton.setEnabled(false);
					    dispose();
					} else {
						LoggingSystem.logInfo("New User, Prompting to Register!");
						int choice = JOptionPane.showConfirmDialog(UserAuthenticationWindow.this, "User not found. Do you want to register?", "Register", JOptionPane.YES_NO_OPTION);
	                    if (choice == JOptionPane.YES_OPTION) {
	                    	if (registerUser(username, password)) {
	                    		JOptionPane.showMessageDialog(UserAuthenticationWindow.this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	                    		LoggingSystem.logInfo("User Registered Successfully!");
	                    		openAccountWindow(username);
	                    		loginButton.setEnabled(false);
	                    		dispose();
	                    	}
	                    }
					}
				} catch (HeadlessException | SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                try {
					registerUser(username, password);
					registerButton.setEnabled(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
        
        // Set frame properties
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
    }
        
 	boolean AuthenticateExistingUsers(String username, String password) throws SQLException{
 			
 			Database mysqlDatabase = DatabaseConnectionFactory.getDatabase();	            
 		    Connection mysqlConnection = mysqlDatabase.connect();
 		    Statement statement = (Statement) mysqlConnection.createStatement();
 		    String retrieveDataQuery = "SELECT * FROM USERS WHERE USERNAME ='"+username+"' AND PASSWORD ='"+password+"'";
 		    ResultSet resultSet = ((java.sql.Statement) statement).executeQuery(retrieveDataQuery);
 		    
 		    if (resultSet.next() == true) {
 		        String id = resultSet.getString("USERNAME");
 		        LoggingSystem.logInfo("User '"+id+"' Authentication Completed Successfully!");
 		        return true;
 		    } else {
 		    	LoggingSystem.logSevere("Unable to Authenticate the User Please Try again!");
 		    }		    
 		    mysqlConnection.close();
			return false; 
 	   }
 	
    private boolean registerUser(String username, String password) throws SQLException {
	    	try {
				Database mysqlDatabase = DatabaseConnectionFactory.getDatabase();	            
	 		    Connection mysqlConnection = mysqlDatabase.connect();
	 		    
	 		    String insertDataQuery = "INSERT INTO USERS (USERNAME,PASSWORD,INSERT_TS) VALUES  ('"+username+"','"+password+"','2024-06-01 00:00:00')";
	            PreparedStatement statement = mysqlConnection.prepareStatement(insertDataQuery);
	 		    statement.executeUpdate(insertDataQuery);
	 		    mysqlConnection.close();
	 		    LoggingSystem.logInfo("User '"+username+"' Authentication Completed Successfully!");
	 		   return true;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        JOptionPane.showMessageDialog(this, "Error occurred during registration.", "Error", JOptionPane.ERROR_MESSAGE);	
		        LoggingSystem.logSevere("Error occurred during registration!");
				return false; 
		    }
    }
    
    private void openAccountWindow(String username) {
        AccountWindow accountWindow = new AccountWindow(username, this);
        accountWindow.setVisible(true);
    }
    
    public void setCartItems(List<String> cartItems) {
        this.cartItems = cartItems;
    }
    
    public void addToCart(String item) {
    	cartItems.add(item);
    	System.out.println("Item added to cart: " + item);
    }
    
    public void removeItemFromCart(String item) {
        cartItems.remove(item);
        System.out.println("Item removed from cart: " + item);
    }

	public List<String> getCartItems() {
		return cartItems;
	}
	
    public void clearCart() {
        cartItems.clear();
    }

}