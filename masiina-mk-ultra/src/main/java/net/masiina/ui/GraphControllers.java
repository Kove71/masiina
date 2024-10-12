package net.masiina.ui;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GraphControllers extends JPanel {

    GraphControllers() {
        setBorder(BorderFactory.createLineBorder(Color.black));

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

}
