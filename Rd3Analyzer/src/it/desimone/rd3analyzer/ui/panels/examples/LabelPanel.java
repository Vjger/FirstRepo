package it.desimone.rd3analyzer.ui.panels.examples;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

public class LabelPanel extends JPanel
implements ActionListener {

	private final int B_WIDTH = 350;
	private final int B_HEIGHT = 350;
	private final int DELAY = 25;

	private float alpha = 0.0f;
	private Timer timer;
	private int x, y;

	public LabelPanel() {

		initBoard();
	}

	private void initBoard() {

		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		
		setBackground(new Color(0.5f,0.5f,0.5f,alpha));
		add(new JLabel("Ciao"));

		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	public void paint(Graphics g) {
		//System.out.println("paint");
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g; //g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.dispose();
	}
	
//	public void paintComponent(Graphics g) {
//		System.out.println("paintComponent");
//		super.paintComponent(g);
//		Graphics2D g2 = (Graphics2D) g; //g.create();
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//        g2.dispose();
//	}


	@Override
	public void actionPerformed(ActionEvent e) {

		alpha +=0.01f;
		
		if (alpha > 1.0f) alpha = 0f;
		
		setBackground(new Color(0.5f,0.5f,0.5f,alpha));
		repaint();
	}
}