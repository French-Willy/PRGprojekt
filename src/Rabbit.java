import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
public class Rabbit extends Animals {
    //Location home;
    Location favoriteBurrow = null;
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
                System.out.println("0");
                double distacetoClosestBurrow = 100.0;
                Location closestBurrow = null;
                Object burrow = null;
                for (Object object : world.getEntities().keySet()) {
                    if (object.getClass() == Burrow.class) {
                        System.out.println("1");
                        if (Burrow.getCheckFullBurrow( object) == true) {
                            System.out.println("2");
                            while (calculateDistance(world.getLocation(this), world.getLocation(object)) < distacetoClosestBurrow) {
                                distacetoClosestBurrow = calculateDistance(world.getLocation(this), world.getLocation(object));
                                closestBurrow = world.getLocation(object);
                                burrow = object;
                                System.out.println("3");
                            }
                        }
                    }
                }
                if (burrow == null) {
                    System.out.println("4");
                    Dig();
                    Burrow.enterBurrow(this,favoriteBurrow);
                }
                //Når kaninen er på hullet, skal den bare removes og det kalder en metode i Burrow:
                //enterBurrow skal sige, at Burrow nu indeholder
                try {
                    if (calculateDistance(world.getLocation(this),world.getLocation(burrow)) == 0){
                        System.out.println("5");
                        Burrow.enterBurrow(this, world.getTile(closestBurrow));
                        world.remove(this);
                        favoriteBurrow = closestBurrow;
                    }
                }
                catch (IllegalArgumentException e){
                }
                if (closestBurrow != null && world.getEntities().get(this) != null) {
                    System.out.println("9");
                    makePath(this, world.getEntities().get(this), closestBurrow);
                }
            }
            else if  (favoriteBurrow != null){
                System.out.println(Burrow.getCheckFullBurrow(favoriteBurrow));
                if (calculateDistance(world.getLocation(this),favoriteBurrow) == 0 && (Burrow.getCheckFullBurrow( favoriteBurrow) == true)) {
                        Burrow.enterBurrow(this, favoriteBurrow);
                        world.remove(this);
                }
                else {
                    System.out.println("8");
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



        public void move () {
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

                hunger = hunger + 4;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        public void seekFood (Class type){
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

        public void Dig () {
        if (world.getTile(world.getCurrentLocation()) != null) {
            try {
                // if (world.getNonBlocking(this.location).getClass() == Grass.class) {
                world.delete((world.getNonBlocking(world.getCurrentLocation())));
            }
            catch (IllegalArgumentException e) {
            }
            Burrow burrow = new Burrow(world);
            world.setTile(world.getCurrentLocation(), burrow);
            favoriteBurrow = world.getCurrentLocation();
            System.out.println("burrow lavet.... pog " + favoriteBurrow);


        } //else if (favoriteBurrow != null) {
                //findFavoriteHole();
                // }
    }


        public void wakeUp (Location location) {
                if (world.isTileEmpty(location)) {
                    world.setTile(location, this);
                }
        }

        private void reproduction (World world){
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



