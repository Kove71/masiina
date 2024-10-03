package net.masiina.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

import net.masiina.MasiinaState;
import net.masiina.StateSynchronizer;
import net.masiina.osc.Oscillator;
import net.masiina.osc.WaveType;

public class UiEventHandler implements ActionListener {

    Oscillator oscillator;
    JPanel panel;

    public UiEventHandler(JPanel panel) {
        oscillator = WaveType.SINE.osc();
        this.panel = panel;
    }

    // TODO change to StateSynchronizer update methods
    @Override
    public void actionPerformed(ActionEvent e) {
        WaveType waveType = WaveType.valueOf(e.getActionCommand());
        if (!waveType.getClass().equals(oscillator.getClass())) {
            oscillator = waveType.osc();
            MasiinaState state = MasiinaState.Builder.fromState(StateSynchronizer.getState())
                .withOscillator(oscillator)
                .build();
            StateSynchronizer.updateState(state);
        }
        System.out.println("EVENT HANDLED, NEW OSC: " + oscillator.getClass().getSimpleName());
        panel.requestFocusInWindow(); // crappy fix so that app listens to keyboard events after clicking buttons
    }

}
