import itumulator.executable.Program;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.DisplayInformation;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Main {
    public static void main(String[] args) throws Exception {

        fileReader file = new fileReader();
        try {
            file.Reader();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        int size = file.worldsize_file;
        int delay = 1000;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        /*
        Type type = new Type(file.type);
        type.spawn();

         */


        Location place = new Location(0, 1);
        Person person = new Person(place);
        int grassAmount = ThreadLocalRandom.current().nextInt(3, 10);
        int animalAmount = ThreadLocalRandom.current().nextInt(1, 4);


        world.setTile(place, person);
        addGrass(world, grassAmount);
        addAnimal("rabbit",world, animalAmount);

        DisplayInformation di = new DisplayInformation(Color.RED);
        DisplayInformation diGrass = new DisplayInformation(Color.GREEN);
        DisplayInformation diRabbit = new DisplayInformation(Color.white);
        DisplayInformation diBurrow = new DisplayInformation(Color.black);
        p.setDisplayInformation(Person.class, di);
        p.setDisplayInformation(Grass.class, diGrass);
        p.setDisplayInformation(Rabbit.class, diRabbit);
        p.setDisplayInformation(Burrow.class, diBurrow);

        p.show(); // viser selve simulationen
        for (int i = 0; i < 50; i++) {
            p.simulate();
        }

    }

    public static void addGrass(World world, int grassAmount) {
        //HashMap<Location, Grass> allGrass = new HashMap<>();
        //Random r = new Random();
        for (int i = 0; i < grassAmount; i++) {
            Grass grass = new Grass(0, world);
            Location spawn = new Location(ThreadLocalRandom.current().nextInt(0, world.getSize()), ThreadLocalRandom.current().nextInt(0, world.getSize()));

             while(world.containsNonBlocking(spawn) || !world.isTileEmpty(spawn)){
                spawn = new Location(ThreadLocalRandom.current().nextInt(0, world.getSize()), ThreadLocalRandom.current().nextInt(0, world.getSize()));
             }
            world.setTile(spawn, grass);
        }
    }
    public static void addAnimal(String animalObject, World world, int animalAmount) throws Exception {
        for (int i = 0; i < animalAmount; i++) {
            Location spawn = new Location(ThreadLocalRandom.current().nextInt(0, world.getSize()), ThreadLocalRandom.current().nextInt(0, world.getSize()));

            while(world.containsNonBlocking(spawn) || !world.isTileEmpty(spawn)){
                spawn = new Location(ThreadLocalRandom.current().nextInt(0, world.getSize()), ThreadLocalRandom.current().nextInt(0, world.getSize()));
            }
            System.out.println(spawn);

            switch(animalObject) {
                case "rabbit":
                    world.setTile(spawn, new Rabbit(0,5,world));
                    break;
                default:
                    throw new Exception("no animalType");
            }
        }
    }
}

