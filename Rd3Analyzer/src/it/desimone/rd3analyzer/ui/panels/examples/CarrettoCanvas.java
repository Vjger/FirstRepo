package it.desimone.rd3analyzer.ui.panels.examples;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CarrettoCanvas extends Canvas {

	public CarrettoCanvas(){
	    setBounds(100, 100, 50, 50);
	    setVisible(true);
	    //setForeground(Color.WHITE);
    	//setBorder(BorderFactory.createLineBorder(Color.WHITE));
	    //setBackground (Color.GRAY);
        //setSize(400, 400);
	    
	}
	
	@Override
	public void paint(Graphics g) {
		System.out.println(this.getClass().getName()+" - paint");
		//super.paint(g);
		Image image = Toolkit.getDefaultToolkit().createImage("C:\\WorkSpaces Eclipse\\RisikoWorkSpace\\Rd3Analyzer\\resources\\images\\giallo_45px.png");
		loadImage(image);
        Graphics2D g2;
        g2 = (Graphics2D) g;
        //g2.drawString ("It is a custom canvas area", 70, 70);
        
        AffineTransform at = new AffineTransform();

        //g2.drawImage(image, at, null);
        g2.drawImage(image, 10, 10, image.getWidth(null), image.getHeight(null), null);
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
