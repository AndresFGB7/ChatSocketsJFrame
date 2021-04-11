package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import view.Message;
import view.Panel;
import view.Window;

public class Ciudadano {
	private Window v;
	private DataOutputStream out;
	private DataInputStream in;
	private Panel panel;
	private Message m;
	private Boolean isAccepted;
	private int puerto;

	/**
	 * Este metodo se encarga de crear un ciudadano
	 * @param adressServer - el servidor
	 * @param port - el puerto
	 */
	public Ciudadano(String adressServer, int port) {
		this.puerto = port;
		
		
		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				do {
					isAccepted = true;
					m = new Message();//JOPTIONPANE
					v = new Window();//Se crea una ventana
					panel = new Panel();
					v.getContentPane().removeAll();
					v.getPestanas().add(panel);
					v.getContentPane().add(v.getPestanas());
					v.getPestanas().setTitleAt(0, "Pestana 1");
					v.repaint();//Se termina de agregar el panel

					panel.getMessageArea().append("Esperando que un agente tome el chat...");
					panel.getTextField().setEnabled(false);
					try {
						var socket = new Socket(adressServer, puerto);
						in = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						out.flush();
						//Este se encarga de la salida de los TEXTFIELD del cliente
						panel.getTextField().addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									panel.getMessageArea().append("\nUsted: " + panel.getTextField().getText());
									out.writeUTF("\nCiudadano: " + panel.getTextField().getText());
									out.flush();

								} catch (IOException e1) {
									e1.printStackTrace();
								}
								panel.getTextField().setText("");
							}
						});
						//Lee los mensajes enviados por el agente y los agrega al Textarea
						while (true) {
							var line = in.readUTF();
							if (line.equals("Se acepto el cliente jaja")) {
								panel.getMessageArea().setText("Un agente se puso en contacto. puede escribir");
								panel.getTextField().setEnabled(true);
							} else {
								panel.getMessageArea().append(line);
							}
						}
					} 
					catch (ConnectException e1) {
						m.message("No hay mas agentes disponibles");
						isAccepted = true;
					} catch (Exception e) {
						//En caso de ser declinado se va con otro agente
						isAccepted = false;
						puerto += 1;
					} finally {
						v.setVisible(false);
						v.dispose();
					}
				} while (!isAccepted);
			}
		});
		hilo.start();
	}
}
