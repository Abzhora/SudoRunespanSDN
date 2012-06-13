package org.sudorunespan.actions;

import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.sudorunespan.AbstractStrategy;
import org.sudorunespan.misc.Methods;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:58 AM
 */

public final class AntiBan extends AbstractStrategy {
    public AntiBan() {
        setLock(false);
    }

    @Override
    public boolean isValid() {
        return Random.nextInt(0, 50) == 0;
    }

    @Override
    public void process() {
        final int num = Random.nextInt(0, 20);

        if (num <= 1) {
            Camera.setPitch(Random.nextInt(0, 100));
        } else if (num <= 6) {
            SceneObject o = SceneEntities.getNearest(new Filter<SceneObject>() {
                @Override
                public boolean accept(SceneObject sceneObject) {
                    return Random.nextInt(0, 10) == 0;
                }
            });

            if (o != null) {
                Camera.turnTo(o);
            }
        } else {
            Methods.wiggleMouse();
        }
    }
}
