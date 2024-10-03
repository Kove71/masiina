package net.masiina.osc.impl;

import net.masiina.osc.Oscillator;

public class SquareOscillator implements Oscillator {

    @Override
    public short generateSample(double freq, double time) {
        double angle = 2.0 * Math.PI * freq; // how many times a second should the wave go
        double sineValue = Math.sin(angle * time);
        return (sineValue > 0) ? Short.MAX_VALUE : Short.MIN_VALUE; // return only max amplitude
    }
    
}
