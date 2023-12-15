import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Rabbit extends Animals {
    Location homeBurrow;
    boolean oneChildOnly;
    HashMap<Object, Location> favoriteBurrowMap;
    Location lastPosition;

    public Rabbit(int age, int hunger, int hp, int animalMeatAmount, World world) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.oneChildOnly = true;
        this.homeBurrow = null;

    }

    @Override
    public DisplayInformation getInformation() {
        if (sleeping) {
            if (this.age >= 1) {
                return new DisplayInformation(Color.red, "rabbit-sleeping", false);
            } else {
                return new DisplayInformation(Color.red, "rabbit-small-sleeping", false);
            }
        } else {
            if (this.age >= 1) {
                return new DisplayInformation(Color.red, "rabbit-large", false);
            } else {
                return new DisplayInformation(Color.red, "rabbit-small", false);
            }
        }
    }

    @Override
    public void act(World world) {
        if (this.getHealth() < 0) {
            //die(this);
        }
        int counter = 0;
        super.act(world);


        if (world.getEntities().get(this) != null && world.isNight()) {
            seekShelter();
        }

        if (world.getEntities().get(this) == null && world.isDay()) {
            System.out.println("vågn op " + lastPosition);
            if (lastPosition != null) {
                wakeUp(lastPosition);
            } else {
                wakeUp(this.location);
            }
        } else if (world.getEntities().get(this) != null && world.isDay()) {
            //System.out.println(world.getEntities().get(this));
            sleeping = false;
            move();
            reproduction(world);
        }
    }

    public void move() {
        this.location = world.getLocation(this);
        if (world.getEmptySurroundingTiles(this.location).isEmpty()) {
        } else {
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
            } else {
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
    }


    public void eat(Location tile) {
        try {
            // if (world.getNonBlocking(this.location).getClass() == Grass.class) {
            world.delete((world.getNonBlocking(tile)));

            hunger = hunger + 3;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void seekFood(Class type) {
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


    public void Dig(Location location) {
        if (world.getTile(location) != null) {
            try {
                if (world.getNonBlocking(location).getClass() == Grass.class) {
                    //   world.delete((world.getNonBlocking(world.getCurrentLocation())));
                }
            } catch (IllegalArgumentException e) {
            }
            if (location != null) {
                Burrow.createNewBurrow(world, world.getEntities().get(this));
            }


        }
    }

    public void wakeUp(Location location) {
        if (world.isTileEmpty(location)) {
            System.out.println();
            world.setTile(location, this);
        }
    }

    private void reproduction(World world) {
        if (age == 1 && this.oneChildOnly) {
            for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile) != null) {
                    System.out.println("hjælp");
                    if (world.getTile(tile).getClass() == Rabbit.class && world.getTile(tile) != this) {
                        Rabbit rabbit = (Rabbit) world.getTile(tile);
                        if (rabbit.age >= 1 && rabbit.oneChildOnly) {
                            Rabbit rabbitChild = new Rabbit(0, 5, 5, 20, world);
                            Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                            List<Location> list = new ArrayList<>(neighbours);
                            if (list.size() <= 0) {
                                //deleteGrass(this,world);
                            } else {
                                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                                Location l = list.get(randomNum);
                                world.setTile(l, rabbitChild);
                                System.out.println("jeg har født");
                                this.oneChildOnly = false;
                                if (this.homeBurrow != null) {
                                    Burrow burrow = (Burrow) world.getNonBlocking(homeBurrow);
                                    if (burrow.burrowSpace.size() < 5) {
                                        System.out.println("mine forældres hul er kun: " + burrow.burrowSpace.size() + " fyldt");
                                        burrow.enterBurrow(rabbitChild);
                                        rabbitChild.homeBurrow = homeBurrow;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }


    public void seekShelter() {
        int counter = 0;
        double distancetoClosestBurrow = 100.0;
        Location closestBurrow = null;
        boolean areHoles = false;
        Burrow burrow = null;


        if (homeBurrow != null) {
            if (world.getEntities().get(this) != null && world.isNight()) {
                if (calculateDistance(world.getEntities().get(this), homeBurrow) <= 1) {
                    lastPosition = world.getEntities().get(this);
                    world.remove(this);
                } else if (calculateDistance(world.getEntities().get(this), homeBurrow) <= 5) {
                    makePath(this, world.getEntities().get(this), homeBurrow);
                } else {
                    sleeping = true;
                }
            }
        } else if (homeBurrow == null) {
            for (Object entity : world.getEntities().keySet()) {
                if (entity.getClass() == Burrow.class) {
                    if (((Burrow) entity).getBurrowSpace(entity).contains(this) == false && ((Burrow) entity).getBurrowSpace(entity).size() < 5) {
                        while (calculateDistance(world.getEntities().get(this), world.getEntities().get(entity)) < distancetoClosestBurrow) {
                            distancetoClosestBurrow = calculateDistance(world.getLocation(this), world.getLocation(entity));
                            closestBurrow = world.getEntities().get(entity);
                            System.out.println(closestBurrow);
                            areHoles = true;
                            burrow = (Burrow) entity;
                        }
                    }
                }
            }
            if (areHoles) {
                if (calculateDistance(world.getEntities().get(this), closestBurrow) <= 1) {
                    lastPosition = world.getEntities().get(this);
                    homeBurrow = closestBurrow;
                    world.remove(this);
                    burrow.enterBurrow(this);
                } else if (distancetoClosestBurrow < 7) {
                    makePath(this, world.getEntities().get(this), closestBurrow);

                } else if (distancetoClosestBurrow >= 7) {
                    try {
                        Dig(world.getEntities().get(this));
                        homeBurrow = world.getEntities().get(this);
                        for (Object object : world.getEntities().keySet()) {
                            if (object.getClass() == Burrow.class) {
                                if (world.getLocation(object) == this.location) {
                                    ((Burrow) object).enterBurrow(this);
                                }
                            }
                        }
                    } catch (IllegalArgumentException i) {
                    }
                }
            } else if (!areHoles) {
                try {
                    Dig(world.getEntities().get(this));
                    homeBurrow = world.getEntities().get(this);
                    for (Object object : world.getEntities().keySet()) {
                        if (object.getClass() == Burrow.class) {
                            if (world.getLocation(object) == this.location) {
                                ((Burrow) object).enterBurrow(this);
                            }
                        }
                    }
                } catch (IllegalArgumentException i) {
                }
            }
        }
    }
}