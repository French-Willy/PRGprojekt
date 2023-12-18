import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Crocodile extends Animals{

    boolean underwater;

    Set<Location> territory;
    Location lastLocation;

    public Crocodile(int age, int hunger, int hp, int animalMeatAmount, World world, Location location) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.territory = world.getSurroundingTiles(location,3);

    }

    public void act(){
        super.act(world);

        if (world.isDay()){

        }
    }

    public void goUnderwater(){
        lastLocation = getLocation(this);
        underwater = true;
        world.remove(this);
    }
    public void emergeFromWater(){
        world.setTile(lastLocation,this);
        underwater = false;
    }


}

