package net.masiina.sound;

public record Envelope(
    int attack,
    int decay,
    double sustain,
    int release
) {
    public Envelope withAttack(int attack) {
        return new Envelope(attack, decay(), sustain(), release());
    }

    public Envelope withDecay(int decay) {
        return new Envelope(attack(), decay, sustain(), release());
    }

    public Envelope withSustain(double sustain) {
        return new Envelope(attack(), decay(), sustain, release());
    }

    public Envelope withRelease(int release) {
        return new Envelope(attack(), decay(), sustain(), release);
    }
}
