import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Crocodile extends Animals {
    boolean underwater;
    Set<Location> homeSwamp;
    Set<Location> territory;

    /**
     * Her bliver en Krokodille instantieret med følgende parameter. Når der er styr på både alder, sult og liv, bliver det muligt at
     * forholde os til forskellige måder hvorpå krokodillen kan dø. Og inde i konstruktøren bliver der initialiseret et Set af lokationer
     * kaldet "territory" - der svarer til dets surroundingTiles når den bliver sat ind i verden, hvor krokodillen vil bo samt beskytte.
     * @param age
     * @param hunger
     * @param hp
     * @param animalMeatAmount
     * @param world
     * @param location
     * @param homeSwamp
     */
    public Crocodile(int age, int hunger, int hp, int animalMeatAmount, World world, Location location, Set<Location> homeSwamp) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.atk = 100;
        this.territory = world.getSurroundingTiles(location, 3);

        if (homeSwamp == null) {
            this.homeSwamp = world.getSurroundingTiles(location, 2);
            world.setTile(location, new Swamp(location));
            for (Location tile : this.homeSwamp) {
                world.setTile(tile, new Swamp(tile));
            }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if (underwater) {
            if (sleeping) {
                return new DisplayInformation(Color.GRAY, "Crocodile-underwatersleeping", true);
            } else {
                return new DisplayInformation(Color.GRAY, "Crocodile-underwater", true);
            }
        } else {
            return new DisplayInformation(Color.GRAY, "Crocodile", false);
        }
    }

    /**
     * Det vigtigste i denne her act-metode er, at krokodillens "underwater" boolean skiftes mellem false og true.
     * Ligesom bjørnen (Se bjørn act-metode), så beskytter krokodillen under alle omstændigheder - medmindre den sover - sit territorie.
     * Dog i modsætning til bjørnen, angriber den alt og alle - ligemeget om den er sulten eller ej.
     * Og først efter den har fjernet alle dyr fra området, vil den søge efter mad.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        super.act(world);
        if (world.isNight()) {
            goToHomeSwamp();
            if (homeSwamp.contains(getLocation(this))) {
                goUnderwater();
                sleeping = true;
            }
        }
        if (world.isDay()) {
            sleeping = false;
            if (containsAnimal()) {
                protectTerritory();
            } else if (hunger < 5) {
                seekFood();
            }
            if(getLocation(this) != null) {
                crocodileMove();
            }
            if (homeSwamp.contains(getLocation(this))) {
                goUnderwater();
            } else {
                emergeFromWater();
            }
        }
    }

    /**
     * seekFood vil tjekke om der er ådsel i krokodillens territorie. Ligesom bjørnen (Se seekFood-metoden i bjørn*).
     * Den kigger igennem territoriet og finder det tættestse ådsel, og spiser det, hvis den er tæt nok, ellers så laver den en
     * vej derhen (Se makePath-metoden)
     */
    private void seekFood(){
    double distanceToClosestCarcass = 100;
    Carcass closestCarcass = null;
    Location CarcassLocation = null;
        
        for(Location tile : territory){
            if(world.getTile(tile) != null){
                if(world.getTile(tile).getClass() == Carcass.class && getLocation(this) != null){
                    if(calculateDistance(getLocation(this), tile) < distanceToClosestCarcass && tile != null){
                        distanceToClosestCarcass = calculateDistance(getLocation(this), tile);
                        closestCarcass = (Carcass) world.getTile(tile);
                        CarcassLocation = tile;
                    }
                }
            }
        }
        if(distanceToClosestCarcass <= 1.5){
            eat(closestCarcass, 30);
        } else if (CarcassLocation != null){
            makePath(this, getLocation(this), CarcassLocation);
        }
    }





    private void crocodileMove() {
        boolean mayIMove = false;
        Location territoryLocation = null;
        Set<Location> neighbours = world.getEmptySurroundingTiles(getLocation(this));
        List<Location> list = new ArrayList<>(neighbours);
        while (mayIMove == false) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
            Location l = list.get(randomNum);
            if (this.territory.contains(l)) {
                mayIMove = true;
                territoryLocation = l;
            } else {
                mayIMove = false;
            }
        }
        world.move(this, territoryLocation);
        this.location = world.getLocation(this);
    }

    public void goUnderwater() {
        underwater = true;

    }

    public void goToHomeSwamp() {
        double tileDistance = 10;
        Location closestTile = null;
        if (!homeSwamp.contains(world.getEntities().get(this))) {
            for (Location tile : homeSwamp) {
                if (calculateDistance(getLocation(this), tile) < tileDistance && world.getTile(tile) != null) {
                    tileDistance = calculateDistance(getLocation(this), tile);
                    closestTile = tile;
                }
            }
            makePath(this, getLocation(this), closestTile);
        }
    }

    public void emergeFromWater() {
        underwater = false;
    }
    
    private boolean containsAnimal() {
        for (Location tile : territory) {
            if (tile != getLocation(this) && world.getTile(tile) != null) {
                if (world.getTile(tile).getClass() == Rabbit.class || world.getTile(tile).getClass() == Wolf.class || world.getTile(tile).getClass() == Bear.class) {
                    return true;
                }
            }
        }
        return false;
    }


    private void protectTerritory() {
        double closestAnimalDistance = 100;
        Animals closestAnimal = null;
        Location closestAnimalLocation = null;
        for (Location tile : territory) {
            if (tile != getLocation(this) && world.getTile(tile) != null) {
                if (world.getTile(tile).getClass() == Rabbit.class || world.getTile(tile).getClass() == Wolf.class || world.getTile(tile).getClass() == Bear.class) {
                    if (calculateDistance(getLocation(this), tile) < closestAnimalDistance) {
                        closestAnimalDistance = calculateDistance(getLocation(this), tile);
                        closestAnimal = (Animals) world.getTile(tile);
                        closestAnimalLocation = tile;
                    }
                }
            }
        }
        if (calculateDistance(getLocation(this), closestAnimalLocation) <= 1.5) {
            attack(this, closestAnimal);
        } else {
            makePath(this, getLocation(this), closestAnimalLocation);
        }
    }
}