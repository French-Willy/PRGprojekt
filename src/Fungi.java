import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Fungi extends Plants implements NonBlocking{

    int fungiSize;
    int fRadius;
    public Fungi(int age, int fungiSize, World world){
        super(age, world);
        this.fungiSize = fungiSize;
        fRadius = 0;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.RED, "fungi2", false);
    }

    @Override
    public void act(World world){
        isFungiDead();
    }

    private void isFungiDead(){
        if (!findCarcass()){
            fungiSize--;
        }else {
            fungiSize++;
        }
        if (fungiSize <= 0) {
            killFungi();
        }
    }

    public void killFungi(){
        world.delete(this);
    }

    private int FungiRadius(){
        if (fungiSize <= 30 && fungiSize > 10){
            fRadius = fungiSize / 10;
        }else if (fungiSize <= 10){
            fRadius = 1;
        }
        return fRadius;
    }

    public boolean findCarcass(){
        this.location = world.getLocation(this);
        List<Location> SurroundingCarcass = new ArrayList<>();
        for (Location tile : world.getSurroundingTiles(FungiRadius())) {
            if (world.getTile(tile) != null && world.getTile(tile).getClass() == Carcass.class){
                SurroundingCarcass.add(tile);
            }
        }
        return !SurroundingCarcass.isEmpty();
    }
}
