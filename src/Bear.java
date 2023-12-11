import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Animals {
    Set<Location> territory;

    public Bear(int age, int hunger, int hp, int animalMeatAmount, World world,Location location) {
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


        } else if (world.isDay() && world.getEntities().get(this) == null) {

            System.out.println("Bear has awoken at this location: " + this.location);

        } else if (world.isDay()) {
            move();
        }
    }

    private void move() {
        boolean mayIMove = false;
        Location territoryLocation = null;
        this.location = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
        List<Location> list = new ArrayList<>(neighbours);
        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());

        while (!mayIMove) {
            Location l = list.get(randomNum);
            for (Location allowedLocation : territory) {
                if (l == allowedLocation) {
                    mayIMove = true;
                    territoryLocation = l;
                }
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
                        attack(tile);
                        break;
                    }
                }

            } else if (surroundingFood.contains(Rabbit.class)) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class) {
                        attack(tile);
                        break;
                    }
                }

            } else if (surroundingFood.contains(Bush.class)) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == Bush.class) {
                        Bush bush = (Bush) world.getTile(tile);
                        if(bush.hasBerries) {
                        eatBerries(bush);
                        }
                        break;
                    }
                }
            }

        } else if (hunger < 5) {
            for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class) {
                    attack(tile);
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

    public void attack(Location tile) {
        try {
            if (world.getTile(tile).getClass() == Rabbit.class) {
                Rabbit rabbit = (Rabbit) world.getTile(tile);
                rabbit.takeDamage(this.atk);
                if (rabbit.hp <= 0) {
                    eat(3);
                    world.delete(rabbit);
                }


            } else if (world.getTile(tile).getClass() == Wolf.class) {
                Wolf wolf = (Wolf) world.getTile(tile);
                wolf.takeDamage(this.atk);
                if (wolf.hp <= 0) {
                    eat(5);
                    world.delete(wolf);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void eatBerries(Bush bush){
        bush.hasBerries = false;
        eat(1);
    }

    public void eat(int foodSize) {
        this.hunger = hunger + foodSize;
    }
}
