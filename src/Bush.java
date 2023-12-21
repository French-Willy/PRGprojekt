import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;

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

    protected void eatBerries(Bear bear) {
        if(hasBerries){
            hasBerries = false;
            bear.hunger = bear.hunger + 2;
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if(hasBerries){
            return new DisplayInformation(Color.GREEN, "bush-berries", false );
        }else{
            return new DisplayInformation(Color.GREEN, "bush", false );
        }
    }
}
