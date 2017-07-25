package it.desimone.rd3analyzer.ui.panels.examples;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;

public class ShowPopupActionListener implements ActionListener {
    private Component component;

    ShowPopupActionListener(Component component) {
      this.component = component;
    }

    public synchronized void actionPerformed(ActionEvent actionEvent) {
      JButton button = new JButton("Hello, World");
      PopupFactory factory = PopupFactory.getSharedInstance();
      Random random = new Random();
      int x = random.nextInt(30);
      int y = random.nextInt(30);
      final Popup popup = factory.getPopup(component, button, x, y);
      popup.show();
      ActionListener hider = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          popup.hide();
        }
      };
      // Hide popup in 3 seconds
      Timer timer = new Timer(3000, hider);
      timer.start();
    }
}
