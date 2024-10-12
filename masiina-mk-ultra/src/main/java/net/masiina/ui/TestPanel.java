package net.masiina.ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import net.masiina.MasiinaEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;

public class TestPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int PADDING = 20;
    static final int GRAPH_HEIGHT = 300;
    static final int GRAPH_WIDTH = 400;
    static final int GRAPH_X = 100;
    static final int GRAPH_Y = PADDING;

    GraphPanel graphPanel;

    public TestPanel() {
        LayoutManager borderLayout = new BorderLayout(5, 5);
        setLayout(borderLayout);
        setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));
        setBackground(ColorPalette.BACKGROUND);
        graphPanel = new GraphPanel();
        createDrawButtons();
        createPlayButton();
        add(graphPanel, BorderLayout.CENTER);
    }

    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    private void createDrawButtons() {
        JPanel drawButtons = new JPanel(new FlowLayout());
        drawButtons.setBackground(ColorPalette.BACKGROUND);
        JButton drawWaveButton = new JButton();
        drawWaveButton.setBackground(ColorPalette.CONTROLLER);
        drawWaveButton.setForeground(ColorPalette.TEXT);
        drawWaveButton.setText("DRAW WAVE");
        drawWaveButton.setActionCommand("WAVE");
        drawWaveButton.addActionListener(graphPanel);
        JButton drawDFTButton = new JButton();
        drawDFTButton.setBackground(ColorPalette.CONTROLLER);
        drawDFTButton.setForeground(ColorPalette.TEXT);
        drawDFTButton.setText("DRAW DFT");
        drawDFTButton.setActionCommand("DFT");
        drawDFTButton.addActionListener(graphPanel);
        drawButtons.add(drawWaveButton);
        drawButtons.add(drawDFTButton);
        add(drawButtons, BorderLayout.NORTH);
    }

    private void createPlayButton() {
        JPanel oscButtonPane = new JPanel();
        BoxLayout oscLayout = new BoxLayout(oscButtonPane, BoxLayout.Y_AXIS);
        oscButtonPane.setLayout(oscLayout);
        JButton playSoundButton = new JButton();
        playSoundButton.setBackground(ColorPalette.CONTROLLER);
        playSoundButton.setForeground(ColorPalette.TEXT);
        playSoundButton.setText("PLAY SOUND");
        playSoundButton.setActionCommand("PLAY");
        playSoundButton.addActionListener(graphPanel);
        oscButtonPane.add(playSoundButton);
        add(oscButtonPane, BorderLayout.WEST);

    }
}
