package org.sudorunespan.paint;

import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.bot.event.listener.PaintListener;
import org.sudorunespan.SudoRunespan;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:50 AM
 */

public final class Painter extends Strategy implements PaintListener {
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private final int startXp, startPoints, startLvl;
    private final Timer timer;
    private final MouseTrail mouseTrail;
    private final FloatingWindow window;
    private BufferedImage banner = null;
    private int gainedPoints;


    public Painter() {
        startXp = Skills.getExperience(Skills.RUNECRAFTING);
        startLvl = Skills.getRealLevel(Skills.RUNECRAFTING);
        startPoints = Integer.parseInt(Widgets.get(1274, 2).getText());
        gainedPoints = 0;

        timer = new Timer(0);
        mouseTrail = new MouseTrail();
        window = new FloatingWindow();

    }

    @Override
    public boolean validate() {
        return false;
    }

    public void onRepaint(final Graphics g) {
        if (SudoRunespan.isNodeBlock()) {
            g.setColor(Color.RED);
            g.setFont(new Font(null, Font.BOLD, 20));
            g.drawString("SCRIPT IS BLOCKING NODES", 171, 100);
            g.drawString("GETTING MORE RUNE ESS", 180, 130);
        }

        final Locatable target = SudoRunespan.getTarget();
        if (target != null && target.getLocation().isOnScreen()) {
            g.setColor(Color.RED);
            g.drawPolygon(target.getLocation().getBounds()[0]);
            g.setColor(new Color(255, 0, 0, 50));
            g.fillPolygon(target.getLocation().getBounds()[0]);
        }

        if (Widgets.get(1274).validate()) {
            gainedPoints = Integer.parseInt(Widgets.get(1274, 2).getText()) - startPoints;
        }
        final int gainedXp = Skills.getExperience(Skills.RUNECRAFTING) - startXp;

        g.setColor(Color.GREEN);
        window.draw(g, gainedXp, gainedPoints);
        mouseTrail.add(Mouse.getLocation());
        mouseTrail.draw(g);
        drawMouse(g);

        if (banner != null) {
            g.drawImage(banner, 216, 264, null);
        } else {
            try {
                banner = ImageIO.read(new URL("http://i50.tinypic.com/2edtyeq.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void drawMouse(final Graphics g) {
        g.setColor(Color.GREEN);
        g.drawOval(Mouse.getX() - 5, Mouse.getY() - 5, 11, 11);
        g.fillOval(Mouse.getX() - 2, Mouse.getY() - 2, 5, 5);
    }

    private final static class MouseTrail {
        private final int SIZE = 25;
        private final double ALPHA_STEP = (255.0 / SIZE);
        private final Point[] points;
        private int index;

        public MouseTrail() {
            points = new Point[SIZE];
            index = 0;
        }

        public void add(final Point p) {
            points[index++] = p;
            index %= SIZE;
        }

        public void draw(final Graphics g) {
            double alpha = 0;

            for (int i = index; i != (index == 0 ? SIZE - 1 : index - 1); i = (i + 1) % SIZE) {
                if (points[i] != null && points[(i + 1) % SIZE] != null) {
                    g.setColor(new Color(0, 255, 0, (int) alpha));
                    g.drawLine(points[i].x, points[i].y, points[(i + 1) % SIZE].x, points[(i + 1) % SIZE].y);
                    alpha += ALPHA_STEP;
                }
            }
        }
    }

    private class FloatingWindow {
        private final Point loc;
        private final Dimension size;

        public FloatingWindow() {
            loc = new Point(10, 180);
            size = new Dimension(200, 135);
        }

        public void draw(final Graphics g, final int xpGained, final int pointsGained) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRoundRect(loc.x, loc.y, size.width, size.height, 5, 5);

            g.setColor(new Color(0, 255, 0, 100));
            g.drawRoundRect(loc.x, loc.y, size.width, size.height, 5, 5);
            g.drawLine(loc.x, loc.y + 20, loc.x + size.width, loc.y + 20);

            g.setColor(Color.GREEN);
            g.setFont(new Font("Tahoma", Font.BOLD, 10));
            g.drawString("-= SudoRunespan =-", loc.x + 40, loc.y + 15);

            g.setFont(new Font("Tahoma", Font.PLAIN, 10));
            g.drawString("Time", loc.x + 5, loc.y + 41);
            g.drawString(timer.toElapsedString(), loc.x + 105, loc.y + 41);

            g.drawString("Exp Gained", loc.x + 5, loc.y + 51);
            g.drawString(String.valueOf(xpGained), loc.x + 105, loc.y + 51);

            final double XPMS = xpGained * (1.0 / timer.getElapsed());
            g.drawString("Exp/h", loc.x + 5, loc.y + 61);
            g.drawString(df.format(XPMS * 3600000), loc.x + 105, loc.y + 61);

            g.drawString("Points", loc.x + 5, loc.y + 71);
            g.drawString(String.valueOf(pointsGained), loc.x + 105, loc.y + 71);

            g.drawString("Points/h", loc.x + 5, loc.y + 81);
            g.drawString(df.format(pointsGained * (3600000.0 / timer.getElapsed())), loc.x + 105, loc.y + 81);

            final int ETL = Skills.getExperienceToLevel(Skills.RUNECRAFTING, Skills.getRealLevel(Skills.RUNECRAFTING) + 1);
            g.drawString("ETL:", loc.x + 5, loc.y + 91);
            g.drawString("" + ETL, loc.x + 105, loc.y + 91);

            g.drawString("TTL:", loc.x + 5, loc.y + 101);
            g.drawString("" + Time.format(XPMS > 0 ? (long) (ETL / XPMS) : 0), loc.x + 105, loc.y + 101);

            final int curLevel = Skills.getRealLevel(Skills.RUNECRAFTING);
            g.drawString("Level:", loc.x + 5, loc.y + 111);
            g.drawString(curLevel + " (+" + (curLevel - startLvl) + ")", loc.x + 105, loc.y + 111);

            g.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g.drawString(getProgressBar(), loc.x + 5, loc.y + 128);
        }

        private String getProgressBar() {
            final double perc = (1 - getPercentNextLvl());
            final int len = (int) (25 * perc);
            String s = "[";

            for (int i = 0; i < len - 1; i++) {
                s += "=";
            }

            s += ">";

            for (int i = 0; i < (26 - len); i++) {
                s += " ";
            }

            s += "] " + (int) (perc * 100) + "%";
            return s;
        }

        public double getPercentNextLvl() {
            final int level = Skills.getLevel(Skills.RUNECRAFTING);

            if (level == 99) {
                return 100;
            }

            final double range = Skills.XP_TABLE[level + 1] - Skills.XP_TABLE[level];
            final double currentLvlExp = Skills.getExperienceToLevel(Skills.RUNECRAFTING, level + 1);
            return (currentLvlExp / range);
        }
    }
}
