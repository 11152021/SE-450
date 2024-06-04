package edu.depaul;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

public class CartRemoveListener implements ActionListenerInterface {
    private JList<String> productList;
    private UserAuthenticationWindow parent;
    private AccountWindow accountWindow;
    private CartWindow cartWindow;
    private String selectedItem;
	
	    public CartRemoveListener(CartWindow cartWindow, String selectedItem, UserAuthenticationWindow parent, AccountWindow accountWindow) {
		
	    this.cartWindow = cartWindow;
	    this.parent = parent;
	    this.accountWindow = accountWindow;
	    this.selectedItem = selectedItem;
	    
	    //DefaultListModel<String> model = new DefaultListModel<>();
	    //productList = new JList<>(model);
	    //productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Set selection mode to single selection
	    
	    }
	      
		public void actionPerformed(ActionEvent e) {
			//String selectedItem = productList.getSelectedValue();
			if (selectedItem != null) {
				parent.removeItemFromCart(selectedItem);
				accountWindow.updateCartInfo(); // Update cart info in AccountWindow
				JOptionPane.showMessageDialog(cartWindow, "Item removed from cart: " + selectedItem);
	            refreshCart();
	        } else {
	            JOptionPane.showMessageDialog(cartWindow, "Please select an item to be removed from cart");
	        }			
		}
		
	    public void refreshCart() {
	        DefaultListModel<String> model = new DefaultListModel<>();
	        List<String> cartItems = parent.getCartItems();
	        for (String item : cartItems) {
	            model.addElement(item);
	        }
	        productList.setModel(model);
	    }
}
