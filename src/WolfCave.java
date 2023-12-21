import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.HashSet;

public class WolfCave extends Inanimate implements NonBlocking {
    Location location;
    HashSet<Wolf> caveSpace;

    public WolfCave(World world, HashSet<Wolf> caveSpace) {
        super(world);
        this.caveSpace = caveSpace;
    }

    /**
     * instantiate hashset and places the burrow in the world
     * @param world
     * @param location
     */
    public static void createNewCave(World world, Location location) {
        HashSet<Wolf> caveSpace = new HashSet<>();
        world.setTile(location, new WolfCave(world, caveSpace));
    }

    /**
     * Gets called when a wolf need ot enter a cave
     * @param wolf
     */
    public void enterCave(Wolf wolf) {
        if (caveSpace.isEmpty() || caveSpace.size() < 10) {
            caveSpace.add(wolf);
        } else if (caveSpace.size() == 10) {

        }
    }

    /**
     * Get called when has to check if a wolf exist in a given cave
     * @param wolfcave
     * @return
     */
    public HashSet<Wolf> getCaveSpace(Object wolfcave) {
        return caveSpace;
    }

    /**
     * Gets called to check if there is space in the cave
     * @param wolfcave
     * @return
     */
    public boolean getWolfCaveHasSpace(Object wolfcave) {
        boolean hasSpace = false;
        if (caveSpace.size() < 10) {
            return true;
        }
        else {
            return false;
        }
    }

}
