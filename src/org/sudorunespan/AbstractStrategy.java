package org.sudorunespan;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.methods.Game;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:37 AM
 */

public abstract class AbstractStrategy extends Strategy implements Task {
    private static ActiveScript context;

    static void setContext(final ActiveScript context) {
        AbstractStrategy.context = context;
    }

    @Override
    public final boolean validate() {
        return isValid();
    }

    @Override
    public final void run() {
        process();
    }

    protected abstract boolean isValid();

    protected abstract void process();

    protected final void error(final String s) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, s, "Error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        while (Game.isLoggedIn()) {
            Game.logout(true);
        }
        context.stop();
    }
}
