package net.masiina.model;

import net.masiina.sound.Voice2;

public class CurrentVoice {

    private static Voice2 curVoice2;

    public static void setCurVoice2(Voice2 curVoice2) {
        CurrentVoice.curVoice2 = curVoice2;
    }

    public static Voice2 getCurVoice2() {
        return curVoice2;
    }
}
