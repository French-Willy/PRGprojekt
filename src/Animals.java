import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Animals implements Actor, DynamicDisplayInformationProvider {
    protected int age;
    protected int timeCount;
    protected int hunger;
    protected World world;
    protected Location location;
    protected int hp;
    protected int atk;
    protected int animalMeatAmount;
    boolean sleeping;
    protected int maxHP;
    protected boolean ofAge;

    public Animals(int age, int hunger, int hp, int animalMeatAmount, World world) {
        this.age = age;
        this.hunger = hunger;
        this.hp = hp;
        this.animalMeatAmount = animalMeatAmount;
        this.world = world;
        this.maxHP = hp;
    }

    /**
     *
     */
    @Override
    public void act(World world) {
        timeCount++;
        if (world.isDay()) {
            hunger();
            //checkHunger();
        }
        ageing();
        if (isDead(this) && getLocation(this) != null) {
            die(this);
        }
    }

    /**
     * Det her er bare en forsimpling af metoden world.getEntities.get(this);
     * @param animal
     * @return Location
     */
    public Location getLocation(Animals animal) {
        return world.getEntities().get(animal);
    }

    /**
     * Den her metode bliver kaldt når dyret står ved siden af et carcass og skal spise. Og biteSize afhænger af dyret
     * @param carcass
     * @param biteSize
     */
    protected void eat(Carcass carcass, int biteSize) {
        carcass.meatEaten(biteSize);
        hunger = hunger + 5;
    }

    /**
     * Den her bliver kørt ved hver act, og sørger for at dyrene bliver ældre hver morgen, pånær første dag
     * Og den sørger for at dyr dør når de bliver for gamle
     */
    public void ageing() {
        if (timeCount % 20 == 0) {
            age++;
        }
        if (age == 10) {
            die(this);
        }
    }

    /**
     * Den her giver dyrene liv når de har høj nok hunger
     */
    public void regenerate() {
        if (hunger > 5) {
            hp++;
        }
    }

    /**
     * Den her bliver kaldt hver gang et dyr tager af skade af nogen art og tjekker om de stadigt skal være live eller om de skal dø og fjernes
     * @param animal
     */
    public void die(Animals animal) {
        Location location = getLocation(animal);
        world.delete(animal);
        if(location != null) {
            world.setTile(location, new Carcass(animalMeatAmount, world));
        }
    }

    /**
     * Den her bevæger dyret i en tilfældig retning og bliver kaldt i underklasserne, hvis de ikke skal lave en specifik bevægelse
     * @param animal
     */
    protected void move(Animals animal){
        world.move(animal, getRandomSurroundingLocation(getLocation(animal)));
    }

    /**
     * Den her metode får dyrenes hunger til at falde hvert 4. step, det vil sige et dyr mister 5 hunger per døgn
     */
    public void hunger() {
        if (timeCount % 4 == 0 && hunger > 0) {
            hunger--;
        }
        checkHunger();
    }

    /**
     * Den her metode tjekker, hvorhvidt dyrets hunger er så lav eller høj at den skal miste eller få liv.
     */
    public void checkHunger() {
        if (hunger <= 0) {
            this.hp--;
        }
        if (hunger > 8 && hp < maxHP) {
            this.hp++;
        }
    }

    /**
     * Det her er vores combat metode, den får input fra hvilket dyr der angriber og hvad der biver angrebet og beregner så skade
     * på den der bliver angrebet og tjekker om det var nok skade til at de dør
     * @param attacker
     * @param target
     */
    public void attack(Animals attacker, Animals target) {
        target.hp = target.hp - attacker.atk;
        if (isDead(target)) {
            target.die(target);
        }
    }

    /**
     * Den her metode tjekker om dyret er dødt
     * @param animal
     * @return true or false
     */
    public boolean isDead(Animals animal) {
        if (animal.hp <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * returnere hp
     * @return hp
     */
    public int getHealth() {
        return hp;
    }

    /**
     * Den her bruger vi hver gang vi skal have en tilfældig omkring liggende lokation, det kan være til move, eller når
     * dyrene skal vågne op fra deres huller eller huler.
     * @param location
     * @return Location
     */
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

    /**
     * Den her finder det tætteste objekt af en angivet type og det bliver brugt når fx en ulv skal jage en kanin
     * @param animal
     * @param type
     * @return objekt
     */
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

    /**
     * Den her gør næsten det samme som ovenover, men giver i stedet afstanden til det tætteste objekt, da et dyr ikke nødvendigvis vil
     * hen til det tætteste objekt hvis det er for langt væk.
     * @param animal
     * @param type
     * @return Double
     */
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

    /**
     * Den her beregner afstanden mellem to lokationer ud fra start og slut lokation ved brug af pythagoras.
     * @param initial
     * @param target
     * @return double
     */
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

    /**
     * Den her metode gør brug af calculateDistance() til at angive den kortest mulige vej fra en start lokation og et step
     * Tættere mod en slut lokation og bliver brugt hver gang noget skal finde vej til noget andet
     * @param object
     * @param initial
     * @param target
     * @return bruges kun til testning, return bruges ikke i koden
     */
    public Location makePath(Object object, Location initial, Location target) {
        double shortestDistance = calculateDistance(initial, target);
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
                return closestTile;
            }
        }
        return null;
    }

    /**
     * Den her gør det samme som ovenover, men bliver brugt hvis noget skal væk fra et objekt fx når kaniner flygter
     * @param object
     * @param initial
     * @param target
     */
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

    @Override
    public DisplayInformation getInformation() {
        return null;
    }
}