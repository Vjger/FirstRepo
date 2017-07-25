package it.desimone.rd3analyzer.ui.panels;

import it.desimone.rd3analyzer.AzioneVsTabellone;
import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.Tabellone;
import it.desimone.rd3analyzer.azioni.Attacco;
import it.desimone.rd3analyzer.azioni.Azione;
import it.desimone.rd3analyzer.azioni.GiocoTris;
import it.desimone.rd3analyzer.azioni.Invasione;
import it.desimone.rd3analyzer.azioni.RicezioneCarta;
import it.desimone.rd3analyzer.azioni.Rinforzo;
import it.desimone.rd3analyzer.azioni.Spostamento;
import it.desimone.rd3analyzer.ui.UIUpdaterDelegate;
import it.desimone.utils.Configurator;
import it.desimone.utils.ResourceLoader;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PulsantieraPanel extends JPanel {

	private ThreadReplay threadReplay;
	
	private JButton playPauseButton;
	private JButton stopButton;
	private JButton forwardButton;
	private JButton backwardButton;
	
	private JSlider sliderTempoAnimazione;
	private JSlider sliderAnimazione;
	
	private UIUpdaterDelegate uiUpdaterDelegate;
	//private final Object[][] azioniVsTabellone;
	private final List<AzioneVsTabellone> azioniVsTabellone;
	
	Icon iconPlay = new ImageIcon(ResourceLoader.getPathPulsante("play"));
	Icon iconStop = new ImageIcon(ResourceLoader.getPathPulsante("stop"));	
	Icon iconPause = new ImageIcon(ResourceLoader.getPathPulsante("pause"));
	Icon iconForward = new ImageIcon(ResourceLoader.getPathPulsante("forward"));
	Icon iconBackward = new ImageIcon(ResourceLoader.getPathPulsante("backward"));
	
	private TitledBorder titleBorderSlider;
	private int velocitaReplay = 1;
	
//	public PulsantieraPanel(final UIUpdaterDelegate uiUpdaterDelegate, final Partita partita){
//		
//		uiUpdaterDelegate.setPulsantieraPanel(this);
//		this.uiUpdaterDelegate = uiUpdaterDelegate;
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		
//		azioniVsTabellone = getAzioniVsTabellone(partita);
//		
//		sliderTempoAnimazione = createSliderTempoAnimazione();
//
//		
//		add(createTastiPanel());
//		
//		sliderAnimazione = createSliderAnimazione();
//		add(sliderAnimazione);
//	}
	
	
	public PulsantieraPanel(UIUpdaterDelegate uiUpdaterDelegate, List<AzioneVsTabellone> azioniVsTabellone){
		
		uiUpdaterDelegate.setPulsantieraPanel(this);
		this.uiUpdaterDelegate = uiUpdaterDelegate;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//setLayout(new BorderLayout());
		
		this.azioniVsTabellone = azioniVsTabellone;
		threadReplay = new ThreadReplay(uiUpdaterDelegate, azioniVsTabellone);
		threadReplay.setIndexAzioni(0);
		
		sliderTempoAnimazione = createSliderTempoAnimazione();

		
		add(createTastiPanel());
		
		titleBorderSlider = BorderFactory.createTitledBorder("Selezione turno [1X]");
		sliderAnimazione = createSliderAnimazione();
		add(sliderAnimazione);
	}
	
	private JPanel createTastiPanel(){
		JPanel tastiPanel = new JPanel();
		tastiPanel.setLayout(new FlowLayout());
		
		backwardButton = createBackwardButton();
		tastiPanel.add(backwardButton);
		
		stopButton = createStopButton();
		tastiPanel.add(stopButton);
		
		playPauseButton = createPlayPauseButton();
		tastiPanel.add(playPauseButton);
		
		forwardButton = createForwardButton();
		tastiPanel.add(forwardButton);
		
		return tastiPanel;
	}
	
	private JButton createPlayPauseButton(){
		JButton playPauseButton = new JButton(iconPlay);
		playPauseButton.setPreferredSize(new Dimension(27,27));
		playPauseButton.setBorder(null);
		playPauseButton.addActionListener(createPlayActionListener());
		return playPauseButton;
	}
	
	private JButton createStopButton(){
		JButton stopButton = new JButton(iconStop);
		stopButton.setPreferredSize(new Dimension(27,27));
		stopButton.setBorder(null);
		stopButton.addActionListener(createStopActionListener());
		return stopButton;
	}
	
	private JButton createForwardButton(){
		JButton forwardButton = new JButton(iconForward);
		forwardButton.setPreferredSize(new Dimension(27,27));
		forwardButton.setBorder(null);
		forwardButton.addActionListener(createForwardActionListener());
		return forwardButton;
	}
	
	private JButton createBackwardButton(){
		JButton backwardButton = new JButton(iconBackward);
		backwardButton.setPreferredSize(new Dimension(27,27));
		backwardButton.setBorder(null);
		backwardButton.addActionListener(createBackwardActionListener());
		return backwardButton;
	}
	
	private ActionListener createStopActionListener(){
		ActionListener stopActionListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				threadReplay.ferma();
				
				for (ActionListener actionListener: playPauseButton.getActionListeners()){
					playPauseButton.removeActionListener(actionListener);
				}

				playPauseButton.addActionListener(createPlayActionListener());
				playPauseButton.setIcon(iconPlay);
				
				
				sliderTempoAnimazione.setValue(0);
				sliderTempoAnimazione.setEnabled(false);
				sliderTempoAnimazione.repaint();
			}
		};
		return stopActionListener;
	}
	
	private ActionListener createPauseResumeActionListener(){
		ActionListener pauseResumeActionListener = new ActionListener(){
			private boolean isPause = true;
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				threadReplay.pauseAndResume();	
				JButton playButton = (JButton) actionEvent.getSource();
				if (isPause){
					playButton.setIcon(iconPlay);
				}else{
					playButton.setIcon(iconPause);
				}
				isPause = !isPause;
			}
		};
		return pauseResumeActionListener;
	}
	
	private ActionListener createPlayActionListener(){
		ActionListener playActionListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				//threadReplay = new ThreadReplay(uiUpdaterDelegate, azioniVsTabellone);
				threadReplay.start();
				
				JButton playButton = (JButton) actionEvent.getSource();
				playButton.removeActionListener(this);
				playButton.addActionListener(createPauseResumeActionListener());
				playButton.setIcon(iconPause);
				
				sliderTempoAnimazione.setEnabled(true);
				sliderTempoAnimazione.repaint();
			}
		};
		return playActionListener;
	}
	
	private ActionListener createForwardActionListener(){
		ActionListener forwardActionListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				velocitaReplay = Math.min(20, velocitaReplay+1);
				//Configurator.ANIMATION_TIME = Math.max(100,Configurator.ANIMATION_TIME - 250);
				Configurator.ANIMATION_TIME = Configurator.DEFAULT_ANIMATION_TIME / velocitaReplay;
				
				titleBorderSlider.setTitle("Selezione turno ["+velocitaReplay+"X]");
			}
		};
		return forwardActionListener;
	}
	
	private ActionListener createBackwardActionListener(){
		ActionListener backwardActionListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				velocitaReplay = Math.max(1, velocitaReplay-1);

				//Configurator.ANIMATION_TIME += 250;
				Configurator.ANIMATION_TIME = Configurator.DEFAULT_ANIMATION_TIME / velocitaReplay;
				
				titleBorderSlider.setTitle("Selezione turno ["+velocitaReplay+"X]");

			}
		};
		return backwardActionListener;
	}
	
	private Object[][] getAzioniVsTabellone(Partita partita){
		Tabellone tabellone = new Tabellone(partita);
		List<Azione> azioniLog = partita.getAzioniLog();

		Object[][] azionivsTabellone = new Object[azioniLog.size()][2];
		
		int index=0;
		for (Azione azione: azioniLog){
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
			azionivsTabellone[index][0] = azione;
			azionivsTabellone[index][1] = new Tabellone(tabellone);
			index++;
		}
		
		return azionivsTabellone;
	}
	
	private JSlider createSliderTempoAnimazione(){
		JSlider slider = new JSlider(0,5);
		slider.setOrientation(SwingConstants.HORIZONTAL);
		slider.setPaintTicks(true);
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				JSlider slider = (JSlider) changeEvent.getSource();
				int index = slider.getValue();
				Configurator.ANIMATION_TIME = 100+(index*500);
			}
		});
		TitledBorder titleBorder = BorderFactory.createTitledBorder("Velocità Animazione");
		slider.setBorder(titleBorder);
		return slider;
	}
	
	private JSlider createSliderAnimazione(){
		//JSlider slider = new JSlider(0,azioniVsTabellone.length-1);
		JSlider slider = new JSlider(0,azioniVsTabellone.size()-1);
		slider.setValue(0);
		slider.setOrientation(SwingConstants.HORIZONTAL);
//		slider.addChangeListener(new ChangeListener(){
//			@Override
//			public void stateChanged(ChangeEvent changeEvent) {
//				JSlider slider = (JSlider) changeEvent.getSource();
//				int index = slider.getValue();
//				threadReplay.setIndexAzioni(index);
//			}
//		});
		
		
//		slider.addMouseListener(new MouseListener() {		
//			@Override
//			public void mouseReleased(MouseEvent mouseEvent) {
//			}		
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//				// TODO Auto-generated method stub			
//			}
//			@Override
//			public void mouseExited(MouseEvent arg0) {
//				// TODO Auto-generated method stub			
//			}
//			@Override
//			public void mouseEntered(MouseEvent arg0) {
//				// TODO Auto-generated method stub			
//			}
//			@Override
//			public void mouseClicked(MouseEvent mouseEvent) {
//			}
//		});
		
		slider.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDragged(MouseEvent mouseEvent) {
				JSlider slider = (JSlider) mouseEvent.getSource();
				int index = slider.getValue();
				threadReplay.setIndexAzioni(index);	
			}
		});
		

		slider.setBorder(titleBorderSlider);
		//slider.setEnabled(false);
		return slider;
	}
	
	public void settaAnimazione(int step){
		sliderAnimazione.setValue(step);
	}
}
