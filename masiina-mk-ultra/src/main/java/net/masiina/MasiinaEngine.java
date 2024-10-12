package net.masiina;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.AudioFormat;

import net.masiina.osc.Oscillator;
import net.masiina.osc.WaveType;
import net.masiina.osc.impl.SineOscillator;
import net.masiina.sound.Envelope;
import net.masiina.sound.Voice;
import net.masiina.sound.Voice2;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MasiinaEngine extends Thread {

    // one sample = 16bits or 2 bytes
    // array containing 4 samples = 4*2 bytes, 8 bytes
    // 1 sample equals  (1 / 44100) * 1000 = 0.0226757369615ms of time
    // so c. 50 samples = 1ms
    // one byte array to be sended could be 50 samples long then?
    // 50*2 = 100 bytes
    // 128 bytes to round out (so a byte array is around 1.5ms)
    // buffer of 4 arrays?
    // 128*4 = 512
    static final int SAMPLE_RATE = 44100; // samples per second
    static final int BIT_DEPTH = 16; // how many bits in one sample, i.e how many discrete amplitude values can be represented
    static final int CHANNELS = 1; // 1=mono, 2= stereo
    static final int PACKET_SIZE = 128; // how many bytes are sent at a time to audio queue (64 samples if bitdepth is 16 and channels 1)
    static final int BUFFER_SIZE = 512; // how many bytes can be in the sourcedataline queue at any given time (4 packets so around 6ms latency)
    static final int BIT_RATE = SAMPLE_RATE * BIT_DEPTH * CHANNELS; // amount of bits that must be fed per second
    static final int BYTE_RATE = BIT_RATE / 8; // amount of bytes per second

    MasiinaState state;
    static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, BIT_DEPTH, CHANNELS, true, true);

    public MasiinaEngine() {
        state = StateSynchronizer.getState();
        // AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, BIT_DEPTH, CHANNELS, true, true);
    }

    @Override
    public void run() {
        try(SourceDataLine line = AudioSystem.getSourceDataLine(AUDIO_FORMAT)) {
            ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
            line.open(AUDIO_FORMAT, BUFFER_SIZE);
            line.start();
            generateSound(line, buffer);
            line.drain();
            line.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void generateSound(SourceDataLine line, ByteBuffer buffer) {
        double time = 0.0;
        double period = (double) 1 / SAMPLE_RATE; // one sample period is 1/44100 = 0.00002267573696145124 seconds
        while(true) {
            state = StateSynchronizer.getState(); // how often should state be updated? every sample probably too heavy but latency could be a problem
            Set<Note> notesPlayed = state.notesPlayed(); // TODO this is grabbing the same instance that is in KeyEventHandler, should create copy
            // Cannot invoke "net.masiina.Note.freq()" because "note" is null
            Oscillator oscillator = state.oscillator();
            int[] envelope = state.envelope(); // 0 attack, 1 decay, 2 sustain, 3 release
            double volume = state.volume(); // amplitude, max value 1 and min 0. multiply sample by this

            // always divide frequency by two to get lower octave and multiply by two to get higher octave.
            // so if octave < 0, freq=note.freq/(2^|octave|) and if octave >= 0 freq=note.freq*(2^|octave|).
            int octave = state.octave();

            double sample = 0;

            if (notesPlayed.size()==1) {
                Note note = notesPlayed.toArray(new Note[1])[0];
                sample = oscillator.generateSample(note.freq(), time, 0.0);
            }

            time+=period;
            buffer.putShort((short) (sample * Short.MAX_VALUE));
            if (!buffer.hasRemaining()) {
                byte[] sampleArray = buffer.array();
                line.write(sampleArray, 0, sampleArray.length);
                buffer.clear();
            }
        }
    }

    public static List<Double> generateWave(int waveAmt, Voice... voices) {
        if (voices == null || voices.length < 1) throw new IllegalArgumentException("Must have at least one voice");
        Voice baseVoice = voices[0];
        System.out.println("CALLING");
        double time = 0.0;
        double period = (double) 1 / SAMPLE_RATE;
        System.out.println("PERIOD (ms)=" + period * 1000);
        double waveLength = 1 / (double) baseVoice.frequency(); // frequency,
        System.out.println("wavelength (ms)=" + waveLength * 1000);
        // Oscillator oscillator = new SineOscillator();
        List<Double> samples = new ArrayList<>((int)(waveLength/period));
        System.out.println("initial length=" + samples.size());
        List<Double> voiceSamples = new ArrayList<>();
        for (time=0.0; time<=waveLength*waveAmt; time+=period) {
            for (Voice voice : voices) {
                double voiceSample = voice.oscillator().generateSample(voice.frequency(), time, 0.0);
                voiceSamples.add(voiceSample);
            }
            double sample = voiceSamples.stream().reduce(0.0, (a,b)->a+b) / voices.length;
            samples.add(sample);
            voiceSamples.clear();
        }
        System.out.println("final length=" + samples.size());
        return samples;
    }

    public void musicMakerTest(SourceDataLine line, ByteBuffer buffer, Voice... voices) {
        int length = 5*SAMPLE_RATE;
        double time = 0.0;
        double period = (double) 1 / SAMPLE_RATE;
        Voice baseVoice = voices[0];
        List<Double> samples = new ArrayList<>(length);
        List<Double> voiceSamples = new ArrayList<>(voices.length);
        int phase = 0;
        boolean first = true;
        for (int i = 0;i<length;i++) {
            for (Voice voice : voices) {
                double voiceSample = voice.oscillator().generateSample(voice.frequency(), time, 0.0);
                voiceSamples.add(voiceSample);
            }
            time+=period;
            double sample = voiceSamples.stream().reduce(0.0, (a,b)->a+b) / voices.length;
            if (first) {
                System.out.println(sample);
                first = false;
            }
            if (sample > 1.0) System.out.println(sample);
            samples.add(sample);
            voiceSamples.clear();
            buffer.putShort((short) (sample * Short.MAX_VALUE));
            if (!buffer.hasRemaining()) {
                byte[] sampleArray = buffer.array();
                line.write(sampleArray, 0, sampleArray.length);
                buffer.clear();
            }
        }
        double min = (double) Integer.MAX_VALUE;
        double max = (double) Integer.MIN_VALUE;
        for (double sample : samples) {
            if (sample < min) {
                min = sample;
            }
            if (sample > max) {
                max = sample;
            }
        }
        System.out.println("max: "+ max + " min:" + min);
    }

    public static List<Double> generateOneSecondSample(Voice... voices) {
        if (voices == null || voices.length < 1) throw new IllegalArgumentException("Must have at least one voice");
        long startTime = System.currentTimeMillis();
        double time = 0.0;
        System.out.println();
        double period = (double) 1 / SAMPLE_RATE;
        List<Double> samples = new ArrayList<>(SAMPLE_RATE);
        List<Double> voiceSamples = new ArrayList<>();
        for (int i=0;i<SAMPLE_RATE;i++) {
            for (Voice voice : voices) {
                double voiceSample = voice.oscillator().generateSample(voice.frequency(), time, 0.0);
                voiceSamples.add(voiceSample);
            }
            double sample = voiceSamples.stream().reduce(0.0, (a,b)->a+b) / voices.length;
            samples.add(sample);
            time+=period;
            voiceSamples.clear();
        }
        System.out.println("Took " + (System.currentTimeMillis()-startTime) + "ms to generate 1 second of sound with " + voices.length + " voices.");
        return samples;
    }

    /**
     *
     * @param sampleAmtBase pref value between 4 max 16. 7 generates 128 or 1.5ms of samples
     * @param voices
     * @return
     */
    public static List<Double> generateFixedAmtSamples(int sampleAmtExponent, Voice... voices) {
        if (voices == null || voices.length < 1) throw new IllegalArgumentException("Must have at least one voice");
        int sampleAmt = (int) Math.pow(2, sampleAmtExponent);
        long startTime = System.currentTimeMillis();
        double time = 0.0;
        System.out.println();
        double period = (double) 1 / SAMPLE_RATE;
        List<Double> samples = new ArrayList<>(SAMPLE_RATE);
        List<Double> voiceSamples = new ArrayList<>();
        for (int i=0;i<sampleAmt;i++) {
            for (Voice voice : voices) {
                double voiceSample = voice.oscillator().generateSample(voice.frequency(), time, 0.0);
                voiceSamples.add(voiceSample);
            }
            double sample = voiceSamples.stream().reduce(0.0, (a,b)->a+b) / voices.length;
            samples.add(sample);
            time+=period;
            voiceSamples.clear();
        }
        System.out.println("Took " + (System.currentTimeMillis()-startTime) + "ms to generate " + samples.size() + " samples with " + voices.length + " voices.");
        return samples;
    }

    /**
     *
     * @param sampleAmtBase pref value between 4 max 16. 7 generates 128 or 1.5ms of samples
     * @param voices
     * @return
     */
    public static List<Double> generateFixedAmtSamples(int sampleAmtExponent, Voice2... voices) {
        if (voices == null || voices.length < 1) throw new IllegalArgumentException("Must have at least one voice");
        int sampleAmt = (int) Math.pow(2, sampleAmtExponent);
        long startTime = System.currentTimeMillis();
        double time = 0.0;
        System.out.println();
        double period = (double) 1 / SAMPLE_RATE;
        List<Double> samples = new ArrayList<>(SAMPLE_RATE);
        List<Double> voiceSamples = new ArrayList<>();
        boolean noteReleased = false;
        for (int i=0;i<sampleAmt;i++) {
            for (Voice2 voice : voices) {
                if (i > 0.75*sampleAmt && !noteReleased) {
                    voice.noteReleased();
                    noteReleased = true;
                }
                voice.envTick();
                double volume = voice.envVolume();
                double frequency = voice.getFrequencies()[0].getFreq();
                double voiceSample = volume*voice.getOscillator().generateSample(frequency, time, 0.0);
                voiceSamples.add(voiceSample);
            }
            double sample = voiceSamples.stream().reduce(0.0, (a,b)->a+b) / voices.length;
            samples.add(sample);
            time+=period;
            voiceSamples.clear();
        }
        System.out.println("Took " + (System.currentTimeMillis()-startTime) + "ms to generate " + samples.size() + " samples with " + voices.length + " voices.");
        return samples;
    }

    public static void playSample(List<Double> samples) {
        try(SourceDataLine line = AudioSystem.getSourceDataLine(AUDIO_FORMAT)) {
            System.out.println("RUNNING");
            ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
            line.open(AUDIO_FORMAT, BUFFER_SIZE);
            line.start();
            for (double sample : samples) {
                buffer.putShort((short) (sample * Short.MAX_VALUE));
                if (!buffer.hasRemaining()) {
                    byte[] sampleArray = buffer.array();
                    line.write(sampleArray, 0, sampleArray.length);
                    buffer.clear();
                }
            }
            while(line.available() < 500) {
                sleep(100L);
            }
            System.out.println("Sample played");
            line.drain();
            line.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MasiinaEngine engine = new MasiinaEngine();
        AudioFormat af = AUDIO_FORMAT;
        new Envelope(20, 20, 0.8, 10);
        // Voice[] voices = {new Voice(WaveType.SINE.osc(), Note.A.freq()), new Voice(WaveType.SINE.osc(), Note.C.freq()), new Voice(WaveType.SINE.osc(), Note.E.freq())};
        Voice[] voices = {new Voice(WaveType.SAW.osc(), Note.A.freq()), new Voice(WaveType.SINE.osc(), Note.G.freq())};
        try(SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
            System.out.println("RUNNING");
            ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
            line.open(af, BUFFER_SIZE);
            line.start();
            engine.musicMakerTest(line, buffer, voices);
            line.drain();
            line.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
