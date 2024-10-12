package net.masiina.osc.impl;

import net.masiina.osc.Oscillator;

public class SineOscillator implements Oscillator {

    @Override
    public double generateSample(double freq, double time, double phase) {
        double angle = 2.0 * Math.PI * freq; // how many times a second should the wave go
        double sineValue = Math.sin(angle * time);
        return sineValue;
    }

    @Override
    public String toString() {
        return "SineOscillator";
    }
}
