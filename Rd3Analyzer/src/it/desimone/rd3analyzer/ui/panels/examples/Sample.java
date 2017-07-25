package it.desimone.rd3analyzer.ui.panels.examples;

import java.awt.Color;
import javax.swing.*;

class Sample {
   int i;
   private JFrame f;
   private JLabel l;

Sample() throws InterruptedException {
    f = new JFrame("test");
    f.setSize(400, 400);
    l = new JLabel("Testing");
    f.add(l);
    f.setVisible(true);

    for (i = 0; i < 6; i++) {
        if (i % 2 == 0) {
            l.setForeground(Color.red);
        } else {
            l.setForeground(Color.BLUE);
        }
        Thread.sleep(500);
    }
    l.setEnabled(false);

}

public static void main(String a[]) throws InterruptedException {
    new Sample();
}
}
