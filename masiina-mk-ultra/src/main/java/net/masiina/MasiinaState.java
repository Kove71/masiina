package net.masiina;

import java.util.EnumSet;
import java.util.Set;

import net.masiina.osc.Oscillator;
import net.masiina.osc.WaveType;

public class MasiinaState {

    // these should not be mutable, changes to these should not propagate to MasiinaEngine before it has 
    // called current state through StateSynchronizer
    private final Oscillator oscillator;
    private final Set<Note> notesPlayed;

    // 0 attack, 1 decay, 2 sustain, 3 release
    // this should be its own value object so can enforce valid states easily
    // sustain should be a relation to amplitude/volume, integer might not be best way to represent
    private final int[] envelope;
    private final double volume;
    private final int octave;

    private MasiinaState(Oscillator oscillator, Set<Note> notesPlayed, int[] envelope, double volume, int octave) {
        this.oscillator = oscillator;
        this.notesPlayed = notesPlayed;
        this.envelope = envelope;
        this.volume = volume;
        this.octave = octave;
    }

    public Oscillator oscillator() {
        return oscillator;
    }

    public Set<Note> notesPlayed() {
        return notesPlayed;
    }

    public int[] envelope() {
        return envelope;
    }

    public double volume() {
        return volume;
    }

    public int octave() {
        return octave;
    }


    public static class Builder {

        private Oscillator oscillator;
        private Set<Note> notesPlayed;
        private int[] envelope;
        private double volume;
        private int octave;


        public static Builder newState() {
            return new Builder();
        }

        public static Builder fromState(MasiinaState state) {
            return new Builder()
                    .withOscillator(state.oscillator())
                    .withNotesPlayed(state.notesPlayed())
                    .withEnvelope(state.envelope())
                    .withVolume(state.volume());
        }

        public Builder withDefaultValues() {
            this.oscillator = WaveType.SINE.osc();
            this.notesPlayed = EnumSet.noneOf(Note.class);
            this.envelope = new int[]{0, 0, 1, 0};
            this.volume = 1;
            this.octave = 0;
            return this;
        }

        public Builder withOscillator(Oscillator oscillator) {
            if (oscillator == null) throw new IllegalArgumentException("Oscillator must not be null");
            this.oscillator = oscillator;
            return this;
        }

        public Builder withNotesPlayed(Set<Note> notesPlayed) {
            if (notesPlayed == null) throw new IllegalArgumentException("notesPlayed must not be null");
            // could do EnumSet.of(notesPlayed)
            this.notesPlayed = notesPlayed;
            return this;
        }

        public Builder withEnvelope(int[] envelope) {
            if (envelope.length != 4) throw new IllegalArgumentException("Envelope must have attack, decay, sustain and release");
            // TODO handle max values
            this.envelope = envelope;
            return this;
        }

        public Builder withVolume(double volume) {
            if (volume > 1 || volume < 0) throw new IllegalArgumentException("Volume must be between 0 and 1");
            this.volume = volume;
            return this;
        }

        // TODO add withOctave

        public MasiinaState build() {
            return new MasiinaState(oscillator, notesPlayed, envelope, volume, octave);
        }
    }

}
