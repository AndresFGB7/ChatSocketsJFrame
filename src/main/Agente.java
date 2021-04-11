package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import view.Message;
import view.Panel;
import view.Window;

public class Agente {
	private static Window v;
	private DataOutputStream out;
	private DataInputStream in;
	private static Socket socket;
	private static ArrayList<Panel> paneles;
	private Panel panel;
	private Message m = new Message();

	public Agente(Socket s) throws Exception {
		socket = s;
		var answer = m.inputMessage("Desea aceptar un nuevo ciudadano?\n" + "(1) Aceptar\n" + "(2) Denegar");
		if (answer.contains("1")) {
			v.setTitle("Agente en linea");
			panel = new Panel();
			paneles.add(panel);
			v.getPestañas().add(panel);
			v.getContentPane().add(v.getPestañas());
			v.getPestañas().setTitleAt((paneles.size() - 1), "Cliente " + (paneles.size() - 1));
			v.repaint();
			socket = s;
		} else {
			s.close();
			throw new Exception("Refused by my");
		}

	}

	public static void main(String[] args) throws Exception {

		paneles = new ArrayList<Panel>();
		v = new Window();
		var port = 4060;
		var contador = 0;
		var pool = Executors.newFixedThreadPool(5);
		var isRunning = false;
		do {
			v.setTitle("Esperando un nuevo ciudadano... estas en el puerto " + port);

			try (var listener = new ServerSocket(port)) {
				while (true) {
					var agent = new Agente(listener.accept());
					pool.execute(agent.run());
				}
			} catch (BindException e) {
				contador++;
				isRunning = true;
				port++;
			} catch (Exception e) {
				System.out.println("Exception");
			}
		} while (isRunning);
	}

	private Runnable run() {

		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					in = new DataInputStream(socket.getInputStream());
					out = new DataOutputStream(socket.getOutputStream());
					out.flush();
					panel.getTextField().addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								panel.getMessageArea().append("\nUsted: "+panel.getTextField().getText());
								out.writeUTF("Agente: "+panel.getTextField().getText());
								out.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							panel.getTextField().setText("");
						}
					});
					while (true) {
						try {
							var input = in.readUTF();
							if (input.toLowerCase().startsWith("/quit")) {
								return;
							} else {
								panel.getMessageArea().append(input + "\n");
							}
						} catch (Exception e) {
						}
						
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return hilo;
	}

}
