package it.desimone.rd3analyzer.ui.panels;

import it.desimone.rd3analyzer.ColoreGiocatore;
import it.desimone.rd3analyzer.Giocatore;
import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.Tabellone.TabellinoGiocatore;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class StatsPanel extends JPanel {

	private Map<ColoreGiocatore, TabellinoPanel> tabelliniMap = new HashMap<ColoreGiocatore, TabellinoPanel>();
	private Map<ColoreGiocatore, ObiettivoPanel> obiettiviMap = new HashMap<ColoreGiocatore, ObiettivoPanel>();
	
	private JLabel labelTime = new JLabel();
	private JLabel labelLogId = new JLabel();
	
	public StatsPanel(Partita partita){
		//setPreferredSize(new Dimension(200,400));
		setLayout(new GridLayout(partita.getGiocatori().size()+1, 2));
		labelTime.setHorizontalAlignment(SwingConstants.CENTER);
		labelTime.setVerticalAlignment(SwingConstants.CENTER);
		labelTime.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		labelLogId.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogId.setVerticalAlignment(SwingConstants.CENTER);
		labelLogId.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		add(labelTime);
		add(labelLogId);
		for (Giocatore giocatore: partita.getGiocatori()){
			TabellinoPanel tabellinoPanel = new TabellinoPanel(giocatore);
			tabelliniMap.put(giocatore.getColoreGiocatore(), tabellinoPanel);
			add(tabellinoPanel);
			ObiettivoPanel obiettivoPanel = new ObiettivoPanel(giocatore);
			obiettiviMap.put(giocatore.getColoreGiocatore(), obiettivoPanel);
			add(obiettivoPanel);
		}

		//setBackground(Color.BLUE);
	}
	
	public void aggiornaTabellini(ColoreGiocatore coloreGiocatore, TabellinoGiocatore tabellinoGiocatore){
		TabellinoPanel tabellinoPanel = tabelliniMap.get(coloreGiocatore);
		
		tabellinoPanel.setNumeroCarri(tabellinoGiocatore.getNumeroArmate());
		tabellinoPanel.setNumeroCarte(tabellinoGiocatore.getCarte().size());
		tabellinoPanel.setNumeroTerritori(tabellinoGiocatore.getTerritori().size());
		tabellinoPanel.setNumeroRinforzi(tabellinoGiocatore.getNumeroRinforzi());
		
		ObiettivoPanel obiettivoPanel = obiettiviMap.get(coloreGiocatore);
		obiettivoPanel.setPunti(tabellinoGiocatore.getNumeroPunti());
	}
	
	public void evidenziaGiocatore(ColoreGiocatore coloreGiocatore){

		for (ColoreGiocatore giocatore: tabelliniMap.keySet()){
			TabellinoPanel tabellinoPanel = tabelliniMap.get(giocatore);
			if (giocatore.equals(coloreGiocatore)){
				tabellinoPanel.setBackground(Color.CYAN);
			}else{
				tabellinoPanel.setBackground(null);
			}
		}
	}
	
	public void settaTimeELogId(String time, Integer logId, Integer numeroTurno){
		//labelTime.setText(time);
		labelTime.setText(String.valueOf(numeroTurno));
		labelLogId.setText(String.valueOf(logId));
	}
	
}
