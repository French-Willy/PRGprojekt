import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Rabbit extends Animals {
    //Location home;
    Location location;
    Location favoriteBurrow;


    public Rabbit(int age, int hunger, World world) {
        super(age, hunger, world);
        //this.home = home;
    }

    @Override
    public void act(World world) {
        super.act(world);
        if (world.getEntities().get(this) != null && world.isNight()) {
            /*world.remove(this);
            System.out.println("Rabbit has fallen asleep at this location: " + this.location);*/
            if(age == 1){
                for(Location tile : world.getSurroundingTiles()){
                    if(world.getTile(tile) == null && favoriteBurrow == null){
                        Burrow burrow = new Burrow(world);
                        world.move(this,tile);
                        world.setTile(tile,burrow);
                        favoriteBurrow = tile;
                        System.out.println("burrow lavet.... pog " + tile);
                        if(world.getLocation(this) == favoriteBurrow){
                            world.remove(this);
                        }

                    }
                }
            }
        } else if (world.isDay() && world.getEntities().get(this) == null) {

            //DENNE METODE OPVÆKKER KANIN FRA DE DØDE EFTER DEN ER DØD AF SULT...

            //world.setTile(this.location, this);
            System.out.println("Rabbit has awoken at this location: " + this.location);


            //VIL GERNE GØRE SÅLEDES, AT VI IKKE BEHØVER AT SKRIVE "ELSE" MEN, HVIS JEG IKKE GØR DET HER, SÅ LAVER DEN ET ELLER ANDET
            //MÆRKELIGT SORT MAGI, HVOR DEN BEVÆGER SIG, SELVOM DEN IKKE ER SAT IND I VERDEN - "SPØGELSE"
            //OG DET DER KAN SKE ER, AT 2 FORSKELLIGE "SPØGELSER" KAN SÆTTE SIG PÅ SAMME TILE, OG NÅR DE SÅ ET TICK SENERE SPAWNER IND
            // SÅ KOMMER DER SELVFØLGELIG EN FEJL FORDI 2 BLOCKING ELEMENTS SPAWNER PÅ HINANDEN
        } else if (world.isDay()) {
            move();
        }
    }

    public void move() {
        this.location = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
        List<Location> list = new ArrayList<>(neighbours);
        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
        Location l = list.get(randomNum);
        world.move(this, l);
        this.location = world.getLocation(this);
        seekFood(Grass.class);
    }






    /*protected void sleep() {
        //Go to this.home;
        if (this.location == this.home) {
            super.sleep();
        }
    }*/

    public void eat(Location tile) {
        try {
            // if (world.getNonBlocking(this.location).getClass() == Grass.class) {
            world.delete((world.getNonBlocking(tile)));

            hunger = hunger + 4;
            System.out.println("my hunger is now: " + hunger);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void seekFood(Class type) {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == type) {
                if (world.isTileEmpty(tile)) {
                    world.move(this, tile);
                    eat(tile);
                    break;

                }
            }
        }
    }
}





    /*public void Dig() {
        if (world.isNight() && this.home == null) {
            //Unalive.spawnHole(this.Location);
            this.home = world.getLocation();
        }
    }*/
