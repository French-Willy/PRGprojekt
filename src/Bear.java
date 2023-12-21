import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Animals {
    Set<Location> territory;
    /**
     * Her bliver en bjørn instantieret med følgende parameter. Når der er styr på både alder, sult og liv, bliver det muligt
     * forholde os til forskellige måder hvorpå bjørnen kan dø. Og inde i konstruktøren bliver der initialiseret et Set af lokationer
     * kaldet "territory" - der svarer til dets surroundingTiles når den bliver sat ind i verden, hvor bjørnen vil bo samt beskytte.*
     * @param age giver indblik i dyrets alder, som bruges til at opdatere både udseende og levetid
     * @param hunger giver et overblik over dyrets sult, som indikerer, hvornår og hvilke dyr bjørnen prioriterer at spise.
     * @param hp skal indikere bjørnens "liv", således at når "liv" bliver nul blandt andet pga. angreb eller sult, så dør bjørnen.
     * @param animalMeatAmount denne parameter fortæller, hvor meget "kød", der er på bjørnen, og dermed, hvor meget af den kan spises af andre dyr
     * @param world - "providing details of the position on which the actor is currently located and much more."
     * @param location - "providing details of the position on which the actor is currently located"
     */
    public Bear(int age, int hunger, int hp, int animalMeatAmount, World world, Location location) {
        super(age, hunger, hp, animalMeatAmount, world);
        this.atk = 10;
        this.hp = hp;
        this.territory = world.getSurroundingTiles(location, 2);
    }

    /**
     * DisplayInformationen bliver opdateret under run-time således, at bjørnen ser anderledes ud i forskellige milljøer/situationer.
     * @return
     */

    @Override
    public DisplayInformation getInformation() {
        if (this.age > 4) {
            if (sleeping) {
                return new DisplayInformation(Color.GRAY, "bear-sleeping", false);
            } else {
                return new DisplayInformation(Color.GRAY, "bear", false);
            }
        } else {
            if (sleeping) {
                return new DisplayInformation(Color.GRAY, "bear-small-sleeping", false);
            } else {
                return new DisplayInformation(Color.GRAY, "bear-small", false);
            }
        }
    }
/**
 * Act kalder først superklassen "Animals" act-metode (Se Animals), hvorefter den kigger i et "if-statement" om den er såret og eventuelt
 * mæt nok til at kroppen kan prioritere regenerering (Se regenerate-metode).**Det næste "if-statement" tjekker om hvorledes, det er nat, hvorefter den sætter boolean-feltet til "true".*
 * Hvis det tilgengæld er dag, skal programmet først se, om bjørnen er gammel nok til at blive fuldvoksen (Se ofAge-metode)
 * sleeping bliver sat til false, og hvis der er fare i området skal den beskytte sit territorie (Se Danger-metode og protectTerritory-metode)
 * *
 * Hvis der er ingen fare, skal bjørnen finde mad, og hvis den ikke er sulten skal den bare gå rundt normalt.
 * *
 * @param world "providing details of the position on which the actor is currently located and much more."
 */
    @Override
    public void act(World world) {
        super.act(world);
        if (hp < 50 && hunger > 6) {
            regenerate();
        }

        if (world.getEntities().get(this) != null && world.isNight()) {
            sleeping = true;

        } else if (world.isDay() && world.getEntities().get(this) == null) {

        } else if (world.isDay()) {
            if (!ofAge) {
                ofAge();
            }
            sleeping = false;
            if (Danger()) {
                protectTerritory();
            } else if (hunger <= 10) {
                seekFood();
            } else {
                bearMove();
            }
        }
    }

    /**
     * Denne metode tjekker om alderen er høj nok til at bjørnen kan blive fuldvoksen - angreb øges og meatamount øges.
     */
    private void ofAge() {
        if (this.age > 4) {
            this.atk = atk + 5;
            this.animalMeatAmount = animalMeatAmount * 2;
            ofAge = true;
        }
    }

    /**
     * bearMove metoden tjekker alle felter omkring den, og sætter dem i en arrayliste.
     * Whileløkken vil tjekke om de emptysurroundingTiles er inden for territoriet - så længe det ikke er inden for territoriet
     * så vil bjørnen ikke gå derhen.
     */
    private void bearMove() {
        boolean mayIMove = false;
        Location territoryLocation = null;
        this.location = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.location);
        List<Location> list = new ArrayList<>(neighbours);

        while (mayIMove == false) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
            Location l = list.get(randomNum);
            if (this.territory.contains(l)) {
                mayIMove = true;
                territoryLocation = l;
            } else {
                mayIMove = false;
            }
        }
        world.move(this, territoryLocation);
        this.location = world.getLocation(this);
    }

    /**
     * Denen metode returnerer en boolean alt efter, om der findes ulve inde i territoriet (Se findFood-metode)
     * @return
     */
    private boolean Danger() {
        HashSet<Class> surroundingAnimals = findFood();
        if (surroundingAnimals.contains(Wolf.class)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Denne metode vil kigge på alle tiles i territories, og se om "closestWolf" fundet fra findClosestObject-metoden (Se findClosestObject-metoden)
     * er den samme.
     * Hvis det er den samme skal den se om ulven er tæt nok på at angribe, eller i stedet gå hen til den.
     */
    private void protectTerritory() {
        double DistanceToClosestWolf = findClosestObjectDistance(this, Wolf.class);
        Object closestWolf = findClosestObject(this, Wolf.class);
        for (Location tile : territory) {
            if (world.getTile(tile) == closestWolf) {
                Wolf wolf = (Wolf) closestWolf;
                if (DistanceToClosestWolf <= 1.5) {
                    attack(this, (Wolf) closestWolf);
                } else {
                    makePath(this, world.getEntities().get(this), world.getLocation(closestWolf));
                    if (DistanceToClosestWolf <= 1.5) {
                        attack(this, (Wolf) closestWolf);
                    }
                }
            }
        }
    }

    /**
     * seekFood metoden kigger på ting i territoriet, og ud fra hvor sulten den er, så prioriterer den forskellige former for mad.
     *
     * Først vil den se om den er meget sulten. I det første "if-statement", hvis den er sulten nok, så vil den
     * prioritere først at finde ådsler, ved at bruge metoderne: findFood(), findClosestObjectDistance() og findClosestObject(). (Se metoderne skrevet).*
     * Ved et for-loop som kører til alle former for mad. Hvis tilen er det samme som tætteste ådsel, vil bjørnen tjekke om ådslet
     * er tæt nok på til at kunne spises. "if(DistnaceToCLoseestCarcass <= 1.5).
     * Den vil kalde eat()-metoden hvis den er tæt nok. (Se eat-metoden).
     * *
     * Hvis ikke vil den finde vej vha. makePath metoden (Se makePath-metoden).
     * **
     * Det samme vil ske for alle andre former for mad.
     * Med berries bruger den dog ikke eat-metoden fra Animals men i stedet eatBerries.
     * (*Se eatBerries-metoden)
     */
    public void seekFood() {
        HashSet<Class> surroundingFood = findFood();
        if (hunger < 4) {
            if (surroundingFood.contains(Carcass.class)) {
                double DistanceToClosestCarcass = findClosestObjectDistance(this, Carcass.class);
                Object closestCarcass = findClosestObject(this, Carcass.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestCarcass) {
                        if (DistanceToClosestCarcass <= 1.5) {
                            eat((Carcass) closestCarcass, 20);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestCarcass));
                            if (DistanceToClosestCarcass <= 1.5) {
                                eat((Carcass) closestCarcass, 20);
                            }
                        }
                    }
                }
            }
            if (surroundingFood.contains(Rabbit.class)) {
                double DistanceToClosestRabbit = findClosestObjectDistance(this, Rabbit.class);
                Object closestRabbit = findClosestObject(this, Rabbit.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestRabbit) {
                        if (DistanceToClosestRabbit <= 1.5) {
                            attack(this, (Animals) closestRabbit);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestRabbit));
                            if (DistanceToClosestRabbit <= 1.5) {
                                attack(this, (Animals) closestRabbit);
                                break;
                            }
                        }
                    }
                }
            } else if (surroundingFood.contains(Bush.class)) {
                double DistanceToClosestBush = findClosestObjectDistance(this, Bush.class);
                Object closestBush = findClosestObject(this, Bush.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestBush) {
                        if (DistanceToClosestBush <= 1.5) {
                            eatBerries((Bush) closestBush);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestBush));
                            if (DistanceToClosestBush <= 1.5) {
                                eatBerries((Bush) closestBush);
                                break;
                            }
                        }

                    }
                }
            } else {
                bearMove();
            }

        } else if (hunger <= 6) {
            if (surroundingFood.contains(Bush.class)) {
                double DistanceToClosestBush = findClosestObjectDistance(this, Bush.class);
                Object closestBush = findClosestObject(this, Bush.class);
                for (Location tile : territory) {
                    if (world.getTile(tile) == closestBush) {
                        if (DistanceToClosestBush <= 1.5) {
                            eatBerries((Bush) closestBush);
                            break;
                        } else {
                            makePath(this, world.getEntities().get(this), world.getLocation(closestBush));
                            if (DistanceToClosestBush <= 1.5) {
                                eatBerries((Bush) closestBush);
                                break;
                            }
                        }

                    }
                }
            } else {
                bearMove();
            }
        }
    }

    private HashSet<Class> findFood() {
        HashSet<Class> surroundingFood = new HashSet<>();

        for (Location tile : territory) {
            if (world.getTile(tile) != null) {
                surroundingFood.add(world.getTile(tile).getClass());
            }
        }
        return surroundingFood;
    }

    /**
     * eatBerries-metoden vil tage imod en bush, hvorefter den vil kalde på metoden inde i Bush (Se eatBerries-metode).
     * @param bush
     */
    public void eatBerries(Bush bush) {
        bush.eatBerries(this);
    }

}
