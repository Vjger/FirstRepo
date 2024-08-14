package it.desimone.replayrd3.ui.animation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

public class MyComponent extends JComponent {

	private Image image ;
	private int rotationAngle;
	public MyComponent(String path, int rotationAngle) {
		super();
		this.rotationAngle = rotationAngle;
		 //BufferedImage image = ImageIO.read(new URL("http://upload.wikimedia.org/wikipedia/en/2/24/Lenna.png"));
			//image = ImageIO.read(new File("C:\\WorkSpaces Eclipse\\RisikoWorkSpace\\Rd3Analyzer\\resources\\images\\FrecciaRossa.gif"));
			image = Toolkit.getDefaultToolkit().createImage(path);
			loadImage(image);

		setOpaque(false);
    	setSize(new Dimension(image.getWidth(null),image.getHeight(this)));
	}
	
	
	
	public int getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(int rotationAngle) {
		this.rotationAngle = rotationAngle;
	}



	@Override
       protected void paintComponent(Graphics g) {
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
            //g2d.drawImage(image, null, null);

            // continue drawing other stuff (non-transformed)
            //...

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
