import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.HashSet;

public class Burrow extends Inanimate implements NonBlocking {
    Location location;
    HashSet<Rabbit> burrowSpace;
    int maxSpace;


    public Burrow(World world, HashSet<Rabbit> burrowSpace) {
        super(world);
        this.burrowSpace = burrowSpace;
    }

    public static void createNewBurrow(World world, Location location) {
        HashSet<Rabbit> burrowSpace = new HashSet<>();
        world.setTile(location, new Burrow(world,burrowSpace));
        System.out.println("burrow lavet.... pog ");
    }

    public void enterBurrow(Rabbit rabbit, Object burrow) {
        if (burrowSpace.isEmpty() || burrowSpace.size() < 5) {
            burrowSpace.add(rabbit);
        } else if (burrowSpace.size() == 4) {

        }
    }

    public HashSet<Rabbit> getBurrowSpace(Object Burrow) {
        return burrowSpace;
    }

    public  void leaveBurrow(Rabbit rabbit) {
        burrowSpace.remove(rabbit);
    }

    public  boolean getCheckFullBurrow(Object burrow) {
        boolean hasSpace = false;
        //System.out.println(burrowSpace + "kaniner i hullet");
        //System.out.println(burrowSpace.size() +" plads i hullet");
        if (burrowSpace.size() <5) {
            return true;
        }
        else {
            return false;
        }
    }


}

//test