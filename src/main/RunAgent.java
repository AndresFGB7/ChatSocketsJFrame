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

public class RunAgent {
	private static Window v;
	private DataOutputStream out;
	private DataInputStream in;
	private static Socket socket;
	private static ArrayList<Panel> paneles;
	private Panel panel;
	private Message m;

	public RunAgent(Socket s, int puerto) throws Exception {
		m = new Message();
		socket = s;
		var answer = m.inputMessage("Agente en el puerto: "+puerto+"\nDesea aceptar un nuevo ciudadano?\n" + "(1) Aceptar\n" + "(2) Denegar");
		if (answer.contains("1")) {
			v.setTitle("Agente en linea");
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("Se acepto el cliente jaja");
			panel = new Panel();
			paneles.add(panel);
			v.getPestañas().add(panel);
			v.getContentPane().add(v.getPestañas());
			v.getPestañas().setTitleAt((paneles.size() - 1), "Cliente " + (paneles.size() - 1));
			v.repaint();
			socket = s;
		} else {
			s.close();
		}

	}

	public static void main(String[] args) throws Exception {

		paneles = new ArrayList<Panel>();
		v = new Window();
		var port = 4060;
		var pool = Executors.newFixedThreadPool(100);
		var isRunning = false;
		do {
			v.setTitle("Esperando un nuevo ciudadano... estas en el puerto " + port);

			try (var listener = new ServerSocket(port)) {
				while (true) {
					var agent = new RunAgent(listener.accept(),port);
					pool.execute(agent.run());
				}
			} catch (BindException e) {
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
								out.writeUTF("\nAgente: "+panel.getTextField().getText());
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
								panel.getMessageArea().append(input );
							}
						} catch (Exception e) {
						}
						
					}

				} catch (IOException e) {
					
				}
			}
		});
		return hilo;
	}

}
