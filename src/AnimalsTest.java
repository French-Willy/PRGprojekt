import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import itumulator.world.*;

class AnimalsTest {

    /**
     * Dyret vil korrekt dø her da det har 0 hp
     */
    @Test
    void isDead() {
        World world = new World(5);
        Animals animal = new Animals(1, 5, 0, 200, world);
        boolean isDead = animal.isDead(animal);
        assertEquals(true, isDead);
    }


    /**
     * Den finder det rigtigt det tætteste objekt af all de objekterne
     */
    @Test
    void findClosestObject() {
        World world = new World(10);

        Location animalLocation = new Location(5, 5);
        Location wolfLocation = new Location(1, 4);
        Location bearLocation1 = new Location(5, 7);
        Location bearLocation2 = new Location(8, 2);
        Location rabbitLocation = new Location(8, 9);
        Animals animal = new Animals(1, 5, 0, 200, world);
        Wolf wolf = new Wolf(1, 5, 10, 100, world);
        ;
        Rabbit rabbit = new Rabbit(1, 5, 5, 50, world);
        Bear bear1 = new Bear(1, 5, 50, 200, world, bearLocation1);
        Bear bear2 = new Bear(1, 5, 50, 200, world, bearLocation2);
        world.setTile(animalLocation, animal);
        world.setTile(wolfLocation, wolf);
        world.setTile(bearLocation1, bear1);
        world.setTile(bearLocation2, bear2);
        world.setTile(rabbitLocation, rabbit);
        Object actualclosest = animal.findClosestObject(animal, Bear.class);
        assertEquals(bear1, actualclosest);
    }

    /**
     * Den viser korrekt at den finder korteste vej
     */
    @Test
    void makePath() {
        World world = new World(5);
        Animals animal = new Animals(1, 5, 0, 200, world);
        Location initial = new Location(1, 2);
        Location target = new Location(3, 5);
        Location expected = new Location(2, 3);
        world.add(animal);
        world.setTile(initial, animal);
        // Location actual = animal.makePath(animal,initial,target);
        Location actual = animal.makePath(animal, initial, target);
        assertEquals(expected, actual);
    }


}