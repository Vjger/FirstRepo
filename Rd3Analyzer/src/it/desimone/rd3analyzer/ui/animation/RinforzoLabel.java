package it.desimone.rd3analyzer.ui.animation;

import it.desimone.utils.Configurator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class RinforzoLabel extends JLabel
implements ActionListener {

	private final int B_WIDTH = 50;
	private final int B_HEIGHT = 50;
	private final int DELAY = Configurator.ANIMATION_TIME/10;

	private float alpha = 1.0f; //0.1f;
	private Timer timer;
	private int x, y;

	public boolean running = true;
	
	public RinforzoLabel(float alpha) {
		this.alpha = alpha;
		initBoard("Ciao");
	}
	
	public RinforzoLabel(String text) {
		initBoard(text);
	}

	private void initBoard(String text) {

		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		setOpaque(true);
		//setBackground(new Color(1.0f,0.5f,1.0f,alpha));
		//setBackground(new Color(1.0f,0.5f,1.0f));
		setBorder(BorderFactory.createLineBorder(Color.RED));
		setText(text);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);

		timer = new Timer(DELAY, this);
		timer.start();
	}

//	@Override
//	public void paint(Graphics g) {
//		//System.out.println("paint");
//		super.paint(g);
//		Graphics2D g2 = (Graphics2D) g; //g.create();
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//        g2.dispose();	
//	}
	
	public void paintComponent(Graphics g) {
		//System.out.println("paintComponent - alpha: "+alpha+" - "+g.getColor());
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(g);


		//setBackground(new Color(1.0f,0.5f,alpha,alpha));
		Graphics2D g2 = (Graphics2D) g; //g.create();
		//g2.setPaint(new Color(1.0f,0.5f,1.0f));
		//setBackground(g.getColor());
		//g2.setColor(new Color(1.0f,0.5f,1.0f));
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        //g2.dispose();
	}

	
//	public void paint(Graphics g) {
//		System.out.println("paint - alpha: "+alpha);
//		Graphics2D g2 = (Graphics2D) g.create();
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//		super.paint(g2);
//        g2.dispose();
//	}



	public void actionPerformed2(ActionEvent e) {
		alpha +=0.01f;	
		if (alpha > 1f){
			System.out.println("Break");
			setOpaque(false);
			alpha = 0.0f;
			repaint();
		}else{
			setOpaque(true);
			setBackground(new Color(1.0f,0.5f,1.0f, alpha));
		}
		


	}
	
	@Override
	public void actionPerformed(ActionEvent e) {


		alpha -=0.1;	
		if (alpha < 0f){
			running = false;
			timer.stop();

			//alpha = 1.0f;
			//repaint();
		}else{
			setOpaque(false);
			setBackground(new Color(1.0f,0.5f,1.0f, alpha));
		}
	}

	public synchronized boolean isRunning() {
		return running;
	}
	
	
}