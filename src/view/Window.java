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

	
	JTabbedPane pestañas;
	public Window() {
		
		setSize(550, 400);
		setLayout(new BorderLayout());
		pestañas = new JTabbedPane();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	

	public JTabbedPane getPestañas() {
		return pestañas;
	}

	public void setPestañas(JTabbedPane pestañas) {
		this.pestañas = pestañas;
	}


	
}
