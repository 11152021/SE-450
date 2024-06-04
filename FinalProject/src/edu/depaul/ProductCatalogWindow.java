package edu.depaul;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

class ProductCatalogWindow extends JFrame {
    private JList<String> productList;
    private JButton addToCartButton;
    private UserAuthenticationWindow parent;
    private AccountWindow accountWindow;

    public ProductCatalogWindow(UserAuthenticationWindow parent, AccountWindow accountWindow) throws SQLException {
        super("Product Catalog");

        this.parent = parent;
        this.accountWindow = accountWindow;

        // Initialize components
        DefaultListModel<String> model = new DefaultListModel<>();
        productList = new JList<>(model);
        addToCartButton = new JButton("Add to Cart");
    

        // Retrieve product catalog data from the database and populate the list
        List<String> products = retrieveProductsFromDatabase();
        for (String product : products) {
        	model.addElement(product);
        }

        // Set layout
        setLayout(new BorderLayout());
        add(new JScrollPane(productList), BorderLayout.CENTER);
        add(addToCartButton, BorderLayout.SOUTH);

        addToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = productList.getSelectedValue();
                if (selectedItem != null) {
                    parent.addToCart(selectedItem);
                    accountWindow.updateCartInfo(); // Update cart info in AccountWindow
                    JOptionPane.showMessageDialog(ProductCatalogWindow.this, "Item added to cart: " + selectedItem);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(ProductCatalogWindow.this, "Please select an item from the catalog.");
                    dispose();
                }
                
            }
        });
        
        // Set frame properties
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

	private List<String> retrieveProductsFromDatabase() throws SQLException {

		List<String> products = new ArrayList<>();
        
			Database mysqlDatabase = DatabaseConnectionFactory.getDatabase();	            
 		    Connection mysqlConnection = mysqlDatabase.connect();
 		    Statement statement = (Statement) mysqlConnection.createStatement();
 		    String retrieveDataQuery = "SELECT PRODUCTNAME, PRICE FROM PRODUCTCATALOG";
 		    ResultSet resultSet = ((java.sql.Statement) statement).executeQuery(retrieveDataQuery);
 		    
            // Process result set
            while (resultSet.next()) {
                String productName = resultSet.getString("PRODUCTNAME");
                double price = resultSet.getDouble("PRICE"); // Retrieve price from result set
                products.add(productName + " - $" + price); // Add both product name and price to the list
            }
            mysqlConnection.close();
            return products;
		    
 		    
    }
	
	
}