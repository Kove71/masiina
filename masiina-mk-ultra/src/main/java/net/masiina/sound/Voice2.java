package net.masiina.sound;

import net.masiina.model.OscillatorSettings;
import net.masiina.osc.Oscillator;

public class Voice2 {

    private Oscillator oscillator;
    private Frequency[] frequencies;
    private Envelope envelope;
    private double volume;
    private int envStage = 0;
    private int currentTick = 0;
    private double envVolume = 1.0;
    private double volumeAtRelease = 1;

    public void fooTest() {
        System.out.println("stage " + envStage + " tick " + currentTick + " envVolume " + envVolume);
    }

    public void envTick() {
        switch (envStage) {
            case 0:
                if (currentTick <= envelope.attack()) {
                    envVolume = (double) currentTick * 1.0 / envelope.attack();
                    currentTick++;
                } else {
                    currentTick = 0;
                    envStage = 1;
                }
                break;
            case 1:
                if (currentTick <= envelope.decay()) {
                    envVolume = 1.0 - (double) currentTick * (1.0 - envelope.sustain()) / envelope.decay();
                    currentTick++;
                } else {
                    currentTick = 0;
                    envStage = 2;
                }
                break;
            case 2:
                envVolume = envelope.sustain();
                break;
            case 3:
                if (currentTick <= envelope.release()) {
                    envVolume = volumeAtRelease - (double) currentTick * (volumeAtRelease / envelope.release());
                    currentTick++;
                } else {
                    currentTick = 0;
                    envStage = 4;
                }
                break;
            default:
                envVolume = 0;
                break;
        }
    }

    public void noteReleased() {
        envStage = 3;
        volumeAtRelease = envVolume;
    }

    public double envVolume() {
        return envVolume * volume;
    }

    public static Voice2 fromSettings(OscillatorSettings settings, String... notesPlaying) {
        Frequency frequency = new Frequency();
        frequency.setFineTuning(settings.getFineTuning());
        frequency.setOctave(settings.getOctave());
        frequency.setEq12TempNote(Frequency.Eq12Temp.A);
        Frequency[] freqs = {frequency};
        return new Voice2(settings.getWaveType().osc(), freqs, settings.getEnvelope(), settings.getVolume());
    }

    public Voice2(Oscillator oscillator, Frequency[] frequency, Envelope envelope, double volume) {
        this.oscillator = oscillator;
        this.frequencies = frequency;
        this.envelope = envelope;
        this.volume = volume;
    }

    public Oscillator getOscillator() {
        return oscillator;
    }

    public void setOscillator(Oscillator oscillator) {
        this.oscillator = oscillator;
    }

    public Frequency[] getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(Frequency[] frequency) {
        this.frequencies = frequency;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

}
