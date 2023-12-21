import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;

public class Bush extends Plants {
    public boolean hasBerries;

    /**
     * Her bliver en Bush instantieret med følgende parameter.
     * param age: denne parameter vil tjekke om der er gået langt nok tid til at gro bær.
     * param world: "providing details of the position on which the actor is currently located and much more."
     * Konstruktøren kalder superklassen med age og world, og sætter hasBerries til at være false, da busken ikke skal spawne
     * med bær på.
     * @param age
     * @param world
     */
    public Bush(int age, World world) {
        super(age, world);
        this.hasBerries = false;
    }

    /**
     * Act kalder sin superklasse med world, og tjekker hvert tick om der er gået lang nok tid til at busken kan gro bær. (Se growBerries-metoden).
     * param world "providing details of the position on which the actor is currently located and much more."
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        super.act(world);
        growBerries();
    }

    /**
     * Her vil metoden tjekke om busken er gammel nok til at gro bær - ved at den sætter "hasBerries" til true
     */
    public void growBerries() {
        if (age % 5 == 0) {
            hasBerries = true;
        }
    }

    /**
     * Metoden her vil tjekke om der først er bær på busken, og derefter vil den sætte hasBerries til false - fordi bjørnen spiser bærene
     * og derefter øger den bjørnens mæthed.
     * param bear: instans a bjørn
     * @param bear
     */
    protected void eatBerries(Bear bear) {
        if(hasBerries){
            hasBerries = false;
            bear.hunger = bear.hunger + 2;
        }
    }

    /**
     * opdaterer buskens udseende alt efter om der er bær på eller ej
     * @return
     */
    @Override
    public DisplayInformation getInformation() {
        if(hasBerries){
            return new DisplayInformation(Color.GREEN, "bush-berries", false );
        }else{
            return new DisplayInformation(Color.GREEN, "bush", false );
        }
    }
}
