package net.masiina.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OscillatorState {

    private static final Map<Integer, OscillatorSettings> oscillatorSettingsList = new HashMap<>();

    public static List<OscillatorSettings> getActiveOscillators() {
        return oscillatorSettingsList.values().stream().filter(o -> o.isOn()).toList();
    }

    public static void updateOscillator(OscillatorSettings oscillatorSettings) {
        oscillatorSettingsList.put(oscillatorSettings.getId(), oscillatorSettings); // TODO this actually doesnt do anything as they reference the same object

    }
}
