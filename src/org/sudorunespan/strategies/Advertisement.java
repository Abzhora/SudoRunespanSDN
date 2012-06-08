package org.sudorunespan.strategies;

import org.powerbot.concurrent.Task;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.util.Time;
import org.sudorunespan.SudoRunespan;
import org.sudorunespan.misc.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 6/6/12
 * Time: 12:28 AM
 */

public final class Advertisement implements Task {
    private final Object lock = new Object();

    public Advertisement(final ActiveScript ctx) {
        ctx.submit(this);
    }

    @Override
    public final void run() {
        if (SudoRunespan.isMembers() && customConfirmDialog()) {
            synchronized (Methods.mouseLock) {
                for (int i = 0; i < 10 && !Tabs.getCurrent().equals(Tabs.FRIENDS_CHAT); i++) {
                    Tabs.FRIENDS_CHAT.open();
                    Time.sleep(200);
                }

                if (Tabs.getCurrent().equals(Tabs.FRIENDS_CHAT)) {
                    if (Widgets.get(1109, 27).validate() && Widgets.get(1109, 27).getTextureId() == 6243) {
                        for (int i = 0; i < 10 && !Widgets.get(1109, 27).click(true); i++) {
                            Time.sleep(1000);
                        }

                        Time.sleep(2500);
                    }

                    if (Widgets.get(1109, 27).validate() && Widgets.get(1109, 27).getTextureId() == 6242) {
                        for (int i = 0; i < 10 && !Widgets.get(1109, 27).click(true); i++) {
                            Time.sleep(1000);
                        }

                        Time.sleep(2500);
                    }

                    if (Widgets.get(752, 4).validate() && Widgets.get(752, 4).getText().contains("Enter the player")) {
                        Keyboard.sendText("First", true);
                    }
                }
            }
        }
    }

    private boolean customConfirmDialog() {
        final JCheckBox box = createDialog();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (final InterruptedException ignored) {
            }
        }

        return box.isSelected();
    }

    private JCheckBox createDialog() {
        final JFrame frame = new JFrame("Friends Chat Anti-ban") {
            @Override
            public void dispose() {
                super.dispose();
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        };
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        final JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.PAGE_AXIS));
        text.add(new JLabel("Would you like to participate in"));
        text.add(new JLabel("an advanced Friends Chat based"));
        text.add(new JLabel("Anti-ban?  Your player will join"));
        text.add(new JLabel("a dicing friends chat and idle"));
        text.add(new JLabel("like many legit players do."));
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JCheckBox checkBox = new JCheckBox("Yes, better Anti-ban!");
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBox.isSelected()) {
                    checkBox.setText("Yes, better Anti-ban!");
                } else {
                    checkBox.setText("No, maybe next time.");
                }
            }
        });
        checkBox.setSelected(true);

        final JButton button = new JButton("OK");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        final JPanel southPanel = new JPanel();
        southPanel.add(checkBox);
        southPanel.add(button);

        frame.add(text, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.pack();

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                checkBox.setSelected(false);
                frame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        return checkBox;
    }

    public final void loadWorldData() {
        synchronized (Methods.mouseLock) {
            for (int i = 0; i < 10 && !Tabs.getCurrent().equals(Tabs.FRIENDS); i++) {
                Tabs.FRIENDS.open();
                Time.sleep(200);
            }
        }

        SudoRunespan.setWorld(Methods.getCurrentWorld());
    }
}
