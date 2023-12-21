import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class WolfTest {

    @Test
    void getInformation() {
    }

    @Test
    void act() {
    }

    @Test
    void move() {
    }

    @Test
    void territory() {
    }

    @Test
    void seekFood() {
        World world = new World(10);

        Location wolf1Location = new Location(5,5);
        Location wolf2Location = new Location(5,4);
        Location cave1 = new Location(0,0);
        Location cave2 = new Location(9,9);

        Wolf wolf1 = new Wolf(1,5,10,100,world);
        Wolf wolf2 = new Wolf(1,5,10,100,world);
        HashSet<Wolf> caveSet1 = new HashSet<>();
        HashSet<Wolf> caveSet2 = new HashSet<>();
        WolfCave wolfcave1 = new WolfCave(world,caveSet1);
        WolfCave wolfcave2 = new WolfCave(world,caveSet2);
        world.setTile(wolf1Location,wolf1);
        world.setTile(wolf2Location,wolf2);
        world.setTile(cave1,wolfcave1);
        world.setTile(cave2,wolfcave2);
        world.setTile(new Location(4,4),new Object());
        world.setTile(new Location(4,5),new Object());
        world.setTile(new Location(4,6),new Object());
        world.setTile(new Location(5,6),new Object());
        world.setTile(new Location(6,4),new Object());
        world.setTile(new Location(6,5),new Object());
        world.setTile(new Location(6,6),new Object());
        wolf1.getLocation(wolf1);
        wolf1.seekFood();
        int wolf2HpActual =  wolf2.getHealth();
        int wolf2HpExpected = 5;
        assertEquals(wolf2HpExpected,wolf2HpActual);


    }

    @Test
    void packHuntBear() {
    }

    @Test
    void isPack() {
    }

    @Test
    void dig() {
    }

    @Test
    void wakeUp() {
    }

    @Test
    void reproduce() {
    }

    @Test
    void seekCave() {
    }
}