package net.masiina;

public class StateSynchronizer {

    private static MasiinaState state;

    // TODO create update methods
    synchronized public static void updateState(MasiinaState state) {
        StateSynchronizer.state = state;
    }

    synchronized public static MasiinaState getState() {
        return state;
    }

    synchronized public static void initState() {
        state = MasiinaState.Builder.newState()
            .withDefaultValues()
            .build();
    }
}
