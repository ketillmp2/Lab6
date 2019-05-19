public final class Karlson extends Creature{
    {
        System.out.println("Creating new Karlson...");
        super.setName("Karlson");
        super.setAge(63);
        super.setHunger(70);
        this.setTerrain(Terrain.ROOF);
        System.out.println("Karlson was created");
    }
    @Override
    public void setName(String name) {
        System.out.println("Karlson is always Karlson");
    }
    @Override
    public void setAge(int age) {
        System.out.println("Karlsons are forever young and forever drunk");
    }
    @Override
    public void setHunger(int hunger) {
        System.out.println("Karlosn is always hungry");
    }
    @Override
    public int hashCode() {
        return super.hashCode() * 11;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Karlson.class && obj.hashCode() == this.hashCode()) {
            return true;
        }
        return false;
    }
}