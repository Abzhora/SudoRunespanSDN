package org.sudorunespan.strategies;

import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.sudorunespan.AbstractStrategy;
import org.sudorunespan.SudoRunespan;
import org.sudorunespan.misc.Methods;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 2:32 PM
 */

public class AttackTarget extends AbstractStrategy {
    private static final int RUNE_ESS_ID = 24227;
    private Tile location;

    @Override
    protected boolean isValid() {
        for (int i=0; i<10 && !Tabs.getCurrent().equals(Tabs.INVENTORY); i++) {
            Tabs.INVENTORY.open();
            Time.sleep(200);
        }

        if (!Methods.validate(SudoRunespan.getTarget(), SudoRunespan.getCurrentId()) ||
                Players.getLocal().getAnimation() == -1) {
            SudoRunespan.setTarget(null);
            SudoRunespan.setCurrentId(-1);
        }

        final Item runeEss = Inventory.getItem(RUNE_ESS_ID);

        if (!SudoRunespan.isNodeBlock()) {
            if (runeEss == null || runeEss.getStackSize() < 50) {
                SudoRunespan.setNodeBlock(true);
            } else {
                final SceneObject node = Methods.getBestReachableNode();

                if (node != null) {
                    if (node.getId() != SudoRunespan.getCurrentId()) {
                        SudoRunespan.setTarget(node.getLocation());
                        SudoRunespan.setCurrentId(node.getId());
                        location = node.getLocation();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        if (SudoRunespan.isNodeBlock() && runeEss != null && runeEss.getStackSize() > 500) {
            SudoRunespan.setNodeBlock(false);
        }

        final NPC monster = Methods.getBestReachableMonster();

        if (monster != null) {
            if (monster.getId() != SudoRunespan.getCurrentId()) {
                SudoRunespan.setTarget(monster.getLocation());
                SudoRunespan.setCurrentId(monster.getId());
                location = monster.getLocation();
                return true;
            } else {
                return false;
            }
        }

        SudoRunespan.setTarget(null);
        SudoRunespan.setCurrentId(-1);
        return false;
    }

    @Override
    protected void process() {
        Methods.interact(SudoRunespan.getTarget(), location, "Siphon",
                Methods.getTargetName(SudoRunespan.getTarget()));
    }
}
