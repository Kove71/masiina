package net.masiina.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.masiina.model.CurrentVoice;
import net.masiina.model.OscillatorSettings;
import net.masiina.model.OscillatorState;
import net.masiina.osc.WaveType;
import net.masiina.sound.Envelope;

public class OscillatorPanel extends JPanel implements ChangeListener, ActionListener, ItemListener {

    private OscillatorSettings oscillatorSettings;

    private static final int ROW_1_HEIGHT = 20;
    private static final int ROW_2_HEIGHT = 180;
    private static final int PANEL_HEIGHT = ROW_1_HEIGHT + ROW_2_HEIGHT;
    private static final int SLIDER_HEIGHT = 150;
    private static final int SLIDER_WIDTH = 300;

    public OscillatorPanel(String oscillatorName, int oscillatorId) {
        this.oscillatorSettings = new OscillatorSettings(oscillatorId, WaveType.SINE, new Envelope(1, 1, 1, 1), 1, 1, 0, false);
        setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));
        setMinimumSize(new Dimension(200, PANEL_HEIGHT));
        setPreferredSize(new Dimension(500, PANEL_HEIGHT));
        setMaximumSize(new Dimension(Short.MAX_VALUE, PANEL_HEIGHT));
        setBackground(ColorPalette.BACKGROUND);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(oscillatorOnCheckButton(oscillatorName), Component.LEFT_ALIGNMENT);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(Box.createHorizontalGlue());
        add(oscillatorType(), Component.RIGHT_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(volume(), Component.RIGHT_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(octaveControl(), Component.RIGHT_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(pitchFineTune(), Component.RIGHT_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(envelopeSliders(), Component.RIGHT_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20, 0)));
    }

    private JCheckBox oscillatorOnCheckButton(String oscName) {

        JCheckBox  oscButton = new JCheckBox(oscName);
        oscButton.setForeground(ColorPalette.TEXT);
        oscButton.setBackground(ColorPalette.BACKGROUND);
        oscButton.addItemListener(this);
        return oscButton;

    }

    private JPanel oscillatorType() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorPalette.BACKGROUND);
        LayoutManager box = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(box);
        JLabel label = new JLabel("Wavetype", SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(80, 20));
        label.setBackground(ColorPalette.BACKGROUND);
        label.setForeground(ColorPalette.TEXT);
        WaveType[] types = {WaveType.SINE, WaveType.SQUARE, WaveType.SAW};
        JComboBox<WaveType> oscTypeBox = new JComboBox<>(types);
        oscTypeBox.setSelectedIndex(0);
        oscTypeBox.addActionListener(this);
        oscTypeBox.setMaximumSize(new Dimension(80, 20));
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(TOP_ALIGNMENT);
        oscTypeBox.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(label, Component.TOP_ALIGNMENT);
        panel.add(oscTypeBox, Component.CENTER_ALIGNMENT);
        return panel;

    }

    private JPanel volume() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorPalette.BACKGROUND);
        LayoutManager box = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(box);
        JLabel label = new JLabel("Volume", SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(80, 20));
        label.setBackground(ColorPalette.BACKGROUND);
        label.setForeground(ColorPalette.TEXT);
        JSlider volumeSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 1000);
        volumeSlider.setName("VOLUME");
        volumeSlider.addChangeListener(this);
        volumeSlider.setBackground(ColorPalette.BACKGROUND);
        volumeSlider.setMaximumSize(new Dimension(20, SLIDER_HEIGHT));
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(TOP_ALIGNMENT);
        volumeSlider.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(label, Component.TOP_ALIGNMENT);
        panel.add(volumeSlider, Component.CENTER_ALIGNMENT);
        return panel;
    }

    private JPanel pitchFineTune() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorPalette.BACKGROUND);
        LayoutManager box = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(box);
        JLabel label = new JLabel("Fine pitch", SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(80, 20));
        label.setBackground(ColorPalette.BACKGROUND);
        label.setForeground(ColorPalette.TEXT);
        JSlider pitchSlider = new JSlider(JSlider.VERTICAL, 900, 1100, 1000);
        pitchSlider.setName("PITCH");
        pitchSlider.addChangeListener(this);
        pitchSlider.setBackground(ColorPalette.BACKGROUND);
        pitchSlider.setMaximumSize(new Dimension(20, SLIDER_HEIGHT));
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(TOP_ALIGNMENT);
        pitchSlider.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(label, Component.TOP_ALIGNMENT);
        panel.add(pitchSlider, Component.CENTER_ALIGNMENT);
        return panel;
    }

    private JPanel octaveControl() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorPalette.BACKGROUND);
        LayoutManager box = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(box);
        JLabel label = new JLabel("Octaves", SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(80, 20));
        label.setBackground(ColorPalette.BACKGROUND);
        label.setForeground(ColorPalette.TEXT);
        SpinnerModel spinnerModel = new SpinnerNumberModel(0, -5, 5, 1);
        spinnerModel.addChangeListener(this);
        JSpinner octaveSpinner = new JSpinner(spinnerModel);
        octaveSpinner.setMaximumSize(new Dimension(80, 20));
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(TOP_ALIGNMENT);
        octaveSpinner.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(label, Component.TOP_ALIGNMENT);
        panel.add(octaveSpinner, Component.CENTER_ALIGNMENT);
        return panel;
    }

    private JPanel envelopeSliders() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorPalette.BACKGROUND);
        LayoutManager box = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(box);
        JLabel label = new JLabel("Envelope", SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(80, 20));
        label.setBackground(ColorPalette.BACKGROUND);
        label.setForeground(ColorPalette.TEXT);
        JPanel envelopePanel = new JPanel(new GridBagLayout());
        envelopePanel.setBackground(ColorPalette.BACKGROUND);
        envelopePanel.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED, ColorPalette.BORDER, ColorPalette.HIGHLIGHT));
        envelopePanel.setPreferredSize(new Dimension(SLIDER_WIDTH, ROW_2_HEIGHT));
        envelopePanel.setMaximumSize(new Dimension(SLIDER_WIDTH, ROW_2_HEIGHT));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        JSlider attackSlider = new JSlider(JSlider.VERTICAL, 1, 500, 1);
        attackSlider.setName("ATTACK");
        attackSlider.addChangeListener(this);
        JLabel attackLabel = new JLabel("Attack", JLabel.CENTER);
        JSlider decaySlider = new JSlider(JSlider.VERTICAL, 1, 500, 1);
        decaySlider.setName("DECAY");
        decaySlider.addChangeListener(this);
        JLabel decayLabel = new JLabel("Decay", JLabel.CENTER);
        JSlider sustainSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
        sustainSlider.setName("SUSTAIN");
        sustainSlider.addChangeListener(this);
        JLabel sustainLabel = new JLabel("Sustain", JLabel.CENTER);
        JSlider releaseSlider = new JSlider(JSlider.VERTICAL, 1, 500, 1);
        releaseSlider.setName("RELEASE");
        releaseSlider.addChangeListener(this);
        JLabel releaseLabel = new JLabel("Release", JLabel.CENTER);
        envelopePanel.add(attackSlider, gbc);
        envelopePanel.add(decaySlider, gbc);
        envelopePanel.add(sustainSlider, gbc);
        envelopePanel.add(releaseSlider, gbc);
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        envelopePanel.add(attackLabel, gbc);
        envelopePanel.add(decayLabel, gbc);
        envelopePanel.add(sustainLabel, gbc);
        envelopePanel.add(releaseLabel, gbc);
        formatComponents(attackSlider, attackLabel, decayLabel, decaySlider, sustainSlider, sustainLabel, releaseSlider, releaseLabel);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(TOP_ALIGNMENT);
        envelopePanel.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(label, Component.TOP_ALIGNMENT);
        panel.add(envelopePanel, Component.CENTER_ALIGNMENT);
        return panel;
    }

    private void formatComponents(Component... cList) {
        for (Component c : cList) {
            c.setBackground(ColorPalette.CONTROLLER);
            c.setForeground(ColorPalette.TEXT);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSlider slider) {
            if (!slider.getValueIsAdjusting()) {
                int sliderValue = slider.getValue();
                if (slider.getName().equals("ATTACK") || slider.getName().equals("DECAY") || slider.getName().equals("RELEASE")) {
                    // int multiplier = (sliderValue < 50) ? 1 : ((int) sliderValue / 50) * 3;
                    // sliderValue = sliderValue * multiplier;
                    sliderValue = sliderValue * (int) Math.pow(2, sliderValue / 100);

                    // sliderValue = sliderValue * (1 + (int) sliderValue / 50);
                }
                switch (slider.getName()) {
                    case "ATTACK":
                        oscillatorSettings.setEnvelope(oscillatorSettings.getEnvelope().withAttack(sliderValue));
                        System.out.println("ATTACK: " + sliderValue);
                        break;
                    case "DECAY":
                        oscillatorSettings.setEnvelope(oscillatorSettings.getEnvelope().withDecay(sliderValue));
                        System.out.println("DECAY: " + sliderValue);
                        break;
                    case "SUSTAIN":
                        oscillatorSettings.setEnvelope(oscillatorSettings.getEnvelope().withSustain((double)sliderValue/1000));
                        System.out.println("SUSTAIN: " + sliderValue);
                        break;
                    case "RELEASE":
                        oscillatorSettings.setEnvelope(oscillatorSettings.getEnvelope().withRelease(sliderValue));
                        System.out.println("RELEASE: " + sliderValue);
                        break;
                    case "PITCH":
                        oscillatorSettings.setFineTuning((double)sliderValue/1000);
                        System.out.println("PITCH: " + sliderValue);
                        break;
                    case "VOLUME":
                        oscillatorSettings.setVolume((double)sliderValue/1000);
                        System.out.println("VOLUME: " + sliderValue);
                        break;
                    default:
                        break;
                }
                notifyOscillatorChange();
            }
        } else if (e.getSource() instanceof SpinnerNumberModel spinner) {
            oscillatorSettings.setOctave(spinner.getNumber().intValue());
            notifyOscillatorChange();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox comboBox) {
            WaveType waveType = (WaveType) comboBox.getSelectedItem();
            oscillatorSettings.setWaveType(waveType);
        }
        notifyOscillatorChange();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            oscillatorSettings.setOn(false);
        }
        if (e.getStateChange() == ItemEvent.SELECTED) {
            oscillatorSettings.setOn(true);
        }
        notifyOscillatorChange();
    }

    private void notifyOscillatorChange() {
        System.out.println(oscillatorSettings);
        OscillatorState.updateOscillator(oscillatorSettings);
    }
}
