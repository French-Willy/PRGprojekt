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
            System.out.println("Jeg har fødselsdag, før var jeg: " + (age-1) + " nu er jeg: " + age);
        }
        if (age == 10) {
            die();
        }
    }

    public void sleep() {
        world.remove(this);
    }

    public void die() {
        world.delete(this);
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
    public void takeDamage(int attack){
        hp = hp-attack;
    }

    public int getHealth(){
        return hp;
    }

    public boolean dead(Object object){
        if (world.getEntities().get(object) == null){
            return true;

        }else
            return false;
    }
}


