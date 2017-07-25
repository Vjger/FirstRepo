package it.desimone.rd3analyzer.ui.panels.examples;

import it.desimone.rd3analyzer.ColoreGiocatore;
import it.desimone.rd3analyzer.Territorio;
import it.desimone.rd3analyzer.ui.animation.CarrettoAnimationPanel;
import it.desimone.rd3analyzer.ui.animation.CarrettoComponent;
import it.desimone.rd3analyzer.ui.animation.RinforzoLabel;
import it.desimone.rd3analyzer.ui.animation.MyComponent;
import it.desimone.rd3analyzer.ui.panels.StatePosition;
import it.desimone.rd3analyzer.ui.panels.StatePosition.PositionAndRotation;
import it.desimone.utils.Configurator;
import it.desimone.utils.ResourceLoader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.event.MouseInputListener;

public class DemoBackgroundSwing extends JPanel{

  private Image img;
	final List<Integer> xCoords = new ArrayList<Integer>();
	final List<Integer> yCoords = new ArrayList<Integer>();
	
	private int xFreccia;
	private int yFreccia;
	
    final MyComponent freccia;
  public DemoBackgroundSwing(){
    img = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getPathPlancia(Configurator.PlanciaSize.BIG));
    loadImage(img);
    //final CarrettoComponent carro = new CarrettoComponent(ResourceLoader.getPathCarro("nero"),0);
    freccia = new MyComponent(ResourceLoader.getPathFreccia("Rossa"),0);
	
	addMouseListener(new MouseListener() {
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {

        	freccia.setBounds(e.getX(), e.getY(), 50, 50);
            add(freccia);
            xFreccia = e.getX();
            yFreccia = e.getY();
        	System.out.println(e.getX()+" - "+e.getY());
        }
    });
	
    addMouseMotionListener(new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
        	double coefficienteAngolare = -(e.getY() - yFreccia)/(e.getX() - xFreccia);
        	Double gradiRotazione = Math.toDegrees(Math.atan(coefficienteAngolare));
        	if ((e.getX() - xFreccia) < 0){
        		gradiRotazione +=180;
        	}
        	System.out.println(e.getX()+" - "+e.getY()+" ruotato di "+gradiRotazione+"°");
        	freccia.setRotationAngle(gradiRotazione.intValue());
        	//freccia.repaint();
		}
	});
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
	//System.out.println(this.getClass().getName()+" - paintComponent");
    super.paintComponent(g);
    //setOpaque(false);
    //g.drawImage(img, 0, 0, null);
    
    AffineTransform at = new AffineTransform();
    //at.scale(0.75, 0.75);

    // draw the image
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(img, at, this);

    
    int[] x = new int[xCoords.size()];
    int[] y = new int[yCoords.size()];
    for (int index = 0 ; index < xCoords.size(); index++){
    	x[index] = xCoords.get(index);
    	y[index] = yCoords.get(index);
    }
    //int[] y = new int[]{400,300,300,100};
    //g.setColor(Color.WHITE);
    //g.drawPolygon (x, y, x.length);    

  }

  public static void main(String... argv) {
    JFrame frame = new JFrame("Demo Background Image");
    DemoBackgroundSwing back = new DemoBackgroundSwing();
    back.setLayout(null);
   
//    MyComponent carroInOntario = new MyComponent(ResourceLoader.getPathCarro("giallo"),0);
//    carroInOntario.setBounds(210, 210, 50, 50);
//    
//    JLabel carroInOntarioLabel = new CarrettoLabel("100");
//    carroInOntarioLabel.setBounds(210, 210, carroInOntarioLabel.getPreferredSize().width, carroInOntarioLabel.getPreferredSize().height);
//
//    MyComponent frecciaOntarioStatiUnitiOccidentali = new MyComponent(ResourceLoader.getPathFreccia("Rossa"),135);
//    frecciaOntarioStatiUnitiOccidentali.setBounds(160, 260, 50, 50);
//    
//    MyComponent carroInStatiUnitiOccidentali = new MyComponent(ResourceLoader.getPathCarro("verde"),0);
//    carroInStatiUnitiOccidentali.setBounds(130, 280, 50, 50); 
//    JLabel carroInStatiUnitiOccidentaliLabel = new CarrettoLabel("100");
//    carroInStatiUnitiOccidentaliLabel.setBounds(130, 280, carroInStatiUnitiOccidentaliLabel.getPreferredSize().width, carroInStatiUnitiOccidentaliLabel.getPreferredSize().height);
//	
//    back.add(carroInOntarioLabel);
//    back.add(carroInOntario);
//    back.add(frecciaOntarioStatiUnitiOccidentali);
//    back.add(carroInStatiUnitiOccidentaliLabel);
//    back.add(carroInStatiUnitiOccidentali);
//
//    CarrettoComponent carro = new CarrettoComponent(ResourceLoader.getPathCarro("nero"),0);
//    carro.setBounds(610, 310, 50, 50);
//    back.add(carro);
//    
//    MyComponent mycp1 = new MyComponent(ResourceLoader.getPathCarro("rosso"),0);
//    mycp1.setBounds(300, 300, 50, 50);
//    MyComponent mycp2 = new MyComponent(ResourceLoader.getPathCarro("rosso"),45);
//    mycp2.setLocation(400,300);
//    MyComponent mycp3 = new MyComponent(ResourceLoader.getPathCarro("rosso"),180);
//    mycp3.setBounds(300, 400, 50, 50);
//    MyComponent mycp4 = new MyComponent(ResourceLoader.getPathCarro("rosso"),270);
//    mycp4.setLocation(400,400);
//    back.add(mycp1);
//    back.add(mycp2);
//    back.add(mycp3);
//    back.add(mycp4);
//    
//    ActionListener actionListener = new ShowPopupActionListener(back);
//    JButton start = new JButton("Pick Me for Popup");
//    start.setOpaque(true);
//    start.addActionListener(actionListener);
//    start.setVisible(true);
//    start.setBounds(30,30,100,100);
//    //back.add(start);
//    
//    JPanel panelScorri1 = createAnimationPanel(new Point2D.Double(1000,  400), new Point2D.Double(800,  600));
//    back.add(panelScorri1);
//    JPanel panelScorri2 = createAnimationPanel(new Point2D.Double(800,  600), new Point2D.Double(1000,  400));
//    back.add(panelScorri2);
//    JPanel panelScorri3 = createAnimationPanel(new Point2D.Double(800,  400), new Point2D.Double(1000,  600));
//    back.add(panelScorri3);
//    JPanel panelScorri4 = createAnimationPanel(new Point2D.Double(1000,  600), new Point2D.Double(800,  400));
//    back.add(panelScorri4);
//    
//	JLabel labelTest = new LabelTest(1.0f);
//	labelTest.setBounds(500, 300, 30, 30);
//	labelTest.setVisible(true);
//	back.add(labelTest);
    
    StatePosition statePosition = new StatePosition(Configurator.PlanciaSize.BIG);
    
    Collection<PositionAndRotation> posizioni = statePosition.confini.values();
    
    for (PositionAndRotation posizione: posizioni){
    	MyComponent freccia = new MyComponent(ResourceLoader.getPathFreccia("Rossa"),posizione.getRotationAngle());
    	freccia.setBounds(((Double)posizione.getPosition().getX()).intValue(),((Double)posizione.getPosition().getY()).intValue(), 50, 50);
    	back.add(freccia);
    }
    

	Territorio[] territori = Territorio.values();
	
	for (Territorio territorio: territori){
		Point posizioneCarro = statePosition.getPosition(territorio);
		if (posizioneCarro != null){
			CarrettoComponent carro = new CarrettoComponent(ResourceLoader.getPathCarro("bianco"), 0);
			carro.setBounds(((Double)posizioneCarro.getX()).intValue(), ((Double)posizioneCarro.getY()).intValue(), 50, 50);
			back.add(carro);
		}
	}

    
    frame.getContentPane().add(back);
    frame.setSize(1250, 900);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    frame.setVisible(true);
    
//    PopupFactory factory = PopupFactory.getSharedInstance();
//
//    final Popup popup = factory.getPopup(back, piazzamentoLabel, 180, 180);
//    
//    popup.show();
//    ActionListener hider = new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        popup.hide();
//      }
//    };
//    // Hide popup in 3 seconds
//    Timer timer = new Timer(3000, hider);
//    timer.start();
    
//    Window window = new Window(frame);
//    window.add(piazzamentoLabel);
//    window.setVisible(true);
//    window.setOpacity(0.3f);
//    window.setBounds(30,30,30,30);
    
    frame.validate();
    

    
    //Timer timer = new Timer();
    //timer.scheduleAtFixedRate(new ScheduleTask(back), 100, 25);
    //timer.scheduleAtFixedRate(new ScheduleTask2(panelScorri), 100, 25);
  }

  private static JPanel createAnimationPanel(Point2D.Double fromPoint, Point2D.Double toPoint){
	  double xFrom = fromPoint.getX();
	  double yFrom = fromPoint.getY();
	  double xTo = toPoint.getX();
	  double yTo = toPoint.getY();
	  
	  int xLeftCorner = ((Double)Math.min(xFrom, xTo)).intValue();
	  int yTopCorner  = ((Double)Math.min(yFrom, yTo)).intValue();
	  
	  int widthPanel = ((Double) Math.max(50.0d, Math.abs(xFrom - xTo))).intValue();
	  int heigthPanel = ((Double) Math.max(50.0d, Math.abs(yFrom - yTo))).intValue();

	  JPanel panelScorri = new CarrettoAnimationPanel(new Point2D.Double((xFrom - xLeftCorner),(yFrom - yTopCorner)), new Point2D.Double((xTo - xLeftCorner),(yTo - yTopCorner)), ColoreGiocatore.ROSSO, (short)10);
	  panelScorri.setBounds(xLeftCorner, yTopCorner, widthPanel, heigthPanel);
	  panelScorri.setBorder(BorderFactory.createBevelBorder(1));
	  return panelScorri;
  }
  
  private static class ScheduleTask extends TimerTask {

	  int x = 0;
	  int y = 0;
	  private DemoBackgroundSwing back;
	  public ScheduleTask(DemoBackgroundSwing back) {
		this.back = back;
	  }
	  
	    @Override
	    public void run() {
	     
	        Graphics g = back.getGraphics();
	        
	        Image image = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getPathCarro("rosso"));
	        back.loadImage(image);

	        g.drawImage(image, x++, y++, back);
	        //Toolkit.getDefaultToolkit().sync();
	        back.repaint();
	    }
	}
  
  private static class ScheduleTask2 extends TimerTask {

	  int x = 0;
	  int y = 0;
	  private JPanel back;
	  public ScheduleTask2(JPanel back) {
		this.back = back;
	  }
	  
	    @Override
	    public void run() {
	     
	        Graphics g = back.getGraphics();
	        
	        Image image = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getPathCarro("rosso"));
	        loadImage(image);

	        g.drawImage(image, x++, y++, back);
	        Toolkit.getDefaultToolkit().sync();
	        //back.repaint();
	    }
	    
	    private void loadImage(Image img) {
	        try {
	          MediaTracker track = new MediaTracker(back);
	          track.addImage(img, 0);
	          track.waitForID(0);
	        } catch (InterruptedException e) {
	          e.printStackTrace();
	        }
	      }
	}
  
}

