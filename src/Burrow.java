import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.HashSet;

public class Burrow extends Inanimate implements NonBlocking {
    Location location;
    HashSet<Rabbit> burrowSpace;



    public Burrow(World world, HashSet<Rabbit> burrowSpace) {
        super(world);
        this.burrowSpace = burrowSpace;
    }

    /**
     * instantiate hashset and places the burrow in the world
     * @param world
     * @param location
     */
    public static void createNewBurrow(World world, Location location) {
        HashSet<Rabbit> burrowSpace = new HashSet<>();
        world.setTile(location, new Burrow(world,burrowSpace));
    }

    /**
     * gets called from rabbit when entering a burrow.
     * @param rabbit
     */
    public void enterBurrow(Rabbit rabbit) {
        if (burrowSpace.isEmpty() || burrowSpace.size() < 5) {
            burrowSpace.add(rabbit);
        } else if (burrowSpace.size() == 4) {

        }
    }

    /**
     * Get called when need to check if a rabbit exist in a burrow
     * @param Burrow
     * @return
     */
    public HashSet<Rabbit> getBurrowSpace(Object Burrow) {
        return burrowSpace;
    }



}
