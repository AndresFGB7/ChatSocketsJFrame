package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Panel extends JPanel {

	JTextField textField;
	JTextArea messageArea;
	public Panel() {
	
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		textField = new JTextField(45);
		messageArea = new JTextArea(16, 45);
		textField.setEditable(true);
		messageArea.setEditable(false);
		add(new JScrollPane(messageArea), BorderLayout.CENTER);
		add(textField, BorderLayout.SOUTH);
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}
	
}
