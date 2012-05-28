package org.sudorunespan.misc;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:51 AM
 */

public enum Node {
    CYCLONE         (70455, 1,  "Cyclone",          false),
    MIND_STORM      (70456, 1,  "Mind storm",       false),
    WATER_POOL      (70457, 5,  "Water pool",       false),
    ROCK_FRAGMENT   (70458, 9,  "Rock fragment",    false),
    VINE            (70460, 17, "Vine",             false),
    FIREBALL        (70459, 14, "Fireball",         false),
    FLESHY_GROWTH   (70461, 20, "Fleshy growth",    false),
    FIRE_STORM      (70462, 27, "Fire storm",       false),
    CHAOTIC_CLOUD   (70463, 35, "Chaotic cloud",    true),
    NEBULA          (70464, 40, "Nebula",           true),
    SHIFTER         (70465, 44, "Shifter",          true),
    JUMPER          (70466, 54, "Jumper",           true),
    SKULLS          (70467, 66, "Skulls",           true),
    BLOOD_POOL      (70468, 77, "Blood pool",       true),
    BLOODY_SKULLS   (70469, 83, "Bloody skulls",    true),
    LIVING_SOUL     (70470, 90, "Living soul",      true),
    UNDEAD_SOUL     (70471, 95, "Undead soul",      true);

    private final int id, lvl;
    private final String name;
    private final boolean members;

    private Node(final int id, final int lvl, final String name, final boolean members) {
        this.id = id;
        this.lvl = lvl;
        this.name = name;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public int getLvl() {
        return lvl;
    }

    public String getName() {
        return name;
    }

    public boolean isMembers() {
        return members;
    }

    public static Node getNode(final int id) {
        for (Node node : Node.values()) {
            if (node.getId() == id) {
                return node;
            }
        }

        return null;
    }

    public static Node[] getValues(boolean members) {
        final ArrayList<Node> nodes = new ArrayList<Node>();

        for (Node node : Node.values()) {
            if (members || !node.isMembers()) {
                nodes.add(node);
            }
        }

        return nodes.toArray(new Node[nodes.size()]);
    }
}
