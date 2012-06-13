package org.sudorunespan;

import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.wrappers.Locatable;
import org.sudorunespan.misc.Methods;
import org.sudorunespan.userinterface.OverlayPaint;
import org.sudorunespan.actions.Advertisement;
import org.sudorunespan.actions.AntiBan;
import org.sudorunespan.actions.AttackTarget;
import org.sudorunespan.actions.GetFreeRuneEss;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:30 AM
 */

@Manifest(name = "SudoRunespan", version = 2.08, description = "The best Runespan bot SDN version!",
        authors = {"Deprecated"}, website = "http://www.powerbot.org/community/topic/688861-deprecateds-sudorunespan-80k-xph/")
public final class SudoRunespan extends ActiveScript {
    private static boolean nodeBlock;
    private static Locatable target;
    private static final Object targetAccessLock = new Object();
    private static int currentId, world;
    private static Advertisement ad;

    @Override
    protected void setup() {
        AbstractStrategy.setContext(this);
        ad = new Advertisement(this);

        provide(new OverlayPaint());
        provide(new AntiBan());
        provide(new GetFreeRuneEss());
        provide(new AttackTarget());
    }

    public static boolean isMembers() {
        if (world == 0) {
            ad.loadWorldData();
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

    public static void setTarget(final Locatable target) {
        synchronized (targetAccessLock) {
            SudoRunespan.target = target;
        }
    }

    public static Locatable getTarget() {
        synchronized (targetAccessLock) {
            return target;
        }
    }

    public static void setWorld(int world) {
        SudoRunespan.world = world;
    }
}
