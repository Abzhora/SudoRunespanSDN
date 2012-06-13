package org.sudorunespan.strategies;

import org.powerbot.concurrent.Task;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.sudorunespan.SudoRunespan;
import org.sudorunespan.misc.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 6/6/12
 * Time: 12:28 AM
 */

public final class Advertisement implements Task {
    private final Pattern clanNamePattern = Pattern.compile("Owner: <col=ffff64>First");
    private final Object lock = new Object();
    private boolean loaded;

    public Advertisement(final ActiveScript ctx) {
        ctx.submit(this);
        loaded = false;
    }

    @Override
    public final void run() {
        if (SudoRunespan.isMembers() && customConfirmDialog()) {
            synchronized (Methods.mouseLock) {
                while (!loaded) {
                    if (!Tabs.getCurrent().equals(Tabs.FRIENDS_CHAT)) {
                        for (int i = 0; i < 10 && !Tabs.getCurrent().equals(Tabs.FRIENDS_CHAT); i++) {
                            Tabs.FRIENDS_CHAT.open();
                            Time.sleep(400);
                        }
                    } else {
                        if (Widgets.get(752).validate() && Widgets.get(752, 4).visible()) {
                            Keyboard.sendText("First", true);
                            Time.sleep(1000);
                        } else if (Widgets.get(1109, 27).validate() && Widgets.get(1109, 27).getTextureId() == 6243) {
                            final Matcher matcher = clanNamePattern.matcher(Widgets.get(1109, 1).getText());
                            if (matcher.find()) {
                                System.out.println(getNumUsers());

                                if (getNumUsers() > 80) {
                                    if (leaveFriendsChat()) {
                                        loaded = true;
                                    }
                                } else {
                                    loaded = true;
                                }
                            } else {
                                leaveFriendsChat();
                            }
                        } else if (Widgets.get(1109, 27).validate() && Widgets.get(1109, 27).getTextureId() == 6242) {
                            for (int i = 0; i < 10 && !Widgets.get(1109, 27).click(true); i++) {
                                Time.sleep(1000);
                            }
                        }

                        Time.sleep(1000);
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

    private int getNumUsers() {
        WidgetChild user = Widgets.get(1109).getChild(5);
        return user.getChildren().length;
    }

    private static boolean leaveFriendsChat() {
        int i;
        for (i = 0; i < 10 && !Widgets.get(1109, 27).click(true); i++) {
            Time.sleep(1000);
        }
        return i != 10;
    }
}
