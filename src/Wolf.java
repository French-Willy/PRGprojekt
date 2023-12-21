import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
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
    public DisplayInformation getInformation() {
        if (sleeping) {
            if (this.age > 2) {
                return new DisplayInformation(Color.GRAY, "wolf-sleeping", false);
            } else {
                return new DisplayInformation(Color.GRAY, "wollfl-small-sleeping", false);
            }
        } else {
            if (this.age > 2) {
                return new DisplayInformation(Color.GRAY, "wolf", false);
            } else {
                return new DisplayInformation(Color.GRAY, "wolf-small", false);
            }

        }
    }

    @Override
    public void act(World world) {
        super.act(world);
        Actions = 0;
        if (hp < 20) {
            regenerate();
        }
        if (huntingBear == true) {
            seekFood();
            Actions++;
        }

        if (Actions == 0 && world.getEntities().get(this) != null && world.isNight() && hunger > 2 && huntingBear == false) {
            seekCave();

        } else if (Actions == 0 && getLocation(this) != null) {
            wolfMove();
        }
        if (Actions == 0 && world.isDay() && getLocation(this) == null) {
            if (homeCave != null) {
                wakeUp(homeCave);
            }
        } else if (Actions == 0 && world.isDay() && getLocation(this) != null) {
            wolfMove();
        }
        if (Actions == 0 && world.isNight() && getLocation(this) == null && homeCave != null) {
            reproduce();
        }
    }

    protected void wolfMove() {
        if (age > 4 || hunger < 5) {
            if (timeCount % 2 == 0) {
                if (hunger < 5) {
                    seekFood();
                } else {
                    move(this);
                }
            }
        } else {
            move(this);
        }
    }


    public void seekFood() {
        int counter = 0;
        double closestPreyDistance = 20;
        double closestCarcasDistance = 20;
        Object closestPrey = null;
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == Wolf.class && homeCave != null) {
                WolfCave wolfcave = (WolfCave) world.getNonBlocking(homeCave);
                if (wolfcave.getCaveSpace(wolfcave).contains(this) && wolfcave.getCaveSpace(wolfcave).contains(world.getTile(tile)) == false) {
                    attack(this, (Animals) world.getTile(tile));
                    break;
                }
            }
        }
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == Carcass.class) {
                eat((Carcass) world.getTile(tile), 10);
                counter++;
                huntingBear = false;
                break;
            } else if (world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class) {
                attack(this, (Animals) world.getTile(tile));
                counter++;
                break;
            }
        }
        if (hunger < 10 && counter == 0 && getLocation(this) != null) {
            if (findClosestObject(this, Carcass.class) != null) {
                makePath(this, getLocation(this), world.getEntities().get(findClosestObject(this, Carcass.class)));
            }
        }
        if (homeCave != null && hunger < 3 && counter == 0 && isPack() == true || huntingBear == true) {
            if (huntingBear == false) {
                packHuntBear();
            }
            closestPrey = findClosestObject(this, Bear.class);
            closestPreyDistance = findClosestObjectDistance(this, Bear.class);
            for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile) != null && world.getTile(tile).getClass() == Bear.class) {
                    attack(this, (Animals) world.getTile(tile));
                    break;
                }
            }
            if (world.getEntities().get(closestPrey) != null && huntingBear == true) {
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

    /**
     *
     */
    public void packHuntBear() {
        WolfCave wolfcave = (WolfCave) world.getNonBlocking(homeCave);
        wolfcave.getCaveSpace(wolfcave);
        for (Wolf wolf : wolfcave.getCaveSpace(wolfcave)) {
            wolf.huntingBear = true;
        }
    }

    public boolean isPack() {
        int WolfCounter = 0;

        for (Location tile : world.getSurroundingTiles()) {
            if (world.getNonBlocking(homeCave).getClass() == WolfCave.class) {
                if (((WolfCave) world.getNonBlocking(homeCave)).getWolfCaveHasSpace(world.getNonBlocking(homeCave).getClass()) == true) {
                    WolfCounter++;
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
                        oneChildOnly = true;
                    } else {
                        if (getRandomSurroundingLocation(homeCave) != null) {
                            world.setTile(getRandomSurroundingLocation(homeCave), wolf);
                            oneChildOnly = true;
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
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
                        counter++;
                        if (world.getEntities().get(this) != null) {
                            if (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) <= 1) {
                                lastPosition = world.getEntities().get(this);
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
                            if (calculateDistance(world.getEntities().get(this), closestWolfCave) <= 1) {
                                lastPosition = world.getEntities().get(this);
                                homeCave = closestWolfCave;
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
                    counter++;
                    try {
                        Dig(world.getEntities().get(this));
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
