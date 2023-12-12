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


    public Wolf(int age, int hunger, int hp, int animalMeatAmount, World world) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.atk = 5;
        this.hp = hp;
    }


    @Override
    public void act(World world) {
        super.act(world);
        if (hp < 20) {
            regenerate();
        }

        if (world.getEntities().get(this) != null && world.isNight()) {
            seekCave();
        }
        if (world.isDay() && getLocation(this) == null) {
            System.out.println("vågn op " + homeCave);
            if (homeCave != null) {
                wakeUp(homeCave);
            }
            else {
                wakeUp(this.location);
            }
        }
        else if (world.isDay() && getLocation(this) != null) {
            move();
        }
        if (world.isNight() && getLocation(this) == null && homeCave != null) {
            reproduce();
        }
    }

    private void move() {
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
                    this.location = world.getLocation(this);
                    seekFood(Rabbit.class);
                }
            } else {
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
    }

    public void seekFood(Class animal) {
        int counter = 0;
        double closestPreyDistance = 7;
        double closestCarcasDistance = 7;
        Object closestPrey = null;
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class) {
                attack(this, (Animals) world.getTile(tile));
                counter++;
                break;
            }
        }
        if (hunger < 3 && counter == 0 && isPack() == true) {
            closestPrey = findClosestObject(this, Bear.class);
            closestPreyDistance = findClosestObjectDistance(this, Bear.class);
            for (Object tile : world.getSurroundingTiles()) {
                if (tile.getClass() == Bear.class) {
                    attack(this, (Animals) tile);
                    break;
                }
            }
            if (world.getEntities().get(closestPrey) != null && isPack() == true) {
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

    public boolean isPack() {
        int WolfCounter = 0;
        for (Object tile : world.getSurroundingTiles()) {
            if (tile.getClass() == Wolf.class) {
                WolfCounter++;
            }
        }
        if (WolfCounter >= 3) {
            return true;
        } else {
            return false;
        }
    }

    public void eat(Location tile) {
        try {
            world.delete((world.getTile(tile)));

            hunger = hunger + 4;
            System.out.println("my hunger is now: " + hunger);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            world.setTile(getRandomSurroundingLocation(location), this);

    }

    public void reproduce() {
        Wolf wolf = new Wolf(0, 5, 20, 50, world);
        int counter = 0;
        if (this.age == 2) {
            world.add(wolf);
            counter++;
        }
        if (world.getNonBlocking(homeCave).getClass() == WolfCave.class && counter != 0) {
            if (((WolfCave) world.getNonBlocking(homeCave)).getWolfCaveHasSpace(world.getNonBlocking(homeCave).getClass()) == true) {
                ((WolfCave) world.getNonBlocking(homeCave)).enterCave(wolf);
                System.out.println("jeg har født i hulen");
            } else {
                System.out.println("Find nyt hul");
                world.setTile(getRandomSurroundingLocation(homeCave), wolf);
            }
        }


    }

    public void seekCave() {
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
                        } else {
                            makePath(this, world.getEntities().get(this), world.getEntities().get(object));
                        }
                    }
                }
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
                                homeCave = getLocation(this);
                                world.remove(this);
                                ((WolfCave) object).enterCave(this);

                                break;
                            } else {
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
                    System.out.println(findClosestObject(this, WolfCave.class));
                    counter++;
                    try {
                        Dig(world.getEntities().get(this));
                        System.out.println("999");
                        for (Object object : world.getEntities().keySet()) {
                            if (object.getClass() == WolfCave.class) {
                                if (world.getLocation(object) == this.location) {
                                    ((WolfCave) object).enterCave(this);
                                }
                            }
                        }
                    } catch (Exception i) {
                    }
                }

                if (findClosestObjectDistance(this, WolfCave.class) > world.getSize() / 5) {
                    try {
                        Dig(world.getEntities().get(this));
                        System.out.println("131");
                        for (Object object : world.getEntities().keySet()) {
                            if (object.getClass() == WolfCave.class) {
                                if (world.getLocation(object) == this.location) {
                                    ((WolfCave) object).enterCave(this);
                                    homeCave = getLocation(this);
                                }
                            }
                        }
                    } catch (Exception i) {
                    }
                } else {
                    makePathAway(this, getLocation(this), world.getEntities().get(findClosestObject(this, WolfCave.class)));
                }
            }
        }

    }
}
