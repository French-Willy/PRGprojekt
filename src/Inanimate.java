import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Inanimate implements Actor {
    protected Location location;
    protected World world;
    public Inanimate(World world){
        this.world = world;
    }

    @Override
    public void act(World world){
        this.location = world.getLocation(this);
    }


    public Location getLocation(Inanimate inanimate){
        return world.getEntities().get(inanimate);
    }
}
