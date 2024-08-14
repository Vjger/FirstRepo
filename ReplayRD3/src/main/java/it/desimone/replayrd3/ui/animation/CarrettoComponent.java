package it.desimone.replayrd3.ui.animation;

import it.desimone.replayrd3.ColoreGiocatore;
import it.desimone.utils.ResourceLoader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class CarrettoComponent extends JComponent {

	private Image image ;
	private int rotationAngle;
	private String numeroCarri = "";

	public CarrettoComponent(String path, int rotationAngle) {
		super();
		this.rotationAngle = rotationAngle;
		//BufferedImage image = ImageIO.read(new URL("http://upload.wikimedia.org/wikipedia/en/2/24/Lenna.png"));
		//image = ImageIO.read(new File("C:\\WorkSpaces Eclipse\\RisikoWorkSpace\\Rd3Analyzer\\resources\\images\\FrecciaRossa.gif"));
		image = Toolkit.getDefaultToolkit().createImage(path);
		//image = Toolkit.getDefaultToolkit().createImage("C:\\WorkSpaces Eclipse\\RisikoWorkSpace\\Rd3Analyzer\\resources\\images\\giallo_45px.png");
		loadImage(image);

		//setBounds(300, 300, getPreferredSize().width, getPreferredSize().height);
		//setBounds(300, 300, 80, 80);
		setOpaque(false);
		//System.out.println("Width Image: "+image.getWidth(null)+" Height Image: "+image.getHeight(this));
		//setPreferredSize(new Dimension(image.getWidth(null),image.getHeight(null)));

		//setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	@Override
	protected void paintComponent(Graphics g) {
		//System.out.println(this.getClass().getName()+" - paintComponent");
		//System.out.println("Width Image: "+image.getWidth(null)+" Height Image: "+image.getHeight(this));
		super.paintComponent(g);

		// create the transform, note that the transformations happen
		// in reversed order (so check them backwards)
		AffineTransform at = new AffineTransform();

		// 4. translate it to the center of the component
		at.translate(getWidth() / 2, getHeight() / 2);

		// 3. do the actual rotation
		at.rotate(Math.toRadians(rotationAngle));

		// 2. just a scale because this image is big
		//at.scale(0.5, 0.5);

		// 1. translate the object so that you rotate it around the 
		//    center (easier :))
		//at.translate(-image.getWidth()/2, -image.getHeight()/2);
		at.translate(-image.getWidth(null)/2, -image.getHeight(null)/2);

		// draw the image
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, at, this);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 17, 15);
		//g2d.fillOval(0, 0, 20, 15);
		g2d.setColor(Color.BLACK);
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		g2d.drawString(numeroCarri, 2, 12);
		//g2d.drawImage(image, null, null);

		// continue drawing other stuff (non-transformed)
		//...

	}



	public String getNumeroCarri() {
		return numeroCarri;
	}

	public void setNumeroCarri(String numeroCarri) {
		this.numeroCarri = numeroCarri;
	}

	public void setColor(ColoreGiocatore color){
		String path = ResourceLoader.getPathCarro(color);
		image = Toolkit.getDefaultToolkit().createImage(path);
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
