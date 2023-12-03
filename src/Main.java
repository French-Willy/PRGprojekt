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
            //file.getInputLines();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int size = file.worldsize_file;
        int delay = 1000;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        Location place = new Location(0, 1);
        Location bearSpawn = new Location(3,3);
        Person person = new Person(place);
        Bear bear = new Bear(0, 10, world);

        world.setTile(place, person);
        world.setTile(bearSpawn, bear);

        //Adds the inputlist from the filereader into main
        for(ArrayList<String> spawnObject : file.inputLines){
            int min = Integer.parseInt(spawnObject.get(1));
            int max = Integer.parseInt(spawnObject.get(2));
            int spawnObjectAmount = ThreadLocalRandom.current().nextInt(min,max + 1);
            addObject(spawnObject.get(0),world,spawnObjectAmount);
        }

        DisplayInformation di = new DisplayInformation(Color.RED,"steve",true);
        DisplayInformation diGrass = new DisplayInformation(Color.GREEN, "grass1",true);
        DisplayInformation diRabbit = new DisplayInformation(Color.white,"rabbit-large",false);
        DisplayInformation diBurrow = new DisplayInformation(Color.black,"hole",false);
        DisplayInformation diBear = new DisplayInformation(Color.GRAY, "bear",false);


        p.setDisplayInformation(Person.class, di);
        p.setDisplayInformation(Grass.class, diGrass);
        p.setDisplayInformation(Rabbit.class, diRabbit);
        p.setDisplayInformation(Burrow.class, diBurrow);
        p.setDisplayInformation(Bear.class, diBear);

        p.show(); // viser selve simulationen
        for (int i = 0; i < 50; i++) {
            p.simulate();
        }

    }

    //Method for spawning object on random tiles. Cheecks it spawns on empty tiles
    //Uses a switch case system that spawns the specific object based on the string thats asigned.
    public static void addObject(String stringSpwanObject, World world, int spawnObjectAmount) throws Exception {
        for (int i = 0; i < spawnObjectAmount; i++) {
            Location spawn = new Location(ThreadLocalRandom.current().nextInt(0, world.getSize()), ThreadLocalRandom.current().nextInt(0, world.getSize()));

            while(world.containsNonBlocking(spawn) || !world.isTileEmpty(spawn)){
                spawn = new Location(ThreadLocalRandom.current().nextInt(0, world.getSize()), ThreadLocalRandom.current().nextInt(0, world.getSize()));
            }
            switch(stringSpwanObject) {
                case "rabbit":
                    world.setTile(spawn, new Rabbit(0,5,world));
                    break;
                case "grass":
                    world.setTile(spawn, new Grass(0,world));
                    break;
                default:
                    throw new Exception("no spawn type");
            }
        }
    }
}

