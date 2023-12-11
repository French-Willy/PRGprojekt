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

    public static void createNewCave(World world, Location location) {
        HashSet<Wolf> caveSpace = new HashSet<>();
        world.setTile(location, new WolfCave(world, caveSpace));
        System.out.println("cave lavet.... pog ");
    }

    public void enterCave(Wolf wolf) {
        if (caveSpace.isEmpty() || caveSpace.size() < 10) {
            caveSpace.add(wolf);
        } else if (caveSpace.size() == 10) {

        }
    }

    public HashSet<Wolf> getCaveSpace(Object wolfcave) {
        return caveSpace;
    }

    public boolean getCheckFullCave(WolfCave wolfcave) {
        boolean hasSpace = false;
        if (wolfcave.caveSpace.size() < 10) {
            return true;
        }
        else {
            return false;
        }
    }
}
