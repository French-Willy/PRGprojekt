import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

public class Carcass extends Inanimate {
    int meatAmount;
    int shroomSize;

    public Carcass(int meatAmount, World world) {
        super(world);
        this.meatAmount = meatAmount;
        shroomSize = 0;
    }

    @Override
    public void act(World world) {
        rotting();
    }

    private void rotting() {
        meatAmount--;
        shroomSize++;
    }

    private void isRotten() {
        if (meatAmount <= 0) {
            createFungi();
        }
    }

    public void meatEaten(int meatChunk) {
        meatAmount -= meatChunk;
        isRotten();
    }

    public void createFungi() {
        this.location = world.getLocation(this);
        for (Location tile : world.getEmptySurroundingTiles(location)) {
            if (world.containsNonBlocking(getLocation(this)) && world.getTile(tile) != null) {
                if (world.getTile(tile).getClass() != Swamp.class) {
                    world.delete(world.getNonBlocking(getLocation(this)));
                    world.delete(this);
                    world.setTile(this.location, new Fungi(0, shroomSize, world));
                }else{
                    world.delete(this);
                }
            } else {
                world.setTile(this.location, new Fungi(0, shroomSize, world));
                world.delete(this);
            }
            break;
        }
    }

}
