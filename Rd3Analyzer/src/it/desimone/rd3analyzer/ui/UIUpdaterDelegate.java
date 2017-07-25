package it.desimone.rd3analyzer.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import it.desimone.rd3analyzer.ColoreGiocatore;
import it.desimone.rd3analyzer.Tabellone;
import it.desimone.rd3analyzer.Tabellone.TabellinoTerritorio;
import it.desimone.rd3analyzer.Territorio;
import it.desimone.rd3analyzer.azioni.Attacco;
import it.desimone.rd3analyzer.azioni.Azione;
import it.desimone.rd3analyzer.azioni.GiocoTris;
import it.desimone.rd3analyzer.azioni.Invasione;
import it.desimone.rd3analyzer.azioni.RicezioneCarta;
import it.desimone.rd3analyzer.azioni.Rinforzo;
import it.desimone.rd3analyzer.azioni.Spostamento;
import it.desimone.rd3analyzer.ui.animation.CarrettoAnimationPanel;
import it.desimone.rd3analyzer.ui.animation.DicePanel;
import it.desimone.rd3analyzer.ui.animation.MyComponent;
import it.desimone.rd3analyzer.ui.animation.RinforzoLabel;
import it.desimone.rd3analyzer.ui.panels.PlanciaPanel;
import it.desimone.rd3analyzer.ui.panels.PulsantieraPanel;
import it.desimone.rd3analyzer.ui.panels.StatePosition;
import it.desimone.rd3analyzer.ui.panels.StatsPanel;
import it.desimone.rd3analyzer.ui.panels.StatePosition.PositionAndRotation;
import it.desimone.utils.Configurator;
import it.desimone.utils.ResourceLoader;

public class UIUpdaterDelegate {
	
	private PlanciaPanel planciaPanel;
	private StatsPanel statsPanel;
	private PulsantieraPanel pulsantieraPanel;
	private StatePosition statePosition = new StatePosition(Configurator.selectedResolution);
	
	public UIUpdaterDelegate(PlanciaPanel planciaPanel, StatsPanel statsPanel) {
		super();
		this.planciaPanel = planciaPanel;
		this.statsPanel = statsPanel;
	}

	public PlanciaPanel getPlanciaPanel() {
		return planciaPanel;
	}

	public void setPlanciaPanel(PlanciaPanel planciaPanel) {
		this.planciaPanel = planciaPanel;
	}

	public StatsPanel getStatsPanel() {
		return statsPanel;
	}

	public void setStatsPanel(StatsPanel statsPanel) {
		this.statsPanel = statsPanel;
	}
	public PulsantieraPanel getPulsantieraPanel() {
		return pulsantieraPanel;
	}

	public void setPulsantieraPanel(PulsantieraPanel pulsantieraPanel) {
		this.pulsantieraPanel = pulsantieraPanel;
	}

	public void settaAnimazione(int step){
		this.pulsantieraPanel.settaAnimazione(step);
	}
	
	public void refreshTabellone(Tabellone tabellone){

		for (Territorio territorio: Territorio.values()){
			TabellinoTerritorio tabellinoTerritorio = tabellone.getTabellinoTerritorio(territorio);
			if(tabellinoTerritorio != null && tabellinoTerritorio.getGiocatore() != null){
				planciaPanel.setColorEArmate(territorio, tabellinoTerritorio.getGiocatore(), tabellone.getArmateTerritorio(territorio));
			}
		}
		planciaPanel.repaint();
			
		for (ColoreGiocatore giocatore: tabellone.getTabelliniGiocatori().keySet()){
			statsPanel.aggiornaTabellini(giocatore, tabellone.getTabellinoGiocatore(giocatore));
		}
		statsPanel.settaTimeELogId(tabellone.getTime(), tabellone.getLogId(), tabellone.getNumeroTurno());
		statsPanel.repaint();
	}
	
	public void effettuaInvasione(Tabellone tabellone, Invasione invasione){
		Territorio territorioInvaso = invasione.getTerritorioInvaso();
		Territorio territorioInvasore = invasione.getTerritorioInvasore();				
		ColoreGiocatore invasore = invasione.getGiocatoreCheAgisce();
		CarrettoAnimationPanel carrettoAnimationPanel = CarrettoAnimationPanel.getInstance(statePosition.getPosition(territorioInvasore), statePosition.getPosition(territorioInvaso), invasore, invasione.getNumeroDiArmateInvasori());
		planciaPanel.add(carrettoAnimationPanel);
		while(carrettoAnimationPanel.isRunning()){
		}
		planciaPanel.setColorEArmate(territorioInvaso, invasione.getGiocatoreCheAgisce(), tabellone.getArmateTerritorio(territorioInvaso));
		planciaPanel.setArmate(territorioInvasore, tabellone.getArmateTerritorio(territorioInvasore));
		
		planciaPanel.remove(carrettoAnimationPanel);
		planciaPanel.repaint();
			
		//Li aggiorno tutti perchè quando arrivo qui non so già più a chi apparteneva lo stato perso
		for (ColoreGiocatore giocatore: tabellone.getTabelliniGiocatori().keySet()){
			statsPanel.aggiornaTabellini(giocatore, tabellone.getTabellinoGiocatore(giocatore));
		}
		statsPanel.settaTimeELogId(tabellone.getTime(), tabellone.getLogId(), tabellone.getNumeroTurno());
		statsPanel.repaint();
	}
	
	public void effettuaSpostamento(Tabellone tabellone, Spostamento spostamento){
		Territorio territorioDiProvenienza = spostamento.getTerritorioDiProvenienza();
		Territorio territorioDiArrivo = spostamento.getTerritorioDiArrivo();
		ColoreGiocatore spostatore = spostamento.getGiocatoreCheAgisce();
		CarrettoAnimationPanel carrettoAnimationPanel = CarrettoAnimationPanel.getInstance(statePosition.getPosition(territorioDiProvenienza), statePosition.getPosition(territorioDiArrivo), spostatore, spostamento.getNumeroDiArmateSpostate());
		planciaPanel.add(carrettoAnimationPanel);
		while(carrettoAnimationPanel.isRunning()){
		}
		planciaPanel.setArmate(territorioDiProvenienza, tabellone.getArmateTerritorio(territorioDiProvenienza));
		planciaPanel.setArmate(territorioDiArrivo, tabellone.getArmateTerritorio(territorioDiArrivo));
		planciaPanel.remove(carrettoAnimationPanel);
		planciaPanel.repaint();
		
		statsPanel.settaTimeELogId(tabellone.getTime(), tabellone.getLogId(), tabellone.getNumeroTurno());
		statsPanel.repaint();
	}

	public void effettuaRinforzo(Tabellone tabellone, Rinforzo rinforzo){
		Territorio territorioRinforzato = rinforzo.getTerritorioRinforzato();			
		RinforzoLabel rinforzoLabel = new RinforzoLabel(rinforzo.getNumeroDiRinforzi().toString());
		Point posizioneCarro = statePosition.getPosition(territorioRinforzato);
		rinforzoLabel.setBounds(((Double)posizioneCarro.getX()).intValue()+30, ((Double)posizioneCarro.getY()).intValue(), 30, 30);
		rinforzoLabel.setVisible(true);
		planciaPanel.add(rinforzoLabel);
		while(/*++azioniCounter > 87 &&*/ rinforzoLabel.isRunning()){
		}
		planciaPanel.setColorEArmate(territorioRinforzato, rinforzo.getGiocatoreCheAgisce(), tabellone.getArmateTerritorio(territorioRinforzato));
		
		planciaPanel.repaint();
		planciaPanel.remove(rinforzoLabel);
			
		ColoreGiocatore giocatore = rinforzo.getGiocatoreCheAgisce();
		statsPanel.aggiornaTabellini(giocatore, tabellone.getTabellinoGiocatore(giocatore));
		statsPanel.settaTimeELogId(tabellone.getTime(), tabellone.getLogId(), tabellone.getNumeroTurno());
		statsPanel.repaint();
	}
	
	public void effettuaAttacco(Tabellone tabellone, Attacco attacco){
		DicePanel dicePanel = planciaPanel.getDicePanel();	
		//dicePanel.setDadiAttacco(attacco.getDadiAttacco());
		//dicePanel.setDadiDifesa(attacco.getDadiDifesa());
		
		Territorio territorioAttaccante = attacco.getTerritorioAttaccante();
		Territorio territorioAttaccato  = attacco.getTerritorioAttaccato();
		
		ColoreGiocatore giocatoreAttaccato = tabellone.getTabellinoTerritorio(territorioAttaccato).getGiocatore();
		dicePanel.setDadiAttacco(attacco.getDadiAttacco(), attacco.getGiocatoreCheAgisce().getColore());
		dicePanel.setDadiDifesa(attacco.getDadiDifesa(), giocatoreAttaccato.getColore());
		dicePanel.setVisible(true);

		PositionAndRotation positionAndRotation = statePosition.getPosizioneERotazione(attacco.getTerritorioAttaccante(), attacco.getTerritorioAttaccato());
		MyComponent freccia = new MyComponent(ResourceLoader.getPathFreccia("Rossa"),positionAndRotation.getRotationAngle());
    	freccia.setBounds(((Double)positionAndRotation.getPosition().getX()).intValue(),((Double)positionAndRotation.getPosition().getY()).intValue(), 50, 50);
    	planciaPanel.add(freccia);
		planciaPanel.repaint();
		try {
			Thread.sleep(Configurator.ANIMATION_TIME*2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dicePanel.setVisible(false);
		planciaPanel.remove(freccia);
		
		planciaPanel.setArmate(territorioAttaccante, tabellone.getArmateTerritorio(territorioAttaccante));
		planciaPanel.setArmate(territorioAttaccato, tabellone.getArmateTerritorio(territorioAttaccato));
		planciaPanel.repaint();
		
		ColoreGiocatore giocatore = attacco.getGiocatoreCheAgisce();
		statsPanel.aggiornaTabellini(giocatore, tabellone.getTabellinoGiocatore(giocatore));
		statsPanel.settaTimeELogId(tabellone.getTime(), tabellone.getLogId(), tabellone.getNumeroTurno());
		statsPanel.repaint();
	}

	public void effettuaRicezioneCarta(Tabellone tabellone, RicezioneCarta ricezioneCarta){
	    PopupFactory factory = PopupFactory.getSharedInstance();
	    JComponent labelCarta = new JLabel("Il giocatore "+ricezioneCarta.getGiocatoreCheAgisce()+" riceve la carta "+(ricezioneCarta.getCartaRicevuta()==null?"":ricezioneCarta.getCartaRicevuta().getNomeTerritorio()));
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-labelCarta.getWidth())/2;
		int y = (screen.height-labelCarta.getHeight())/2;
		Popup popup = factory.getPopup(planciaPanel,labelCarta , x, y);
		popup.show();    
		try {
			Thread.sleep(Configurator.ANIMATION_TIME*2);
			popup.hide();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		ColoreGiocatore giocatore = ricezioneCarta.getGiocatoreCheAgisce();
		statsPanel.aggiornaTabellini(giocatore, tabellone.getTabellinoGiocatore(giocatore));
		statsPanel.settaTimeELogId(tabellone.getTime(), tabellone.getLogId(), tabellone.getNumeroTurno());
		statsPanel.repaint();
	}
	
	public void effettuaGiocoTris(Tabellone tabellone, GiocoTris giocoTris){
	    PopupFactory factory = PopupFactory.getSharedInstance();
	    JComponent labelTris = new JLabel("Il giocatore "+giocoTris.getGiocatoreCheAgisce()+" gioca il tris "+Arrays.toString(giocoTris.getCarteTris())+" da "+giocoTris.getValoreTris()+" armate");
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-labelTris.getWidth())/2;
		int y = (screen.height-labelTris.getHeight())/2;
		Popup popup = factory.getPopup(planciaPanel,labelTris , x, y);
		popup.show();    
		try {
			Thread.sleep(Configurator.ANIMATION_TIME*2);
			popup.hide();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		ColoreGiocatore giocatore = giocoTris.getGiocatoreCheAgisce();
		statsPanel.aggiornaTabellini(giocatore, tabellone.getTabellinoGiocatore(giocatore));
		statsPanel.settaTimeELogId(tabellone.getTime(), tabellone.getLogId(), tabellone.getNumeroTurno());
		statsPanel.repaint();
	}

	public void evidenzaGiocatoreCheAGisce(Azione azione){
		statsPanel.evidenziaGiocatore(azione.getGiocatoreCheAgisce());
		statsPanel.repaint();
	}
	
}
