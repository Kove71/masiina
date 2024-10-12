package net.masiina.sound;

public class Frequency {
    
    private Class<? extends Tuning> tuning = Eq12Temp.class;
    private int octave = 0;
    private double fineTuning = 1;
    private Eq12Temp eq12TempNote;
    private double freq = 440;

    public double getFreq() {
        freq = 440;
        if (tuning == Eq12Temp.class) {
            freq = eq12TempNote.getStdFreq();
            freq = freq * fineTuning;
            double octaveRel = Math.pow(2, Math.abs(octave));
            freq = (octave >= 0) ? freq * octaveRel : freq / octaveRel;
        }
        return freq;
    }

    private void updateFreq() { // called whenever a setter is used
        if (tuning == Eq12Temp.class) {
            freq = eq12TempNote.getStdFreq();
            freq = freq * fineTuning;
            double octaveRel = Math.pow(2, Math.abs(octave));
            freq = (octave >= 0) ? freq * octaveRel : freq / octaveRel;
        }
    }
    
    public interface Tuning {
        double getStdFreq();      
    }

    public static enum Eq12Temp implements Tuning {
        C,
        C_S,
        D,
        D_S,
        E,
        F,
        F_S,
        G,
        G_S,
        A,
        A_S,
        B;

        public static final int REF_FREQ = 440; // reference frequency (hz) of A4
        public static final int A_ORDINAL = 9;
        public static final double TWELTH_RT_OF_2 = 1.0594631;  

        private final double stdFreq;

        private Eq12Temp() {
            int relationToA = ordinal() - A_ORDINAL;
            // this is the relation of one notes frequency to anothers in the
            // 12 equal temperament tuning system, which is the standard in Western music
            stdFreq = REF_FREQ * Math.pow(TWELTH_RT_OF_2, relationToA);
        }

        public double getStdFreq() {
            return stdFreq;
        }
    }

    public void setTuning(Class<? extends Tuning> tuning) {
        this.tuning = tuning;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public void setFineTuning(double fineTuning) {
        this.fineTuning = fineTuning;
    }

    public void setEq12TempNote(Eq12Temp eq12TempNote) {
        this.eq12TempNote = eq12TempNote;
    }
}
