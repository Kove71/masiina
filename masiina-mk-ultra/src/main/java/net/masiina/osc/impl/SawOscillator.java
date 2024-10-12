package net.masiina.osc.impl;

import net.masiina.osc.Oscillator;

/**
 * Algorithm for saw wave
 * y = a(x-floor(x)), where a = amplitude, x = time and floor(x) = nearest integer value of x
 * to get frequency
 * y = a((x*f)-(floor(x*f))
 * To get floor just round it to nearest int by adding 0.5 and casting it (int) x*f+0.5 (or use Math.round())
 *
 * With an amplitude of 1 this generates a saw wave with min value of 0 and max value of 1.
 * Because the default audioformat MASIINA-MK-ULTRA uses is a signed value, we must get amplitudes
 * between -1 and 1, just as the sine and square waves. To do this, we add a phase shift of -1 and double the
 * amplitude, so the final algorithm is
 * y = 2*a*((x*f)-((int) x*f) -1
 * to get the short value we duplicate by Short.MAX_VALUE (32 767)
 */
public class SawOscillator implements Oscillator{


    @Override
    public double generateSample(double freq, double time, double phase) {
        double sawSample = 2 * (time*freq - (int) (time*freq)) - 1;
        // double sawSample = 1 * ((time*freq) - (double) ((int) time*freq +0.5));

        // double sawSample = 2 * (time*freq - (Math.round(time*freq))) - 1;
        return sawSample;
    }

    @Override
    public String toString() {
        return "SawOscillator";
    }

}
