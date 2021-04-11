package main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import view.Message;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RunCiudadano {
	Message m = new Message() ;
	int numCiudadanos=0;
	private String[] dataPet = new String[] { "(1) Especie", "(2) Tamaño", "(3) Localidad", "(4) Dirección",
			"(5) Nombre completo de la persona que reporta", "(6) Teléfono de la persona que reporta ",
			"(7) Email de la persona que reporta", "(8) Comentarios generales" }; // Info to submit of pet
	private String dataPet2; //To save the submit info

	public RunCiudadano() {
		m = new Message();//JOPTION PANE
		dataPet2 = "";

		// Choose the action
		m.message("Ciudadanos de 4 Patas");
		String[] option = { "(1) Crear caso", "(2) Hablar con un agente" };
		var accion = m.listMessage("Elegir accion", "Escoge", option, 0);

		if (accion.contains("1")) {
			// Add the case
			var caso = "";
			String[] casos = { "(1) Pérdida", "(2) Robo", "(3) Abandono", "(4) Animal peligroso",
					"(5) Manejo indebido en vía pública" };
			caso = m.listMessage("Elegir tipo de caso", "caso", casos, 0);

			// Submit the info in dataPet2
			for (int i = 0; i < dataPet.length; i++) {
				var me =m.inputMessage("Por favor ingresa:\n" + dataPet[i]);
				if (me.equals("") || me.equals(" ")) {
					System.out.println("No ingresaste el valor valido");
					i = i - 1;
				} else {
					dataPet2 += me.toUpperCase() + ",";
				}
			}

			// BONO :) adding the csv
			try {
				String archCSV = ("casosPet.csv");
				CSVReader csvReader = new CSVReader(new FileReader(archCSV));
				var csvReader2 = new CSVReader(new FileReader(archCSV));
				List<String[]> datos = csvReader.readAll();
				int f = 0;
				try {
					String[] id = datos.get((datos.size() - 1));
					f = Integer.parseInt(id[0]) + 1;
				} catch (Exception e) {
				}
				// Get date and time
				Date date = new Date();
				DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
				var hora = hourFormat.format(date);
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				var fecha = dateFormat.format(date);

				// Split the data in previous for
				String data[] = dataPet2.split(",");
				String allData[] = { f + "", fecha, hora, caso, data[0], data[1], data[2], data[3], data[4], data[5],
						data[6], data[7] };

				// adding data to an array for csv
				datos.add(allData);
				csvReader.close();
				// add info of array to csv
				CSVWriter writer = new CSVWriter(new FileWriter(archCSV));
				writer.writeAll(datos);
				writer.close();
				// info added and choose exit or start again
				String[] yesorno = { "(1) Volver al inicio", "(2) Salir" };
				var me = m.listMessage("El caso ha sido creado", "Seleccione la opción a continuación:", yesorno, 0);
				if (me.contains("1")) {
					new RunCiudadano();
				} else {
					System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (accion.contains("2")) {
			var isRunning = false;
			var port = 4060;
			do {
				isRunning = false;
				try {
					// Create a new Citizen in a specified port
					Ciudadano chatCitizen = new Ciudadano("127.0.0.1", port);
					numCiudadanos++;
				} catch (Exception e) {
					System.out.println("fuck");
					port++;
					System.out.println(port);
					isRunning = true;
					e.printStackTrace();
				}
			} while (isRunning);
		} else {
			m.message("Ingresaste un numero invalido");
			new RunCiudadano();
		}
	}

	public static void main(String[] args) {
		RunCiudadano c = new RunCiudadano();
	}

}
