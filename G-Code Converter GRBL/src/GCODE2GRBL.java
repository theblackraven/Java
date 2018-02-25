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
		scrollPane.setBounds(10, 45, 322, 473);
		frmTargetToGrbl.getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		//Datei auswählen
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
		btnOpenGgode.setBounds(10, 11, 109, 23);
		frmTargetToGrbl.getContentPane().add(btnOpenGgode);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(555, 45, 322, 473);
		frmTargetToGrbl.getContentPane().add(scrollPane_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(887, 45, 322, 473);
		frmTargetToGrbl.getContentPane().add(scrollPane_2);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		JTextArea textArea_2 = new JTextArea();
		scrollPane_2.setViewportView(textArea_2);
		
		JButton btnUmwandeln = new JButton("Umwandeln");
		btnUmwandeln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String Sentence = textArea.getText();
				Sentence = Sentence.replace("M01", "M1");
				Sentence = Sentence.replace("M02", "M2");
				Sentence = Sentence.replace("M03", "M3");
				Sentence = Sentence.replace("M04", "M4");
				Sentence = Sentence.replace("M05", "M5");
				Sentence = Sentence.replace("M06", "M6");
				Sentence = Sentence.replace("M07", "");
				Sentence = Sentence.replace("M08", "M8");
				Sentence = Sentence.replace("M09", "M9");
				Sentence = Sentence.replace("G71", "G21");
			
				
				Sentence = Sentence.replaceFirst("(?m)^M6.*", "");

				
				String[] splitSentence = Sentence.split("(MSG, TURN OVER PCB NOW AND CONTINUE)");
				splitSentence[0] = splitSentence[0].replace("(", "");
				splitSentence[0] = splitSentence[0].replace(")", "");
				splitSentence[0] = splitSentence[0].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
				splitSentence[0] = splitSentence[0] + "M2\nM30";
				splitSentence[1] = splitSentence[1].replace("(", "");
				splitSentence[1] = splitSentence[1].replace(")", "");
				splitSentence[1] = "G21\nG90\nG94\nM5\nM9\n" + splitSentence[1];
				splitSentence[1] = splitSentence[1].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
				splitSentence[0] = splitSentence[0].replace("\n", "\r\n");
				splitSentence[1] = splitSentence[1].replace("\n", "\r\n");
				textArea_1.setText(splitSentence[0]);
				textArea_2.setText(splitSentence[1]);
				
				
			}
		});
		btnUmwandeln.setBounds(384, 442, 161, 23);
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
					try{
						FileWriter fw = new FileWriter(fi.getPath()+"Front.nc");
						fw.write(textArea_1.getText());
						fw.flush();
						fw.close();
						
						FileWriter fw1 = new FileWriter(fi.getPath()+"Back.nc");
						fw1.write(textArea_2.getText());
						fw1.flush();
						fw1.close();
						JOptionPane.showMessageDialog(frame, "Erfolgreich gespeichert");					
						
						} catch (Exception e2){
							JOptionPane.showMessageDialog(null,  e2.getMessage());
						
						}
						
					
					}
				}
			});
		btnSpeichern.setBounds(384, 468, 109, 23);
		frmTargetToGrbl.getContentPane().add(btnSpeichern);
		
		
		
		
		
	}
	
	
}


 
