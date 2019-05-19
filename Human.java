import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Human extends Creature implements Serializable {
    {
        this.setAlive(true);
        this.setTerrain(Terrain.KITCHEN);
        this.setAge(18);
    }

    public Human(){

    }

    public Human(int age) {
        this.setAge(age);
    }

    public Human(String name) {
        this.setName(name);
    }

    public Human(String name, int age) {
        this.setAge(age);
        this.setName(name);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Human.class && obj.hashCode() == this.hashCode()) {
            return true;
        }
        return false;
    }
}