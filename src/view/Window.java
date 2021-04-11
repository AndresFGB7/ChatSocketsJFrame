package view;

import java.awt.BorderLayout;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.RunCiudadano;

public class Window extends JFrame {
	JTabbedPane pestanas;
	//Inicia la ventana
	public Window() {
		setSize(550, 400);
		setLayout(new BorderLayout());
		pestanas = new JTabbedPane();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	

	public JTabbedPane getPestanas() {
		return pestanas;
	}

	public void setPestanas(JTabbedPane pestanas) {
		this.pestanas = pestanas;
	}



}
