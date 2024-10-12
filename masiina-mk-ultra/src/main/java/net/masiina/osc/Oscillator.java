package net.masiina.osc;

public interface Oscillator {
    
    double generateSample(double freq, double time, double phase);
}
