package it.desimone.replayrd3.ui.panels;

import it.desimone.replayrd3.AzioneVsTabellone;
import it.desimone.replayrd3.ColoreGiocatore;
import it.desimone.replayrd3.Partita;
import it.desimone.replayrd3.Tabellone;
import it.desimone.replayrd3.Territorio;
import it.desimone.replayrd3.azioni.Attacco;
import it.desimone.replayrd3.azioni.Azione;
import it.desimone.replayrd3.azioni.GiocoTris;
import it.desimone.replayrd3.azioni.Invasione;
import it.desimone.replayrd3.azioni.RicezioneCarta;
import it.desimone.replayrd3.azioni.Rinforzo;
import it.desimone.replayrd3.azioni.Spostamento;
import it.desimone.replayrd3.ui.UIUpdaterDelegate;
import it.desimone.replayrd3.ui.animation.CarrettoAnimationPanel;
import it.desimone.replayrd3.ui.animation.DicePanel;
import it.desimone.replayrd3.ui.animation.MyComponent;
import it.desimone.replayrd3.ui.animation.RinforzoLabel;
import it.desimone.replayrd3.ui.panels.StatePosition.PositionAndRotation;
import it.desimone.utils.Configurator;
import it.desimone.utils.MyLogger;
import it.desimone.utils.ResourceLoader;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class ThreadReplay extends Thread {
	
	private Partita partita;
	private List<AzioneVsTabellone> azionivsTabellone;
	private PlanciaPanel planciaPanel;
	private UIUpdaterDelegate uiUpdaterDelegate;
	
	private boolean reversed = false;
	private int indexAzioni = 0;
	private volatile boolean threadSuspended;
	private volatile Thread blinker;
	private boolean refreshAll = false;
	
	
	public ThreadReplay(PlanciaPanel planciaPanel, Partita partita){
		this.partita = partita;
		this.planciaPanel = planciaPanel;
	}
	
	public ThreadReplay(UIUpdaterDelegate uiUpdaterDelegate, Partita partita){
		this.partita = partita;
		this.uiUpdaterDelegate = uiUpdaterDelegate;
	}
	
	public ThreadReplay(UIUpdaterDelegate uiUpdaterDelegate, List<AzioneVsTabellone> azionivsTabellone){
		this.azionivsTabellone = azionivsTabellone;
		this.uiUpdaterDelegate = uiUpdaterDelegate;
		blinker = this;
	}
	
	
	 public void run(){
        createAndShowUI3();
     }
	 
    public synchronized void ferma() {
        blinker = null;
        notify();
    }
    
    public synchronized void pauseAndResume(){
    	 threadSuspended = !threadSuspended;
         if (!threadSuspended)
             notify();
    }

	private void createAndShowUI(Partita partita){
		StatePosition statePosition = new StatePosition(Configurator.selectedResolution);
		Tabellone tabellone = new Tabellone(partita);
		
		List<Azione> azioniLog = partita.getAzioniLog();
		//MyLogger.getLogger().info("[INIZIO] - "+azioniLog.size()+" azioni");

		short azioniCounter = 0;
		
		for (Azione azione: azioniLog){
			RinforzoLabel labelTest = null;
		    CarrettoAnimationPanel carrettoAnimationPanel = null; 
			DicePanel dicePanel = null;
			MyComponent freccia = null;
			//azioniCounter++;
			//System.err.println("["+azioniCounter+"]Azione: "+azione.getTipoAzione());
			switch (azione.getTipoAzione()) {
			case ATTACCO:
				Attacco attacco = (Attacco) azione;
				dicePanel = new DicePanel();
				dicePanel.setDadiAttacco(attacco.getDadiAttacco());
				dicePanel.setDadiDifesa(attacco.getDadiDifesa());
				switch (Configurator.selectedResolution) {
				case BIG:
					dicePanel.setBounds(793, 673, 50, 100);	
					break;
				case SMALL:
					dicePanel.setBounds(674, 549, 50, 100);	
					break;
				default:
					break;
				}
				planciaPanel.add(dicePanel);
				PositionAndRotation positionAndRotation = statePosition.getPosizioneERotazione(attacco.getTerritorioAttaccante(), attacco.getTerritorioAttaccato());
		    	freccia = new MyComponent(ResourceLoader.getPathFreccia("Rossa"),positionAndRotation.getRotationAngle());
		    	freccia.setBounds(((Double)positionAndRotation.getPosition().getX()).intValue(),((Double)positionAndRotation.getPosition().getY()).intValue(), 50, 50);
		    	planciaPanel.add(freccia);
				try {
					Thread.sleep(Configurator.ANIMATION_TIME*2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				tabellone.calcolaAttacco(attacco);
				Territorio territorioAttaccante = attacco.getTerritorioAttaccante();
				Territorio territorioAttaccato  = attacco.getTerritorioAttaccato();
				planciaPanel.setArmate(territorioAttaccante, tabellone.getArmateTerritorio(territorioAttaccante));
				planciaPanel.setArmate(territorioAttaccato, tabellone.getArmateTerritorio(territorioAttaccato));
				break;
			case RINFORZO:
				Rinforzo rinforzo = (Rinforzo) azione;
				tabellone.calcolaRinforzo(rinforzo);
				Territorio territorioRinforzato = rinforzo.getTerritorioRinforzato();			
				labelTest = new RinforzoLabel(rinforzo.getNumeroDiRinforzi().toString());
				Point posizioneCarro = statePosition.getPosition(territorioRinforzato);
				labelTest.setBounds(((Double)posizioneCarro.getX()).intValue()+30, ((Double)posizioneCarro.getY()).intValue(), 30, 30);
				//labelTest.setBounds(((Double)posizioneCarro.getX()).intValue()+0, ((Double)posizioneCarro.getY()).intValue(), 30, 30);
				labelTest.setVisible(true);
				planciaPanel.add(labelTest);
				while(/*++azioniCounter > 87 &&*/ labelTest.isRunning()){
				}
				planciaPanel.setColorEArmate(territorioRinforzato, azione.getGiocatoreCheAgisce(), tabellone.getArmateTerritorio(territorioRinforzato));
				break;
			case INVASIONE:
				Invasione invasione = (Invasione) azione;
				tabellone.calcolaInvasione(invasione);
				Territorio territorioInvaso = invasione.getTerritorioInvaso();
				Territorio territorioInvasore = invasione.getTerritorioInvasore();				
				carrettoAnimationPanel = createAnimationPanel(statePosition.getPosition(invasione.getTerritorioInvasore()), statePosition.getPosition(invasione.getTerritorioInvaso()), invasione.getGiocatoreCheAgisce(), invasione.getNumeroDiArmateInvasori());
				planciaPanel.add(carrettoAnimationPanel);
				while(carrettoAnimationPanel.isRunning()){
				}
				planciaPanel.setColorEArmate(territorioInvaso, azione.getGiocatoreCheAgisce(), tabellone.getArmateTerritorio(territorioInvaso));
				planciaPanel.setArmate(territorioInvasore, tabellone.getArmateTerritorio(territorioInvasore));
				break;
			case SPOSTAMENTO:
				Spostamento spostamento = (Spostamento) azione;
				tabellone.calcolaSpostamento(spostamento);
				Territorio territorioDiProvenienza = spostamento.getTerritorioDiProvenienza();
				Territorio territorioDiArrivo = spostamento.getTerritorioDiArrivo();
				carrettoAnimationPanel = createAnimationPanel(statePosition.getPosition(spostamento.getTerritorioDiProvenienza()), statePosition.getPosition(spostamento.getTerritorioDiArrivo()), spostamento.getGiocatoreCheAgisce(), spostamento.getNumeroDiArmateSpostate());
				planciaPanel.add(carrettoAnimationPanel);
				while(carrettoAnimationPanel.isRunning()){
				}
				planciaPanel.setArmate(territorioDiProvenienza, tabellone.getArmateTerritorio(territorioDiProvenienza));
				planciaPanel.setArmate(territorioDiArrivo, tabellone.getArmateTerritorio(territorioDiArrivo));
				break;
			case RICEZIONE_CARTA:
				RicezioneCarta ricezioneCarta = (RicezioneCarta) azione;
				tabellone.calcolaRicezioneCarta(ricezioneCarta);
				
			    PopupFactory factory = PopupFactory.getSharedInstance();
				Popup popup = factory.getPopup(planciaPanel, new JLabel("Il giocatore "+ricezioneCarta.getGiocatoreCheAgisce()+" riceve la carta "+ricezioneCarta.getCartaRicevuta().getNomeTerritorio()), 180, 180);
				popup.show();    
				try {
					Thread.sleep(Configurator.ANIMATION_TIME*2);
					popup.hide();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
				break;
			case GIOCO_TRIS:
				GiocoTris giocoTris = (GiocoTris) azione;
				tabellone.calcolaGiocoTris(giocoTris);
				
			    PopupFactory factory2 = PopupFactory.getSharedInstance();
				Popup popup2 = factory2.getPopup(planciaPanel, new JLabel("Il giocatore "+giocoTris.getGiocatoreCheAgisce()+" gioca il tris "+Arrays.toString(giocoTris.getCarteTris())+" da "+giocoTris.getValoreTris()+" armate"), 180, 180);
				popup2.show();    
				try {
					Thread.sleep(Configurator.ANIMATION_TIME*10);
					popup2.hide();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}


			planciaPanel.repaint();
			if (dicePanel != null){
				planciaPanel.remove(dicePanel);
			}
			if (freccia != null){
				planciaPanel.remove(freccia);
			}
			if (labelTest != null){
				planciaPanel.remove(labelTest);
			}
			if (carrettoAnimationPanel != null){
				planciaPanel.remove(carrettoAnimationPanel);
			}
						
		}
		MyLogger.getLogger().info("[FINE]");
		
	}
	
	private void createAndShowUI2(){
		Tabellone tabellone = new Tabellone(partita);
		
		List<Azione> azioniLog = partita.getAzioniLog();
		//MyLogger.getLogger().info("[INIZIO] - "+azioniLog.size()+" azioni");
		for (Azione azione: azioniLog){
			switch (azione.getTipoAzione()) {
			case ATTACCO:
				Attacco attacco = (Attacco) azione;
				tabellone.calcolaAttacco(attacco);
				uiUpdaterDelegate.effettuaAttacco(tabellone, attacco);
				break;
			case RINFORZO:
				Rinforzo rinforzo = (Rinforzo) azione;
				tabellone.calcolaRinforzo(rinforzo);
				uiUpdaterDelegate.effettuaRinforzo(tabellone, rinforzo);
				break;
			case INVASIONE:
				Invasione invasione = (Invasione) azione;
				tabellone.calcolaInvasione(invasione);
				uiUpdaterDelegate.effettuaInvasione(tabellone, invasione);
				break;
			case SPOSTAMENTO:
				Spostamento spostamento = (Spostamento) azione;
				tabellone.calcolaSpostamento(spostamento);
				uiUpdaterDelegate.effettuaSpostamento(tabellone, spostamento);
				break;
			case RICEZIONE_CARTA:
				RicezioneCarta ricezioneCarta = (RicezioneCarta) azione;
				tabellone.calcolaRicezioneCarta(ricezioneCarta);
				uiUpdaterDelegate.effettuaRicezioneCarta(tabellone,ricezioneCarta);
				break;
			case GIOCO_TRIS:
				GiocoTris giocoTris = (GiocoTris) azione;
				tabellone.calcolaGiocoTris(giocoTris);
				uiUpdaterDelegate.effettuaGiocoTris(tabellone,giocoTris);
				break;
			default:
				break;
			}	
			
            if (threadSuspended) {
                synchronized(this) {
                    while (threadSuspended){
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
            }
		}
		//MyLogger.getLogger().info("[FINE]");
		
	}
	
    public void start() {
        blinker = new Thread(this);
        blinker.start();
    }
	
	private void createAndShowUI3(){
		Thread thisThread = Thread.currentThread();
		while (indexAzioni >=0 && indexAzioni < azionivsTabellone.size() && blinker == thisThread){
//			Azione azione = (Azione) azionivsTabellone[indexAzioni][0];
//			Tabellone tabellone = (Tabellone) azionivsTabellone[indexAzioni][1];	
			AzioneVsTabellone azioneVsTabellone = azionivsTabellone.get(indexAzioni);
			Azione azione = azioneVsTabellone.getAzione();
			Tabellone tabellone = azioneVsTabellone.getTabellone();
			
			if (refreshAll){
				uiUpdaterDelegate.refreshTabellone(tabellone);
				refreshAll = false;
			}
			
			uiUpdaterDelegate.settaAnimazione(indexAzioni);
			gestisciAzione(azione, tabellone);
			if (reversed){
				indexAzioni--;
			}else{
				indexAzioni++;
			}	
            if (threadSuspended) {
                synchronized(this) {
                    while (threadSuspended){
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
            }
			
		}

	}
	
	
	private void gestisciAzione(Azione azione, Tabellone tabellone){
		
		uiUpdaterDelegate.evidenzaGiocatoreCheAGisce(azione);
		switch (azione.getTipoAzione()) {
		case ATTACCO:
			Attacco attacco = (Attacco) azione;
			uiUpdaterDelegate.effettuaAttacco(tabellone, attacco);
			break;
		case RINFORZO:
			Rinforzo rinforzo = (Rinforzo) azione;
			uiUpdaterDelegate.effettuaRinforzo(tabellone, rinforzo);
			break;
		case INVASIONE:
			Invasione invasione = (Invasione) azione;
			uiUpdaterDelegate.effettuaInvasione(tabellone, invasione);
			break;
		case SPOSTAMENTO:
			Spostamento spostamento = (Spostamento) azione;
			uiUpdaterDelegate.effettuaSpostamento(tabellone, spostamento);
			break;
		case RICEZIONE_CARTA:
			RicezioneCarta ricezioneCarta = (RicezioneCarta) azione;
			uiUpdaterDelegate.effettuaRicezioneCarta(tabellone,ricezioneCarta);
			break;
		case GIOCO_TRIS:
			GiocoTris giocoTris = (GiocoTris) azione;
			uiUpdaterDelegate.effettuaGiocoTris(tabellone,giocoTris);
			break;
		default:
			break;
		}					
	}
	
	
	  private static CarrettoAnimationPanel createAnimationPanel(Point fromPoint, Point toPoint, ColoreGiocatore coloreGiocatore, Short numeroCarri){
		  double xFrom = fromPoint.getX();
		  double yFrom = fromPoint.getY();
		  double xTo = toPoint.getX();
		  double yTo = toPoint.getY();
		  
		  int xLeftCorner = ((Double)Math.min(xFrom, xTo)).intValue();
		  int yTopCorner  = ((Double)Math.min(yFrom, yTo)).intValue();
		  
		  int widthPanel = ((Double) Math.max(50.0d, Math.abs(xFrom - xTo))).intValue()+50;
		  int heigthPanel = ((Double) Math.max(50.0d, Math.abs(yFrom - yTo))).intValue()+50;

		  CarrettoAnimationPanel panelScorri = new CarrettoAnimationPanel(new Point2D.Double((xFrom - xLeftCorner),(yFrom - yTopCorner)), new Point2D.Double((xTo - xLeftCorner),(yTo - yTopCorner)), coloreGiocatore, numeroCarri);
		  panelScorri.setBounds(xLeftCorner, yTopCorner, widthPanel, heigthPanel);
		  //panelScorri.setBorder(BorderFactory.createBevelBorder(1));
		  return panelScorri;
	  }

	public void reverseAnimation(){
		reversed = !reversed;
	}

	public int getIndexAzioni() {
		return indexAzioni;
	}

	public void setIndexAzioni(int indexAzioni) {
		this.indexAzioni = indexAzioni;
		refreshAll = true;
	}
	
}
