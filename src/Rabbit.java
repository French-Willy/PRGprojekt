import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
public class Rabbit extends Animals {
    //Location home;
    Location favoriteBurrow;
    boolean oneChildOnly;
    Location lastPosition;

    public Rabbit(int age, int hunger, int hp, World world) {
        super(age, hunger, hp, world);
        this.oneChildOnly = true;
        this.favoriteBurrow = null;

    }

    @Override
    public void act(World world) {
        if (this.getHealth() < 0) {
            die();
        }
        int counter = 0;
        super.act(world);
       // System.out.println(favoriteBurrow + " favorit hul");
        double distancetoClosestBurrow = 100.0;
        Location closestBurrow = null;
        if (world.getEntities().get(this) != null && world.isNight()) {
            /*world.remove(this);
            System.out.println("Rabbit has fallen asleep at this location: " + this.location);*/

                for (Object object : world.getEntities().keySet()) {
                    if (object.getClass() == Burrow.class) {
                       System.out.println(((Burrow) object).getBurrowSpace(object) + "  hvad er der i hullet    " + object);
                        //System.out.println(object);
                        if (((Burrow) object).getBurrowSpace(object).contains(this)) {
                            counter++;
                            System.out.println("9 " + this);
                            if (world.getEntities().get(this) != null) {
                                if (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) == 0) {
                                    //Burrow.enterBurrow(this, world.getEntities().get(object));
                                    world.remove(this);
                                    lastPosition = world.getEntities().get(this);
                                    System.out.println("10 " + this);
                                    break;
                                } else {
                                    makePath(this, world.getEntities().get(this), world.getEntities().get(object));
                                    System.out.println("11 " + this);
                                }
                            }
                        }
                    }
                }
                if (world.getEntities().get(this) !=null){
                for (Object object : world.getEntities().keySet()) {
                    if (object.getClass() == Burrow.class) {
                        if (((Burrow) object).getBurrowSpace(object).contains(this) == false) {
                            System.out.println("6 " + this);
                            counter++;
                            if (((Burrow) object).getBurrowSpace(object).size() < 3) {

                                System.out.println("12 " + this + world.getEntities().get(this));
                                //if (world.getEntities().get(this) != null){
                                while (calculateDistance(world.getEntities().get(this), world.getEntities().get(object)) < distancetoClosestBurrow) {
                                    distancetoClosestBurrow = calculateDistance(world.getLocation(this), world.getLocation(object));
                                    closestBurrow = world.getEntities().get(object);
                                }
                                if (calculateDistance(world.getEntities().get(this), closestBurrow) == 0) {
                                    //Burrow.enterBurrow(this, object);
                                    world.remove(this);
                                    lastPosition = world.getEntities().get(this);
                                    System.out.println("13 " + this);
                                    ((Burrow) object).enterBurrow(this, object);
                                    break;
                                } else {
                                    makePath(this, world.getEntities().get(this), closestBurrow);
                                    System.out.println("14 " + this);
                                    break;
                                }
                            } else {
                                try {
                                    System.out.println("17 " + this);
                                    Dig(world.getEntities().get(this));
                                    for (Object burrow : world.getEntities().keySet()) {
                                        if (burrow.getClass() == Burrow.class) {
                                            if (world.getLocation(burrow) == this.location) {
                                                ((Burrow) burrow).enterBurrow(this, burrow);
                                            }
                                        }
                                    }
                                    //((Burrow) object).enterBurrow(this, object);
                                } catch (IllegalArgumentException i) {
                                }
                            }
                        }
                    }
                }
            }
        if (counter ==0) {
                try {
                    System.out.println("1 " + this);
                    Dig(world.getEntities().get(this));
                    for (Object object : world.getEntities().keySet()) {
                        if (object.getClass() == Burrow.class) {
                            if (world.getLocation(object) == this.location) {
                                ((Burrow) object).enterBurrow(this,object);
                            }
                        }
                    }

                } catch (IllegalArgumentException i) {
                }
            }
        }
        if (world.getEntities().get(this) == null && world.isDay()) {
           System.out.println("vågn op " + lastPosition);
            wakeUp(this.location);
        }
        else if (world.getEntities().get(this) != null && world.isDay()) {
                //System.out.println(world.getEntities().get(this));
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


        public void eat (Location tile){
            try {
                // if (world.getNonBlocking(this.location).getClass() == Grass.class) {
                world.delete((world.getNonBlocking(tile)));

                hunger = hunger + 3;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        public void seekFood (Class type){
            if (hunger < 5) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == type) {
                        eat(tile);
                            world.move(this, tile);
                            break;
                        }
                    }
                }
            }


        public void Dig (Location location) {
        if (world.getTile(location) != null) {
            try {
                if (world.getNonBlocking(location).getClass() == Grass.class) {
                 //   world.delete((world.getNonBlocking(world.getCurrentLocation())));
                }
            }
            catch (IllegalArgumentException e) {
            }
            if (location != null) {
                Burrow.createNewBurrow(world, world.getEntities().get(this));
            }


        }
    }


        public void wakeUp (Location location) {
                if (world.isTileEmpty(location)) {
                    System.out.println();
                    world.setTile(location, this);
                }
        }

        private void reproduction (World world){
            if (age == 1 && this.oneChildOnly)
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null) {
                        System.out.println("hjælp");
                        if (world.getTile(tile).getClass() == Rabbit.class && world.getTile(tile) != this) {
                            System.out.println("jeg har født");
                            Rabbit rabbitChild = new Rabbit(0, 5, 5,world);
                            Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
                            List<Location> list = new ArrayList<>(neighbours);
                            if (list.size() <= 0) {
                                //deleteGrass(this,world);
                            } else {
                                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                                Location l = list.get(randomNum);
                                world.setTile(l, rabbitChild);
                                this.oneChildOnly = false;
                                break;

                            }
                        }
                    }
                }
        }
        public void pathing () {
            for (Object object : world.getEntities().keySet()) {
                if (object.getClass() == Burrow.class) {
                    makePath(this, world.getCurrentLocation(), world.getLocation(object));
                    //System.out.println(new Location(0,0)+"target");
                    // world.getEntities().get(Burrow.class)
                    //world.getLocation(object)
                }
            }
        }

        public double calculateDistance (Location initial, Location target){
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

        public void makePath (Object object, Location initial, Location target){
            double shortestDistance = calculateDistance(world.getCurrentLocation(), target);
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
                }
            }
        }
    }


/*

        if (favoriteBurrow == null) {
        System.out.println("intet favorit hul");

        // Location closestBurrow = null;
        Object burrow = null;
        for (Object object : world.getEntities().keySet()) {
        if (object.getClass() == Burrow.class) {
        if (Burrow.getCheckFullBurrow( object) == true) {
        while (calculateDistance(world.getLocation(this), world.getLocation(object)) < distancetoClosestBurrow) {
        distancetoClosestBurrow = calculateDistance(world.getLocation(this), world.getLocation(object));
        closestBurrow = world.getLocation(object);
        burrow = object;
        }
        }
        }
        }
        if (burrow == null) {
        Dig();
        favoriteBurrow = closestBurrow;
        Burrow.enterBurrow(this,favoriteBurrow);

         {
            {

                //Når kaninen er på hullet, skal den bare removes og det kalder en metode i Burrow:
                //enterBurrow skal sige, at Burrow nu indeholder
                try {
                    if (calculateDistance(world.getLocation(this),world.getLocation(burrow)) == 0 &&  Burrow.getCheckFullBurrow( favoriteBurrow) == true){
                        Burrow.enterBurrow(this, world.getTile(closestBurrow));
                        world.remove(this);
                        favoriteBurrow = closestBurrow;
                    }
                }
                catch (IllegalArgumentException e){
                }
                 if (closestBurrow != null && world.getEntities().get(this) != null) {

                    makePath(this, world.getEntities().get(this), closestBurrow);
                }
            }
            else if  (favoriteBurrow != null){
                System.out.println(Burrow.getCheckFullBurrow(favoriteBurrow)+" finder favorit hul");
                if (Burrow.getCheckFullBurrow( favoriteBurrow) == false){
                    favoriteBurrow = null;
                }
                else if(calculateDistance(world.getLocation(this),favoriteBurrow) == 0 && (Burrow.getCheckFullBurrow( favoriteBurrow) == true)) {
                        Burrow.enterBurrow(this, favoriteBurrow);
                        world.remove(this);
                }
                else {
                    System.out.println("lav nyt favorit hul");
                    makePath(this, world.getCurrentLocation(), favoriteBurrow);
                    favoriteBurrow = null;
                }
            }

            } else if (world.isDay() && world.getEntities().get(this) == null) {
                //DENNE METODE OPVÆKKER KANIN FRA DE DØDE EFTER DEN ER DØD AF SULT...
                //world.setTile(this.location, this);
                System.out.println("Rabbit has awoken at this location: " + this.location);
                wakeUp(favoriteBurrow);
                Burrow.leaveBurrow(this);

                //VIL GERNE GØRE SÅLEDES, AT VI IKKE BEHØVER AT SKRIVE "ELSE" MEN, HVIS JEG IKKE GØR DET HER, SÅ LAVER DEN ET ELLER ANDET
                //MÆRKELIGT SORT MAGI, HVOR DEN BEVÆGER SIG, SELVOM DEN IKKE ER SAT IND I VERDEN - "SPØGELSE"
                //OG DET DER KAN SKE ER, AT 2 FORSKELLIGE "SPØGELSER" KAN SÆTTE SIG PÅ SAMME TILE, OG NÅR DE SÅ ET TICK SENERE SPAWNER IND
                // SÅ KOMMER DER SELVFØLGELIG EN FEJL FORDI 2 BLOCKING ELEMENTS SPAWNER PÅ HINANDEN
            } else if (world.isDay()) {
                move();
                reproduction(world);
            }
        }
 */