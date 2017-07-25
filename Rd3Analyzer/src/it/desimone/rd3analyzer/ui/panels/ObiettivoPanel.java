package it.desimone.rd3analyzer.ui.panels;

import it.desimone.rd3analyzer.Giocatore;
import it.desimone.utils.ResourceLoader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class ObiettivoPanel extends JPanel implements MouseListener {
	
	private JLabel labelPunti;
	private Giocatore giocatore;
	private Image img;
	Popup popup = null;
	JLabel labelObiettivo;
 
	public ObiettivoPanel(Giocatore giocatore){
		this.giocatore = giocatore;
		
		
		img = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getPathObiettivo(giocatore.getNumeroObiettivo()));
		loadImage(img);
		setLayout(null);

		setPreferredSize(new Dimension(70,70));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		labelPunti = new JLabel("0");
		labelPunti.setBackground(Color.WHITE);
		labelPunti.setOpaque(true);
		labelPunti.setBounds(26, 28, 18, 15);
		labelPunti.setHorizontalAlignment(SwingConstants.CENTER);
		add(labelPunti);
		
		labelObiettivo = new JLabel();
		Icon icon = new ImageIcon(ResourceLoader.getPathObiettivo(giocatore.getNumeroObiettivo()));
		labelObiettivo.setIcon(icon);
		
		addMouseListener(this);
	}
	
	public Giocatore getGiocatore() {
		return giocatore;
	}

	public void setGiocatore(Giocatore giocatore) {
		this.giocatore = giocatore;
	}

	public void setPunti(Integer punti) {
		this.labelPunti.setText(String.valueOf(punti));
	}


	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		AffineTransform at = new AffineTransform();
        at.translate(0,(70-img.getHeight(null)*0.4)/2);
		at.scale(0.4, 0.4);
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((screen.width - labelObiettivo.getPreferredSize().getWidth())/2);
		int y = (int) ((screen.height - labelObiettivo.getPreferredSize().getHeight())/2);
		popup = PopupFactory.getSharedInstance().getPopup(this,labelObiettivo , x, y);
		popup.show();   		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		popup.hide();   
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
