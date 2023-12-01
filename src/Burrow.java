import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;

public class Burrow extends Inanimate implements NonBlocking {
    Location location;
    static ArrayList<Rabbit> burrowSpace;
    int maxSpace;
    int burrowBed;


    public Burrow(World world) {
        super(world);
        this.burrowSpace = new ArrayList<>();
    }

    public static void enterBurrow(Rabbit rabbit, Object burrow) {
        if (burrowSpace.size() < 3) {
            burrowSpace.add(rabbit);
        } else if (burrowSpace.size() == 3) {

        }
    }

    public ArrayList<Rabbit> getBurrowSpace(Object Burrow) {
        return burrowSpace;
    }

    public static void leaveBurrow(Rabbit rabbit) {
        burrowSpace.remove(rabbit);
    }

    public static boolean getCheckFullBurrow(Object burrow) {
        boolean hasSpace = false;
        if (burrowSpace.size() <3) {
            return true;
        }
        else {
            return false;
        }
    }
}
    //hello
    //hello back
    //test
    //tooBad
    //hello agiain

