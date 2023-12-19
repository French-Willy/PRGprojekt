import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Animals {
    Set<Location> territory;

    public Bear(int age, int hunger, int hp, int animalMeatAmount, World world, Location location) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.atk = 10;
        this.hp = hp;
        this.territory = world.getSurroundingTiles(location, 2);
    }

    @Override
    public DisplayInformation getInformation() {
        if (this.age > 4) {
            if (sleeping) {
                return new DisplayInformation(Color.GRAY, "bear-sleeping", false);
            } else {
                return new DisplayInformation(Color.GRAY, "bear", false);
            }
        } else {
            if (sleeping) {
                return new DisplayInformation(Color.GRAY, "bear-small-sleeping", false);
            } else {
                return new DisplayInformation(Color.GRAY, "bear-small", false);
            }
        }
    }

    @Override
    public void act(World world) {
        super.act(world);
        if (hp < 50 && hunger > 6) {
            regenerate();
        }

        if (world.getEntities().get(this) != null && world.isNight()) {
            sleeping = true;

        } else if (world.isDay() && world.getEntities().get(this) == null) {

            System.out.println("Bear has awoken at this location: " + this.location);

        } else if (world.isDay()) {
            if (!ofAge) {
                ofAge();
            }
            sleeping = false;
            if (Danger()) {
                protectTerritory();
            } else if (hunger <= 10) {
                seekFood();
                System.out.println("HUNGRYYY: " + hunger);
            } else {
                move();
            }
        }
    }

    private void ofAge() {

        if (this.age > 4) {
            this.atk = atk + 5;
            this.animalMeatAmount = animalMeatAmount * 2;
            ofAge = true;
        }
    }

    private void move() {
        boolean mayIMove = false;
        Location territoryLocation = null;
        this.location = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
        List<Location> list = new ArrayList<>(neighbours);

        while (mayIMove == false) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
            Location l = list.get(randomNum);
            if (this.territory.contains(l)) {
                mayIMove = true;
                territoryLocation = l;
            } else {
                mayIMove = false;
            }
        }
        world.move(this, territoryLocation);
        this.location = world.getLocation(this);
    }

    private boolean Danger() {
        HashSet<Class> surroundingAnimals = findFood();
        if (surroundingAnimals.contains(Wolf.class)) {
            return true;
        } else {
            return false;
        }
    }

    private void protectTerritory() {
        double DistanceToClosestWolf = findClosestObjectDistance(this, Wolf.class);
        Object closestWolf = findClosestObject(this, Wolf.class);
        for (Location tile : territory) {
            if (world.getTile(tile) == closestWolf) {
                Wolf wolf = (Wolf) closestWolf;
                if (DistanceToClosestWolf <= 1.5) {
                    attack(this, (Wolf) closestWolf);
                } else {
                    makePath(this, world.getEntities().get(this), world.getLocation(closestWolf));
                    if (DistanceToClosestWolf <= 1.5) {
                        attack(this, (Wolf) closestWolf);
                    }
                }
            }
        }
    }

    public void seekFood() {
        HashSet<Class> surroundingFood = findFood();
        if (hunger < 11) {
            if (surroundingFood.contains(Carcass.class)) {
                double DistanceToClosestCarcass = findClosestObjectDistance(this, Carcass.class);
                Object closestCarcass = findClosestObject(this, Carcass.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestCarcass) {
                        if (DistanceToClosestCarcass <= 1.5) {
                            eat((Carcass) closestCarcass, 20);
                            System.out.println("NOM NOM NOM, my hunger is now: " + hunger);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestCarcass));
                            if (DistanceToClosestCarcass <= 1.5) {
                                eat((Carcass) closestCarcass, 20);
                                System.out.println("NOM NOM NOM, my hunger is now: " + hunger);
                            }
                        }
                    }
                }
            }
            if (surroundingFood.contains(Rabbit.class)) {
                double DistanceToClosestRabbit = findClosestObjectDistance(this, Rabbit.class);
                Object closestRabbit = findClosestObject(this, Rabbit.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestRabbit) {
                        if (DistanceToClosestRabbit <= 1.5) {
                            attack(this, (Animals) closestRabbit);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestRabbit));
                            if (DistanceToClosestRabbit <= 1.5) {
                                attack(this, (Animals) closestRabbit);
                                break;
                            }
                        }
                    }
                }
            } else if (surroundingFood.contains(Bush.class)) {
                double DistanceToClosestBush = findClosestObjectDistance(this, Bush.class);
                Object closestBush = findClosestObject(this, Bush.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestBush) {
                        if (DistanceToClosestBush <= 1.5) {
                            eatBerries((Bush) closestBush);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestBush));
                            if (DistanceToClosestBush <= 1.5) {
                                eatBerries((Bush) closestBush);
                                break;
                            }
                        }

                    }
                }
            } else { move(); }

        } else if (hunger <= 6) {
            if (surroundingFood.contains(Bush.class)) {
                double DistanceToClosestBush = findClosestObjectDistance(this, Bush.class);
                Object closestBush = findClosestObject(this, Bush.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestBush) {
                        if (DistanceToClosestBush <= 1.5) {
                            eatBerries((Bush) closestBush);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestBush));
                            if (DistanceToClosestBush <= 1.5) {
                                eatBerries((Bush) closestBush);
                                break;
                            }
                        }

                    }
                }
            } else {
                move();
            }
        }
    }

    private HashSet<Class> findFood() {
        HashSet<Class> surroundingFood = new HashSet<>();

        for (Location tile : territory) {
            if (world.getTile(tile) != null) {
                surroundingFood.add(world.getTile(tile).getClass());
            }
        }
        return surroundingFood;
    }

    public void eatBerries(Bush bush) {
        bush.eatBerries();
        eat(1);
    }

    public void eat(int foodSize) {
        this.hunger = hunger + foodSize;
    }
}

