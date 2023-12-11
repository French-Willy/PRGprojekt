import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
public class Fungi extends Plants implements NonBlocking{

    int fungiSize;
    public Fungi(int age, int fungiSize, World world){
        super(age, world);
        this.fungiSize = fungiSize;
    }


}
