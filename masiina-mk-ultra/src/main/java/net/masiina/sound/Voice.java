package net.masiina.sound;

import net.masiina.osc.Oscillator;

// add amplitude, octave
public record Voice(
    Oscillator oscillator,
    double frequency
) {
    
}
