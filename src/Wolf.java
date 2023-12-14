import itumulator.world.Location;
import itumulator.world.World;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.spi.LocaleServiceProvider;

public class Wolf extends Animals {
    Location lastPosition;
    Location homeCave;

    boolean oneChildOnly;

    boolean huntingBear = false;
    int Actions;


    public Wolf(int age, int hunger, int hp, int animalMeatAmount, World world) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.atk = 5;
        this.hp = hp;
    }


    @Override
    public void act(World world) {
        super.act(world);
        //System.out.println(this + " " + this.hunger);
        Actions = 0;
        if (hp < 20) {
            regenerate();
        }
        if (huntingBear == true) {
            seekFood();
            Actions ++;
        }

        if (Actions == 0 && world.getEntities().get(this) != null && world.isNight() && hunger > 2 && huntingBear == false) {
            seekCave();

        }
        else if (Actions == 0 && getLocation(this) != null){
            move();
        }
        if (Actions == 0 && world.isDay() && getLocation(this) == null) {
            System.out.println("vågn op wolf " + homeCave);
            if (homeCave != null) {
                wakeUp(homeCave);
            }
        }
        else if (Actions == 0 && world.isDay() && getLocation(this) != null){
            move();
        }
        if (Actions == 0 && world.isNight() && getLocation(this) == null && homeCave != null) {
            reproduce();
        }
    }

    private void move() {
        //System.out.println(getLocation(this) + " har det en location");
        this.location = world.getLocation(this);
        if (world.getEmptySurroundingTiles(this.location).isEmpty()) {
        } else {
            if (age > 4 || hunger < 3) {
                if (timeCount % 2 == 0) {
                    this.location = world.getLocation(this);
                    Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                    List<Location> list = new ArrayList<>(neighbours);
                    int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                    Location l = list.get(randomNum);
                    world.move(this, l);
                    seekFood();
                }
            } else {
                this.location = world.getLocation(this);
                Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                List<Location> list = new ArrayList<>(neighbours);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                Location l = list.get(randomNum);
                world.move(this, l);
                seekFood();
            }
        }
    }public void eat(Animals animal, Carcass carcass) {
        carcass.meatEaten(10);
        hunger = 7;
    }


    public void seekFood() {
        int counter = 0;
        double closestPreyDistance = 7;
        double closestCarcasDistance = 7;
        Object closestPrey = null;
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == Carcass.class){
                eat(this, (Carcass) world.getTile(tile));
                System.out.println("spiser kadaver");
                counter ++;
                break;
            }
            else if (world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class) {
                attack(this, (Animals) world.getTile(tile));
                counter ++;
                break;
            }
        }
        if (hunger < 10 && counter == 0){
            if (findClosestObject(this, Carcass.class) != null) {
                System.out.println("finder carcas");
                makePath(this, getLocation(this),world.getEntities().get(findClosestObject(this,Carcass.class)));
            }
        }
        //System.out.println(counter + " counter " + isPack());
        if (homeCave != null && hunger < 3 && counter == 0 && isPack() == true || huntingBear == true) {
            System.out.println("jagter bjørn");
            if (huntingBear == false) {
                packHuntBear();
            }
            closestPrey = findClosestObject(this, Bear.class);
            closestPreyDistance = findClosestObjectDistance(this, Bear.class);
            for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile) != null && world.getTile(tile).getClass() == Bear.class) {
                    attack(this, (Animals) world.getTile(tile));
                    System.out.println(" angiber bjørn");
                    break;
                }
            }
            if (world.getEntities().get(closestPrey) != null && huntingBear == true) {
                System.out.println(" på vej mod bjørn");
                makePath(this, world.getEntities().get(this), world.getEntities().get(closestPrey));
            }
        } else if (hunger < 5 && counter == 0) {
            closestPrey = findClosestObject(this, Rabbit.class);
            closestPreyDistance = findClosestObjectDistance(this, Rabbit.class);
            if (world.getEntities().get(closestPrey) != null) {
                makePath(this, world.getEntities().get(this), world.getEntities().get(closestPrey));
            }
        }
    }

    public void packHuntBear(){
        WolfCave wolfcave = (WolfCave) world.getNonBlocking(homeCave);
        wolfcave.getCaveSpace(wolfcave);
        for (Wolf wolf : wolfcave.getCaveSpace(wolfcave)){
            wolf.huntingBear= true;
        }
    }

    public boolean isPack() {
        int WolfCounter = 0;

        for (Location tile : world.getSurroundingTiles()) {
            if (world.getNonBlocking(homeCave).getClass() == WolfCave.class) {
                if (((WolfCave) world.getNonBlocking(homeCave)).getWolfCaveHasSpace(world.getNonBlocking(homeCave).getClass()) == true) {
                    WolfCounter++;
                    System.out.println(WolfCounter + " ulve omrking");
                }
            }
        }
        if (WolfCounter >= 2) {
            return true;
        } else {
            return false;
        }
    }



    public void Dig(Location location) {
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

    public void wakeUp(Location location) {
        if (getRandomSurroundingLocation(location) != null) {
            world.setTile(getRandomSurroundingLocation(location), this);
        }
    }

    public void reproduce() {
        Wolf wolf = new Wolf(0, 5, 20, 50, world);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4);
        try {

            if (randomNum == 1) {
                if (world.getNonBlocking(homeCave).getClass() == WolfCave.class && oneChildOnly == false) {
                    if (((WolfCave) world.getNonBlocking(homeCave)).getWolfCaveHasSpace(world.getNonBlocking(homeCave).getClass()) == true) {
                        world.add(wolf);
                        wolf.homeCave = homeCave;
                        ((WolfCave) world.getNonBlocking(homeCave)).enterCave(wolf);
                        System.out.println("jeg har født i hulen");
                        oneChildOnly = true;
                    } else {
                        System.out.println("Find nyt hul");
                        if (getRandomSurroundingLocation(homeCave) != null) {
                            world.setTile(getRandomSurroundingLocation(homeCave), wolf);
                            oneChildOnly = true;
                        }
                    }
                }
            }
        }
        catch(IllegalArgumentException e) {
        }
    }

    public void seekCave() {
        int counter = 0;
        double distancetoClosestWolfCave = 100.0;
        Location closestWolfCave = null;
        if (world.getEntities().get(this) != null && world.isNight()) {
            for (Object object : world.getEntities().keySet()) {
                if (object.getClass() == WolfCave.class) {
                    if (((WolfCave) object).getCaveSpace(object).contains(this)) {
                        //System.out.println(((WolfCave) object).getCaveSpace(object) + " Hvad er der i caven");
                        counter++;
                        if (world.getEntities().get(this) != null) {
                            if (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) <= 1) {
                                lastPosition = world.getEntities().get(this);
                                //System.out.println(" wtf");
                                world.remove(this);
                                break;
                            } else {
                                makePath(this, world.getEntities().get(this), world.getEntities().get(object));
                                makePath(this, world.getEntities().get(this), world.getEntities().get(object));
                            }
                        }
                    }
                }
            }
        }
        if (counter == 0) {
            if (world.getEntities().get(this) != null) {
                for (Object object : world.getEntities().keySet()) {
                    if (object.getClass() == WolfCave.class) {
                        if (((WolfCave) object).getCaveSpace(object).contains(this) == false && ((WolfCave) object).getCaveSpace(object).size() < 10) {
                            counter++;
                            while (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) < distancetoClosestWolfCave) {
                                distancetoClosestWolfCave = calculateDistance(world.getLocation(this), world.getLocation(object));
                                closestWolfCave = world.getEntities().get(object);
                            }
                            if (calculateDistance(world.getEntities().get(this), closestWolfCave) <=1) {
                                lastPosition = world.getEntities().get(this);
                                System.out.println(closestWolfCave + " closestwoflcave 2");
                                homeCave = closestWolfCave;
                                //System.out.println(" this is fine");
                                ((WolfCave) object).enterCave(this);
                                world.remove(this);


                                break;
                            } else {
                                makePath(this, world.getEntities().get(this), closestWolfCave);
                                makePath(this, world.getEntities().get(this), closestWolfCave);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (counter == 0) {
            if (getLocation(this) != null) {
                if (getLocation(this) != null && findClosestObject(this, WolfCave.class) == null) {
                    //System.out.println(findClosestObject(this, WolfCave.class));
                    counter++;
                    try {
                        Dig(world.getEntities().get(this));
                        //System.out.println("999");
                        for (Object object : world.getEntities().keySet()) {
                            if (object.getClass() == WolfCave.class) {
                                if (world.getLocation(object) == this.location) {
                                    homeCave = getLocation(this);
                                    ((WolfCave) object).enterCave(this);
                                }
                            }
                        }
                    } catch (Exception i) {
                    }
                }

                if (findClosestObjectDistance(this, WolfCave.class) > world.getSize() / 3) {
                    try {
                        Dig(world.getEntities().get(this));
                        //System.out.println("131");
                        for (Object object : world.getEntities().keySet()) {
                            if (object.getClass() == WolfCave.class) {
                                if (world.getLocation(object) == this.location) {
                                    homeCave = getLocation(this);
                                    ((WolfCave) object).enterCave(this);

                                }
                            }
                        }
                    } catch (Exception i) {
                    }
                } else {
                    makePathAway(this, getLocation(this), world.getEntities().get(findClosestObject(this, WolfCave.class)));
                    makePathAway(this, getLocation(this), world.getEntities().get(findClosestObject(this, WolfCave.class)));
                }
            }
        }

    }
}
