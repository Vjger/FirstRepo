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
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

public class ImagePanel extends JPanel
implements ActionListener {

	private final int B_WIDTH = 350;
	private final int B_HEIGHT = 350;
	private final int INITIAL_X = -40;
	private final int INITIAL_Y = -40;
	private final int DELAY = 25;

	private Image star;
	private Timer timer;
	private int x, y;

	public ImagePanel() {

		initBoard();
	}

	private void loadImage() {

		ImageIcon ii = new ImageIcon("C:\\Users\\Marco\\Pictures\\solareclipse.jpg");
		star = ii.getImage();
	}

	private void initBoard() {

		//setBackground(Color.BLACK);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

		loadImage();

		x = INITIAL_X;
		y = INITIAL_Y;

		//timer = new Timer(DELAY, this);
		//timer.start();
	}

//	@Override
//	public void paint(Graphics g) {
//		super.paint(g);
//		drawStar(g);
//	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawStar(g);
	}

	
	private void drawStar(Graphics g) {

		g.drawImage(star, 0, 0, this);
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		x += 1;
		y += 1;

		if (y > B_HEIGHT) {

			y = INITIAL_Y;
			x = INITIAL_X;
		}
		if (x < 0 || x > 255){
			setBackground(new Color(200,200,200));
		}else{
			setBackground(new Color(200,200,x, x));
		}
		repaint();
	}
}