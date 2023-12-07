import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Wolf extends Animals{

    public Wolf(int age, int hunger, int hp, World world) {
        super(age, hunger, hp, world);
        this.atk = 5;
    }


    @Override
    public void act(World world) {
        super.act(world);
        if (world.getEntities().get(this) != null && world.isNight()) {


        } else if (world.isDay() && world.getEntities().get(this) == null) {
            System.out.println("Wolf has awoken at this location: " + this.location);

        } else if (world.isDay()) {
            move();
        }
    }

    private void move() {
        this.location = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
        List<Location> list = new ArrayList<>(neighbours);
        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
        Location l = list.get(randomNum);
        world.move(this, l);
        this.location = world.getLocation(this);
        if (hunger < 4)
            seekFood(Rabbit.class);

    }

    public void seekFood(Class animal) {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == animal) {
                eat(tile);
                break;

            }
        }
    }

    public void eat (Location tile){
        try {
            world.delete((world.getTile(tile)));

            hunger = hunger + 4;
            System.out.println("my hunger is now: " + hunger);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
