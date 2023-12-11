import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Wolf extends Animals{
    Location lastPosition;

    public Wolf(int age, int hunger, int hp, int animalMeatAmount, World world) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.atk = 5;
        this.hp = hp;
    }


    @Override
    public void act(World world) {
        super.act(world);
        if(hp < 20){
            regenerate();
        }

        if (world.getEntities().get(this) != null && world.isNight()) {
            seekCave();

        } else if (world.isDay() && world.getEntities().get(this) == null) {
            System.out.println("Wolf has awoken at this location: " + this.location);

        } else if (world.isDay()) {
            move();
        }
    }

    private void move() {
        this.location = world.getLocation(this);
        if (world.getEmptySurroundingTiles(this.location).isEmpty()) {
        }
        else {
            if (age > 4 || hunger < 3) {
                if (timeCount % 2 == 0) {
                    this.location = world.getLocation(this);
                    Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                    List<Location> list = new ArrayList<>(neighbours);
                    int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                    Location l = list.get(randomNum);
                    world.move(this, l);
                    this.location = world.getLocation(this);
                    seekFood(Rabbit.class);
                }
            }
            else {
                this.location = world.getLocation(this);
                Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                List<Location> list = new ArrayList<>(neighbours);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());Location l = list.get(randomNum);
                world.move(this, l);
                this.location = world.getLocation(this);
                seekFood(Rabbit.class);
            }
        }
    }

    public void seekFood(Class animal) {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == animal) {
                eat(tile);
                break;
            }
        }
        for (Object food : world.getEntities().keySet()) {
            if (food.getClass() == )

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
    public void Dig (Location location) {
        if (world.getTile(location) != null) {
            try {
                if (world.getNonBlocking(location).getClass() == Grass.class) {
                    //   world.delete((world.getNonBlocking(world.getCurrentLocation())));
                }
            } catch (IllegalArgumentException e) {
            }
            if (location != null) {
                WolfCave.createNewCave(world, world.getEntities().get(this));
            }
        }
    }

    public void seekCave(){
        int counter = 0;
        double distancetoClosestWolfCave = 100.0;
        Location closestWolfCave = null;
        if (world.getEntities().get(this) != null && world.isNight()) {
            for (Object object : world.getEntities().keySet()) {
                if (object.getClass() == WolfCave.class) {
                    if (((WolfCave) object).getCaveSpace(object).contains(this))
                        counter++;
                        if (world.getEntities().get(this) != null) {
                            if (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) == 0) {
                                lastPosition = world.getEntities().get(this);
                                world.remove(this);
                                break;
                            }
                            else {
                                makePath(this, world.getEntities().get(this), world.getEntities().get(object));
                            }
                        }
                    }
                //else if(((Burrow) object).getBurrowSpace(object).contains(this) == false && ((Burrow) object).getBurrowSpace(object).size() < 5){

            }
        }
        if (counter == 0) {
            if (world.getEntities().get(this) != null) {
                for (Object object : world.getEntities().keySet()) {
                    if (object.getClass() == WolfCave.class) {
                        if (((WolfCave) object).getCaveSpace(object).contains(this) == false && ((WolfCave) object).getCaveSpace(object).size() < 5) {
                            counter++;
                            while (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) < distancetoClosestWolfCave) {
                                distancetoClosestWolfCave = calculateDistance(world.getLocation(this), world.getLocation(object));
                                closestWolfCave = world.getEntities().get(object);
                            }
                            if (calculateDistance(world.getEntities().get(this), closestWolfCave) == 0) {
                                lastPosition = world.getEntities().get(this);
                                world.remove(this);
                                ((WolfCave) object).enterCave(this);
                                break;
                            }
                            else {
                                makePath(this, world.getEntities().get(this), closestWolfCave);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (counter == 0) {
            try {
                Dig(world.getEntities().get(this));
                for (Object object : world.getEntities().keySet()) {
                    if (object.getClass() == WolfCave.class) {
                        if (world.getLocation(object) == this.location) {
                            ((WolfCave) object).enterCave(this);
                        }
                    }
                }
            }
            catch (IllegalArgumentException i) {
            }
        }
    }
}
