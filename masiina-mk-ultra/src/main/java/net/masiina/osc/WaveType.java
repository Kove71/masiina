package net.masiina.osc;

import net.masiina.osc.impl.SawOscillator;
import net.masiina.osc.impl.SineOscillator;
import net.masiina.osc.impl.SquareOscillator;

public enum WaveType {
    SINE(new SineOscillator()),
    SQUARE(new SquareOscillator()),
    SAW(new SawOscillator());

    private final Oscillator osc;

    WaveType(Oscillator osc) {
        this.osc = osc;
    }

    public Oscillator osc() {
        return osc;
    }
}
