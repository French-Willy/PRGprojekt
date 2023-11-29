import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class Person implements Actor {
    private Location location;


    public Person(Location location) {

        this.location = location;
    }


    @Override
    public void act(World world) {
        if (world.getEntities().get(this) != null && world.isNight()) {
            world.remove(this);
            System.out.println("Person has fallen asleep at this location: " + this.location);
        } else if (world.isDay() && world.getEntities().get(this) == null) {
            try{
                world.setTile(this.location, this);
                System.out.println("Person has awoken at this location: " + this.location);
            }catch (Exception e){
                System.out.println("nørd");
            }
        } else {

            if (world.isDay()) {
                Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                List<Location> list = new ArrayList<>(neighbours);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                Location l = list.get(randomNum);
                world.move(this, l);
                this.location = world.getLocation(this);

            }
        }
    }
}


