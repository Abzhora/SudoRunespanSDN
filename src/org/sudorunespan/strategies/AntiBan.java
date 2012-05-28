package org.sudorunespan.strategies;

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

public class AntiBan extends AbstractStrategy {
    public AntiBan() {
        setLock(false);
    }

    @Override
    public boolean isValid() {
        return Random.nextInt(0, 50) == 0;
    }

    @Override
    public void process() {
        switch (Random.nextInt(0, 20)) {
            case 0: case 1:
                Camera.setPitch(Random.nextInt(0, 100));
                break;

            case 2: case 3: case 4: case 5: case 6:
                SceneObject o = SceneEntities.getNearest(new Filter<SceneObject>() {
                    @Override
                    public boolean accept(SceneObject sceneObject) {
                        return Random.nextInt(0, 10) == 0;
                    }
                });

                if (o != null) {
                    Camera.turnTo(o);
                }
                break;

            default:
                Methods.wiggleMouse();
                break;
        }
    }
}
