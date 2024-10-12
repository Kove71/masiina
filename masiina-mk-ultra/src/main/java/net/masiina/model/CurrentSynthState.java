package net.masiina.model;

import net.masiina.sound.Voice2;

public class CurrentSynthState {

    private final Voice2[] voices;

    public Voice2[] getVoices() {
        return voices;
    }

    public CurrentSynthState(Voice2[] voices) {
        this.voices = voices;
    }
}
