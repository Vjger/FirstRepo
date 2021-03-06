package it.desimone.rd3analyzer.ui.panels.examples;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class UtilityTimerExample extends JFrame {

    public UtilityTimerExample() {

        initUI();
    }
    
    private void initUI() {
        
        add(new Board2());
                        
        setResizable(false);
        pack();
        
        setTitle("Star");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         
    }

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {                
                JFrame ex = new UtilityTimerExample();
                ex.setVisible(true);                
            }
        });
    }
}
