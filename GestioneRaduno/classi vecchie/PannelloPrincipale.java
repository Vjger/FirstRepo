package it.desimone.risiko.torneo.panels;

import it.desimone.risiko.torneo.batch.ExcelAccess;
import it.desimone.risiko.torneo.batch.GeneratoreTavoli;
import it.desimone.risiko.torneo.batch.RadGester;
import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.ScorePlayer;
import it.desimone.risiko.torneo.utils.PrioritaSorteggio;
import it.desimone.risiko.torneo.utils.TipoTorneo;
import it.desimone.utils.Logger;
import it.desimone.utils.MyException;
import it.desimone.utils.TextException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PannelloPrincipale extends JFrame implements ActionListener {

	private final int WIDTH = 600;
	private final int HEIGHT = 200;
	
	private JFileChooser fileChooser = new JFileChooser(".\\");
	
	private JComboBox tipoTornei = new JComboBox(TipoTorneo.values());
	private JSlider numeroTurno = new JSlider(SwingConstants.HORIZONTAL,1,10,1);
	private JCheckBox stampaTurno = new JCheckBox("Stampa");
	
	private JButton fileButton = new JButton("Seleziona File Excel");
	private JButton classificaButton = getClassificaButton();
	private File excelFile;
	
	private JMenuBar 			menuBar;
	private JMenu fileMenu    = new JMenu("File");
	private JMenuItem menuOpenFile= new JMenuItem("Open");
	private JMenu helpMenu    = new JMenu("?");
	private JMenuItem menuHelp= new JMenuItem("Help");
	private JMenuItem menuXls= new JMenuItem("Nuovo xls");
	private JMenuItem menuAbout= new JMenuItem("About");
	private String nomeFileHelp = "help.html";
	private String nomeFileExcel = "ModuloTorneo.xls";
	private File fileHelp;
	
	public PannelloPrincipale(){
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-WIDTH)/2;
		int y = (screen.height-HEIGHT)/2;
		Rectangle r = new Rectangle(x,y,WIDTH+50,HEIGHT+50);
		setBounds(r);
		setResizable(false);
		
		aggiungiComponenti();
		setJMenuBar(menuBar);
		
		fileChooser.addChoosableFileFilter(new ExcelFileFilter());
		
		fileButton = getChooserButton();
		fileButton.addActionListener(this);
		
        numeroTurno.setMajorTickSpacing(9);
        numeroTurno.setMinorTickSpacing(1);
		numeroTurno.setPaintTicks(true);
		numeroTurno.setPaintLabels(true);
		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		toolTipManager.setInitialDelay(0);
		numeroTurno.addMouseListener(toolTipManager);
		numeroTurno.addMouseMotionListener(toolTipManager);
		numeroTurno.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				JSlider slider = (JSlider) arg0.getSource();
				slider.setToolTipText(String.valueOf(slider.getValue()));
			}
		});
		
		setLayout(new BorderLayout());
		//add(fileButton,BorderLayout.NORTH);
		add(getVoidPanel(),BorderLayout.EAST);
		add(getFilterPanel(),BorderLayout.WEST);
		add(getChecksPanel(),BorderLayout.CENTER);
		add(classificaButton,BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		 //setSize(95,95);
	}
	
	private void aggiungiComponenti() {
		gestioneMenu();
	}
	
	private void gestioneMenu(){
		menuBar 		= new JMenuBar();
		fileMenu.add(menuOpenFile);
		helpMenu.add(menuHelp);
		helpMenu.add(menuXls);
		helpMenu.add(menuAbout);
		
		 ActionListener listOpenFile = new ActionListener(){
			public void actionPerformed (ActionEvent e){
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					excelFile = fileChooser.getSelectedFile();
				}
			}
		};
		
		/* Gestione degli eventi sul menù ? */
		 ActionListener listHelp = new ActionListener(){
			public void actionPerformed (ActionEvent e){
				try{
					if ( (fileHelp == null) || (!fileHelp.exists()) ) {
						fileHelp = new File(nomeFileHelp);
						fileHelp.deleteOnExit();
						InputStream is = this.getClass().getResourceAsStream("/" +nomeFileHelp);
						byte [] buff = new byte [1024];
						OutputStream out = new FileOutputStream(fileHelp);
						int n;
						while( (n = is.read(buff, 0, buff.length))!= -1){
							out.write(buff, 0 , n);
						}
						is.close();
						out.close();
					}
					Runtime.getRuntime().exec("cmd /c "+fileHelp.getName());	
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		 };
		 
			/* Gestione degli eventi sul menù nuovo xls */
		 ActionListener listXls = new ActionListener(){
			public void actionPerformed (ActionEvent e){
				try{
					JFileChooser jfc = new JFileChooser();
					int returnVal = jfc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION){
	                    File file = jfc.getSelectedFile();
						InputStream is = this.getClass().getResourceAsStream("/" +nomeFileExcel);
						byte [] buff = new byte [1024];
						OutputStream out = new FileOutputStream(file);
						int n;
						while( (n = is.read(buff, 0, buff.length))!= -1){
							out.write(buff, 0 , n);
						}
						is.close();
						out.close();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		 };
		 
		/* Gestione degli eventi sul menù About */
		 ActionListener listAbout = new ActionListener(){
			public void actionPerformed (ActionEvent e){
				JOptionPane.showMessageDialog(menuAbout,"GestioneRaduno 1.2 \n\n\n Sviluppato da Marco De Simone \n\n\n vjger69@gmail.com");
			}	
		 };

		menuOpenFile.addActionListener(listOpenFile);
		menuHelp.addActionListener(listHelp);
		menuXls.addActionListener(listXls);
		menuAbout.addActionListener(listAbout);
		menuBar.add(fileMenu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);
	}
	
	private JPanel getFilterPanel(){
		JPanel panel = new JPanel();
        JList list = new JList(PrioritaSorteggio.values());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
        panel.add(list);
        return panel;
	}
	
	private JPanel getVoidPanel(){
		JPanel voidPanel = new JPanel();
		voidPanel.setPreferredSize(new Dimension(15,15));
		return voidPanel;
	}
	
	private JPanel getChecksPanel(){
		JPanel checksPanel = new JPanel();
		checksPanel.setSize(new Dimension(10,10));
        JLabel tipoTorneoLabel = new JLabel("Tipo Torneo", JLabel.CENTER);
        tipoTorneoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checksPanel.add(tipoTorneoLabel);
		checksPanel.add(tipoTornei);
        JLabel sliderLabel = new JLabel("Numero Turno", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checksPanel.add(sliderLabel);
		checksPanel.add(numeroTurno);
		checksPanel.add(getConfirmButton());
		checksPanel.add(stampaTurno);
		checksPanel.setVisible(true);
		return checksPanel;
	}
	
	private JButton getClassificaButton(){
		JButton button = new JButton("Classifica");
		button.setPreferredSize(new Dimension(60,25));
		button.addActionListener(this);
		return button;
	}
	
	private JButton getConfirmButton(){
		JButton button = new JButton("GO!");
		button.setPreferredSize(new Dimension(60,25));
		button.addActionListener(this);
		return button;
	}
	
	private JButton getChooserButton(){
		JButton button = new JButton("Seleziona File Excel");
		button.setSize(new Dimension(5,15));
		return button;
	}
	
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		try{
			if (source == fileButton){
				int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					excelFile = fileChooser.getSelectedFile();
				}
			}else if (source == classificaButton){
				if (excelFile == null){
					throw new MyException("Selezionare il foglio Excel con i partecipanti");
				}else{
					ExcelAccess excelAccess = new ExcelAccess(excelFile);
					excelAccess.openFileExcel();
					excelAccess.scriviClassifica(getTipoTorneo());
					excelAccess.closeFileExcel();
					Logger.debug("***  Fine calcolo classifica ***");
					String message = "Fine Elaborazione\n"+Logger.getLog();
					JOptionPane.showMessageDialog(null, message, "Risultato Finale", JOptionPane.INFORMATION_MESSAGE);
				}
			}else{
				if (excelFile == null){
					throw new MyException("Selezionare il foglio Excel con i partecipanti");
				}else if (getNumeroTurno() == 0){
					throw new MyException("Effettuare la scelta di almeno un turno");
				}else{
					ExcelAccess excelAccess = new ExcelAccess(excelFile);
					excelAccess.openFileExcel();
					int numeroTurno = getNumeroTurno();
					List<GiocatoreDTO> giocatoriTurno = excelAccess.getListaGiocatori(true);
					Partita[] partiteTurno = null;
					if (numeroTurno == 1){
						switch (getTipoTorneo()) {
						case RadunoNazionale:
							partiteTurno = GeneratoreTavoli.tavoliPrimoTurno(giocatoriTurno);
							break;
						case CampionatoGufo:
							partiteTurno = GeneratoreTavoli.tavoliPrimoTurnoCampionatoGufo(giocatoriTurno);
							break;
						case ColoniDiCatan:
						case Dominion:
						case StoneAge:
							partiteTurno = GeneratoreTavoli.tavoliPrimoTurnoTorneoColoni(giocatoriTurno);
							break;
						default:
							partiteTurno = GeneratoreTavoli.tavoliPrimoTurnoTorneoGufo(giocatoriTurno);
							break;
						} 
					}else{
						List<Partita> listaPartitePrecedenti = new ArrayList<Partita>();
						for (int i = 1; i < numeroTurno; i++){
							Partita[] partiteTurnoi = excelAccess.loadPartite(i,false,getTipoTorneo());
							if (partiteTurnoi == null){
								throw new MyException("E' stato richiesto il sorteggio per il turno "+numeroTurno+" ma non esiste il turno "+i);
							}
							listaPartitePrecedenti.addAll(Arrays.asList(partiteTurnoi));
						}
						Partita[] partitePrecedenti = listaPartitePrecedenti.toArray(new Partita[0]);
						switch (getTipoTorneo()) {
						case RadunoNazionale:
							partiteTurno = GeneratoreTavoli.getTavoliSecondoTurno(giocatoriTurno, partitePrecedenti);
							break;
						case CampionatoGufo:
							partiteTurno = GeneratoreTavoli.tavoliTurniSuccessiviCampionatoGufo(giocatoriTurno, partitePrecedenti);
							break;
						case ColoniDiCatan:
							List<ScorePlayer> scores = excelAccess.getClassificaTorneoColoni();
							List<GiocatoreDTO> players = new ArrayList<GiocatoreDTO>();
							for (ScorePlayer scorePlayer: scores){
								if (giocatoriTurno.contains(scorePlayer.getGiocatore())){
									players.add(scorePlayer.getGiocatore());
								}
							}
							partiteTurno = GeneratoreTavoli.tavoliOrdinatiColoni(players);
							break;
						case Dominion:
							List<ScorePlayer> scoresDominion = excelAccess.getClassificaTorneoDominion();
							List<GiocatoreDTO> playersDominion = new ArrayList<GiocatoreDTO>();
							for (ScorePlayer scorePlayer: scoresDominion){
								if (giocatoriTurno.contains(scorePlayer.getGiocatore())){
									playersDominion.add(scorePlayer.getGiocatore());
								}
							}
							partiteTurno = GeneratoreTavoli.tavoliOrdinatiColoni(playersDominion);
							break;
						case StoneAge:
							List<ScorePlayer> scoresStoneAge = excelAccess.getClassificaTorneoStoneAge();
							List<GiocatoreDTO> playersStoneAge = new ArrayList<GiocatoreDTO>();
							for (ScorePlayer scorePlayer: scoresStoneAge){
								if (giocatoriTurno.contains(scorePlayer.getGiocatore())){
									playersStoneAge.add(scorePlayer.getGiocatore());
								}
							}
							partiteTurno = GeneratoreTavoli.tavoliOrdinatiColoni(playersStoneAge);
							break;
						default:
							partiteTurno = GeneratoreTavoli.tavoliTurniSuccessiviTorneoGufo(giocatoriTurno, partitePrecedenti);
							break;
						} 
					}
					for (Partita partita: partiteTurno){
						switch (getTipoTorneo()) {
						case StoneAge:
							excelAccess.scriviPartiteStoneAge(ExcelAccess.getNomeTurno(numeroTurno), partita);
							break;
						case Dominion:
							excelAccess.scriviPartiteDominion(ExcelAccess.getNomeTurno(numeroTurno), partita);
							break;
						default:
							excelAccess.scriviPartite(ExcelAccess.getNomeTurno(numeroTurno), partita);
							break;
						}
						if (stampaTurno != null && stampaTurno.isSelected()){
							excelAccess.stampaPartite("Stampa "+ExcelAccess.getNomeTurno(numeroTurno), partita);
						}
					}
					excelAccess.scriviLog(Logger.getLog());
					excelAccess.closeFileExcel();
					String message = "Fine Elaborazione\n";//+Logger.getLog();
					JOptionPane.showMessageDialog(null, message, "Risultato Finale", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}catch (Exception e) {
			RadGester.writeException(e);
			JOptionPane.showMessageDialog(null, new TextException(e),"Orrore!",JOptionPane.ERROR_MESSAGE);
		}
	}

	private int getNumeroTurno(){
		int numeroTurno = this.numeroTurno.getValue();
		return numeroTurno;
	}
	
	private TipoTorneo getTipoTorneo(){
		return (TipoTorneo)tipoTornei.getSelectedItem();
	}
}
