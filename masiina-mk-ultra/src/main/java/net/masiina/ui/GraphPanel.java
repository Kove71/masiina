package net.masiina.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import net.masiina.MasiinaEngine;
import net.masiina.Note;
import net.masiina.model.OscillatorState;
import net.masiina.osc.WaveType;
import net.masiina.sound.Voice;
import net.masiina.sound.Voice2;

import static net.masiina.sound.Fourier.*;

class GraphPanel extends JPanel implements ActionListener {

    static int panelWidth = 300;
    static int panelHeight = 400;
    static int panelX = 100;
    static int panelY = 20;
    boolean drawDft = false;
    boolean drawWave = false;
    List<double[]> dftGraph;
    List<Double> waveGraph;

    GraphPanel() {
        setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));
        setBackground(ColorPalette.BACKGROUND);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

    public void drawWaveGraph(Graphics2D g2) {
        if (waveGraph == null)
            return;
        final int SAMPLE_AMT = waveGraph.size();
        int yBottom = panelHeight - panelY;
        double yRelation = (double) (panelHeight) / 2 - panelY; // max amplitude is 2
        double xRelation = (double) panelWidth / SAMPLE_AMT;
        // double xRelation = (SAMPLE_AMT > panelWidth) ? (double) panelWidth /
        // SAMPLE_AMT : (double) SAMPLE_AMT / panelWidth;
        g2.setColor(new Color(58, 191, 28));
        for (int i = 0; i < waveGraph.size() - 1; i++) {
            double firstPoint = waveGraph.get(i);
            double firstAmplitude = (firstPoint + 1) * yRelation; // inverse because y coordinates go from big to small
            double secondPoint = waveGraph.get(i + 1);
            double secondAmplitude = (secondPoint + 1) * yRelation;
            Line2D line = new Line2D.Double(i * xRelation, yBottom - firstAmplitude, (i + 1) * xRelation,
                    yBottom - secondAmplitude);
            g2.draw(line);
        }
        g2.setColor(Color.GRAY);
        Line2D centerY = new Line2D.Double(0, panelHeight / 2, panelWidth, panelHeight / 2);
        g2.draw(centerY);
    }

    public void drawDFTGraph(Graphics2D g2) {
        if (dftGraph == null)
            return;
        final int MAX_FREQ = 22050;
        double maxAmp = 2000.0; // TODO variable according to actual max amp
        double yRelation = (double) panelHeight / maxAmp;
        // double xRelation = (double) MAX_FREQ / panelWidth;
        // double xRelation = panelWidth / (Math.log(MAX_FREQ) / Math.log(2));
        double xRelation = panelWidth / (Math.log(MAX_FREQ) / Math.log(2));
        for (int i = 0; i < dftGraph.size(); i++) {
            double[] number = dftGraph.get(i);
            // double relativeFreq = (Math.log(number[2]) / Math.log(2));
            double relativeFreq = (Math.log(number[2]) / Math.log(2));
            double xPosition = xRelation * relativeFreq; // TODO doesnt lineup with the x values
            // double xPosition = xRelation*number[2]*(double)i;
            double yHeight = Math.abs(number[1]) * yRelation;
            Line2D line = new Line2D.Double(xPosition, panelHeight - 20 - panelY, xPosition,
                    panelHeight - yHeight - 20 - panelY);
            // System.out.printf("xPosition for freq %-6.2f: %10.2f", xPosition, number[1]);
            Stroke stroke = new BasicStroke(2);
            g2.setColor(ColorPalette.GRAPH);
            g2.draw(stroke.createStrokedShape(line));
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        panelX = getX();
        panelY = getY();
        panelWidth = getWidth();
        panelHeight = getHeight();
        drawFrequencyXValues(g2);
        if (drawDft) {
            drawDFTGraph(g2);
        }
        if (drawWave) {
            drawWaveGraph(g2);
        }
        // Draw Text
        // g.drawString("This is my custom Panel!", 10, 20);
        // drawGraph(g, null);
    }

    public void drawFrequencyXValues(Graphics2D g2) {
        System.out.println(panelWidth);
        final int MAX_FREQ = 22050;
        double xRelation = (double) MAX_FREQ / panelWidth;
        double period = 10;
        double xPeriod = panelWidth / period;
        for (int i = 0; i < panelWidth - xPeriod; i += xPeriod) {
            // double freqValue = i * (Math.log(i*xRelation) / Math.log(2));
            double freqValue = MAX_FREQ / Math.pow(2, period - i / xPeriod); // these dont work right
            if (i != 0) {
                g2.setColor(ColorPalette.TEXT);
                g2.drawString(String.valueOf((int) freqValue), i, panelHeight - 5);
            }
        }
    }

    // TODO change from swing workers to a better multithreading solution
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("DFT")) {
            var dft = getDftSamples();
            this.dftGraph = dft;
            drawDft = true;
            drawWave = false;
            repaint();
            // dftWorker.execute();
            // try {
            //     var dft = dftWorker.get();
            //     this.dftGraph = dft;
            //     drawDft = true;
            //     drawWave = false;
            //     repaint();
            // } catch (InterruptedException | ExecutionException e1) {
            //     e1.printStackTrace();
            // }
        } else if (e.getActionCommand().equals("WAVE")) {
            System.out.println("Calling wave button");
            List<Double> wave = getWaveSamples();
            this.waveGraph = wave;
            drawDft = false;
            drawWave = true;
            repaint();
            // waveWorker.execute();
            // try {
            //     List<Double> wave = waveWorker.get();
            //     this.waveGraph = wave;
            //     drawDft = false;
            //     drawWave = true;
            //     repaint();
            // } catch (InterruptedException | ExecutionException e1) {
            //     e1.printStackTrace();
            // }
        } else if (e.getActionCommand().equals("PLAY")) {
            List<Double> wave = getWaveSamples();
            MasiinaEngine.playSample(wave);

        }
    }

    // TODO async
    private List<Double> getWaveSamples() {
        // List<Voice> voices = OscillatorState.getActiveOscillators().stream()
        //         .map(s -> Voice2.fromSettings(s, "null"))
        //         .map(v2 -> new Voice(v2.getOscillator(), v2.getFrequencies()[0].getFreq()))
        //         .toList();
        // System.out.println(voices); // TODO this executes only once
        // Voice[] voisArr = new Voice[voices.size()];

        // var samples = MasiinaEngine.generateFixedAmtSamples(10, voices.toArray(voisArr));
        List<Voice2> voices = OscillatorState.getActiveOscillators().stream()
                .map(s -> Voice2.fromSettings(s, "null"))
                .toList();
        Voice2[] voisArr = new Voice2[voices.size()];
        var samples = MasiinaEngine.generateFixedAmtSamples(15, voices.toArray(voisArr));
        return samples;
    }

    private List<double[]> getDftSamples() {
        List<Voice> voices = OscillatorState.getActiveOscillators().stream()
                .map(s -> Voice2.fromSettings(s, "null"))
                .map(v2 -> new Voice(v2.getOscillator(), v2.getFrequencies()[0].getFreq()))
                .toList();
        Voice[] voisArr = new Voice[voices.size()];

        var samples = MasiinaEngine.generateFixedAmtSamples(12, voices.toArray(voisArr));
        System.out.println("Samplesize: " + samples.size());
        var dftNumers = dft(44100, samples, (int) Math.pow(2, 12));
        dftNumers.sort(Comparator.comparing(d -> d[1]));
        dftNumers = dftNumers.stream().filter(d -> complexMagnitudeRegisters(d, 0)).toList();
        System.out.println("DFT-values size: " + dftNumers.size());
        double maxImgAmp = Integer.MIN_VALUE;
        double maxRealAmp = Integer.MIN_VALUE;
        for (int i = 0; i < 10; i++) {
            double[] freqNumbers = dftNumers.get(i);
            System.out.println(
                    "Frequency " + freqNumbers[2] + ": (" + freqNumbers[0] + "), i(" + freqNumbers[1] + ")");
            if (Math.abs(freqNumbers[1]) > maxImgAmp) {
                maxImgAmp = freqNumbers[1];
            }
            if (Math.abs(freqNumbers[0]) > maxRealAmp) {
                maxRealAmp = freqNumbers[0];
            }
        }
        return dftNumers;
    }

    SwingWorker<List<Double>, Void> waveWorker = new SwingWorker<List<Double>, Void>() {
        @Override
        public List<Double> doInBackground() {
            // Voice sineA = new Voice(WaveType.SINE.osc(), Note.A.freq());
            // Voice sawC = new Voice(WaveType.SAW.osc(), Note.C.freq());
            // Voice squareA = new Voice(WaveType.SQUARE.osc(), Note.G.freq());
            // // List<Double> samples = MasiinaEngine.generateWave(8, sineA, squareA);
            // List<Double> samples = MasiinaEngine.generateWave(8, sineA);
            Voice sineA = new Voice(WaveType.SINE.osc(), Note.A.freq());
            Voice sineF = new Voice(WaveType.SINE.osc(), Note.F.freq());
            Voice sawC = new Voice(WaveType.SAW.osc(), Note.C.freq());
            Voice squareA = new Voice(WaveType.SQUARE.osc(), Note.G.freq());
            // var samples = MasiinaEngine.generateOneSecondSample(sineA, sineF, sawC,
            // squareA);
            System.out.println(OscillatorState.getActiveOscillators());
            List<Voice> voices = OscillatorState.getActiveOscillators().stream()
                    .map(s -> Voice2.fromSettings(s, "null"))
                    .map(v2 -> new Voice(v2.getOscillator(), v2.getFrequencies()[0].getFreq()))
                    .toList();
            System.out.println(voices); // TODO this executes only once
            Voice[] voisArr = new Voice[voices.size()];
            var samples = MasiinaEngine.generateFixedAmtSamples(10, voices.toArray(voisArr));
            return samples;
        }

        @Override
        public void done() {
            try {
                var samples = get();
                waveGraph = samples;
                drawDft = false;
                drawWave = true;
                repaint();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    SwingWorker<List<double[]>, Void> dftWorker = new SwingWorker<List<double[]>, Void>() {
        @Override
        public List<double[]> doInBackground() {
            Voice sineA = new Voice(WaveType.SINE.osc(), Note.A.freq());
            Voice sineF = new Voice(WaveType.SINE.osc(), Note.F.freq());
            Voice sawC = new Voice(WaveType.SAW.osc(), Note.C.freq());
            Voice squareA = new Voice(WaveType.SQUARE.osc(), Note.G.freq());
            // var samples = MasiinaEngine.generateOneSecondSample(sineA, sineF, sawC,
            // squareA);

            List<Voice> voices = OscillatorState.getActiveOscillators().stream()
                    .map(s -> Voice2.fromSettings(s, "null"))
                    .map(v2 -> new Voice(v2.getOscillator(), v2.getFrequencies()[0].getFreq()))
                    .toList();
            Voice[] voisArr = new Voice[voices.size()];

            var samples = MasiinaEngine.generateFixedAmtSamples(12, voices.toArray(voisArr));

            // var samples = MasiinaEngine.generateOneSecondSample(new
            // Voice(WaveType.SINE.osc(), Note.A.freq()));
            // var samples = MasiinaEngine.generateOneSecondSample(new
            // Voice(WaveType.SINE.osc(), 440), new Voice(WaveType.SINE.osc(), 600));
            // var samples = MasiinaEngine.generateOneSecondSample(new
            // Voice(WaveType.SQUARE.osc(), 440));

            System.out.println("Samplesize: " + samples.size());
            // samples = reduceSampleResolution(44100, 2, samples);
            // samples = amplify(samples, 10);
            // System.out.println(samples.toString());
            // System.out.println("Reduced samplesize: " + samples.size());
            var dftNumers = dft(44100, samples, (int) Math.pow(2, 12));
            dftNumers.sort(Comparator.comparing(d -> d[1]));
            dftNumers = dftNumers.stream().filter(d -> complexMagnitudeRegisters(d, 0)).toList();
            System.out.println("DFT-values size: " + dftNumers.size());
            double maxImgAmp = Integer.MIN_VALUE;
            double maxRealAmp = Integer.MIN_VALUE;
            for (int i = 0; i < 10; i++) {
                double[] freqNumbers = dftNumers.get(i);
                System.out.println(
                        "Frequency " + freqNumbers[2] + ": (" + freqNumbers[0] + "), i(" + freqNumbers[1] + ")");
                if (Math.abs(freqNumbers[1]) > maxImgAmp) {
                    maxImgAmp = freqNumbers[1];
                }
                if (Math.abs(freqNumbers[0]) > maxRealAmp) {
                    maxRealAmp = freqNumbers[0];
                }
            }
            return dftNumers;
        }
    };

}
