package net.masiina;

public enum Note {
    A(220),
    B(246.942),
    C(261.626),
    D(293.665),
    E(329.628),
    F(349.228),
    G(391.995);

    private final double freq;

    private Note(double freq) {
        this.freq = freq;
    }

    public double freq() {
        return freq;
    }
}
