package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.RunCiudadano;

public class Window extends JFrame {

	
	JTabbedPane pesta�as;
	public Window() {
		
		setSize(550, 400);
		setLayout(new BorderLayout());
		pesta�as = new JTabbedPane();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	

	public JTabbedPane getPesta�as() {
		return pesta�as;
	}

	public void setPesta�as(JTabbedPane pesta�as) {
		this.pesta�as = pesta�as;
	}


	
}
