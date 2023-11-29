import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Grass extends Plants implements NonBlocking {
    Location location;
    World world;

    public Grass(int age, World world) {
        super(age, world);
    }

    @Override
    public void act(World world) {
        try{
        this.location = world.getLocation(this);
        super.act(world);

        if (age % 4 == 0 && world.isDay()) {
            reproduction(this, world);
        } else if (age % 8 == 0 && world.isNight())
            reproduction(this,world);

        if (age == 10) {
            die(this);
            // world.delete(this);
        }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }


    private void deleteGrass(Grass grass, World world) {

        world.remove(this)
        ;
    }


    private void reproduction(Grass grass, World world) {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null) {

                if (world.getTile(tile).getClass() == Grass.class) {
                    Grass grass1 = new Grass(0, world);

                    Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                    List<Location> list = new ArrayList<>(neighbours);


                    if (list.size() <= 0) {
                        //deleteGrass(this,world);
                    } else {
                        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                        Location l = list.get(randomNum);
                        if (world.containsNonBlocking(l) == false) {
                            world.setTile(l, grass1);

                        }
                    }
                }
            }
        }
    }
}
