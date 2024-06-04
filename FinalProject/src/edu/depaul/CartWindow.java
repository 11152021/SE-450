package edu.depaul;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

class CartWindow extends JFrame {
    private UserAuthenticationWindow parent;
    private JList<String> productList;
    private AccountWindow accountWindow;
    private String username;

    public CartWindow(String username, UserAuthenticationWindow parent, AccountWindow accountWindow) {
        super("Cart");

        this.parent = parent;
        this.accountWindow = accountWindow;
        this.username = username;
   
        // Initialize components
        DefaultListModel<String> model = new DefaultListModel<>();
        productList = new JList<>(model);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Set selection mode to single selection
        //String selectedItem = productList.getSelectedValue();
        JScrollPane scrollPane = new JScrollPane(productList);
        JButton checkoutButton = new JButton("Checkout");
        JButton removeButton = new JButton("Remove");

        // Build the cart content
        List<String> cartItems = parent.getCartItems();
        for (String item : cartItems) {
            model.addElement(item);
        }
        
        // Add components to the frame
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.SOUTH);
        panel.add(checkoutButton, BorderLayout.NORTH);
        add(panel);

        // Set frame properties
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add action listener for checkoutButton
        checkoutButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                openCheckoutWindow();
                dispose();
         }
        	
	       private void openCheckoutWindow() {
	                CheckoutWindow checkoutWindow = new CheckoutWindow(username, parent, accountWindow);
	                checkoutWindow.setVisible(true);
	       }
       });
        
        //removeButton.addActionListener(new CartRemoveListener(this,selectedItem, parent, accountWindow));
        
        
         removeButton.addActionListener(new ActionListener() {
         
            public void actionPerformed(ActionEvent e) {
                String selectedItem = productList.getSelectedValue();
                if (selectedItem != null) {
                    parent.removeItemFromCart(selectedItem);
                    accountWindow.updateCartInfo(); // Update cart info in AccountWindow
                    JOptionPane.showMessageDialog(CartWindow.this, "Item removed from cart: " + selectedItem);
                    refreshCart();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CartWindow.this, "Please select an item to be removed from cart");
                }
            }
        }); 
        
    }
    
   // Method to refresh the cart contents after item removal
    public void refreshCart() {
        DefaultListModel<String> model = new DefaultListModel<>();
        List<String> cartItems = parent.getCartItems();
        for (String item : cartItems) {
            model.addElement(item);
        }
        productList.setModel(model);
    }
}


