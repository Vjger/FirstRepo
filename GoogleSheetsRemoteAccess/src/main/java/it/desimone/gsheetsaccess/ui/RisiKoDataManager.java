package it.desimone.gsheetsaccess.ui;

import it.desimone.gsheetsaccess.ReportPublisher;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.utils.MyLogger;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

public class RisiKoDataManager extends JFrame {
    /**
     * The text area which is used for displaying logging information.
     */
    private JTextArea textArea;
     
    private JButton buttonPublish = new JButton("Pubblica Tornei");
    private JButton buttonDelete = new JButton("Elimina Torneo");
    private JButton buttonClear = new JButton("Clear");
    private JLabel torneoToDeleteLabel = new JLabel("ID Torneo");
    private JTextField torneoToDelete = new JTextField(200);
     
    private PrintStream standardOut;
     
    public RisiKoDataManager() {
        super("Demo printing to JTextArea");
         
        textArea = new JTextArea(250, 10);
        textArea.setEditable(false);
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
         
        // keeps reference of standard output stream
        standardOut = System.out;
         
        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
 
        // creates the GUI
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;
         
        add(buttonPublish, constraints);

        constraints.gridx = 1;
        add(torneoToDeleteLabel, constraints);
        
        torneoToDelete.setPreferredSize(new Dimension(150,30));
        torneoToDelete.setMinimumSize(new Dimension(140,25));
        constraints.gridx = 2;
        add(torneoToDelete, constraints);
      
        constraints.gridx = 3;
        add(buttonDelete, constraints);
        
       constraints.gridx = 4;
       add(buttonClear, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 5;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
         
        add(new JScrollPane(textArea), constraints);
         
        // adds event handler for button Start
        buttonPublish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //printLog();
                publish();
            }
        });
        
        buttonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	String idTorneo = torneoToDelete.getText();
            	if (idTorneo == null || idTorneo.trim().length() == 0){
            		JOptionPane.showMessageDialog(null, "Selezionare l'id del Torneo da cancellare");
            	}else{
            		deleteTorneo(idTorneo);
            	}
            }
        });
         
        // adds event handler for button Clear
        buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // clears the text area
                try {
                    textArea.getDocument().remove(0, textArea.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });
         
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 320);
        setLocationRelativeTo(null);    // centers on screen
    }
     
    /**
     * Prints log statements for testing in a thread
     */
    private void printLog() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("Time now is " + (new Date()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
    
    private void publish() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
            	MyLogger.setConsoleLogLevel(Level.INFO);
            	ReportPublisher.main(null);
            }
        });
        thread.start();
    }
    
    private void deleteTorneo(final String idTorneo) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
            	MyLogger.setConsoleLogLevel(Level.INFO);
            	TorneiUtils.deleteTorneo(idTorneo);
            }
        });
        thread.start();
    }
     
    /**
     * Runs the program
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RisiKoDataManager().setVisible(true);
            }
        });
    }
}