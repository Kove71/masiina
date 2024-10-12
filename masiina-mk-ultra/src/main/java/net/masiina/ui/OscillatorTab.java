package net.masiina.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OscillatorTab extends JPanel {


    public OscillatorTab() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorPalette.BACKGROUND);
        setPreferredSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(400, 600));
        OscillatorPanel osc1 = new OscillatorPanel("OSC 1", 1);
        add(osc1);
        // add(Box.createVerticalGlue());
        OscillatorPanel osc2 = new OscillatorPanel("OSC 2", 2);
        add(osc2);
        // add(Box.createVerticalGlue());
        OscillatorPanel osc3 = new OscillatorPanel("OSC 3",3);
        add(osc3);

    }
}
