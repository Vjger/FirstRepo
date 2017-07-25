package it.desimone.rd3analyzer.ui.panels.examples;

import it.desimone.rd3analyzer.ui.animation.RinforzoLabel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwingTimerExample extends JFrame {

    public SwingTimerExample() {
        
        initUI();
    }
    
    private void initUI() {
    	
    	JPanel panel = new ImagePanel();
    	panel.setSize(500,500);
    	
   	
    	for (float fl = 1.0f; fl <= 1.0f; fl+=0.1f){
    		System.out.println(fl);
    		JLabel labelTest = new RinforzoLabel(fl);
    		labelTest.setVisible(true);
    		panel.add(labelTest);
    	}

        add(panel);

        setSize(600,600);
        setResizable(true);
        pack();
        
        setTitle("Star");
        setLocationRelativeTo(null);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
         
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {                
                JFrame ex = new SwingTimerExample();
                ex.setVisible(true);  
            }
        });
    }
}