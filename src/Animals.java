import itumulator.simulator.Actor;
import itumulator.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
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

    public Animals(int age, int hunger, int hp, World world) {
        this.age = age;
        this.hunger = hunger;
        this.hp = hp;
        this.world = world;
    }

    public void ageing() {
        timeCount++;
        if (timeCount % 20 == 0) {
            age++;
            System.out.println("Jeg har fødselsdag, før var jeg: " + (age - 1) + " nu er jeg: " + age);
        }
        if (age == 10) {
            die();
        }
    }

    /*public void seekFood(Object object) {
        for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile).getClass() == object.getClass()) {
                    world.move(this, tile);
                    eating(object);
                }
        }
    }*/


    public void sleep() {
        world.remove(this);
    }

    public void die() {
        world.delete(this);
    }


    public void reproduce() {

    }

    //Gør det som alle dyr har tilfælles.
    @Override
    public void act(World world) {
        if (world.isDay()) {
            hunger--;
            //checkHunger();
        }
        ageing();
    }

    public void checkHunger() {
        if (hunger == 0) {
            die();
            System.out.println("I died of hunger");
        }
    }

    public void takeDamage(int attack) {
        hp = hp - attack;
    }

    public Object findObject(Object object, Class type) {
        for (Object objects : world.getEntities().keySet()) {
            if (object.getClass() == type) {
                return objects;
            }
        }
        return null;
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

    public int getHealth() {
        return hp;
    }

    public boolean dead(Object object) {
        if (world.getEntities().get(object) == null) {
            return true;

        } else
            return false;
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
}