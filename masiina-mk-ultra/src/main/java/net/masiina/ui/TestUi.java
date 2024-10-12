package net.masiina.ui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestUi {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RootPanel rootPanel = new RootPanel();
        rootPanel.setOpaque(true);
        f.setContentPane(rootPanel);
        f.setBackground(ColorPalette.BACKGROUND);
        f.pack();
        f.setVisible(true);
    }

}
