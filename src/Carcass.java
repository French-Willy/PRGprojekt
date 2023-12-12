import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Carcass extends Inanimate {
    int meatAmount;
    int shroomSize;
    public Carcass(int meatAmount, World world){
        super(world);
        this.meatAmount = meatAmount;
        shroomSize = 0;
    }

    @Override
    public void act(World world){
        rotting();
        isRotten();
    }

    private void rotting(){
        meatAmount--;
        shroomSize++;
    }
    private void isRotten(){
        if (meatAmount <= 0){
            createFungi();
        }
    }

    public void meatEaten(int meatChunk){meatAmount -= meatChunk;}
    public void createFungi(){
        this.location = world.getLocation(this);
        System.out.println(this.location);
        if (world.containsNonBlocking(getLocation(this))){
            world.delete(world.getNonBlocking(getLocation(this)));
        }
        world.setTile(this.location, new Fungi(0,shroomSize,world));
        world.delete(this);
    }

}
