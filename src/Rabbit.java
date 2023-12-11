import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
public class Rabbit extends Animals {
    //Location home;
    Location homeBurrow;
    boolean oneChildOnly;
    HashMap<Object, Location> favoriteBurrowMap;
    Location lastPosition;

    public Rabbit(int age, int hunger, int hp, World world) {
        super(age, hunger, hp, world);
        this.oneChildOnly = true;
        this.homeBurrow = null;

    }

    @Override
    public void act(World world) {
        if (this.getHealth() < 0) {
            die();
        }
        int counter = 0;
        super.act(world);

        seekShelter();
            if (world.getEntities().get(this) == null && world.isDay()) {
                System.out.println("vågn op " + lastPosition);
                if (lastPosition != null ){
                    wakeUp(lastPosition);
                }
                else {
                    wakeUp(this.location);
                }
            } else if (world.getEntities().get(this) != null && world.isDay()) {
                //System.out.println(world.getEntities().get(this));
                move();
                reproduction(world);
            }
    }

    public void move() {
        this.location = world.getLocation(this);
        if (world.getEmptySurroundingTiles(this.location).isEmpty()) {
        }
        else {
            if (age > 1 || hunger < 3) {
                if (timeCount % 2 == 0) {
                    this.location = world.getLocation(this);
                        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                        List<Location> list = new ArrayList<>(neighbours);
                        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                        Location l = list.get(randomNum);
                        world.move(this, l);
                        this.location = world.getLocation(this);
                        seekFood(Grass.class);
                }
            }
            else {
                this.location = world.getLocation(this);
                Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                List<Location> list = new ArrayList<>(neighbours);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());Location l = list.get(randomNum);
                world.move(this, l);
                this.location = world.getLocation(this);
                seekFood(Grass.class);
            }
        }
    }


    public void eat (Location tile){
        try {
            // if (world.getNonBlocking(this.location).getClass() == Grass.class) {
            world.delete((world.getNonBlocking(tile)));

                hunger = hunger + 3;
            } catch (Exception e) {
                System.out.println(e.getMessage());
        }
    }


        public void seekFood (Class type){
            if (hunger < 5) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == type) {
                        eat(tile);
                            world.move(this, tile);
                            break;
                        }
                    }
                }
            }


        public void Dig (Location location) {
        if (world.getTile(location) != null) {
            try {
                if (world.getNonBlocking(location).getClass() == Grass.class) {
                 //   world.delete((world.getNonBlocking(world.getCurrentLocation())));
                }
            }
            catch (IllegalArgumentException e) {
            }
            if (location != null) {
                Burrow.createNewBurrow(world, world.getEntities().get(this));
            }


        }
    }
    public void wakeUp (Location location) {
        if (world.isTileEmpty(location)) {
            System.out.println();
            world.setTile(location, this);
        }
    }

    private void reproduction (World world){
        if (age == 1 && this.oneChildOnly) {
            for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile) != null) {
                    System.out.println("hjælp");
                    if (world.getTile(tile).getClass() == Rabbit.class && world.getTile(tile) != this) {
                        System.out.println("jeg har født");
                        Rabbit rabbitChild = new Rabbit(0, 5, 5,world);
                        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                        List<Location> list = new ArrayList<>(neighbours);
                        if (list.size() <= 0) {
                            //deleteGrass(this,world);
                        } else {
                            int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                            Location l = list.get(randomNum);
                            world.setTile(l, rabbitChild);
                            this.oneChildOnly = false;
                            break;
                        }
                    }
                }
            }
        }
    }


    public void seekShelter(){
        int counter = 0;
        double distancetoClosestBurrow = 100.0;
        Location closestBurrow = null;
        if (world.getEntities().get(this) != null && world.isNight()) {
            for (Object object : world.getEntities().keySet()) {
                if (object.getClass() == Burrow.class) {
                    if (((Burrow) object).getBurrowSpace(object).contains(this)) {
                        counter++;
                        if (world.getEntities().get(this) != null) {
                            if (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) == 0) {
                                //Burrow.enterBurrow(this, world.getEntities().get(object));
                                lastPosition = world.getEntities().get(this);
                                world.remove(this);
                                break;
                            }
                            else {
                                makePath(this, world.getEntities().get(this), world.getEntities().get(object));
                            }
                        }
                    } else if(((Burrow) object).getBurrowSpace(object).contains(this) == false && ((Burrow) object).getBurrowSpace(object).size() < 5){
                        
                    }




                }
            }
            if (counter == 0) {
                if (world.getEntities().get(this) != null) {
                    for (Object object : world.getEntities().keySet()) {
                        if (object.getClass() == Burrow.class) {
                            if (((Burrow) object).getBurrowSpace(object).contains(this) == false && ((Burrow) object).getBurrowSpace(object).size() < 5) {
                                counter++;
                                while (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) < distancetoClosestBurrow) {
                                    distancetoClosestBurrow = calculateDistance(world.getLocation(this), world.getLocation(object));
                                    closestBurrow = world.getEntities().get(object);
                                }
                                if (calculateDistance(world.getEntities().get(this), closestBurrow) == 0) {
                                    lastPosition = world.getEntities().get(this);
                                    world.remove(this);
                                    ((Burrow) object).enterBurrow(this, object);
                                    break;
                                }
                                else {
                                    makePath(this, world.getEntities().get(this), closestBurrow);
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
                        if (object.getClass() == Burrow.class) {
                            if (world.getLocation(object) == this.location) {
                                ((Burrow) object).enterBurrow(this, object);
                            }
                        }
                    }
                }
                catch (IllegalArgumentException i) {
                }
            }
        }
    }
}