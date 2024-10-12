package net.masiina.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.masiina.MasiinaEngine;
import net.masiina.Note;
import net.masiina.osc.WaveType;

public class Fourier {
    
    // X(k)=∑t=0n−1x(t)e−2πitk/n.
    // −2πitk/n can be replaced by [cos(2πkn/N)−i⋅sin(2πkn/N)] per the Euler formula
    // ∑n=0N−1xn[cos(2πkn/N)−i⋅sin(2πkn/N)]
    // Max frequencys in a sample is half the sample rate per the Nyquist frequency
    // This means frequencys above sampleRate/2 can be ignored
    // 
    public static List<double[]> dft(double sampleRate, List<Double> samples) {
        int sampleAmt = samples.size();
        int freqAmt = (int) sampleAmt / 2 + 1; // ?
        List<double[]> complexNumbers = new ArrayList<>(sampleAmt);
        long startTime = System.currentTimeMillis();
        for (int k=0; k<freqAmt; k++) { // frequencys, i.e. output amt
            double realNumber = 0.0;
            double imgNumber = 0.0;
            for (int n=0;n<sampleAmt;n++) {
                double angle = Math.PI * 2 * n * k / sampleAmt;
                realNumber += samples.get(n) * Math.cos(angle);
                imgNumber -= samples.get(n) * Math.sin(angle);
            }
            complexNumbers.add(new double[]{realNumber, imgNumber, k});
        }
        System.out.printf("Took %dms to compute DFT with %d samples and %d frequencies.%n", System.currentTimeMillis()-startTime, sampleAmt, freqAmt);
        return complexNumbers;

    }

    public static List<double[]> dft(double sampleRate, List<Double> samples, int origSampleAmt) {
        double freqRel = sampleRate / origSampleAmt; 
        int sampleAmt = samples.size();
        int freqAmt = (int) sampleAmt / 2 + 1; // ?
        // int freqAmt = (int) sampleRate / 2 + 1; // ?

        List<double[]> complexNumbers = new ArrayList<>(sampleAmt);
        long startTime = System.currentTimeMillis();
        // TODO there should still probably be as many ks as there are frequencies that you want to measure
        for (int k=0; k<freqAmt; k++) { // frequencys, i.e. output amt
            double realNumber = 0.0;
            double imgNumber = 0.0;
            for (int n=0;n<sampleAmt;n++) {
                double angle = Math.PI * 2 * n * k / sampleAmt;
                realNumber += samples.get(n) * Math.cos(angle);
                imgNumber -= samples.get(n) * Math.sin(angle);
            }
            // TODO issue here as k is always an integer -> which means that the resolution suffers a lot and frequencies below the freqRel cant be found
            complexNumbers.add(new double[]{realNumber, imgNumber, k*freqRel}); 
            // the frequency values also change
            // complexNumbers.add(new double[]{realNumber, imgNumber, k}); 

        }
        System.out.println("Freq rel: " + freqRel);
        System.out.printf("Took %dms to compute DFT with %d samples and %d frequencies.%n", System.currentTimeMillis()-startTime, sampleAmt, freqAmt);
        return complexNumbers;

    }


    public static List<Double> reduceSampleResolution(double sampleRate, int reduceAmt, List<Double> samples) {
        double reducedSampleRate =  sampleRate / reduceAmt;
        if (reducedSampleRate % 1 != 0) {
            System.out.println("Error, new samplerate must be an integer. Instead new sample rate is " + reducedSampleRate);
        }
        List<Double> reducedSamples = new ArrayList<>();
        for (int i=0;i<samples.size();i+=reduceAmt) {
            if (i>=samples.size()) {
                System.out.println("Went out of original sample size");
            }
            reducedSamples.add(samples.get(i));
        }
        return reducedSamples;

    }

    public static List<Double> amplify(List<Double> samples, int ampAmount) {
        return samples.stream().map(d -> d*ampAmount).toList();
    }

    public static void main(String[] args) {
        // var samples = MasiinaEngine.generateOneSecondSample(new Voice(WaveType.SINE.osc(), Note.A.freq()));
        // var samples = MasiinaEngine.generateOneSecondSample(new Voice(WaveType.SINE.osc(), 440), new Voice(WaveType.SINE.osc(), 600));
        var samples = MasiinaEngine.generateOneSecondSample(new Voice(WaveType.SQUARE.osc(), 440));

        System.out.println("Samplesize: " + samples.size());
        samples = reduceSampleResolution(44100, 2, samples);
        // samples = amplify(samples, 10);
        // System.out.println(samples.toString());
        System.out.println("Reduced samplesize: " + samples.size());
        var dftNumers = dft(44100, samples);
        dftNumers.sort(Comparator.comparing(d -> d[1]));
        dftNumers = dftNumers.stream().filter(d -> complexMagnitudeRegisters(d, 10)).toList();
        System.out.println("DFT-values size: " + dftNumers.size());
        double maxImgAmp = Integer.MIN_VALUE;
        double maxRealAmp = Integer.MIN_VALUE;
        for(int i=0;i<10;i++) {
            double[] freqNumbers = dftNumers.get(i);
            System.out.println("Frequency " + freqNumbers[2] + ": (" + freqNumbers[0] + "), i(" + freqNumbers[1] + ")");
            if (Math.abs(freqNumbers[1]) > maxImgAmp) {
                maxImgAmp = freqNumbers[1]; 
            }
            if (Math.abs(freqNumbers[0]) > maxRealAmp) {
                maxRealAmp = freqNumbers[0]; 
            }
        }
    }

    public static boolean complexMagnitudeRegisters(double[] complexNumber, double magnitude) {
        boolean imgRegisters = Math.abs(complexNumber[1]) > magnitude;
        boolean realRegisters = Math.abs(complexNumber[0]) > magnitude;
        return imgRegisters || realRegisters;
    }

}
