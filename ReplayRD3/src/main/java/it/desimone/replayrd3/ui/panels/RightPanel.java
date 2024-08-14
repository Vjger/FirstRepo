package it.desimone.replayrd3.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import it.desimone.replayrd3.AzioneVsTabellone;
import it.desimone.replayrd3.Partita;
import it.desimone.replayrd3.ui.UIUpdaterDelegate;

public class RightPanel extends JPanel{
	
	private PulsantieraPanel pulsantieraPanel;
	private StatsPanel statsPanel;
	
//	public RightPanel(PlanciaPanel planciaPanel, Partita partita){
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		statsPanel = new StatsPanel(partita);
//		add(statsPanel);
//
//		JPanel emptyPanel = new JPanel();
//		emptyPanel.setPreferredSize(new Dimension(200,300));
//		emptyPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//		emptyPanel.setBackground(Color.ORANGE);
//		add(emptyPanel);
//		
//		UIUpdaterDelegate uiUpdaterDelegate = new UIUpdaterDelegate(planciaPanel, statsPanel);
//		
//		pulsantieraPanel = new PulsantieraPanel(uiUpdaterDelegate, partita);
//		pulsantieraPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//		pulsantieraPanel.setBackground(Color.PINK);
//		add(pulsantieraPanel);
//		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//		setBackground(Color.RED);
//	}
	
	public RightPanel(PlanciaPanel planciaPanel, Partita partita, List<AzioneVsTabellone> azioniVsTabellone){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		statsPanel = new StatsPanel(partita);
		//statsPanel.setPreferredSize(new Dimension(200,70*(partita.getGiocatori().size()+1)));
		add(statsPanel);

		JPanel emptyPanel = new JPanel();
		emptyPanel.setPreferredSize(new Dimension(200,180));
		emptyPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		emptyPanel.setBackground(Color.ORANGE);
		add(emptyPanel);
		
		UIUpdaterDelegate uiUpdaterDelegate = new UIUpdaterDelegate(planciaPanel, statsPanel);
		
		pulsantieraPanel = new PulsantieraPanel(uiUpdaterDelegate, azioniVsTabellone);
		//pulsantieraPanel.setPreferredSize(new Dimension(200,30));
		pulsantieraPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		pulsantieraPanel.setBackground(Color.PINK);
		add(pulsantieraPanel);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setBackground(Color.RED);
	}
	
}
