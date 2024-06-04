package edu.depaul;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

class AccountWindow extends JFrame {
    private JLabel usernameLabel, cartLabel, productCountLabel;
    private JButton logoutButton, cartButton;
    private String username;
    private UserAuthenticationWindow parent;

    public AccountWindow(String username, UserAuthenticationWindow parent) {
        super("User Account");

        this.username = username;
        this.parent = parent;

        // Initialize components
        usernameLabel = new JLabel("Username: " + username);
        cartLabel = new JLabel("Shopping Cart: Empty");
        logoutButton = new JButton("Logout");
        cartButton = new JButton("Cart");
        AbstractButton browseButton = new JButton("Product Catalog");
        productCountLabel = new JLabel("Product Count: 0");

        // Set layout
        setLayout(new GridLayout(3, 1));
        add(usernameLabel);
        add(cartLabel);
        add(logoutButton);
        add(cartButton);
        add(browseButton);
        add(productCountLabel);

        // Add action listeners
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateCartInDatabase();
                dispose(); // Close the account window
            }
        });
        
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					openProductCatalogWindow(parent,this);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
        
        // Add action listeners
        cartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	openCartWindow();
            }

        });

        // Set frame properties
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Load cart data from database on window open
        loadCartFromDatabase();
        
        
    }
    
    private void openProductCatalogWindow(UserAuthenticationWindow parent, ActionListener actionListener) throws SQLException {
    	LoggingSystem.logInfo("Opening Product Catalog Window!");
        ProductCatalogWindow catalogWindow = new ProductCatalogWindow(parent,this);
        catalogWindow.setVisible(true);      
    }
    
    public void updateCartInfo() {
        List<String> cartItems = parent.getCartItems();
        StringBuilder cartText = new StringBuilder("Shopping Cart: ");
        if (cartItems.isEmpty()) {
            cartText.append("Empty");
        } else {
            for (String item : cartItems) {
                cartText.append(item).append(", ");
            }
            cartText.setLength(cartText.length() - 2);
        }
        cartLabel.setText(cartText.toString());
        productCountLabel.setText("Product Count: " + cartItems.size());
        LoggingSystem.logInfo("Updated Cart Label!");
    }
    
    private void openCartWindow() {
    	LoggingSystem.logInfo("Opening new Cart Window");
        CartWindow cartWindow = new CartWindow(username, parent, this);
        cartWindow.setVisible(true);       
    }
    
    // Method to load cart data from the database
    private void loadCartFromDatabase() {
    	LoggingSystem.logInfo("Cart Data for the user is loaded to Cart Window");
        List<String> cartItems = new ArrayList<>();
        Database mysqlDatabase = DatabaseConnectionFactory.getDatabase();
        
        try (Connection mysqlConnection = mysqlDatabase.connect()) 
        {
        	Statement statement = (Statement) mysqlConnection.createStatement();
            String selectDataQuery = "SELECT PRODUCTNAME FROM CART WHERE USERNAME='"+username+"'";
            ResultSet resultset = statement.executeQuery(selectDataQuery);
            while (resultset.next()) {
                cartItems.add(resultset.getString("PRODUCTNAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        parent.setCartItems(cartItems); // Update cart items in UserAuthenticationWindow
        updateCartInfo(); // Update cart info in AccountWindow
    }

    // Method to update cart data in the database
    private void updateCartInDatabase() {
    	LoggingSystem.logInfo("Cart Information is saved to the Database");
        List<String> cartItems = parent.getCartItems();
        Database mysqlDatabase = DatabaseConnectionFactory.getDatabase();	 
        
        try (Connection mysqlConnection = mysqlDatabase.connect()) 
        {
        	Statement statement = (Statement) mysqlConnection.createStatement();
            // Clear existing cart data for the user
            String clearDataQuery = "DELETE FROM CART WHERE USERNAME='"+username+"'";
            statement.executeUpdate(clearDataQuery);
            
            // Insert new cart data for the user
            for (String item : cartItems) {
                String insertDataQuery = "INSERT INTO CART (USERNAME, PRODUCTNAME) VALUES ('"+username+"','"+item+"')";
                statement.executeUpdate(insertDataQuery);
            }
            mysqlConnection.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
