package net.masiina;

import java.nio.ByteBuffer;
import java.util.Set;

import javax.sound.sampled.AudioFormat;

import net.masiina.osc.Oscillator;

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
    final AudioFormat AUDIO_FORMAT;

    public MasiinaEngine() {
        state = StateSynchronizer.getState();
        AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, BIT_DEPTH, CHANNELS, true, true);;
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
            
            short sample = 0;

            if (notesPlayed.size()==1) {
                Note note = notesPlayed.toArray(new Note[1])[0];
                sample = oscillator.generateSample(note.freq(), time);
            } 
            time+=period;
            buffer.putShort(sample);
            if (!buffer.hasRemaining()) {
                byte[] sampleArray = buffer.array();
                line.write(sampleArray, 0, sampleArray.length);
                buffer.clear();
            }
        }

    }
}
