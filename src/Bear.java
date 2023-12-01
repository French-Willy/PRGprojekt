import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Animals {

    public Bear(int age, int hunger, World world) {
        super(age, hunger, world);
    }


    @Override
    public void act(World world) {
        super.act(world);
        if (world.getEntities().get(this) != null && world.isNight()) {


        } else if (world.isDay() && world.getEntities().get(this) == null) {

            System.out.println("Bear has awoken at this location: " + this.location);
            wakeUp();

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
        if (hunger < 6)
            findFood();

    }

    public void seekFood(HashSet<Class> surroundingAnimals) {
        if (hunger < 2) {

            if (surroundingAnimals.contains(Rabbit.class) || surroundingAnimals.contains(Wolf.class)) {
                for (Location tile : world.getSurroundingTiles()) {

                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == Wolf.class) {
                        eat(tile);
                        break;

                    } else if(world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class){
                        eat(tile);
                        break;
                    }
                }
            }
        } else if(hunger < 5){
            for (Location tile : world.getSurroundingTiles()){
                if(world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class){
                    eat(tile);
                    break;
                }
            }
        }
    }


            private void findFood () {
                HashSet<Class> surroundingAnimals = new HashSet<>();

                for (Object x : world.getSurroundingTiles(this.location)) {
                    surroundingAnimals.add(x.getClass());
                }
                seekFood(surroundingAnimals);
            }

            public void eat (Location tile){
                try {
                    // if (world.getNonBlocking(this.location).getClass() == Grass.class) {
                    world.delete((world.getTile(tile)));

                    hunger = hunger + 4;
                    System.out.println("my hunger is now: " + hunger);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }


            private void wakeUp () {

            }

        }
