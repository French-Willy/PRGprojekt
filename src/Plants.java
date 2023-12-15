import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Plants implements Actor, DynamicDisplayInformationProvider {
    int age;
    World world;
    Location location;

    public Plants(int age, World world) {
        this.age = age;
        this.world = world;
    }

    @Override
    public DisplayInformation getInformation() {
        return null;
    }


    @Override
    public void act(World world) {
        age++;
    }


    public void die(Object object) {

        world.delete(object);
    }
}