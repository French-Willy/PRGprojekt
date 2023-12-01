import itumulator.world.Location;
import itumulator.world.World;

public class Bear extends Animals {

    public Bear(int age, int hunger, World world) {
        super(age, hunger, world);
    }


    @Override
    public void act(World world) {
        super.act(world);
        if (world.getEntities().get(this) != null && world.isNight()) {

        } else if (world.isDay() && world.getEntities().get(this) == null) {

            System.out.println("Rabbit has awoken at this location: " + this.location);
            wakeUp();

        } else if (world.isDay()) {
            // move();
        }
    }
    private void move(){

    }

}
