package net.masiina;

import org.junit.jupiter.api.Test;

import net.masiina.osc.WaveType;
import net.masiina.osc.impl.SineOscillator;
import net.masiina.sound.Envelope;
import net.masiina.sound.Frequency;
import net.masiina.sound.Voice;
import net.masiina.sound.Voice2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    void testApp() {
        assertEquals(1, 1);
    }

    @Test
    void foo() {
        System.out.println("aawdwa");
        int PADDING = 20;
        int WIDTH = 400;
        int HEIGHT = 300;
        List<Double> samples = MasiinaEngine.generateWave(1, new Voice(WaveType.SINE.osc(), 440), new Voice(WaveType.SAW.osc(), Note.C.freq()));
        System.out.println(samples);
        List<int[]> graphPlot = new ArrayList<>(samples.size());
        int x = PADDING;
        for (double sample : samples) {
            sample = (sample + 1) / 2;
            int xIncrement =  (WIDTH-PADDING*2) / samples.size();
            graphPlot.add(new int[]{x, (int) (sample * (double) (HEIGHT - PADDING*2) + PADDING)});
            x += xIncrement;
        }
        String plotStr = graphPlot.stream().map(arr -> Arrays.toString(arr)).collect(Collectors.joining(","));
        System.out.println(plotStr);
    }

    @Test
    void testFreq() {
        Frequency frequency = new Frequency();

        frequency.setOctave(6);
        for (Frequency.Eq12Temp note : Frequency.Eq12Temp.values()) {
            frequency.setEq12TempNote(note);
            System.out.println(frequency.getFreq());
        }
        frequency.setEq12TempNote(Frequency.Eq12Temp.C_S);
        frequency.setOctave(1);
        // System.out.println(frequency.getFreq());
    }
    @Test
    void testFreq2() {
        Frequency frequency = new Frequency();
        frequency.setEq12TempNote(Frequency.Eq12Temp.F);
        List<Double> freqs = new ArrayList<>();
        for (int i=-4;i<6;i++) {
            frequency.setOctave(i);
            double freq = frequency.getFreq();
            freqs.add(freq);
            System.out.println(freq);

            // for (Frequency.Eq12Temp note : Frequency.Eq12Temp.values()) {
            //     frequency.setEq12TempNote(note);
            //     double freq = frequency.getFreq();
            //     if (freq > 22050) {
            //         break;
            //     }
            //     freqs.add(freq);
            // }
        }
        System.out.println(freqs.size());
    }

    @Test
    void testLog() {
        Frequency frequency = new Frequency();
        frequency.setEq12TempNote(Frequency.Eq12Temp.F);
        List<double[]> freqs = new ArrayList<>();
        for (int i=-5;i<6;i++) {
            frequency.setOctave(i);
            double freq = frequency.getFreq();
            double value = Math.log(freq) / Math.log(2);
            freqs.add(new double[]{freq, value});
            System.out.println(String.format("Freq: %8.2f | Log: %5.2f", freq, value));
        }
        double testWidth = 1200;
        double maxFreq = 22050;
        // double xRelation = maxFreq / testWidth;
        double xRelation = testWidth / 14.428491035332245;
        // double relativeFreq = freqs.get(0)[0];
        // double xPosition = xRelation * relativeFreq;
        System.out.println(xRelation);
        System.out.println(Math.log(maxFreq) / Math.log(2));
        for (double[] logValue : freqs) {
            double relativeFreq = logValue[1];
            double xPosition = xRelation * relativeFreq - 200;
            System.out.printf("log: %.2f, x-position: %.2f%n",relativeFreq, xPosition);

        }
        // 100 = 10
        // 200 = 21
        // 300 = 42
        // 400 = 85
        // 500 = 171
        // 600 = 343
        // double yRelation = (double) getHeight() / maxAmp;
        // double xRelation = (double) MAX_FREQ / getWidth();
        // if (dft == null ) return;
        // for (int i=0;i<dft.size();i++) {
        //     double[] number = dft.get(i);
        //     double relativeFreq = (Math.log(number[2]) / Math.log(2));
        //     double xPosition = xRelation*relativeFreq;
        //     // double xPosition = xRelation*number[2]*(double)i;
    }
    @Test
    public void testEnvelopeTicks() {
        Frequency freq = new Frequency();
        freq.setEq12TempNote(Frequency.Eq12Temp.A);
        freq.setOctave(0);
        freq.setFineTuning(1);
        Voice2 voice2 = new Voice2(new SineOscillator(), new Frequency[]{freq}, new Envelope(5, 5, 0.5, 20), 1);

        for (int i=0;i<60;i++) {
            if (i == 20) {
                voice2.noteReleased();
                System.out.println("note released");
            }
            voice2.envTick();
            System.out.print("index " + i + ": ");
            voice2.fooTest();
            System.out.println(voice2.envVolume());
        }
    }
}
