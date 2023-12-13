import itumulator.simulator.Actor;
import itumulator.world.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Animals implements Actor {
    protected int age;
    protected int timeCount;
    protected int hunger;
    protected String type;
    protected World world;
    protected Location location;
    protected int hp;
    protected int atk;
    protected int animalMeatAmount;
    protected int maxHP;

    public Animals(int age, int hunger, int hp, int animalMeatAmount, World world) {
        this.age = age;
        this.hunger = hunger;
        this.hp = hp;
        this.animalMeatAmount = animalMeatAmount;
        this.world = world;
        this.maxHP = hp;
    }

    public Location getLocation(Animals animal) {
        return world.getEntities().get(animal);
    }

    public void ageing() {
        if (timeCount % 20 == 0) {
            age++;
        }
        if (age == 10) {
            die(this);
        }
    }


    public void regenerate() {
        if (hunger > 5) {
            hp++;
        }
    }

    public void die(Animals animal) {
        Location location = world.getLocation(animal);
        world.delete(animal);
        world.setTile(location, new Carcass(animalMeatAmount, world));
    }


    public void reproduce() {

    }

    //Gør det som alle dyr har tilfælles.
    @Override
    public void act(World world) {
        timeCount ++;
        if (world.isDay()) {
            hunger();
            //checkHunger();
        }
        ageing();
    }

    public void hunger(){
        if (timeCount % 4 == 0 && hunger > 0){
            hunger --;
        }
        checkHunger();
    }

    public void checkHunger() {
        if (hunger <= 0) {
            this.hp --;
        }
        if (hunger > 8 && hp < maxHP){
            this.hp ++;
        }
    }

    public void attack(Animals attacker, Animals target) {
        takeDamage(target, attacker.atk);
        if (isDead(target)) {
            target.die(target);
        }
    }

    public void takeDamage(Animals target, int attack) {
        target.hp = target.hp - attack;
    }

    public boolean isDead(Animals animal) {
        if (animal.hp <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getHealth() {
        return hp;
    }

    public Location getRandomSurroundingLocation(Location location) {
        List<Location> SurroundingTiles = new ArrayList<>();
        for (Location tile : world.getEmptySurroundingTiles(location)) {
            SurroundingTiles.add(tile);
        }
        if (SurroundingTiles.size() > 0) {
            int randomInt = ThreadLocalRandom.current().nextInt(0, SurroundingTiles.size());
            return SurroundingTiles.get(randomInt);
        } else {
            return null;
        }
    }

    public Object findClosestObject(Animals animal, Class type) {
        int counter = 0;
        double closestDistance = 100;
        Object closestObject = null;
        for (Object object : world.getEntities().keySet()) {
            if (object.getClass() == type) {
                if (world.getEntities().get(object) != null) {
                    while (calculateDistance(world.getEntities().get(animal), world.getEntities().get(object)) < closestDistance) {
                        closestDistance = calculateDistance(world.getEntities().get(animal), world.getEntities().get(object));
                        closestObject = object;
                    }
                }
            }
        }
        return closestObject;
    }

    public double findClosestObjectDistance(Animals animal, Class type) {
        int counter = 0;
        double closestDistance = 100;
        Object closestObject = null;
        for (Object object : world.getEntities().keySet()) {
            if (object.getClass() == type) {
                if (world.getEntities().get(object) != null) {
                    while (calculateDistance(world.getEntities().get(animal), world.getEntities().get(object)) < closestDistance) {
                        closestDistance = calculateDistance(world.getEntities().get(animal), world.getEntities().get(object));
                        closestObject = object;
                    }
                }
            }
        }
        return closestDistance;
    }


    public double calculateDistance(Location initial, Location target) {
        double x;
        double y;
        if (initial.getX() == target.getX()) {
            x = 0;
        } else if (initial.getX() > target.getY()) {
            x = (initial.getX() - target.getX());
            x = x * x;
        } else {
            x = (-initial.getX() + target.getX());
            x = x * x;
        }
        if (initial.getY() == target.getY()) {
            y = 0;
        } else if (initial.getY() > target.getY()) {
            y = (initial.getY() - target.getY());
            y = y * y;
        } else {
            y = (-initial.getY() + target.getY());
            y = y * y;
        }
        double distance = Math.sqrt(x + y);
        return distance;
    }

    public void makePath(Object object, Location initial, Location target) {
        double shortestDistance = calculateDistance(world.getCurrentLocation(), target);
        Location closestTile = null;

        if (initial == target) {
        } else {
            for (Location emptyTile : world.getEmptySurroundingTiles(initial)) {
                while (calculateDistance(emptyTile, target) < shortestDistance) {
                    shortestDistance = calculateDistance(emptyTile, target);
                    closestTile = emptyTile;
                }
            }
            if (closestTile != null) {
                world.move(object, closestTile);
            }
        }
    }

    public void makePathAway(Object object, Location initial, Location target) {
        double furthestDistance = calculateDistance(world.getCurrentLocation(), target);
        Location furthestTile = null;

        if (initial == target) {
        } else {
            for (Location emptyTile : world.getEmptySurroundingTiles(initial)) {
                while (calculateDistance(emptyTile, target) > furthestDistance) {
                    furthestDistance = calculateDistance(emptyTile, target);
                    furthestTile = emptyTile;
                }
            }
            if (furthestTile != null) {
                world.move(object, furthestTile);
            }
        }
    }
}