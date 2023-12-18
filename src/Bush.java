import itumulator.world.World;

public class Bush extends Plants {
    public boolean hasBerries;

    public Bush(int age, World world) {
        super(age, world);
        this.hasBerries = false;
    }

    @Override
    public void act(World world) {
        super.act(world);
        growBerries();
    }

    public void growBerries() {
        if (age % 5 == 0) {
            hasBerries = true;
        }
    }

    public boolean getHasBerries() {
        return hasBerries;
    }
}