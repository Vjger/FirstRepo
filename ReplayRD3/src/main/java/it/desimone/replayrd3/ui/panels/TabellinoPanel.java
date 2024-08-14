package it.desimone.replayrd3.ui.panels;

import it.desimone.replayrd3.Giocatore;
import it.desimone.utils.ResourceLoader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class TabellinoPanel extends JPanel {
	
	private Giocatore giocatore;
	private Image img;
	
	private JLabel labelCarri, labelTerritori, labelRinforzi, labelCarte;
	
	public TabellinoPanel(Giocatore giocatore){
		this.giocatore = giocatore;
			
		img = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getPathCarroBig(giocatore.getColoreGiocatore()));
		loadImage(img);
		setLayout(null);
		setToolTipText(giocatore.getNickname());

		setPreferredSize(new Dimension(70,70));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		labelCarri = new JLabel("0");
		labelCarri.setBackground(Color.WHITE);
		labelCarri.setOpaque(true);
		labelCarri.setBounds(0, 5, 22, 15);
		labelCarri.setHorizontalAlignment(SwingConstants.CENTER);
		labelCarri.setToolTipText("Armate");
		add(labelCarri);
		
		labelTerritori = new JLabel("0");
		labelTerritori.setBackground(Color.WHITE);
		labelTerritori.setOpaque(true);
		labelTerritori.setBounds(getPreferredSize().width-23, 5, 22, 15);
		labelTerritori.setHorizontalAlignment(SwingConstants.CENTER);
		labelTerritori.setToolTipText("Territori");
		add(labelTerritori);
		
		labelRinforzi = new JLabel("0");
		labelRinforzi.setBackground(Color.WHITE);
		labelRinforzi.setOpaque(true);
		labelRinforzi.setBounds(0,getPreferredSize().height-22, 22, 15);
		labelRinforzi.setHorizontalAlignment(SwingConstants.CENTER);
		labelRinforzi.setToolTipText("Rinforzi");
		add(labelRinforzi);
		
		labelCarte = new JLabel("0");
		labelCarte.setBackground(Color.WHITE);
		labelCarte.setOpaque(true);
		labelCarte.setBounds(getPreferredSize().width-23,getPreferredSize().height-22, 22, 15);
		labelCarte.setHorizontalAlignment(SwingConstants.CENTER);
		labelCarte.setToolTipText("Carte");
		add(labelCarte);
	}
	

	public Giocatore getGiocatore() {
		return giocatore;
	}

	public void setGiocatore(Giocatore giocatore) {
		this.giocatore = giocatore;
	}

	public void setNumeroCarri(Integer carri){
		labelCarri.setText(String.valueOf(carri));
	}
	public void setNumeroRinforzi(Integer rinforzi){
		labelRinforzi.setText(String.valueOf(rinforzi));
	}
	public void setNumeroTerritori(Integer territori){
		labelTerritori.setText(String.valueOf(territori));
	}
	public void setNumeroCarte(Integer carte){
		labelCarte.setText(String.valueOf(carte));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		AffineTransform at = new AffineTransform();
		
        // 4. translate it to the center of the component
        //at.translate(getWidth() / 2, getHeight() / 2);
        at.translate(img.getWidth(null)/1.6, getHeight() / 2);

        // 1. translate the object so that you rotate it around the 
        //    center (easier :))
        //at.translate(-image.getWidth()/2, -image.getHeight()/2);
        at.translate(-img.getWidth(null)/2, -img.getHeight(null)/2);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, this);

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
}
