import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

public class Fungi extends Plants implements NonBlocking{

    int fungiSize;
    public Fungi(int age, int fungiSize, World world){
        super(age, world);
        this.fungiSize = fungiSize;
    }

    @Override
    public void act(World world){
        findCarcass();
    }

    public void findCarcass(){
        this.location = world.getLocation(this);
        List<Location> SurroundingCarcass = new ArrayList<>();
        for (Location tile : world.getSurroundingTiles()) {
            //System.out.println(world.getSurroundingTiles());
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == Carcass.class && !SurroundingCarcass.equals(tile)){
                System.out.println("cool");
                SurroundingCarcass.add(tile);
                System.out.println(SurroundingCarcass);
            }
        }
    }


}
