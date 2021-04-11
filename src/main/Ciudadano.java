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

public class Ciudadano  {
	private Window v;
	private DataOutputStream out;
	private DataInputStream in;
	private Panel panel;
	private Message m;
	private Boolean isAccepted;
	private int puerto;

	public Ciudadano(String adressServer, int port) {
		this.puerto = port;
		isAccepted = true;
		m = new Message();
		v = new Window();
		panel = new Panel();
		v.getContentPane().removeAll();
		v.getPestañas().add(panel);
		v.getContentPane().add(v.getPestañas());
		v.getPestañas().setTitleAt(0, "Pestaña 1");
		v.repaint();
		panel.getMessageArea().append("Esperando que un agente tome el chat...");
		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				do {
					try {
						var socket = new Socket(adressServer, puerto);
						in = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						out.flush();
						panel.getTextField().addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									panel.getMessageArea().append("\nUsted: "+panel.getTextField().getText());
									out.writeUTF("Ciudadano: "+panel.getTextField().getText());
									out.flush();
									
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								panel.getTextField().setText("");
							}
						});
						while (true) {
							var line = in.readUTF();
							panel.getMessageArea().append(line + "\n");
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (ConnectException e1) {
						m.message("No hay mas agentes disponibles");
						isAccepted = true;
						e1.printStackTrace();
					} catch (Exception e) {
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
