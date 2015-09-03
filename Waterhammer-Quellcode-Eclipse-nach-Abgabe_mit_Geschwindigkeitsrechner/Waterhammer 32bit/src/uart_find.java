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
	JLabel herz = new JLabel ("Frequenz in Hz");
	JLabel u_min = new JLabel ("Spannung min. in mV");
	JLabel u_max = new JLabel ("Spannung max. in mV");
	JLabel p_min = new JLabel ("Druck min. in mbar");
	JLabel p_max = new JLabel ("Druck_max. in mbar");
	JLabel sensor1 = new JLabel ("Drucksensor 1");
	JLabel sensor2 = new JLabel ("Drucksensor 2");
	JLabel p_trigger = new JLabel ("Druckschwelle in mbar");
	JLabel buffer = new JLabel ("Pufferzeit in ms");
	JLabel time = new JLabel ("Uhrzeit");
	JLabel time_format = new JLabel ("hh:mm:ss");
	JLabel date = new JLabel ("Datum");
	JLabel year = new JLabel ("Jahr (yyyy)");
	JLabel month = new JLabel ("Monat (MM)");
	JLabel day = new JLabel ("Tag (dd)");
	JLabel speed = new JLabel ("<html>Geschwindigkeit<p/>der Datenübermittlung</html>");
	JLabel pressure1 = new JLabel ("Drucksensor 1 in mbar");
	JLabel pressure2 = new JLabel ("Drucksensor 2 in mbar");
	JLabel pressure1_text = new JLabel ("");
	JLabel pressure2_text = new JLabel ("");
	JLabel trigger_u = new JLabel ("Triggerschwelle in mV");
	JLabel Geschwindigkeit_text = new JLabel ("Fließgeschwindigkeit in m/s");
	JLabel Geschwindigkeit = new JLabel ("");
	JLabel Durchmesser_text = new JLabel ("Innenrohrdurchmesser in mm");
	JLabel Volumenstrom_text = new JLabel ("Volumenstrom in l/min");
	JLabel Wandstaerke_text = new JLabel ("Wandstärke in mm");
	JLabel E_Modul_text = new JLabel ("E-Modul Rohr in N/m²");
	
	JLabel joukowsky_text = new JLabel ("Joukowksy-Stoß in bar");
	JLabel joukowsky = new JLabel ("");
	
	

	JTextField Platzhalter1 = new JTextField ("Platzhalter");
	JTextField Platzhalter2 = new JTextField ("Platzhalter");
	JTextField Platzhalter3 = new JTextField ("Platzhalter");
	JTextField Platzhalter4 = new JTextField ("Platzhalter");
	JTextField Wandstaerke = new JTextField();
	JTextField Durchmesser = new JTextField();
	JTextField Volumenstrom = new JTextField();
	JTextField E_Modul = new JTextField();
	
	JTextField herz_text = new JTextField();
	JTextField u_min_text = new JTextField();
	JTextField u_max_text = new JTextField();
	JTextField p_max_text = new JTextField();
	JTextField p_min_text = new JTextField();
	JTextField u_min_text_2 = new JTextField();
	JTextField u_max_text_2 = new JTextField();
	JTextField p_max_text_2 = new JTextField();
	JTextField p_min_text_2 = new JTextField();
	JTextField p_trigger_text = new JTextField();
	JTextField buffer_text = new JTextField();
	JTextField time_format_text = new JTextField();
	JTextField year_format_text = new JTextField();
	JTextField month_format_text = new JTextField();
	JTextField day_format_text = new JTextField();
	JTextField speed_text = new JTextField();
	JTextField trigger_u_text = new JTextField();
	







	JComboBox auswahl = new JComboBox();
	JButton oeffnen = new JButton("Verbindung herstellen");
	JButton schliessen = new JButton("Verbidnung trennen");
	JButton aktualisieren = new JButton("Nach neuen Geräten suchen");
	//JButton leeren = new JButton("Parameterliste leeren");
	//JButton leeren_log = new JButton("Log leeren");
	JButton para = new JButton("Parametrieren");
	JButton senden = new JButton("Messung manuell starten");
	JButton reset = new JButton("Reset");
	JButton save = new JButton("Speichern");
	JButton messung_gange = new JButton("Messung im Gange"); 
	JLabel messung_bereit = new JLabel("Messung bereit");
	JButton messung_frei = new JButton("Neue Messung");
	JButton plot_button = new JButton("Datei plotten");
	//JTextField nachricht = new JTextField();

	JCheckBox magnet = new JCheckBox("Magnetventil vorhanden?");
	JCheckBox teiler1 = new JCheckBox("Spannungsteiler 1");
	JCheckBox teiler2 = new JCheckBox("Spannungsteiler 2");
	JComboBox agilent = new JComboBox();
	JComboBox speed_combo = new JComboBox();
	JComboBox adc = new JComboBox();
	JTextArea empfangen = new JTextArea();
	JTextArea logdaten = new JTextArea();
	JScrollPane empfangenJScrollPane = new JScrollPane();
	JScrollPane logdatenJScrollPane = new JScrollPane();
	JFrame Open = new JFrame("This is a test");
	
	


	protected void load() {
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(Open);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			showText(file);
		}
	}
	
	
	public void joukowsky()
	{
		String working_dir = System.getProperty("user.dir");	
		String working_dir_new = working_dir.replaceAll("\\\\", "/");
		try
		{
		RandomAccessFile file = new RandomAccessFile(working_dir_new + "/plot_current.csv", "r" );
		file.seek(85); // to the beginning
		byte[] p_start = new byte[5];
		file.read(p_start);
		String p_start_string = new String(p_start, "UTF-8");
		uart_find.p_start_double = Double.parseDouble(p_start_string);
		file.close();
		}
		catch (IOException ex) {
	         ex.printStackTrace();
	      }
		
		
		try
		{
		RandomAccessFile file = new RandomAccessFile(working_dir_new + "/waterhammer.plt", "rw" );
		file.seek(0); // to the beginning
		String joukowsky_temp;
		if (isDouble(Volumenstrom.getText())==true)
		{
		
		if (Double.parseDouble(Volumenstrom.getText())==0){
			joukowsky_temp = "joukowsky=0.000";
			file.write(joukowsky_temp.getBytes());
		}
		else
		{
		double joukowsky_double = Double.parseDouble(joukowsky.getText());
		joukowsky_double=joukowsky_double+uart_find.p_start_double*0.001;
		String joukowsky_string =  String.valueOf(joukowsky_double).substring(0,5);
		joukowsky_temp = "joukowsky="+joukowsky_string;
		file.write(joukowsky_temp.getBytes());
		}
		}
		else
		{
			joukowsky_temp = "joukowsky=0.000";
			file.write(joukowsky_temp.getBytes());	
		}
		file.close();
		}
		
		
		catch (IOException ex) {
	         ex.printStackTrace();
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
		javax.swing.Timer t = new javax.swing.Timer( 1000, new ActionListener() {
			public void actionPerformed( ActionEvent e ) 
			{ 

				time_format_text.setText(getCurrentTimeAsString());
				DateFormat yearFormat = new SimpleDateFormat("yyyy");
				year_format_text.setText(yearFormat.format(new Date()));
				
				if (isDouble(Volumenstrom.getText())==true & isDouble(Durchmesser.getText())==true & isDouble(Wandstaerke.getText())==true  & isDouble(E_Modul.getText())==true){
					
				
				double volume_flow=Double.parseDouble(Volumenstrom.getText());
				double e_modul_double=Double.parseDouble(E_Modul.getText());
			    double pipe_diameter=Double.parseDouble(Durchmesser.getText());
				double pipe_thickness=Double.parseDouble(Wandstaerke.getText());
				if (pipe_diameter<=0 | volume_flow<=0 | e_modul_double<=0 | pipe_thickness<=0) {
					
				}
				else
				{
				double velocity=volume_flow/(0.25*0.06*Math.PI*pipe_diameter*pipe_diameter);
				Geschwindigkeit.setText(String.valueOf(velocity).substring(0,5));
			
				double joukowsky_double=0.01*velocity*1.0/(Math.sqrt(1000.0*((pipe_diameter*0.001)/(pipe_thickness*0.001*e_modul_double*1000000.0)+(1.0/2100000000.0))));
				joukowsky.setText(String.valueOf(joukowsky_double).substring(0,5));
				
				
			    }
				}
			}
			
		}
				);
		t.start();
		GridBagConstraints constraints = new GridBagConstraints();

		setTitle("Waterhammer Version 1");
		addWindowListener(new WindowListener());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// TODO schliessen.setEnabled(false);
		// TODO senden.setEnabled(false);
		
		
      
       
		plot_button.addActionListener( new loadActionListener());
		oeffnen.addActionListener( new oeffnenActionListener());
		schliessen.addActionListener(new schliessenActionListener());
		aktualisieren.addActionListener(new aktualisierenActionListener());
		senden.addActionListener(new sendenActionListener());
		para.addActionListener(new paraActionListener());
		adc.addActionListener(new adcActionListener());
		messung_frei.addActionListener(new messung_freiActionListener());
		//leeren.addActionListener(new leerenActionListener());
		//leeren_log.addActionListener(new leeren_logActionListener());
		reset.addActionListener(new resetActionListener());
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

		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(adc, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(herz, constraints);

		constraints.gridx = 2;
		constraints.gridy = 3;
		panel.add(teiler1, constraints);
		teiler1.setSelected(false);

		constraints.gridx = 2;
		constraints.gridy = 4;
		panel.add(teiler2, constraints);
		teiler2.setSelected(false);

		constraints.gridy = 3;
		constraints.gridx = 1;

		panel.add(herz_text, constraints);
		herz_text.setText("10000");



		constraints.gridx = 1;
		constraints.gridy = 6;
		panel.add(sensor1, constraints);
		
		

		constraints.gridx = 2;
		constraints.gridy = 6;
		panel.add(sensor2, constraints);
		

		constraints.gridx = 0;
		constraints.gridy = 7;
		
		panel.add(u_min, constraints);
		panel.add(Platzhalter1, constraints);
		Platzhalter1.setVisible(false);

		constraints.gridy = 7;
		constraints.gridx = 1;

		panel.add(u_min_text, constraints);
		u_min_text.setText("200");

		constraints.gridy = 7;
		constraints.gridx = 2;

		panel.add(u_min_text_2, constraints);
		u_min_text_2.setText("200");

		constraints.gridx = 0;
		constraints.gridy = 8;
		panel.add(u_max, constraints);
		panel.add(Platzhalter2, constraints);
		Platzhalter2.setVisible(false);

		constraints.gridy = 8;
		constraints.gridx = 1;

		panel.add(u_max_text, constraints);
		u_max_text.setText("2200");

		constraints.gridy = 8;
		constraints.gridx = 2;

		panel.add(u_max_text_2, constraints);
		u_max_text_2.setText("2200");

		constraints.gridx = 0;
		constraints.gridy = 9;
		panel.add(p_min, constraints);
		panel.add(Platzhalter3, constraints);
		Platzhalter3.setVisible(false);

		constraints.gridy = 9;
		constraints.gridx = 1;

		panel.add(p_min_text, constraints);
		p_min_text.setText("0");

		constraints.gridy = 9;
		constraints.gridx = 2;

		panel.add(p_min_text_2, constraints);
		p_min_text_2.setText("0");

		constraints.gridx = 0;
		constraints.gridy = 10;
		panel.add(p_max, constraints);
		panel.add(Platzhalter4, constraints);
		Platzhalter4.setVisible(false);

		constraints.gridy = 10;
		constraints.gridx = 1;

		panel.add(p_max_text, constraints);
		p_max_text.setText("50000");
		

		constraints.gridy = 10;
		constraints.gridx = 2;

		panel.add(p_max_text_2, constraints);
		p_max_text_2.setText("50000");

		constraints.gridx = 0;
		constraints.gridy = 4;
		panel.add(p_trigger, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		panel.add(trigger_u, constraints);
		trigger_u.setVisible(false);

		constraints.gridy = 4;
		constraints.gridx = 1;
		panel.add(p_trigger_text, constraints);
		p_trigger_text.setText("7000");

		constraints.gridy = 4;
		constraints.gridx = 1;
		panel.add(trigger_u_text, constraints);
		trigger_u_text.setText("3300");
		trigger_u_text.setVisible(false);

		constraints.gridx = 0;
		constraints.gridy = 5;
		panel.add(buffer, constraints);

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
		constraints.gridy = 15;
		panel.add(messung_gange, constraints);

		messung_gange.setVisible(false);
		messung_gange.setBackground(Color.red);

		constraints.gridx = 4;
		constraints.gridy = 16;
		panel.add(messung_bereit, constraints);
		messung_bereit.setVisible(false);
		messung_bereit.setForeground(Color.green);

		

		constraints.gridx = 5;
		constraints.gridy = 17;
		panel.add(save, constraints);

		constraints.gridx = 4;
		constraints.gridy = 17;
		panel.add(messung_frei, constraints);

		

		constraints.gridy = 15;
		constraints.gridx = 1;

		panel.add(year_format_text, constraints);

		constraints.gridy = 15;
		constraints.gridx = 2;

		panel.add(month_format_text, constraints);

		constraints.gridy = 15;
		constraints.gridx = 3;

		panel.add(day_format_text, constraints);

		

		constraints.gridx = 0;
		constraints.gridy = 16;
		panel.add(speed, constraints);
		speed.setToolTipText("<html>Wenn Probleme bei der Dantübertragung zwischen PC und Mikrocontroller auftreten <p> kleinere Werte ausprobieren.</html>");

		constraints.gridy = 16;
		constraints.gridx = 1;
		panel.add(speed_text, constraints);
		speed_text.setText("9999");
		speed_text.setToolTipText("<html>Wenn Probleme bei der Dantübertragung zwischen PC und Mikrocontroller auftreten <p> kleinere Werte ausprobieren.</html>");

		constraints.gridx = 2;
		constraints.gridy = 21;
		panel.add(plot_button, constraints);

		constraints.gridy = 5;
		constraints.gridx = 1;
		constraints.gridwidth = 1;
		panel.add(buffer_text, constraints);
		buffer_text.setText("20");

		constraints.gridy = 0;
		constraints.gridx = 1;

		panel.add(oeffnen, constraints);

		constraints.gridy = 1;
		constraints.gridx = 1;
		panel.add(schliessen, constraints);

		constraints.gridy = 2;
		constraints.gridx = 1;
		panel.add(aktualisieren, constraints);

		constraints.gridy = 0;
		constraints.gridx = 4;
		panel.add(para, constraints);
		
		constraints.gridy = 1;
		constraints.gridx = 4;
		panel.add(Durchmesser_text, constraints);
		
		constraints.gridy = 1;
		constraints.gridx = 5;
		panel.add(Durchmesser, constraints);
		Durchmesser.setText("13");
		
		constraints.gridy = 2;
		constraints.gridx = 4;
		panel.add(Wandstaerke_text, constraints);
		
		constraints.gridy = 2;
		constraints.gridx = 5;
		panel.add(Wandstaerke, constraints);
		Wandstaerke.setText("1");
		
		constraints.gridy = 3;
		constraints.gridx = 4;
		panel.add(E_Modul_text, constraints);
		
		constraints.gridy = 3;
		constraints.gridx = 5;
		panel.add(E_Modul, constraints);
		E_Modul.setText("100000");
		
		constraints.gridy = 4;
		constraints.gridx = 4;
		panel.add(Volumenstrom_text, constraints);
		
		constraints.gridy = 4;
		constraints.gridx = 5;
		panel.add(Volumenstrom, constraints);
		
		
		constraints.gridy = 5;
		constraints.gridx = 4;
		panel.add(Geschwindigkeit_text, constraints);
		
		constraints.gridy = 5;
		constraints.gridx = 5;
		panel.add(Geschwindigkeit, constraints);
		
		constraints.gridy = 6;
		constraints.gridx = 4;
		panel.add(joukowsky_text, constraints);
		
		constraints.gridy = 6;
		constraints.gridx = 5;
		panel.add(joukowsky, constraints);
		joukowsky.setText("0.000");
		
		
		
		
		


		constraints.gridy = 0;
		constraints.gridx = 5;
		panel.add(reset, constraints);

		constraints.gridx = 5;
		constraints.gridy = 16;

		panel.add(senden, constraints);



		senden.setVisible(false);
		save.setVisible(false);

		constraints.gridx = 2;
		constraints.gridy = 0;

		panel.add(pressure1, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;

		panel.add(pressure2, constraints);





		constraints.gridx = 2;
		constraints.gridy = 3;

		panel.add(pressure1_text, constraints);


		constraints.gridx = 2;
		constraints.gridy = 1;

		panel.add(pressure1_text, constraints);

		constraints.gridx = 3;
		constraints.gridy = 1;

		panel.add(pressure2_text, constraints);

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
		adc.addItem("Druck");
		adc.addItem("Spannung");
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
			System.out.println("Serialport bereits geÃ¶ffnet");
			return false;
		}
		System.out.println("Öffne Serialport");
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
			serialPort = (SerialPort) serialPortId.open("Ã–ffnen und Senden", 500);
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
			System.out.println("TooManyListenersException fÃ¼r Serialport");
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
			System.out.println("Schließe Serialport");
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
			System.out.println("Serialport ist geÃ¶ffnet");
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
			if (adc.getSelectedItem() == "Spannung")
			{
				plot_voltage();
			}
			else
			{
				plot();
				messung_bereit.setVisible(false);
			}
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
					pressure1.setText("Spannung 1 in mV");
					pressure2.setText("Spannung 2 in mV");
				}
				check_end = check_temp.replaceAll("[^\6\7]", "");
				if (check_end.length()>=1){
					check_end = check_temp.replaceAll("[\6\7]", "");
					String[] splitResult = check_end.split( "[; ]" ); // –> splitten am Semikolon
					pressure1_text.setText(splitResult[0]);
					pressure2_text.setText(splitResult[1]);

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
					pressure1.setText("Spannung 1 in mV");
					pressure2.setText("Spannung 2 in mV");
					
				}
				check_end = check_temp.replaceAll("[^\6]", "");
				if (check_end.length()>=1){
					pressure1.setText("Drucksensor 1 in mbar");
					pressure2.setText("Drucksensor 2 in mbar");
				}
				check_end = check_temp.replaceAll("[^\6\7]", "");
				if (check_end.length()>=1){
					check_end = check_temp.replaceAll("[\6\7]", "");
					String[] splitResult = check_end.split( ";" ); // –> splitten am Semikolon
					pressure1_text.setText(splitResult[0]);
					pressure2_text.setText(splitResult[1]);



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
					messung_gange.setVisible(false);


					save_file();
					if (pressure1.getText()=="Spannung 1 in mV")
					{
						plot_voltage();
					}
					else
					{
						if (joukowsky.getText()=="")
						{
							
						}
						else
						{
					    joukowsky();
						}
						plot();
						messung_bereit.setVisible(false);
					}
				}
				check_end = check_temp.replaceAll("[^\3\1\2\4]", "");
				if (check_end.length()>=1){
					messung_bereit.setVisible(false);
					messung_gange.setVisible(true);
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
			// TODO sperre Button Ã–ffnen und Aktualisieren
			// TODO entsperre Nachricht senden und SchlieÃŸen
			oeffneSerialPort((String) auswahl.getSelectedItem());


		}

	}
	class adcActionListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {

			if (adc.getSelectedItem() == "Druck") {
				trigger_u.setVisible(false);
				trigger_u_text.setVisible(false);
				p_trigger.setVisible(true);
				p_trigger_text.setVisible(true);
				sensor1.setVisible(true);
				sensor2.setVisible(true);
				u_min.setVisible(true);
				u_max.setVisible(true);
				p_min.setVisible(true);
				p_max.setVisible(true);
				u_min_text.setVisible(true);
				u_max_text.setVisible(true);
				p_min_text.setVisible(true);
				p_max_text.setVisible(true);
				u_min_text_2.setVisible(true);
				u_max_text_2.setVisible(true);
				p_min_text_2.setVisible(true);
				p_max_text_2.setVisible(true);
				Volumenstrom.setVisible(true);
				Volumenstrom_text.setVisible(true);
				Geschwindigkeit.setVisible(true);
				Geschwindigkeit_text.setVisible(true);
				E_Modul.setVisible(true);
				E_Modul_text.setVisible(true);
				Wandstaerke.setVisible(true);
				Wandstaerke_text.setVisible(true);
				Durchmesser.setVisible(true);
				Durchmesser_text.setVisible(true);
				joukowsky.setVisible(true);
				joukowsky_text.setVisible(true);
				Platzhalter1.setVisible(false);
				Platzhalter2.setVisible(false);
				Platzhalter3.setVisible(false);
				Platzhalter4.setVisible(false);
			
			
			}
			if (adc.getSelectedItem() == "Spannung") {
				trigger_u.setVisible(true);
				trigger_u_text.setVisible(true);
				p_trigger.setVisible(false);
				p_trigger_text.setVisible(false);
				sensor1.setVisible(false);
				sensor2.setVisible(false);
				u_min.setVisible(false);
				u_max.setVisible(false);
				p_min.setVisible(false);
				p_max.setVisible(false);
				u_min_text.setVisible(false);
				u_max_text.setVisible(false);
				p_min_text.setVisible(false);
				p_max_text.setVisible(false);
				u_min_text_2.setVisible(false);
				u_max_text_2.setVisible(false);
				p_min_text_2.setVisible(false);
				p_max_text_2.setVisible(false);
				Volumenstrom.setVisible(false);
				Volumenstrom_text.setVisible(false);
				Geschwindigkeit.setVisible(false);
				Geschwindigkeit_text.setVisible(false);
				E_Modul.setVisible(false);
				E_Modul_text.setVisible(false);
				Wandstaerke.setVisible(false);
				Wandstaerke_text.setVisible(false);
				Durchmesser.setVisible(false);
				Durchmesser_text.setVisible(false);
				joukowsky.setVisible(false);
				joukowsky_text.setVisible(false);
				Platzhalter1.setVisible(true);
				Platzhalter2.setVisible(true);
				Platzhalter3.setVisible(true);
				Platzhalter4.setVisible(true);
				
			    


			}

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
			// TODO entsperre Button Ã–ffnen und Aktualisieren
			// TODO sperre Nachricht senden und SchlieÃŸen
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
			messung_bereit.setVisible(true);

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

			messung_bereit.setVisible(false);
			messung_gange.setVisible(false);
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
			boolean ok=true;
			
			if (Integer.parseInt(speed_text.getText())>9999)
			{
				JOptionPane.showMessageDialog(null, "Bitte einen Wert für die Geschwindigkeit für die Datenübertragung kleiner 9999 eingeben");
				ok=false;
			}
			
			
			if (Integer.parseInt(herz_text.getText())>10000)
			{
				JOptionPane.showMessageDialog(null, "Bitte eine Frequenz kleiner 10001 Hz eingeben");
				ok=false;
			}
			
			if (Integer.parseInt(buffer_text.getText())>=((3000000/Integer.parseInt(herz_text.getText()))/2))
		    {
				JOptionPane.showMessageDialog(null, "Bitte eine Pufferzeit kleiner " + ((3000000/Integer.parseInt(herz_text.getText()))/2) + " ms eingeben" );
			    ok=false;
		    }
			if (ok==true)
			{
				int adc_value =2 ;
				check=false;
				System.out.println("paraActionListener");
				if (adc.getSelectedItem() == "Spannung") {
					adc_value=1;
				}
				int agilent_value=1 ;
				if (agilent.getSelectedItem() == "Logger") {
					agilent_value=3;
				}
				if (agilent.getSelectedItem() == "Terminal") {
					agilent_value=2;
				}
				reset.doClick();



				sendeSerialPort("1");

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}
				sendeSerialPort("\r");
				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}

				sendeSerialPort(agilent_value +"\r\n");


				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}

				if (teiler1.isSelected()==true)
				{
					sendeSerialPort("1\r\n");
				}
				else
				{
					sendeSerialPort("2\r\n");	
				}

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}

				if (teiler2.isSelected()==true)
				{
					sendeSerialPort("1\r\n");
				}
				else
				{
					sendeSerialPort("2\r\n");	
				}

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}


				sendeSerialPort(adc_value + "\r\n");


				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}

				sendeSerialPort(herz_text.getText()+"\r\n");

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}
				if (adc_value==2)
				{
					sendeSerialPort(u_min_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(u_max_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(p_min_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(p_max_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(u_min_text_2.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(u_max_text_2.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(p_min_text_2.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(p_max_text_2.getText()+"\r\n");
				}


				String time_split = time_format_text.getText();
				String[] splits = time_split.split(":");

				if(agilent_value==3 || agilent_value==2){
					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(year_format_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(month_format_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(day_format_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(splits[0]+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
					sendeSerialPort(splits[1]+"\r\n");


				}

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}
				if (adc_value==2)
				{
					sendeSerialPort(p_trigger_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
				}
				if (adc_value==1)
				{
					sendeSerialPort(trigger_u_text.getText()+"\r\n");

					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
					}
				}


				sendeSerialPort(speed_text.getText()+"\r\n");

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}
				sendeSerialPort(buffer_text.getText()+"\r\n");

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}
				sendeSerialPort("1\r\n");

				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
				}


				messung_bereit.setVisible(true);
				senden.setVisible(true);
				save.setVisible(true);


				try
				{
					Thread.sleep(1000);
				}
				catch (Exception e)
				{
				}
				uart_find.check= true;
			}

		}
	}



	/**
	 * Liefert die gegenwÃ¤rtige Zeit GMT + 2:00 (entspricht der deutschen Zeit) im
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
