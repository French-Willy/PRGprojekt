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

    public Crocodile(int age, int hunger, int hp, int animalMeatAmount, World world, Location location, Set<Location> homeSwamp) {
        super(age, hunger, hp, animalMeatAmount, world);
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
                return new DisplayInformation(Color.GRAY, "Crocodile-underwatersleeping", false);
            } else {
                return new DisplayInformation(Color.GRAY, "Crocodile-underwater", false);
            }
        } else {
            return new DisplayInformation(Color.GRAY, "Crocodile", false);
        }
    }


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
            move();
            if (homeSwamp.contains(getLocation(this))) {
                goUnderwater();
            } else {
                emergeFromWater();
            }
        }
    }

    public void test(){
        
    }

    private void move() {
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
                if (calculateDistance(getLocation(this), tile) < tileDistance) {
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


}
