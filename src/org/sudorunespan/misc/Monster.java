package org.sudorunespan.misc;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: deprecated
 * Date: 5/28/12
 * Time: 11:54 AM
 */

public enum Monster {
    AIR_ESSLING         (15403, 16571, 1,  "Air essling",       false),
    MIND_ESSLING        (15404, 16571, 1,  "Mind essling",      false),
    WATER_ESSLING       (15405, 16571, 5,  "Water essling",     false),
    EARTH_ESSLING       (15406, 16571, 9,  "Earth essling",     false),
    FIRE_ESSLING        (15407, 16571, 14, "Fire essling",      false),

    BODY_ESSHOUND       (15408, 16661, 20, "Body esshound",     false),
    COSMIC_ESSHOUND     (15409, 16661, 27, "Cosmic esshound",   true),
    CHAOS_ESSHOUND      (15410, 16661, 35, "Chaos esshound",    true),
    ASTRAL_ESSHOUND     (15411, 16661, 40, "Astral esshound",   true),
    NATURE_ESSHOUND     (15412, 16661, 44, "Nature esshound",   true),
    LAW_ESSHOUND        (15413, 16661, 54, "Law esshound",      true),

    DEATH_ESSWRAITH     (15414, 16641, 65, "Death esswraith",   true),
    BLOOD_ESSWRAITH     (15415, 16641, 77, "Blood esswraith",   true),
    SOUL_ESSWRAITH      (15416, 16641, 90, "Soul esswraith",    true);


    private final int id, deathAnimation, lvl;
    private final String name;
    private final boolean members;

    private Monster(final int id, final int deathAnimation, final int lvl, final String name, boolean members) {
        this.id = id;
        this.deathAnimation = deathAnimation;
        this.lvl = lvl;
        this.name = name;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public int getDeathAnimation() {
        return deathAnimation;
    }

    public int getLvl() {
        return lvl;
    }

    public boolean isMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public static Monster getMonster(final int id) {
        for (Monster monster : Monster.values()) {
            if (monster.id == id) {
                return monster;
            }
        }

        return null;
    }

    public static Monster[] getValues(boolean members) {
        final ArrayList<Monster> monsters = new ArrayList<Monster>();

        for (Monster monster : Monster.values()) {
            if (members || !monster.isMembers()) {
                monsters.add(monster);
            }
        }

        return monsters.toArray(new Monster[monsters.size()]);
    }
}
