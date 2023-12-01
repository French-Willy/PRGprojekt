import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;

public class Burrow extends Inanimate implements NonBlocking {
    Location location;
    ArrayList<Rabbit> burrowSpace;
    int maxSpace;
    int burrowBed;



    public Burrow(World world) {
        super(world);
        this.burrowSpace = new ArrayList<>();
    }

    public static void enterBurrow(Rabbit rabbit, Burrow burrow) {
        if(burrow.burrowSpace.size() < 3) {
            burrow.burrowSpace.add(rabbit);
        } else if(burrow.burrowSpace.size() == 3){

        }
    }

    public void leaveBurrow(Rabbit rabbit) {
        for(Rabbit rabbitname : burrowSpace){
            if(rabbit == rabbitname){
                burrowSpace.remove(rabbitname);
            }
        }
        burrowSpace.remove(0);
    }

    public static boolean checkFullBurrow(Burrow burrow) {
        boolean hasSpace = false;
        if (burrow.burrowSpace.get(2).getClass() == Rabbit.class) {
            hasSpace = false;
        }
        if (burrow.burrowSpace.get(2).getClass() != Rabbit.class) {
            hasSpace = true;
        }
        return hasSpace;
    }
    //hello
    //hello back
    //test
    //tooBad
    //hello agiain
}
