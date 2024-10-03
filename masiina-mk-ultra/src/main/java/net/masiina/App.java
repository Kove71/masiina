package net.masiina;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.masiina.ui.KeyEventHandler;
import net.masiina.ui.UiBuilder;
import net.masiina.ui.UiEventHandler;

public final class App {

    public static void main(String[] argv) throws Exception {
        StateSynchronizer.initState();

        JPanel panel = new JPanel();
        UiEventHandler uiEventHandler = new UiEventHandler(panel);
        KeyEventHandler keyEventHandler = new KeyEventHandler();
        // TODO relegate setting up ui to its own class
        UiBuilder ui = new UiBuilder(uiEventHandler); 
        ui.setLayout(new FlowLayout());
        ui.add(panel);
        for (JRadioButton button : ui.setUpOscRadioButtons()) {
            panel.add(button);
        }
        panel.addKeyListener(keyEventHandler);
        ui.setSize(400, 350);
        ui.setVisible(true);
        MasiinaEngine engine = new MasiinaEngine();
        engine.start();

    }
    
}
