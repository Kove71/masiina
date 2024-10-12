package net.masiina.ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;

public class RootPanel extends JPanel {

    public RootPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));
        setBackground(ColorPalette.BACKGROUND);
        JTabbedPane tab = new JTabbedPane();
        tab.setPreferredSize(new Dimension(600, 620));
        TestPanel testPanel = new TestPanel();
        tab.addTab("Graph", testPanel);
        OscillatorTab oscillatorTab = new OscillatorTab();
        tab.addTab("Osc", oscillatorTab);
        add(tab);

        // add(graphPanel, BorderLayout.CENTER);
    }

    private void createTabPanel() {

    }
}
