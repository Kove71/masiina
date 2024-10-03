package net.masiina.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumSet;

import net.masiina.MasiinaState;
import net.masiina.Note;
import net.masiina.StateSynchronizer;

public class KeyEventHandler implements KeyListener {

    private EnumSet<Note> notesPlayed;

    // TODO change to map or something
    private static final Note[] notes = new Note[100];

    static {
        notes[KeyEvent.VK_A] = Note.A;
        notes[KeyEvent.VK_S] = Note.B;
        notes[KeyEvent.VK_D] = Note.C;
        notes[KeyEvent.VK_F] = Note.D;
        notes[KeyEvent.VK_G] = Note.E;
        notes[KeyEvent.VK_H] = Note.F;
        notes[KeyEvent.VK_J] = Note.G;
    }

    public KeyEventHandler() {
        this.notesPlayed = EnumSet.noneOf(Note.class);
    }

     // TODO change to StateSynchronizer update methods
    @Override
    public void keyPressed(KeyEvent e) {
        Note note = notes[e.getKeyCode()];
        if (!notesPlayed.contains(note)) {
            notesPlayed.add(notes[e.getKeyCode()]);
            StateSynchronizer.updateState(
                    MasiinaState.Builder.fromState(StateSynchronizer.getState())
                            .withNotesPlayed(notesPlayed)
                            .build());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Note note = notes[e.getKeyCode()];
        if (notesPlayed.contains(note)) {
            notesPlayed.remove(notes[e.getKeyCode()]);
            StateSynchronizer.updateState(
                    MasiinaState.Builder.fromState(StateSynchronizer.getState())
                            .withNotesPlayed(notesPlayed)
                            .build());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
