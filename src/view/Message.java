package view;

import javax.swing.JOptionPane;

public class Message {

	/**
	 * Show a message with JOption
	 * 
	 * @param string - the message to whow
	 */
	public void message(String string) {
		JOptionPane.showMessageDialog(null, string);
	}

	/**
	 * Show a input message
	 * 
	 * @param string - the message
	 * @return the input (String)
	 */
	public String inputMessage(String string) {
		return JOptionPane.showInputDialog(null, string);

	}

	/**
	 * Show a list of option with a message
	 * 
	 * @param message - String
	 * @param title   - String
	 * @param option  - String
	 * @param init    - int
	 * @return (String) the option chosen of the list.
	 */
	public String listMessage(String message, String title, String option[], int init) {
		Object x = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, option,
				option[init]);
		return x.toString();
	}

	
}
