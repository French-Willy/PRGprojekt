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

    public Animals(int age, int hunger, World world) {
        this.age = age;
        this.hunger = hunger;
        this.world = world;
    }

    public void ageing() {
        timeCount++;
        if (timeCount % 20 == 0) {
            age++;
            System.out.println("Jeg har fødselsdag, før var jeg: " + (age-1) + " nu er jeg: " + age);
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


}


