package edu.depaul;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public interface ActionListenerInterface extends ActionListener, EventListener {
	    void actionPerformed(ActionEvent e);
	}
