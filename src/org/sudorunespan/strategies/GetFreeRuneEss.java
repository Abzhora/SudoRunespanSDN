package org.sudorunespan.strategies;

import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.sudorunespan.AbstractStrategy;
import org.sudorunespan.misc.Methods;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:42 AM
 */

public final class GetFreeRuneEss extends AbstractStrategy {
    private static final int RUNE_ESS_ID = 24227;
    private static final int FLOATING_ESS_ID = 15402;
    private int failCheck = 0;

    @Override
    public boolean isValid() {
        if (!Tabs.getCurrent().equals(Tabs.INVENTORY)) {
            synchronized (Methods.mouseLock) {
                Tabs.INVENTORY.open();
                return false;
            }
        } else {
            return Inventory.getItem(RUNE_ESS_ID) == null;
        }
    }

    @Override
    public void process() {
        final NPC floatingEss = Methods.getNearestReachableNPC(FLOATING_ESS_ID);

        if (floatingEss == null) {
            if (Tabs.getCurrent() == Tabs.INVENTORY && Game.getClientState() == 11) {
                if (failCheck > 5) {
                    error("Out of rune essence and no reachable floating rune essence!");
                } else {
                    Time.sleep(2000);
                    failCheck++;
                }
            }
        } else {
            failCheck = 0;
            Methods.interact(floatingEss, floatingEss.getLocation(), "Collect", "Floating essence");
        }
    }
}
