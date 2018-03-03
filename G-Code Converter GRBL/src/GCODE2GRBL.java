import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;
import java.util.Scanner;

public class GCODE2GRBL {

	private JFrame frmTargetToGrbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GCODE2GRBL window = new GCODE2GRBL();
					window.frmTargetToGrbl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GCODE2GRBL() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public static class Globals {
		public static int a;
	}

	private void initialize() {
		frmTargetToGrbl = new JFrame();
		frmTargetToGrbl.setTitle("Target3001 to GRBL");
		frmTargetToGrbl.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmTargetToGrbl.setBounds(100, 100, 1239, 567);
		frmTargetToGrbl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTargetToGrbl.getContentPane().setLayout(null);


		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 45, 320, 480);
		frmTargetToGrbl.getContentPane().add(scrollPane);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);


		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(350, 45, 320, 480);
		frmTargetToGrbl.getContentPane().add(scrollPane_1);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(850, 45, 320, 480);
		frmTargetToGrbl.getContentPane().add(scrollPane_2);

		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		JTextArea textArea_2 = new JTextArea();
		scrollPane_2.setViewportView(textArea_2);


		/**Chose Files*/
		JButton btnOpenGgode = new JButton("Open G-Gode");
		btnOpenGgode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				String filename=f.getAbsolutePath();

				try
				{
					FileReader reader =new FileReader(filename);
					textArea.read(reader, null);
					textArea.requestFocus();
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}

			}

		});




		btnOpenGgode.setBounds(10, 10, 150, 25);
		frmTargetToGrbl.getContentPane().add(btnOpenGgode);

		JButton btnOpenDrillTable = new JButton("Open Drill/Mill Table");
		btnOpenDrillTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				String filename=f.getAbsolutePath();

				try
				{
					FileReader reader =new FileReader(filename);
					textArea_1.read(reader, null);
					textArea_1.requestFocus();
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}

			}

		});
		btnOpenDrillTable.setBounds(350, 10, 150, 25);
		frmTargetToGrbl.getContentPane().add(btnOpenDrillTable);



		String[] filenames_new = new String[50];
		String[] files = new String[50];

		JButton btnUmwandeln = new JButton("Umwandeln");
		btnUmwandeln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String Sentence = textArea.getText();	
				Sentence = Sentence.replace("(MSG, TURN OVER PCB NOW AND CONTINUE)", "T");
				String[] splitSentence = Sentence.split("T");			
				String filenames = textArea_1.getText();
				String[] filenames_ = new String[50];	


				Scanner scannerFilenames = new Scanner(filenames);
				int z = 1;
				while (scannerFilenames.hasNextLine()) {
					filenames_[z] = scannerFilenames.nextLine();
					z++;
				}
				for(int i = 1; i < splitSentence.length; i++  )
				{					
					Scanner scanner = new Scanner(splitSentence[i]);
					splitSentence[i] = "";
					int k = 0;
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (k == 0) {
							boolean check = false;
							for (int h = 1; h<z; h++) {
								if (filenames_[h].contains("T" + line +";"))
								{
									filenames_new[i] = filenames_[h];
									check = true;
								}
							}
							if (check == false)
							{filenames_new[i]="Turn_Over_and_Mill";
							}
							splitSentence[i] = "%M6 "+filenames_new[i] +"\r\nG21\r\nG90\r\nG94\r\nG0 Z1.0\r\n";
							filenames_new[i] = filenames_new[i].split("; Depth")[0];
						}
						else
						{
							if (line.contains("M") || line.contains("G2") || line.contains("G3" ) || line.contains("G4") || line.contains("G5") || line.contains("G6") || line.contains("G7") || line.contains("G8") || line.contains("G9") &&  !line.contains("%"))
							{
								line = "";
							}
							else
							{
								splitSentence[i] = splitSentence[i] + line + "\r\n";
							}
						}
						k++;
					}
					scanner.close();
					splitSentence[i] = splitSentence[i] + "G0 Z30.0\r\n";
					textArea_2.append(splitSentence[i]);
					files[i] = splitSentence[i];
					Globals.a = i -1 ;	
				}
			}
		});
		btnUmwandeln.setBounds(685, 465, 150, 25);
		frmTargetToGrbl.getContentPane().add(btnUmwandeln);

		JButton btnSpeichern = new JButton("Speichern");
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();


				JFileChooser fs = new JFileChooser(new File(""));
				fs.setDialogTitle("geänderten G-Code speichern");
				int result = fs.showSaveDialog(null);
				if (result== JFileChooser.APPROVE_OPTION){
					File fi = fs.getSelectedFile();
					for (int y=1; y<=Globals.a; y++)
					{
						try{
							FileWriter fw = new FileWriter(fi.getPath()+"_" +y +"_" + filenames_new[y]+".nc");
							fw.write(files[y]);
							fw.flush();
							fw.close();						
						} 
						catch (Exception e2){
							JOptionPane.showMessageDialog(null,  e2.getMessage());

						}

					}
				}
			}
		});
		btnSpeichern.setBounds(685, 500, 150, 25);
		frmTargetToGrbl.getContentPane().add(btnSpeichern);





	}


}



