import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.*;
import java.util.Set;

public class Swamp implements DynamicDisplayInformationProvider, NonBlocking {
Location area;
    public Swamp(Location area){
        this.area = area;
    }

    public void test(){

    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "Water", false);
    }
}
