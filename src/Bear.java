import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Animals {

    public Bear(int age, int hunger, int hp, World world) {
        super(age, hunger, hp, world);
        this.atk = 10;
        this.hp = hp;
    }


    @Override
    public void act(World world) {
        super.act(world);
        if(hp < 50){
            regenerate();
        }

        if (world.getEntities().get(this) != null && world.isNight()) {


        } else if (world.isDay() && world.getEntities().get(this) == null) {

            System.out.println("Bear has awoken at this location: " + this.location);
            wakeUp();

        } else if (world.isDay()) {
            move();
        }
    }

    private void move() {
        this.location = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
        List<Location> list = new ArrayList<>(neighbours);
        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
        Location l = list.get(randomNum);
        world.move(this, l);
        this.location = world.getLocation(this);
        if (hunger < 6)
            findFood();

    }

    public void seekFood(HashSet<Class> surroundingAnimals) {
        if (hunger < 2) {
            if (surroundingAnimals.contains(Wolf.class)) {
                for (Location tile : world.getSurroundingTiles()) {
                    if (world.getTile(tile) != null && world.getTile(tile).getClass() == Wolf.class) {
                        attack(tile);
                        break;
                    }
                }

            } else if (surroundingAnimals.contains(Rabbit.class)) {
                for (Location tile : world.getSurroundingTiles()) {
                    if(world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class){
                        attack(tile);
                        break;
                    }
                }

            }

        } else if(hunger < 5){
            for (Location tile : world.getSurroundingTiles()){
                if(world.getTile(tile) != null && world.getTile(tile).getClass() == Rabbit.class){
                    attack(tile);
                    break;
                }
            }
        }
    }


            private void findFood () {
                HashSet<Class> surroundingAnimals = new HashSet<>();

                for (Object x : world.getSurroundingTiles(this.location)) {
                    surroundingAnimals.add(x.getClass());
                }
                seekFood(surroundingAnimals);
            }

            public void attack (Location tile){
                try {
                    if(world.getTile(tile).getClass() == Rabbit.class) {
                        Rabbit rabbit = (Rabbit) world.getTile(tile);
                        rabbit.takeDamage(this.atk);
                        if(rabbit.hp <= 0){
                            eat(3);
                            world.delete(rabbit);
                        }


                    }else if (world.getTile(tile).getClass() == Wolf.class){
                        Wolf wolf = (Wolf) world.getTile(tile);
                        wolf.takeDamage(this.atk);
                       if(wolf.hp <= 0){
                           eat(5);
                           world.delete(wolf);
                       }
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }


            public void eat(int foodSize){
                this.hunger = hunger + foodSize;
            }

            private void wakeUp () {

            }

        }
