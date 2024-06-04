package edu.depaul;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class PaymentWindow extends JFrame {
    private UserAuthenticationWindow parent;
    private JComboBox<String> paymentTypeComboBox;
    private JTextField cardNameField, cardNumberField, pinField, validThruField;
    private static final Logger logger = Logger.getLogger(LoggingSystem.class.getName());
    private String username;
    private JList<String> productList;
    private AccountWindow accountWindow;

    public PaymentWindow(String username, UserAuthenticationWindow parent, AccountWindow accountWindow) {
        super("Enter Payment Details ");

        this.parent = parent;
        this.username = username;
        this.accountWindow = accountWindow;

        // Initialize components
        JLabel paymentTypeLabel = new JLabel("Payment Type:");
        String[] paymentTypes = {"CREDIT", "DEBIT"};
        paymentTypeComboBox = new JComboBox<>(paymentTypes);
        JLabel cardNameLabel = new JLabel("Card Name:");
        cardNameField = new JTextField(20);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = new JTextField(20);
        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JTextField(4);
        JLabel validThruLabel = new JLabel("Valid Thru (MM/YY):");
        validThruField = new JTextField(5);
        JButton verifyButton = new JButton("Verify");

        // Add components to the frame
        JPanel panel = new JPanel(new GridLayout(10, 10));
        panel.add(paymentTypeLabel);
        panel.add(paymentTypeComboBox);
        panel.add(cardNameLabel);
        panel.add(cardNameField);
        panel.add(cardNumberLabel);
        panel.add(cardNumberField);
        panel.add(pinLabel);
        panel.add(pinField);
        panel.add(validThruLabel);
        panel.add(validThruField);
        panel.add(new JLabel());
        panel.add(verifyButton); // Add an empty label for spacing
        add(panel);

        // Add action listener for verifyButton
        verifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					verifyPayment();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                dispose();
            }
        });

        // Set frame properties
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void verifyPayment() throws HeadlessException, SQLException {
        // Get payment details entered by the user
        String paymentType = (String) paymentTypeComboBox.getSelectedItem();
        String cardName = cardNameField.getText();
        String cardNumber = cardNumberField.getText();
        String pin = pinField.getText();
        String validThru = validThruField.getText();

        // Database verification logic
        if (verifyPaymentDetails(username, paymentType, cardName, cardNumber, pin, validThru)) {
            JOptionPane.showMessageDialog(this, "Payment details verified");
            placeOrder(username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid payment details. Order cannot be placed. Please check your payment information");           
        }
    }

    private boolean verifyPaymentDetails(String username, String paymentType, String cardName, String cardNumber, String pin, String expiryDate) throws SQLException 
    {
			Database mysqlDatabase = DatabaseConnectionFactory.getDatabase();	            
 		    Connection mysqlConnection = mysqlDatabase.connect();
 		    Statement statement = (Statement) mysqlConnection.createStatement();
 		    String retrieveDataQuery = "SELECT * FROM PAYMENTDETAILS WHERE USERNAME ='"+username+"'";
 		    ResultSet resultSet = ((java.sql.Statement) statement).executeQuery(retrieveDataQuery);
 		    
 		    if (resultSet.next() == true) {
 		        String id = resultSet.getString("USERNAME");
 		        LoggingSystem.logInfo("User "+id+" Found! Payment Details Verification Complete");
 		        return true;
 		    } else {
 		    	LoggingSystem.logSevere("Unable to Verify Payment details for this user"+username+" Please try Again!");
 		    }		    
 		    mysqlConnection.close();
			return false;
    }
    
    private void placeOrder(String username) {   	
        JOptionPane.showMessageDialog(this, "Order Placed.");
        
    	try {
			Database mysqlDatabase = DatabaseConnectionFactory.getDatabase();	            
 		    Connection mysqlConnection = mysqlDatabase.connect();
 		    
 		    String insertDataQuery = "INSERT INTO ORDERS (USERNAME, ORDERID, ORDER_TS) VALUES ('" +username+ "',FLOOR(RAND() * 1000), CURRENT_TIMESTAMP)";
            PreparedStatement statement = mysqlConnection.prepareStatement(insertDataQuery);
 		    statement.executeUpdate(insertDataQuery);
 		    mysqlConnection.close();
 		    LoggingSystem.logInfo("Inserted Order Details into ORDERS Table");
 		    parent.clearCart();
 		    accountWindow.updateCartInfo();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        LoggingSystem.logSevere("Unable to Insert Order Details into ORDERS Table");		
	    }   	
	}

}
