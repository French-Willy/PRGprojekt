import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Plants implements Actor {
int age;
World world;
Location location;

    public Plants(int age, World world) {
        this.age = age;
        this.world = world;
    }

@Override
    public void act(World world){
        age++;
    }


    public void die (Object object){

        world.delete(object);
    }
}
