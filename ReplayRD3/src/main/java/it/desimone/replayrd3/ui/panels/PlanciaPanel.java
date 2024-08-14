package it.desimone.replayrd3.ui.panels;

import it.desimone.replayrd3.ColoreGiocatore;
import it.desimone.replayrd3.Territorio;
import it.desimone.replayrd3.ui.animation.CarrettoComponent;
import it.desimone.replayrd3.ui.animation.DicePanel;
import it.desimone.utils.Configurator;
import it.desimone.utils.ResourceLoader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class PlanciaPanel extends JPanel {
	
	private static final long serialVersionUID = -5576075695785559444L;
	private Image img;

	private Map<Territorio, CarrettoComponent> carretti = new HashMap<Territorio, CarrettoComponent>();
	private DicePanel dicePanel;
	private JLabel labelPartita;
	
	public PlanciaPanel() {
		img = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getPathPlancia(Configurator.selectedResolution));
		loadImage(img);
		setLayout(null);

//		switch (Configurator.selectedResolution) {
//		case SMALL:
//			setPreferredSize(new Dimension(1010, 700));
//			break;
//		case BIG:
//			setSize(1550, 900);
//			break;
//		default:
//			break;
//		}
		
		initCarri();
		initDicePanel();
		initLabelPartita();
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}

	private void loadImage(Image img) {
		try {
			MediaTracker track = new MediaTracker(this);
			track.addImage(img, 0);
			track.waitForID(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		AffineTransform at = new AffineTransform();

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, this);

	}
	
	public void paint(Graphics g) {
		try{
			super.paint(g);
		}catch(java.lang.ArrayIndexOutOfBoundsException aie){
		}
	}
	
	private void initCarri(){
		StatePosition statePosition = new StatePosition(Configurator.selectedResolution);
		Territorio[] territori = Territorio.values();
		
		for (Territorio territorio: territori){
			Point posizioneCarro = statePosition.getPosition(territorio);
			if (posizioneCarro != null){
				CarrettoComponent carro = new CarrettoComponent(ResourceLoader.getPathCarro("bianco"), 0);
				carro.setBounds(((Double)posizioneCarro.getX()).intValue(), ((Double)posizioneCarro.getY()).intValue(), 50, 50);
				add(carro);
				carretti.put(territorio, carro);
			}
		}
	}
	
	private void initDicePanel(){
		dicePanel = new DicePanel();
		switch (Configurator.selectedResolution) {
		case BIG:
			dicePanel.setBounds(825, 673, 75, 115);	
			break;
		case SMALL:
			dicePanel.setBounds(690, 565, 75, 115);	
			break;
		default:
			break;
		}
		dicePanel.setVisible(false);
		add(dicePanel);
	}
	
	private void initLabelPartita(){
		labelPartita = new JLabel();
		labelPartita.setForeground(Color.WHITE);
		switch (Configurator.selectedResolution) {
		case BIG:
			labelPartita.setBounds(793, 10, 250, 20);	
			break;
		case SMALL:
			labelPartita.setBounds(690, 10, 250, 20);	
			break;
		default:
			break;
		}
		labelPartita.setVisible(false);
		add(labelPartita);
	}
	
	public void setColor(Territorio territorio, ColoreGiocatore color){
		CarrettoComponent carrettoComponent = carretti.get(territorio);
		if (carrettoComponent != null){
			carrettoComponent.setColor(color);
		}
		
	}
	public void setArmate(Territorio territorio, Integer armate){
		CarrettoComponent carrettoComponent = carretti.get(territorio);
		if (carrettoComponent != null){
			carrettoComponent.setNumeroCarri(armate.toString());
		}
	}
	public void setColorEArmate(Territorio territorio, ColoreGiocatore color, Integer armate){
		setColor(territorio, color);
		setArmate(territorio, armate);
	}

	public DicePanel getDicePanel() {
		return dicePanel;
	}

	public void setDicePanel(DicePanel dicePanel) {
		this.dicePanel = dicePanel;
	}
		
	public void setNomePartita(String nomePartita) {
		labelPartita.setText(nomePartita);
		labelPartita.setVisible(true);
	}
}
