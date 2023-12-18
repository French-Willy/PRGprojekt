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
    public void act(World world) {
        super.act(world);
        if (hp < 50) {
            regenerate();
        }

        if (world.getEntities().get(this) != null && world.isNight()) {
            sleeping = true;

        } else if (world.isDay() && world.getEntities().get(this) == null) {

            System.out.println("Bear has awoken at this location: " + this.location);

        } else if (world.isDay()) {
            move();
            ofAge();
        }
    }

    private void ofAge(){
        if(this.age > 4){
            this.atk = atk + 5;
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
            if (territory.contains(l)) {
                mayIMove = true;
                territoryLocation = l;
            } else {
                mayIMove = false;
            }
        }
        world.move(this, territoryLocation);
        this.location = world.getLocation(this);
        if (hunger < 6)
            findFood();

    }


    public void seekFood(HashSet<Class> surroundingFood) {
        if (hunger < 2) {
            if (surroundingFood.contains(Wolf.class)) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == Wolf.class) {
                        attack(this, (Animals) world.getTile(tile));
                        break;
                    }
                }

            } else if (surroundingFood.contains(Rabbit.class)) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class) {
                        attack(this, (Animals) world.getTile(tile));
                        break;
                    }
                }

            } else if (surroundingFood.contains(Bush.class)) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == Bush.class) {
                        Bush bush = (Bush) world.getTile(tile);

                        if (bush.hasBerries) {
                            eatBerries(bush);
                        }
                        break;
                    }
                }
            }
        } else if (hunger < 5) {
            for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class) {
                    attack(this, (Animals) world.getTile(tile));
                    break;
                }
            }
        }
    }


    private void findFood() {
        HashSet<Class> surroundingFood = new HashSet<>();

        for (Object x : world.getSurroundingTiles(this.location)) {
            surroundingFood.add(x.getClass());
        }
        seekFood(surroundingFood);
    }

    public void eatBerries(Bush bush) {
        bush.eatBerries();
        eat(1);
    }

    public void eat(int foodSize) {
        this.hunger = hunger + foodSize;
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
}

