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

    public void leaveBurrow(Rabbit rabbit) {
        for (Rabbit rabbitname : burrowSpace) {
            if (rabbit == rabbitname) {
                burrowSpace.remove(rabbitname);

            }
        }
        burrowSpace.remove(0);
    }

    public static boolean getCheckFullBurrow(Burrow burrow) {
        boolean hasSpace = false;
        if (burrow.burrowSpace.size() < 4) {
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

