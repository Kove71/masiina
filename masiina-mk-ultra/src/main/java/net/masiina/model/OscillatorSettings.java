package net.masiina.model;

import net.masiina.osc.WaveType;
import net.masiina.sound.Envelope;

public class OscillatorSettings {
    private int id;
    private WaveType waveType;
    private Envelope envelope;
    private double fineTuning;
    private double volume;
    private int octave;
    private boolean on;

    public OscillatorSettings(int id, WaveType oscillator, Envelope envelope, double fineTuning, double volume, int octave, boolean on) {
        this.id = id;
        this.waveType = oscillator;
        this.envelope = envelope;
        this.fineTuning = fineTuning;
        this.volume = volume;
        this.octave = octave;
        this.on = on;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WaveType getWaveType() {
        return waveType;
    }
    public void setWaveType(WaveType oscillator) {
        this.waveType = oscillator;
    }
    public Envelope getEnvelope() {
        return envelope;
    }
    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }
    public double getFineTuning() {
        return fineTuning;
    }
    public void setFineTuning(double fineTuning) {
        this.fineTuning = fineTuning;
    }
    public double getVolume() {
        return volume;
    }
    public void setVolume(double volume) {
        this.volume = volume;
    }
    public int getOctave() {
        return octave;
    }
    public void setOctave(int octave) {
        this.octave = octave;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public String toString() {
        return "OscillatorSettings [id=" + id + ", waveType=" + waveType + ", envelope=" + envelope + ", fineTuning="
                + fineTuning + ", volume=" + volume + ", octave=" + octave + ", on=" + on + "]";
    }

}
