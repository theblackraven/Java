import gnu.io.*;

// import javax.comm.*; 
import java.util.*;
import java.io.*;
import java.util.TooManyListenersException;
import java.text.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.lang.*;

// TODO Dialog zur Konfiguration der Schnittstellenparameter


public class uart_find extends JFrame{

    public static double p_start_double;
	public static boolean check=false;
	/**
	 * Variable declaration
	 */

	CommPortIdentifier serialPortId;
	Enumeration enumComm;
	SerialPort serialPort;
	OutputStream outputStream;
	InputStream inputStream;
	Boolean serialPortGeoeffnet = false;

	int baudrate = 115200;
	int dataBits = SerialPort.DATABITS_8;
	int stopBits = SerialPort.STOPBITS_1;
	int parity = SerialPort.PARITY_NONE;

	/**
	 * Fenster
	 */
	JPanel panel = new JPanel (new GridBagLayout());
	
	JLabel time = new JLabel ("Uhrzeit");
	JLabel time_format = new JLabel ("hh:mm:ss");
	JLabel date = new JLabel ("Datum");
	JLabel year = new JLabel ("Jahr (yyyy)");
	JLabel month = new JLabel ("Monat (MM)");
	JLabel day = new JLabel ("Tag (dd)");
	
	
	


	
	
	JTextField time_format_text = new JTextField();
	JTextField year_format_text = new JTextField();
	JTextField month_format_text = new JTextField();
	JTextField day_format_text = new JTextField();
	
	







	JComboBox auswahl = new JComboBox();
	JButton oeffnen = new JButton("Verbindung herstellen");
	JButton schliessen = new JButton("Verbidnung trennen");
	JButton aktualisieren = new JButton("Nach neuen Ger�ten suchen");
	JButton senden = new JButton("Messung starten");
	JButton save = new JButton("Messung Speichern");
	JButton plot_button = new JButton("Datei plotten");
	

	JCheckBox magnet = new JCheckBox("Magnetventil vorhanden?");
	
	JComboBox agilent = new JComboBox();
	JComboBox speed_combo = new JComboBox();
	
	JTextArea empfangen = new JTextArea();
	JTextArea logdaten = new JTextArea();
	JScrollPane empfangenJScrollPane = new JScrollPane();
	JScrollPane logdatenJScrollPane = new JScrollPane();
	JFrame Open = new JFrame("This is a text");
	
	


	protected void load() {
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(Open);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			showText(file);
		}
	}
	
	

	
	private void showText(File file){
		StringBuffer buf = new StringBuffer();
		if(file.exists( )){
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "";
				while((line = reader.readLine( )) != null){
					buf.append(line+"\n");
				}
				reader.close( );
			}
			catch (FileNotFoundException e) {
				e.printStackTrace( );
			}
			catch (IOException e) {
				e.printStackTrace( );
			}
		}
		this.logdaten.setText(buf.toString( ));
	}

	protected void save() {
		final JFileChooser fc = new JFileChooser();

		int returnVal = fc.showSaveDialog(Open);


		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			saveText(file);
		}
	}

	protected void save_file(){
		FileWriter fw = null;
		try {
			String working_dir = System.getProperty("user.dir");	
			String working_dir_new = working_dir.replaceAll("\\\\", "/");
			fw = new FileWriter(working_dir_new + "/plot_current.csv");
			String text = this.logdaten.getText();
			fw.write(text);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fw != null) try { fw.close(); } catch (IOException e) {}
		}

	}

	private void saveText(File file) {
		try {
			FileWriter writer = new FileWriter(file);
			String text = this.logdaten.getText( );
			writer.write(text);
			writer.flush( );
			writer.close( );
		}
		catch (IOException e) {
			e.printStackTrace( );
		}
	}
	/**
	 * @param args
	 */


	public static void main(String[] args)throws InterruptedException {
		System.out.println("Programm gestartet");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new uart_find();

			}
		});
		System.out.println("Main durchlaufen");
	}

	/**
	 * Konstruktor
	 */
	public uart_find()
	{
		System.out.println("Konstruktor aufgerufen");
		initComponents();
	}
	protected void finalize()
	{
		System.out.println("Destruktor aufgerufen");
	}

	
	void initComponents()
	{
		javax.swing.Timer t = new javax.swing.Timer
				( 1000, new ActionListener() {
			public void actionPerformed( ActionEvent e ) 
			{ 

				time_format_text.setText(getCurrentTimeAsString());
				DateFormat yearFormat = new SimpleDateFormat("yyyy");
				year_format_text.setText(yearFormat.format(new Date()));
				
				
				
			    
				
			}
			
		}
				);
		t.start();
		GridBagConstraints constraints = new GridBagConstraints();

		setTitle("Etching1");
		addWindowListener(new WindowListener());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	
		
		
      
       
		plot_button.addActionListener( new loadActionListener());
		oeffnen.addActionListener( new oeffnenActionListener());
		schliessen.addActionListener(new schliessenActionListener());
		aktualisieren.addActionListener(new aktualisierenActionListener());
		senden.addActionListener(new sendenActionListener());
		save.addActionListener(new saveActionListener());

		empfangenJScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		empfangenJScrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		empfangenJScrollPane.setViewportView(empfangen);

		logdatenJScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logdatenJScrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		logdatenJScrollPane.setViewportView(logdaten);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(auswahl, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(agilent, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;


		constraints.gridx = 0;
		constraints.gridy = 13;
		panel.add(time, constraints);

		constraints.gridx = 1;
		constraints.gridy = 12;
		panel.add(time_format, constraints);

		constraints.gridy = 13;
		constraints.gridx = 1;

		panel.add(time_format_text, constraints);


		constraints.gridx = 0;
		constraints.gridy = 15;
		panel.add(date, constraints);

		constraints.gridx = 1;
		constraints.gridy = 14;
		panel.add(year, constraints);

		constraints.gridx = 2;
		constraints.gridy = 14;
		panel.add(month, constraints);

		constraints.gridx = 3;
		constraints.gridy = 14;
		panel.add(day, constraints);

		

		

		constraints.gridx = 5;
		constraints.gridy = 17;
		panel.add(save, constraints);
		

		constraints.gridy = 15;
		constraints.gridx = 1;

		panel.add(year_format_text, constraints);

		constraints.gridy = 15;
		constraints.gridx = 2;

		panel.add(month_format_text, constraints);

		constraints.gridy = 15;
		constraints.gridx = 3;

		panel.add(day_format_text, constraints);

		save.setToolTipText("<html>Wenn Probleme bei der Dant�bertragung zwischen PC und Mikrocontroller auftreten <p> kleinere Werte ausprobieren.</html>");

		
		constraints.gridx = 2;
		constraints.gridy = 21;
		panel.add(plot_button, constraints);


		constraints.gridy = 0;
		constraints.gridx = 1;

		panel.add(oeffnen, constraints);

		constraints.gridy = 1;
		constraints.gridx = 1;
		panel.add(schliessen, constraints);

		constraints.gridy = 2;
		constraints.gridx = 1;
		panel.add(aktualisieren, constraints);

		
		
		

		constraints.gridx = 5;
		constraints.gridy = 16;

		panel.add(senden, constraints);



		senden.setVisible(false);
		save.setVisible(false);

		constraints.gridx = 2;
		constraints.gridy = 0;

		
		constraints.gridx = 3;
		constraints.gridy = 7;
		constraints.gridheight= 7;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx=0.2;
		panel.add(empfangenJScrollPane, constraints);

		constraints.gridx = 5;
		constraints.gridy = 7;
		constraints.gridheight= 7;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx=0.1;
		panel.add(logdatenJScrollPane, constraints);

		aktualisiereSerialPort();

		add(panel);
		pack();
		setSize(1024, 600);
		setVisible(true);
		agilent.addItem("Terminal");
		//agilent.addItem("Agilent");
		agilent.addItem("Logger");
		time_format_text.setText(getCurrentTimeAsString());
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		year_format_text.setText(yearFormat.format(new Date()));
		DateFormat monthFormat = new SimpleDateFormat("MM");
		month_format_text.setText(monthFormat.format(new Date()));
		DateFormat dayFormat = new SimpleDateFormat("dd");
		day_format_text.setText(dayFormat.format(new Date()));

		System.out.println("Fenster erzeugt");
	}

	boolean oeffneSerialPort(String portName)
	{
		Boolean foundPort = false;
		if (serialPortGeoeffnet != false) {
			System.out.println("Serialport bereits geöffnet");
			return false;
		}
		System.out.println("�ffne Serialport");
		enumComm = CommPortIdentifier.getPortIdentifiers();
		while(enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (portName.contentEquals(serialPortId.getName())) {
				foundPort = true;
				break;
			}
		}
		if (foundPort != true) {
			System.out.println("Serialport nicht gefunden: " + portName);
			return false;
		}
		try {
			serialPort = (SerialPort) serialPortId.open("Öffnen und Senden", 500);
		} catch (PortInUseException e) {
			System.out.println("Port belegt");
		}
		try {
			outputStream = serialPort.getOutputStream();
		} catch (IOException e) {
			System.out.println("Keinen Zugriff auf OutputStream");
		}
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			System.out.println("Keinen Zugriff auf InputStream");
		}
		try {
			serialPort.addEventListener(new serialPortEventListener());
		} catch (TooManyListenersException e) {
			System.out.println("TooManyListenersException für Serialport");
		}
		serialPort.notifyOnDataAvailable(true);
		try {
			serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
		} catch(UnsupportedCommOperationException e) {
			System.out.println("Konnte Schnittstellen-Paramter nicht setzen");
		}

		serialPortGeoeffnet = true;
		oeffnen.setBackground(Color.green);

		oeffnen.setLabel("Verbunden");
		return true;
	}




	void schliesseSerialPort()
	{
		if ( serialPortGeoeffnet == true) {
			System.out.println("Schlie�e Serialport");
			serialPort.close();
			oeffnen.setLabel("Verbindung herstellen");
			oeffnen.setBackground(schliessen.getBackground ());
			serialPortGeoeffnet = false;
		} else {
			System.out.println("Serialport bereits geschlossen");
		}
	}

	void aktualisiereSerialPort()
	{
		System.out.println("Akutalisiere Serialport-Liste");
		if (serialPortGeoeffnet != false) {
			System.out.println("Serialport ist geöffnet");
			return;
		}
		auswahl.removeAllItems();
		enumComm = CommPortIdentifier.getPortIdentifiers();
		while(enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println("Found:" + serialPortId.getName());
				auswahl.addItem(serialPortId.getName());
			}
		}
	}


	class loadActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			logdaten.setText("");
			load();
			save_file();
				plot_voltage();
			
			
			logdaten.setText("");
		}

	}

	void sendeSerialPort(String nachricht)
	{
		System.out.println("Sende: " + nachricht);
		if (serialPortGeoeffnet != true)
			return;
		try {
			outputStream.write(nachricht.getBytes());
		} catch (IOException e) {
			System.out.println("Fehler beim Senden");
		}
	}
	private static boolean containsString( String s, String subString ) {
		return s.indexOf( subString ) > -1 ? true : false;
	}
	void serialPortDatenVerfuegbar() {
		try {
			byte[] data = new byte[150];
			int num;
			while(inputStream.available() > 0) {
				num = inputStream.read(data, 0, data.length);
				System.out.println("Empfange: "+ new String(data, 0, num));

				String check_end;
				String check_temp= new String(data, 0, num);
				check_end = check_temp.replaceAll("[^\7]", "");
				if (check_end.length()>=1){
					
				}
				check_end = check_temp.replaceAll("[^\6\7]", "");
				if (check_end.length()>=1){
					check_end = check_temp.replaceAll("[\6\7]", "");
					String[] splitResult = check_end.split( "[; ]" ); // �> splitten am Semikolon
					

					sendeSerialPort("3");
					try
					{
						Thread.sleep(500);
					}
					catch (Exception e)
					{
					}
					uart_find.check= true;

					senden.setVisible(true);
					save.setVisible(true);


				}
				else
				{
					empfangen.append(new String(data, 0, num));
				}
				if (check_temp.contains("gestartet"))
				{
					empfangen.setText("");
				}

			}
		} catch (IOException e) {
			System.out.println("Fehler beim Lesen empfangener Daten");
		}
	}

	void serialPortlogdaten() {
		try {
			byte[] data = new byte[150];
			int num;
			String check_end;
			while(inputStream.available() > 0) {
				num = inputStream.read(data, 0, data.length);
				//System.out.println("Empfange: "+ new String(data, 0, num));

				String check_temp= new String(data, 0, num);
				check_end = check_temp.replaceAll("[^\7]", "");
				if (check_end.length()>=1){
				
				}
				check_end = check_temp.replaceAll("[^\6]", "");
				if (check_end.length()>=1){
				}
				check_end = check_temp.replaceAll("[^\6\7]", "");
				if (check_end.length()>=1){
					check_end = check_temp.replaceAll("[\6\7]", "");
					String[] splitResult = check_end.split( ";" ); // �> splitten am Semikolon
					


				}
				
				check_end = check_temp.replaceAll("[^\5\6\7]", "");
				if (check_end.length()>=1){

				}


				else
				{
					logdaten.append(new String(data, 0, num));
				}

				check_end = check_temp.replaceAll("[^\0]", "");
				if (check_end.length()>=1){


					
					
				}
				check_end = check_temp.replaceAll("[^\3\1\2\4]", "");
				if (check_end.length()>=1){
					
					logdaten.setText("");
				}

				if (check_temp.contains("gestartet"))
				{
					empfangen.setText("");
				}

			}
		} catch (IOException e) {
			System.out.println("Fehler beim Lesen empfangener Daten");
		}


	}
	public void plot()
	{
		String working_dir = System.getProperty("user.dir");	
		String working_dir_new = working_dir.replaceAll("\\\\", "/");
		System.out.println(working_dir_new);
		try {
			Runtime.getRuntime().exec("cmd /c waterhammer.plt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void plot_voltage()
	{
		String working_dir = System.getProperty("user.dir");	
		String working_dir_new = working_dir.replaceAll("\\\\", "/");
		System.out.println(working_dir_new);
		try {
			Runtime.getRuntime().exec("cmd /c voltage.plt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class WindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			schliesseSerialPort();
			System.out.println("Fenster wird geschlossen");
		}
	}

	class oeffnenActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			System.out.println("oeffnenActionListener");
			// TODO sperre Button Öffnen und Aktualisieren
			// TODO entsperre Nachricht senden und Schließen
			oeffneSerialPort((String) auswahl.getSelectedItem());


		}

	}
	class adcActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			}

		}

	
	
	public boolean isDouble(String s) {
		  try {
		    Double.parseDouble(s);
		    return true;
		  } catch (NumberFormatException e) {
		    return false;
		  }
		}
	
	class schliessenActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			System.out.println("schliessenActionListener");
			// TODO entsperre Button Öffnen und Aktualisieren
			// TODO sperre Nachricht senden und Schließen
			schliesseSerialPort();
		}
	}
	class aktualisierenActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			System.out.println("aktualisierenActionListener");
			aktualisiereSerialPort();

		}
	}
	class sendenActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			//System.out.println("sendenActionListener");
			//if ( echo.isSelected() == true)
			//empfangen.append(nachricht.getText() + "\n");
			sendeSerialPort("\r");
		}
	}

	class messung_freiActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			if (magnet.isSelected())
			{
				sendeSerialPort("4");
				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}
				sendeSerialPort("4");

			}
			else
			{
				sendeSerialPort("2");
				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}
				sendeSerialPort("2");
			}

			try
			{
				Thread.sleep(100);
			}
			catch (Exception e)
			{
			}
			logdaten.setText("");

		}
	}
	
	

	class leerenActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			empfangen.setText("");
		}
	}

	class resetActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			uart_find.check= false;
			logdaten.setText("");
			empfangen.setText("");
			senden.setVisible(false);
			save.setVisible(false);
		}
	}
	class leeren_logActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			logdaten.setText("");
		}
	}
	
	
	
	class saveActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			save();
		}
	}
	class paraActionListener implements ActionListener  {
		public void actionPerformed (ActionEvent event){
	
				uart_find.check= true;
			

		}
	}



	/**
	 * Liefert die gegenwärtige Zeit GMT + 2:00 (entspricht der deutschen Zeit) im
	 * folgenden Format: "HH:mm:ss"
	 * @return String-Objekt
	 */
	public String getCurrentTimeAsString()
	{
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss"); 
		formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		return formatter.format(new Date());
	}


	/**
	 * 
	 */
	class serialPortEventListener implements SerialPortEventListener {
		public void serialEvent(SerialPortEvent event) {
			System.out.println("serialPortEventlistener");
			switch (event.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				if(uart_find.check==false){
					serialPortDatenVerfuegbar();}
				else
				{
					System.out.println("datenlogger");
					serialPortlogdaten();
				}
				break;
			case SerialPortEvent.BI:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.FE:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			case SerialPortEvent.PE:
			case SerialPortEvent.RI:
			default:
			}
		}
	}



}
