package net.masiina.osc;

public interface Oscillator {
    
    short generateSample(double freq, double time);
}
