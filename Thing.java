import java.io.Serializable;

public class Thing implements Serializable {
    private String name;
    private boolean is_taken;
    private int taste;
    private Human owner;

    public Thing() {
    }

    public Thing(String name) {
        this.name = name;
    }

    public Thing(int taste) {
        this.taste = taste;
    }


    public Thing(String name, int taste) {
        this.name = name;
        this.setTaste(taste);
    }


    public Thing(String name, Human owner){
        this.owner = owner;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    protected void setTaste(int taste) {
        if (taste < 0) {
            System.out.println("Taste can't be less, than 0. So it's 0");
            this.taste = 0;
        }
        this.taste = taste;
    }

    public boolean isTaken() {
        return this.is_taken;
    }

    public Creature getOwner() {
        return this.owner;
    }

    public void setOwner(Human owner){
        this.owner = owner;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public void put() {
        this.is_taken = false;
        this.owner = null;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public String toString() {
        return "Thing" + this.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Thing.class && obj.hashCode() == this.hashCode()) {
            return true;
        }
        return false;
    }
}