package org.sudorunespan.paint;

import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.bot.event.listener.PaintListener;
import org.sudorunespan.SudoRunespan;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:50 AM
 */

public class Painter extends Strategy implements PaintListener {
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private int startXp, startPoints, gainedPoints;
    private Timer timer;
    private MouseTrail mouseTrail;
    private FloatingWindow window;

    public Painter() {
        startXp = Skills.getExperience(Skills.RUNECRAFTING);
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

        final Tile tile = SudoRunespan.getTarget();
        if (tile != null && tile.isOnScreen()) {
            g.setColor(Color.RED);
            g.drawPolygon(tile.getBounds()[0]);
            g.setColor(new Color(255, 0, 0, 50));
            g.fillPolygon(tile.getBounds()[0]);
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
    }

    private static void drawMouse(final Graphics g) {
        g.setColor(Color.GREEN);
        g.drawOval(Mouse.getX()-5, Mouse.getY()-5, 11, 11);
        g.fillOval(Mouse.getX()-2, Mouse.getY()-2, 5, 5);
    }

    private final static class MouseTrail {
        private final int SIZE = 25;
        private final double ALPHA_STEP = (255.0/SIZE);
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

            for (int i = index; i != (index == 0 ? SIZE-1 : index-1); i = (i+1)%SIZE) {
                if (points[i] != null && points[(i+1)%SIZE] != null) {
                    g.setColor(new Color(0, 255, 0, (int) alpha));
                    g.drawLine(points[i].x, points[i].y, points[(i + 1) % SIZE].x, points[(i + 1) % SIZE].y);
                    alpha += ALPHA_STEP;
                }
            }
        }
    }

    private class FloatingWindow {
        private static final int MAX_WAIT = 5;
        private final Point loc;
        private final Dimension size;
        private int spinner = 0, wait = 0;

        public FloatingWindow() {
            loc = new Point(10, 180);
            size = new Dimension(200, 125);
        }

        public void draw(final Graphics g, final int xpGained, final int pointsGained) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRoundRect(loc.x, loc.y, size.width, size.height, 5, 5);

            g.setColor(new Color(0, 255, 0, 100));
            g.drawRoundRect(loc.x, loc.y, size.width, size.height, 5, 5);
            g.drawLine(loc.x, loc.y+20, loc.x+size.width, loc.y+20);

            g.setColor(Color.GREEN);
            g.setFont(new Font("Monospaced", Font.BOLD, 10));
            g.drawString("-= SudoRunespan =-", loc.x+40, loc.y+15);

            g.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g.drawString("Time", loc.x+5, loc.y+41);
            g.drawString(timer.toElapsedString(), loc.x+105, loc.y+41);

            g.drawString("Exp Gained", loc.x+5, loc.y+51);
            g.drawString(String.valueOf(xpGained), loc.x+105, loc.y+51);

            final double XPMS = xpGained * (1.0/timer.getElapsed());
            g.drawString("Exp/h", loc.x+5, loc.y+61);
            g.drawString(df.format(XPMS*3600000), loc.x+105, loc.y+61);

            g.drawString("Points", loc.x+5, loc.y+71);
            g.drawString(String.valueOf(pointsGained), loc.x+105, loc.y+71);

            g.drawString("Points/h", loc.x+5, loc.y+81);
            g.drawString(df.format(pointsGained *(3600000.0/timer.getElapsed())), loc.x+105, loc.y+81);

            final int ETL = Skills.getExperienceToLevel(Skills.RUNECRAFTING, Skills.getRealLevel(Skills.RUNECRAFTING) + 1);
            g.drawString("ETL:", loc.x+5, loc.y+91);
            g.drawString("" + ETL, loc.x+105, loc.y+91);

            g.drawString("TTL:", loc.x+5, loc.y+101);
            g.drawString("" + Time.format(XPMS > 0 ? (long) (ETL / XPMS) : 0), loc.x+105, loc.y+101);

            g.drawString(getProgressBar(), loc.x+5, loc.y+118);
        }

        private String getProgressBar() {
            final double perc = (1-getPercentNextLvl(Skills.RUNECRAFTING));
            final int len = (int) (27 * perc);
            String s = "";

            for (int i=0; i<len-1; i++) {
                s+=".";
            }

            switch (spinner) {
                case 0:
                    s+="-";
                    break;
                case 1:
                    s+="\\";
                    break;
                case 2:
                    s+="|";
                    break;
                case 3:
                    s+="/";
                    break;
            }

            for (int i=0; i<(28-len); i++) {
                s+=" ";
            }

            wait++;
            if (wait == MAX_WAIT) {
                spinner = (spinner+1) % 4;
                wait = 0;
            }

            s+= (int) (perc*100) + "%";
            return s;
        }

        public double getPercentNextLvl(int skill) {
            final int level = Skills.getLevel(skill);

            if (level == 99) {
                return 100;
            }

            final double range = Skills.XP_TABLE[level+1] - Skills.XP_TABLE[level];
            final double currentLvlExp = Skills.getExperienceToLevel(skill, level+1);
            return (currentLvlExp/range);
        }
    }
}
