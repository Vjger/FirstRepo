package it.desimone.replayrd3.ui.panels;

import it.desimone.replayrd3.AzioneVsTabellone;
import it.desimone.replayrd3.Partita;
import it.desimone.replayrd3.main.ReplayStarter;
import it.desimone.utils.Configurator;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;

public class ReplayFrame extends JFrame {
	
	PlanciaPanel planciaPanel = new PlanciaPanel();

	
//	public ReplayFrame(Partita partita){
//		
//		setLayout(null);
//		planciaPanel.setBounds(5, 5, 1010, 700);
//		planciaPanel.setNomePartita(partita.getNomePartita());
//		getContentPane().add(planciaPanel);
//
//		RightPanel pulsantieraPanel = new RightPanel(planciaPanel, partita);
//		pulsantieraPanel.setBounds(1020, 5, 150, 700);
//		getContentPane().add(pulsantieraPanel);
//
//		switch (Configurator.selectedResolution) {
//		case SMALL:
//			setSize(1180, 750);
//			break;
//		case BIG:
//		    setSize(1600, 900);
//			break;
//		default:
//			break;
//		}
//
//		setResizable(false);
//	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    
//	    setVisible(true);
//	}
	
	public ReplayFrame(Partita partita, List<AzioneVsTabellone> azioniVsTabellone){
//
		setTitle(ReplayStarter.VERSIONE);
		
//		setLayout(new BorderLayout());
//		getContentPane().add(planciaPanel, BorderLayout.CENTER);
//		getContentPane().add(new PulsantieraPanel(planciaPanel, partita), BorderLayout.LINE_END);
		
		setLayout(null);

		
//		setLayout(new FlowLayout());
//		getContentPane().add(planciaPanel);
//		getContentPane().add(new PulsantieraPanel(planciaPanel, partita));

		RightPanel pulsantieraPanel = new RightPanel(planciaPanel, partita, azioniVsTabellone);
		
		//getContentPane().add(new ReplayPanel());
		switch (Configurator.selectedResolution) {
		case SMALL:
			//setSize(1190, 740);
			setSize(1200, 750);
			planciaPanel.setBounds(5, 5, 1010, 700);
			pulsantieraPanel.setBounds(1020, 5, 150, 700);
			break;
		case BIG:
		    //setSize(1380, 890);
		    setSize(1400, 890);
			planciaPanel.setBounds(5, 5, 1210, 840);
			pulsantieraPanel.setBounds(1220, 5, 150, 840);
			break;
		default:
			break;
		}

		planciaPanel.setNomePartita(partita.getNomePartita());
		getContentPane().add(planciaPanel);
		getContentPane().add(pulsantieraPanel);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-getWidth())/2;
		int y = (screen.height-getHeight())/2;
		Rectangle r = new Rectangle(x,y,getWidth(),getHeight());
		setBounds(r);	
		
		setResizable(false);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    setVisible(true);
	}
	
}
