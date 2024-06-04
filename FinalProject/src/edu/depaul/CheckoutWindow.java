package edu.depaul;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

class CheckoutWindow extends JFrame {
    private UserAuthenticationWindow parent;
    private String username;
    private AccountWindow accountWindow;

    public CheckoutWindow(String username, UserAuthenticationWindow parent, AccountWindow accountWindow) {
        super("Checkout");

        this.parent = parent;
        this.username = username;
        this.accountWindow=accountWindow;

        // Initialize components
        JButton placeOrderButton = new JButton("Place Order");
        
        JTextArea CheckoutTextArea = new JTextArea(10, 20);
        CheckoutTextArea.setEditable(false);

        // Add components to the frame
        add(placeOrderButton,BorderLayout.SOUTH);
        add(CheckoutTextArea, BorderLayout.CENTER);

        // Set frame properties
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        StringBuilder CheckoutContent = new StringBuilder();
        List<String> cartItems = parent.getCartItems();
        for (String item : cartItems) {
        	CheckoutContent.append(item).append("\n");
        }
        CheckoutTextArea.setText(CheckoutContent.toString());
        
        placeOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openPaymentWindow();
                dispose();
            }
        });
    }
    
    private void openPaymentWindow() {
        PaymentWindow paymentWindow = new PaymentWindow(username, parent, accountWindow);
        paymentWindow.setVisible(true);
        LoggingSystem.logInfo("Opening Payment Window!");
    }
    

}