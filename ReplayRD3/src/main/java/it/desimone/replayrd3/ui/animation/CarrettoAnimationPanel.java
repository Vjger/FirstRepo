package it.desimone.replayrd3.ui.animation;

import it.desimone.replayrd3.ColoreGiocatore;
import it.desimone.utils.Configurator;
import it.desimone.utils.ResourceLoader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CarrettoAnimationPanel extends JPanel {
	
	private Point2D.Double startingPoint, endPoint;
    private final int INITIAL_DELAY = Configurator.ANIMATION_TIME/10;
    private final int PERIOD_INTERVAL = Configurator.ANIMATION_TIME/40;
	
    private ColoreGiocatore coloreGiocatore;
    private Image star;
    private Timer timer;
    private int xp, yp;
    private Double coefficienteAngolare;
    private Double coefficienteCostante;
    
    private Short numeroCarri;
    
    private ScheduleTask task = new ScheduleTask();
    
    private List<Point> luogoDeiPuntiDellaRetta;
    private int indexOfPoints;
    private int animationStep;
    
    public CarrettoAnimationPanel(Point2D.Double startingPoint, Point2D.Double endPoint, ColoreGiocatore coloreGiocatore, Short numeroCarri) {
    
    	setOpaque(false);
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        calcolaCoefficienteAngolare(startingPoint, endPoint);
        this.numeroCarri = numeroCarri;
        this.coloreGiocatore = coloreGiocatore;
        initBoard();        
    }
    
    private void calcolaCoefficienteAngolare(Point2D.Double startingPoint, Point2D.Double endPoint){
    	 
    	coefficienteAngolare = (endPoint.getY() - startingPoint.getY())/(endPoint.getX() - startingPoint.getX());
    	
    	coefficienteCostante =  startingPoint.getY() - coefficienteAngolare*startingPoint.getX();
    }
    
	public static CarrettoAnimationPanel getInstance(Point fromPoint, Point toPoint, ColoreGiocatore coloreGiocatore, Short numeroCarri){
		  double xFrom = fromPoint.getX();
		  double yFrom = fromPoint.getY();
		  double xTo = toPoint.getX();
		  double yTo = toPoint.getY();
		  
		  int xLeftCorner = ((Double)Math.min(xFrom, xTo)).intValue();
		  int yTopCorner  = ((Double)Math.min(yFrom, yTo)).intValue();
		  
		  int widthPanel = ((Double) Math.max(50.0d, Math.abs(xFrom - xTo))).intValue()+50;
		  int heigthPanel = ((Double) Math.max(50.0d, Math.abs(yFrom - yTo))).intValue()+50;

		  CarrettoAnimationPanel panelScorri = new CarrettoAnimationPanel(new Point2D.Double((xFrom - xLeftCorner),(yFrom - yTopCorner)), new Point2D.Double((xTo - xLeftCorner),(yTo - yTopCorner)), coloreGiocatore, numeroCarri);
		  panelScorri.setBounds(xLeftCorner, yTopCorner, widthPanel, heigthPanel);
		  return panelScorri;
	 }
    
    
    private void loadImage() {
        
        //ImageIcon ii = new ImageIcon("C:\\WorkSpaces Eclipse\\RisikoWorkSpace\\Rd3Analyzer\\resources\\images\\verde_45px.png");
		star = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getPathCarro(coloreGiocatore));
        //star = ii.getImage();        
    }
    
    private void initBoard() {
        
        setDoubleBuffered(true);
        
        loadImage();

        xp = ((Double) startingPoint.getX()).intValue();
        yp = ((Double) startingPoint.getY()).intValue();
        
        calcolaPuntiDellaRetta();
        
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 
                INITIAL_DELAY, PERIOD_INTERVAL);        
    }
        

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawStar(g);
    }
    
    private void drawStar(Graphics g) {
        
        g.drawImage(star, xp, yp, this);
        
        g.setColor(Color.WHITE);
        g.fillRect(xp, yp+5, 17, 15);
        g.setColor(Color.BLACK);
        g.setFont(getFont().deriveFont(Font.BOLD));
        g.drawString(numeroCarri.toString(), xp+2, yp+16);
        
        Toolkit.getDefaultToolkit().sync();
    }


    private class ScheduleTaskOld extends TimerTask {
    	
        private boolean running = true;

        @Override
        public void run() {
        	boolean stop = false;
        	if (endPoint.getX() > startingPoint.getX()){
        		xp += 1;
        		stop = xp > endPoint.getX();
        	}else{
        		xp -= 1;
        		stop = xp < endPoint.getX();
        	}
            yp = ((Double)(xp*coefficienteAngolare + coefficienteCostante)).intValue();
            
            if (stop){
            	running = false;
            	this.cancel();
            }
            
            repaint();
        }
        
    	public boolean isRunning() {
    		return running;
    	}
    }
    
    private class ScheduleTask extends TimerTask {
    	
        private boolean running = true;

        @Override
        public void run() {
        	boolean stop = false;
        	
        	if (indexOfPoints > luogoDeiPuntiDellaRetta.size() -1){
        		stop = true;
        		Point point = luogoDeiPuntiDellaRetta.get(luogoDeiPuntiDellaRetta.size() -1);
        		xp = ((Double) point.getX()).intValue();
        		yp = ((Double) point.getY()).intValue();
        	}else{
        		Point point = luogoDeiPuntiDellaRetta.get(indexOfPoints);
        		xp = ((Double) point.getX()).intValue();
        		yp = ((Double) point.getY()).intValue();
        		
        		indexOfPoints += animationStep; //fissato uno step di 5 punti.
        	}
            
            if (stop){
            	running = false;
            	this.cancel();
            }
            
            repaint();
        }
        
    	public boolean isRunning() {
    		return running;
    	}
    }
    
	public synchronized boolean isRunning() {
		return task.isRunning();
	}
	
	private void calcolaPuntiDellaRetta(){
		luogoDeiPuntiDellaRetta = new ArrayList<Point>();
		boolean stop = false;
		int xpp = xp;
		int ypp = yp;
		while (!stop){
			if (coefficienteAngolare <= 1){
		    	if (endPoint.getX() > startingPoint.getX()){
		    		xpp += 1;
		    		stop = xpp > endPoint.getX();
		    	}else{
		    		xpp -= 1;
		    		stop = xpp < endPoint.getX();
		    	}
		        ypp = ((Double) (xpp*coefficienteAngolare + coefficienteCostante)).intValue();
	    	}else{
		    	if (endPoint.getY() > startingPoint.getY()){
		    		ypp += 1;
		    		stop = ypp > endPoint.getY();
		    	}else{
		    		ypp -= 1;
		    		stop = ypp < endPoint.getY();
		    	}
		        xpp = ((Double) ((ypp - coefficienteCostante)/coefficienteAngolare)).intValue();
	    	}

	        Point point = new Point(xpp,ypp);
	        luogoDeiPuntiDellaRetta.add(point);
		}
		
		animationStep = Math.max(luogoDeiPuntiDellaRetta.size()/40, 1);
	}
}
