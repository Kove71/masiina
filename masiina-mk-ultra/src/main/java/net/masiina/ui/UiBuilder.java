package net.masiina.ui;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import net.masiina.osc.WaveType;

public class UiBuilder extends JFrame {

    UiEventHandler eventHandler;
    // JFrame frame;
    public UiBuilder(UiEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        // this.frame = frame;
    }

    public void initUi() {
        setUpOscRadioButtons();        
    }

    public JRadioButton[] setUpOscRadioButtons() {
            JRadioButton sineOscButton = new JRadioButton(WaveType.SINE.name());
            sineOscButton.setSelected(true);
            sineOscButton.setActionCommand(WaveType.SINE.name());
            JRadioButton squareOscButton = new JRadioButton(WaveType.SQUARE.name());
            squareOscButton.setActionCommand(WaveType.SQUARE.name());
            JRadioButton sawOscButton = new JRadioButton(WaveType.SAW.name());
            sawOscButton.setActionCommand(WaveType.SAW.name());

            ButtonGroup group = new ButtonGroup();
            group.add(sineOscButton);
            group.add(squareOscButton);
            group.add(sawOscButton);

            sineOscButton.addActionListener(eventHandler);
            squareOscButton.addActionListener(eventHandler);
            sawOscButton.addActionListener(eventHandler);

            return new JRadioButton[]{sineOscButton, squareOscButton, sawOscButton};
    }

    public void setUpEnvelopeSliders() {

    }

    public void setUpVolumeKnob() {

    }

    public void setUpOctaveTransposer() {

    }

}
