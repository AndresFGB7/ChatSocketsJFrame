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
	private static Window v; //Ventana
	private DataOutputStream out; //outputs
	private DataInputStream in; //inputs
	private static Socket socket;
	private static ArrayList<Panel> paneles; //Se almacenan los paneles
	private Panel panel; //Un panel
	private Message m; //JOPTION

	public RunAgent(Socket s, int puerto) throws Exception {
		m = new Message();
		socket = s;
		//Llega un ciudadano acepta o no
		var answer = m.inputMessage("Agente en el puerto: "+puerto+"\nDesea aceptar un nuevo ciudadano?\n" + "(1) Aceptar\n" + "(2) Denegar");
		if (answer.contains("1")) {
			v.setTitle("Agente en linea");
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("Se acepto el cliente jaja");//Se habilita el textarea del cliente con este mensaje
			panel = new Panel();
			paneles.add(panel);
			v.getPestanas().add(panel);
			v.getContentPane().add(v.getPestanas());
			v.getPestanas().setTitleAt((paneles.size() - 1), "Cliente " + (paneles.size() - 1));// Se agrega el nombre al panel
			v.repaint();
			socket = s;
		} else {
			s.close();
		}

	}

	public static void main(String[] args) throws Exception {

		paneles = new ArrayList<Panel>();
		v = new Window(); // Se crea la ventana
		var port = 4060;
		var pool = Executors.newFixedThreadPool(100);//maximo 100 clientes :)
		var isRunning = false;
		do {
			v.setTitle("Esperando un nuevo ciudadano... estas en el puerto " + port);
			//Cada que llegue un cliente lo acepta
			try (var listener = new ServerSocket(port)) {
				while (true) {
					var agent = new RunAgent(listener.accept(),port);
					pool.execute(agent.run());
				}
			} catch (BindException e) {
				//En caso que se cree un agente y este ya exista se crea uno nuevo sumandole uno al puerto
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
					//Este se encarga de la salida de los TEXTFIELD del agente en cada panel
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
					//Lee los mensajes enviados por el cliente y los agrega al Textarea
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
