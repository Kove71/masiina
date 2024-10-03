package net.masiina.osc.impl;

import net.masiina.osc.Oscillator;

public class SineOscillator implements Oscillator {
    
    @Override
    public short generateSample(double freq, double time) {
        double angle = 2.0 * Math.PI * freq; // how many times a second should the wave go
        double sineValue = Math.sin(angle * time);
        return (short) (sineValue * Short.MAX_VALUE); // transposing float between -1 and 1 to 16bit integer -> multiply by 32 767 (max signed value)
    }
}
