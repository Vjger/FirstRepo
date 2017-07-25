package it.desimone.rd3analyzer.ui.panels;

import it.desimone.rd3analyzer.Analizzatore;
import it.desimone.rd3analyzer.AzioneVsTabellone;
import it.desimone.rd3analyzer.ColoreGiocatore;
import it.desimone.rd3analyzer.Giocatore;
import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.Tabellone;
import it.desimone.rd3analyzer.azioni.Attacco;
import it.desimone.rd3analyzer.azioni.Azione;
import it.desimone.rd3analyzer.azioni.GiocoTris;
import it.desimone.rd3analyzer.azioni.Invasione;
import it.desimone.rd3analyzer.azioni.RicezioneCarta;
import it.desimone.rd3analyzer.azioni.Rinforzo;
import it.desimone.rd3analyzer.azioni.Spostamento;
import it.desimone.rd3analyzer.main.ReplayStarter;
import it.desimone.utils.Configurator;
import it.desimone.utils.MyLogger;
import it.desimone.utils.ResourceLoader;
import it.desimone.utils.Configurator.PlanciaSize;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class StarterReplayFrame extends JFrame {

	private static final int widthFrame = 500;
	private static final int heigthFrame = 350;
	
	private JLabel labelHDReady;
	private JLabel labelFullHD;
	private JPanel startPanel;
	
	private PlanciaSize planciaSizeSelected;
	
	public StarterReplayFrame(){
		
		setTitle(ReplayStarter.VERSIONE);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-widthFrame)/2;
		int y = (screen.height-heigthFrame)/2;
		Rectangle r = new Rectangle(x,y,widthFrame,heigthFrame);
		setBounds(r);		
		
		setLayout(null);
		
		//getContentPane().setLayout(new BorderLayout());
		
		createLabelHDReady();
		createLabelFullHD();
		createStartPanel();
		
		labelHDReady.setBounds(30, 10, 200, 100);
		labelFullHD.setBounds(270, 10, 200, 100);
		startPanel.setBounds(195,140,110,160);
		
		//getContentPane().add(labelFullHD, BorderLayout.EAST);
		//getContentPane().add(labelHDReady, BorderLayout.WEST);
		//getContentPane().add(startPanel, BorderLayout.SOUTH);
		
		getContentPane().add(labelFullHD);
		getContentPane().add(labelHDReady);
		getContentPane().add(startPanel);
		
		setResizable(false);
		
		getContentPane().setBackground(Color.ORANGE);
		
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    setVisible(true);
	}
	
	private void createLabelHDReady(){
		labelHDReady = new JLabel();
		Icon iconHDReay = new ImageIcon(ResourceLoader.getPathPlanciaHDReady());
		labelHDReady.setIcon(iconHDReay);
		
		labelHDReady.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				labelHDReady.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.RED));
				labelFullHD.setBorder(null);
				planciaSizeSelected = PlanciaSize.SMALL;
			}
		});
	}
	
	private void createLabelFullHD(){
		labelFullHD = new JLabel();
		Icon iconFullHD = new ImageIcon(ResourceLoader.getPathPlanciaFullHD());
		labelFullHD.setIcon(iconFullHD);
		
		
		labelFullHD.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				labelFullHD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.RED));
				labelHDReady.setBorder(null);
				planciaSizeSelected = PlanciaSize.BIG;
			}
		});
	}
	
	private void createStartPanel(){
		startPanel = new JPanel();
		startPanel.setLayout(null); //new BoxLayout(startPanel, BoxLayout.Y_AXIS));
		//startPanel.setPreferredSize(new Dimension(50,200));
		startPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		final JLabel idPartitaLabel = new JLabel("ID della partita");
		idPartitaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		idPartitaLabel.setBounds(10,0,100,30);
		
		final JTextField idPartitaTextfield = new JTextField();
		idPartitaTextfield.setHorizontalAlignment(SwingConstants.CENTER);
		idPartitaTextfield.setBounds(5,35,100,30);
		
		final JButton startReplayButton = new JButton("Replay!");
		startReplayButton.setBounds(15,70,80,30);

		final JLabel progressLabel = new JLabel("");
		//progressLabel.setBounds(5,105,90,30);
		
		final JCheckBox disposizioneInizialeButton = new JCheckBox("Disp. iniziale");
		//disposizioneInizialeButton.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Disp. iniziale"));
		disposizioneInizialeButton.setBounds(5,105,95,30);
		
		startPanel.add(idPartitaLabel);
		startPanel.add(idPartitaTextfield);
		startPanel.add(startReplayButton);	
		startPanel.add(disposizioneInizialeButton);
	
		
		startReplayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (planciaSizeSelected == null){
					JOptionPane.showMessageDialog(null, "Selezionare la risoluzione preferita");
				}else if (idPartitaTextfield.getText() == null || idPartitaTextfield.getText().trim().length() == 0){
					JOptionPane.showMessageDialog(null, "Indicare l'ID della partita");
				}else{
					Configurator.selectedResolution = planciaSizeSelected;
					String idPartita = idPartitaTextfield.getText().trim();
					
					StarterThread starterThread = new StarterThread(idPartita, progressLabel);
					Thread t = new Thread(starterThread);
					t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
						
						@Override
						public void uncaughtException(Thread arg0, Throwable arg1) {
							JOptionPane.showMessageDialog(startPanel, arg1.getMessage());
							//arg1.printStackTrace();
						}
					});
					
					Partita partita = null;		
					try{	
						Partita partitaSerializzata = ResourceLoader.readPartita(idPartita);

						if (partitaSerializzata == null){
							partita = Analizzatore.elaboraPartita(idPartita, false);
							if (partita == null){
								throw new IOException("Problemi nel recuperare la scheda della partita: verificare che la url \n"+Analizzatore.SITE_PARTITA+idPartita+"\nsia raggiungibile");
							}
							ResourceLoader.savePartita(partita);
						}else{
							partita = partitaSerializzata;
						}
						
						
						//progressLabel.setText("Acquisisco i dati della Partita");
						t.start();
//						try {
//							t.join();
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						//Partita partita = starterThread.getPartita();
						//ReplayFrame replayFrame = new ReplayFrame(partita);
						List<AzioneVsTabellone> azioniVsTabellone = getAzioniVsTabellone(partita, disposizioneInizialeButton.isSelected());
						ReplayFrame replayFrame = new ReplayFrame(partita, azioniVsTabellone);
						
					} catch (IOException e) {
						MyLogger.getLogger().severe("IOException: "+e.getMessage());
						JOptionPane.showMessageDialog(startPanel, e.getMessage());
					} catch (ParseException e) {
						MyLogger.getLogger().severe("ParseException: "+e.getMessage());
						JOptionPane.showMessageDialog(startPanel, e.getMessage());
					} catch (Exception e) {
						MyLogger.getLogger().severe(e.getClass().getName()+" "+e.getMessage());
						JOptionPane.showMessageDialog(startPanel, e.getMessage());
					}	

				}
			}
		});
	}
	
	
	private List<AzioneVsTabellone> getAzioniVsTabellone(Partita partita, boolean conDisposizioneIniziale){
		Tabellone tabellone = new Tabellone(partita);
		List<Azione> azioniLog = partita.getAzioniLog();
		
		boolean disposizioneInizialeCompletata = false;

		List<ColoreGiocatore> giocatori = new ArrayList<ColoreGiocatore>();
		ColoreGiocatore precedenteGiocatore = null;
		int numeroTurno = 0;
		List<AzioneVsTabellone> azioniVsTabellone = new ArrayList<AzioneVsTabellone>();
		
		for (Azione azione: azioniLog){	

			if (disposizioneInizialeCompletata){		
				ColoreGiocatore giocatoreCheAgisce = azione.getGiocatoreCheAgisce();
				if (giocatoreCheAgisce != precedenteGiocatore){
					if (!giocatori.contains(giocatoreCheAgisce)){
						numeroTurno++;
						giocatori = coloreGiocatori(partita);
					}
					giocatori.remove(giocatoreCheAgisce);
					precedenteGiocatore = giocatoreCheAgisce;
				}
				tabellone.setNumeroTurno(numeroTurno);
			}
			
			switch (azione.getTipoAzione()) {
			case ATTACCO:
				Attacco attacco = (Attacco) azione;
				tabellone.calcolaAttacco(attacco);
				break;
			case RINFORZO:
				Rinforzo rinforzo = (Rinforzo) azione;
				tabellone.calcolaRinforzo(rinforzo);
				break;
			case INVASIONE:
				Invasione invasione = (Invasione) azione;
				tabellone.calcolaInvasione(invasione);
				break;
			case SPOSTAMENTO:
				Spostamento spostamento = (Spostamento) azione;
				tabellone.calcolaSpostamento(spostamento);
				break;
			case RICEZIONE_CARTA:
				RicezioneCarta ricezioneCarta = (RicezioneCarta) azione;
				tabellone.calcolaRicezioneCarta(ricezioneCarta);
				break;
			case GIOCO_TRIS:
				GiocoTris giocoTris = (GiocoTris) azione;
				tabellone.calcolaGiocoTris(giocoTris);
				break;
			default:
				break;
			}	
			
			if (!disposizioneInizialeCompletata && tabellone.isTabelloneCompleto()){
				disposizioneInizialeCompletata = true;
			}
			
			if (conDisposizioneIniziale || disposizioneInizialeCompletata){
				AzioneVsTabellone azioneVsTabellone = new AzioneVsTabellone(azione, new Tabellone(tabellone));
				azioniVsTabellone.add(azioneVsTabellone);
			}
		}
		
		return azioniVsTabellone;
	}
	
	private List<ColoreGiocatore> coloreGiocatori(Partita partita){
		List<ColoreGiocatore> result = new ArrayList<ColoreGiocatore>();
		for (Giocatore giocatore: partita.getGiocatori()){
			result.add(giocatore.getColoreGiocatore());
		}
		return result;
	}
	
	public static class StarterThread implements Runnable{

		private String idPartita;
		private Partita partita;
		private JLabel progressLabel;
		public StarterThread(String idPartita, JLabel progressLabel){
			this.idPartita = idPartita;
			this.progressLabel = progressLabel;
		}
		@Override
		public void run() {
			progressLabel.setText("Acquisisco i dati della Partita");

		}
		public Partita getPartita() {
			return partita;
		}
		
		
	}
}
