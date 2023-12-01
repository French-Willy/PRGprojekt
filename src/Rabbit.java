import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Rabbit extends Animals {
    //Location home;
    Location favoriteBurrow;
    boolean oneChildOnly;

    public Rabbit(int age, int hunger, World world) {
        super(age, hunger, world);
        this.oneChildOnly = true;
    }

    @Override
    public void act(World world) {
        super.act(world);
        if (world.getEntities().get(this) != null && world.isNight()) {
            /*world.remove(this);
            System.out.println("Rabbit has fallen asleep at this location: " + this.location);*/

            if (favoriteBurrow == null) {
                if (findAllEmptyBurrows() == null) {
                    Dig();

                } else {
                    //****** TJEK METODE I BUNDEN FOR AT FORSTÅ*****
                   Location locationBurrow = getClosestBurrow(findAllEmptyBurrows());
                    while(locationBurrow != this.location){

                        //LAV PATHING TIL LOCATION
                    }

                    //Når kaninen er på hullet, skal den bare removes og det kalder en metode i Burrow:
                    //enterBurrow skal sige, at Burrow nu indeholder
                    if(this.location == locationBurrow) {
                        Burrow.enterBurrow(this, (Burrow) world.getTile(locationBurrow));
                        world.remove(this);
                        favoriteBurrow = locationBurrow;
                    }

                }
            } else if (favoriteBurrow != null){
                while(favoriteBurrow != this.location){
                    //LAV PATHING TIL "favoriteBurrow".
                }
            }
        } else if (world.isDay() && world.getEntities().get(this) == null) {
            //DENNE METODE OPVÆKKER KANIN FRA DE DØDE EFTER DEN ER DØD AF SULT...
            //world.setTile(this.location, this);
            System.out.println("Rabbit has awoken at this location: " + this.location);
            wakeUp();

            //VIL GERNE GØRE SÅLEDES, AT VI IKKE BEHØVER AT SKRIVE "ELSE" MEN, HVIS JEG IKKE GØR DET HER, SÅ LAVER DEN ET ELLER ANDET
            //MÆRKELIGT SORT MAGI, HVOR DEN BEVÆGER SIG, SELVOM DEN IKKE ER SAT IND I VERDEN - "SPØGELSE"
            //OG DET DER KAN SKE ER, AT 2 FORSKELLIGE "SPØGELSER" KAN SÆTTE SIG PÅ SAMME TILE, OG NÅR DE SÅ ET TICK SENERE SPAWNER IND
            // SÅ KOMMER DER SELVFØLGELIG EN FEJL FORDI 2 BLOCKING ELEMENTS SPAWNER PÅ HINANDEN
        } else if (world.isDay()) {
            move();
            reproduction(world);
        }
    }


    public void move() {
        this.location = world.getLocation(this);
        if (world.getEmptySurroundingTiles(this.location).isEmpty()) {

        } else {
            if (age > 1 || hunger < 3) {
                if (timeCount % 2 == 0) {
                    this.location = world.getLocation(this);
                    Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                    List<Location> list = new ArrayList<>(neighbours);
                    int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                    Location l = list.get(randomNum);
                    world.move(this, l);
                    this.location = world.getLocation(this);
                    seekFood(Grass.class);
                }
            } else {
                this.location = world.getLocation(this);
                Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                List<Location> list = new ArrayList<>(neighbours);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                Location l = list.get(randomNum);
                world.move(this, l);
                this.location = world.getLocation(this);
                seekFood(Grass.class);

            }
        }
    }

    public void eat(Location tile) {
        try {
            // if (world.getNonBlocking(this.location).getClass() == Grass.class) {
            world.delete((world.getNonBlocking(tile)));

            hunger = hunger + 4;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void seekFood(Class type) {
        if (hunger < 5) {
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


    public void Dig() {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) == null && favoriteBurrow == null) {
                Burrow burrow = new Burrow(world);
                world.move(this, tile);
                world.setTile(tile, burrow);
                favoriteBurrow = tile;
                System.out.println("burrow lavet.... pog " + favoriteBurrow);
                if (world.getLocation(this) == favoriteBurrow) {
                    this.location = world.getLocation(this);
                    world.remove(this);
                }
            } //else if (favoriteBurrow != null) {
            //findFavoriteHole();
            // }
        }
    }

    public void wakeUp() {
        for (Location tile : world.getSurroundingTiles(this.favoriteBurrow))
            if (world.isTileEmpty(tile)) {
                world.setTile(tile, this);
                this.location = world.getLocation(this);
                break;
            } else {

            }
    }


    private void reproduction(World world) {
        if (age == 2 && this.oneChildOnly)
            for (Location tile : world.getSurroundingTiles()) {
                if (world.getTile(tile) != null) {
                    if (world.getTile(tile).getClass() == Rabbit.class && world.getTile(tile) != this) {
                        Rabbit rabbitChild = new Rabbit(0, 5, world);

                        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                        List<Location> list = new ArrayList<>(neighbours);


                        if (list.size() <= 0) {
                            //deleteGrass(this,world);
                        } else {
                            int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                            Location l = list.get(randomNum);
                            world.setTile(l, rabbitChild);
                            rabbitChild.favoriteBurrow = this.favoriteBurrow;
                            this.oneChildOnly = false;
                            break;

                        }
                    }


                }
            }

    }


    //DENNE METODE VIL FINDE ALLE HULLER HVOR DER ER PLADS TIL KANINEN
    public ArrayList<Location> findAllEmptyBurrows() {
        ArrayList<Location> allBurrowsLocation = new ArrayList<>();
        ArrayList<Location> allEmptyBurrowsLocation = new ArrayList<>();

        // Denne foreach-lykke vil kigge igennem alle entities på mappen, og bagefter se, om det er en Burrow class
        //og så tilføje lokationen til arraylisten allBurrowsLocation.
        for (Object x : world.getEntities().keySet()) {
            if (x.getClass() == Burrow.class) {
                allBurrowsLocation.add(world.getEntities().get(x));
            }
        }
        if(allBurrowsLocation.isEmpty()){
            return null;
        }
        //Denne foreach-lykke er lidt kompliceret.
        //Vi kigger igennem alle lookationerne af burrowsene, og når vi kalder "world.getTile(x)" og vi caster den til
        // at være en Burrow så vil checkFullBurrow-metoden fra Burrow-klassen tjekke hvilke Burrows har ledig plads,
        // og tilføje dem til den anden arraylist "allEmptyBurrowsLocation".
        for (Location x : allBurrowsLocation) {
            if (Burrow.checkFullBurrow((Burrow) world.getTile(x))) {
                allEmptyBurrowsLocation.add(x);
            }
        }
        //Det kunne jo være, at der ikke var nogle huller der var ledige, så den returner null og så vil
        //rabbit grave sit eget hul.
        if (allEmptyBurrowsLocation.isEmpty()) {
            return null;


            //Returnerer ArrayList med burrows, hvor der er plads til kaninen.
        } else {
            return allEmptyBurrowsLocation;
        }
    }

    //Denne metode er lavet til at modtage den Arrayliste, som kommer fra "findAllEmptyBurrows".
    // Lige nu gør den ikke noget, men jeg tænker den skal |||||returnere, den burrow der er tættest på kaninen.|||||
    // Kan være der skal lidt pathing til?.

    public Location getClosestBurrow(ArrayList<Location> allBurrows) {
        Location closestBurrow = null;

        //Kig igennem et ArrayList med locations of find ud af, hvilken location der er tættest på, og så returner den.


        return closestBurrow;
    }

}


