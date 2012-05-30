package org.sudorunespan;

import org.powerbot.concurrent.Task;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.sudorunespan.misc.Methods;
import org.sudorunespan.paint.Painter;
import org.sudorunespan.strategies.AntiBan;
import org.sudorunespan.strategies.AttackTarget;
import org.sudorunespan.strategies.GetFreeRuneEss;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:30 AM
 */

@Manifest(name = "SudoRunespan", version = 2.04, description = "The best Runespan bot SDN version! V2.04",
        authors = {"Deprecated"}, website = "http://www.powerbot.org/community/topic/688861-deprecateds-sudorunespan-80k-xph/")
public class SudoRunespan extends ActiveScript {
    private static boolean nodeBlock;
    private static Tile target;
    private static final Object targetAccessLock = new Object();
    private static int currentId, world;

    @Override
    protected void setup() {
        AbstractStrategy.setContext(this);

        provide(new Painter());
        submit(new Setup());
        provide(new AntiBan());
        provide(new GetFreeRuneEss());
        provide(new AttackTarget());
    }

    private final class Setup implements Task {
        @Override
        public void run() {
            loadWorldData();
        }
    }

    private static void loadWorldData() {
        synchronized (Methods.mouseLock) {
            for (int i = 0; i < 10 && !Tabs.getCurrent().equals(Tabs.FRIENDS); i++) {
                Tabs.FRIENDS.open();
                Time.sleep(200);
            }
        }

        world = Methods.getCurrentWorld();
    }

    public static boolean isMembers() {
        if (world == 0) {
            loadWorldData();
        }

        return Methods.isMembersWorld(world);
    }

    public static boolean isNodeBlock() {
        return nodeBlock;
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        SudoRunespan.currentId = currentId;
    }

    public static void setNodeBlock(final boolean nodeBlock) {
        SudoRunespan.nodeBlock = nodeBlock;
    }

    public static void setTarget(final Tile target) {
        synchronized (targetAccessLock) {
            SudoRunespan.target = target;
        }
    }

    public static Tile getTarget() {
        synchronized (targetAccessLock) {
            return target;
        }
    }
}
