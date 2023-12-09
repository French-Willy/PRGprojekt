import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Carcass extends Inanimate implements NonBlocking {
    Location location;
    int meatAmount;
    public Carcass(int meatAmount, World world){
        super(world);
        this.meatAmount = meatAmount;
    }

    public void act(World world){
        this.location = world.getLocation(this);
    }

    public static void createNewCarcass(World world, Location location, int meatAmount){
        world.setTile(location, new Carcass(meatAmount,world));
        System.out.println("Carcass lavet.... (⌐■_■)");
    }
}
