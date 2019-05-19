import java.util.ArrayList;
import java.util.List;

abstract class Creature implements ICreature{
    private int age;
    private String name;
    private List<Skill> skills = new ArrayList<Skill>();
    private List<Thing> pocket = new ArrayList<Thing>();
    private boolean alive;
    private Terrain terrain;
    private int hunger;
    public RecipeBook RecipeBook;
    {
        this.name = "New Creature";
        this.age = 1;
    }
    protected void setName(String name) {
        this.name = name;
    }
    protected void setAge(int age) {
        this.age = age;
    }
    public void setSkills(List<Skill> skills) {
        forgetSkills(this.getSkills());
        learnSkills(skills);
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    protected void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        terrain.increment();
    }
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }
    public int getHunger() {
        return this.hunger;
    }
    public List<Skill> getSkills() {
        return this.skills;
    }
    public List<Thing> getThings(){
        return this.pocket;
    }
    public boolean haveSkill(String name) {
        for(Skill i: this.skills) {
            if(i.getName() == name) {
                return true;
            }
        }
        return false;
    }
    public void learnSkill(Skill skill) {
        if(this.haveSkill(skill.getName()) == false) {
            this.skills.add(skill);
            System.out.println(this.name + " learned skill " + skill.getName());
        }
        else {
            System.out.println(this.name+" alrady know skill "+skill.getName());
        }
    }
    public void learnSkills(List<Skill> skills) {
        for(Skill i:skills) {
            this.learnSkill(i);
        }
    }
    public void forgetSkill(Skill skill) {
        if(this.haveSkill(skill.getName()) == true) {
            this.skills.remove(skill);
            System.out.println(this.name + " forgot skill " + skill.getName());
        }
        else {
            System.out.println(this.name+" doesn't know skill "+skill.getName());
        }
    }
    public void forgetSkills(List<Skill> skills) {
        for(Skill i:skills) {
            this.forgetSkill(i);
        }
    }
    public Skill findSkill(String name) {
        for(Skill i:this.getSkills()) {
            if(name == i.getName()) {
                return i;
            }
        }
        System.out.println(this.getName()+" doesn't have skill "+name+", so return was null");
        return null;
    }
    public void useSkill(String name) {
        if(this.haveSkill(name) == true) {
            this.findSkill(name).use(this);
        }
        else {
            System.out.println(this.getName()+" doesn't have skill "+ name);
        }
    }
    public int getAge() {
        return this.age;
    }
    public RecipeBook getRecipeBook() {
        return this.RecipeBook;
    }
    public String getName() {
        return this.name;
    }
    public Terrain getTerrain() {
        return this.terrain;
    }
    public boolean isAlive() {
        return this.alive;
    }
    public void move(Terrain terrain){
        if(this.getTerrain() == terrain) {
            System.out.println(this.name+" is already here");
        }
        else {
            if(terrain != Terrain.VOID) {
                if(this.getHunger() <= 97) {
                    if(this.getTerrain() != null) {
                        this.getTerrain().decrement();
                    }
                    System.out.println(this.name+" moved to the "+terrain.getName() + " and get 2 hunger");
                    this.setHunger(this.getHunger() + 2);
                    this.terrain = terrain;
                    this.getTerrain().increment();
                }
                else {
                    System.out.println(this.name+ " can't move because of " + this.getHunger() + " hunger");
                }
            }
            else {
                System.out.println("Creatures can't be moved to the Void");
            }
        }
    }
    public void put(Thing thing) {
        if(thing.getOwner() == this) {
            if(thing.getClass() == RecipeBook.getClass()) {
                System.out.println(this.getName() + " can't put down Recipe Book");
                return;
            }
            thing.put();
            this.pocket.remove(thing);
            System.out.println(this.getName() + " put " + thing.getName());
        }
        else {
            System.out.println(this.getName() + " doesn't have " + thing.getName() + " in pocket");
        }
    }
    protected void isOk() throws AgeException{
        if(this.getAge() < 0 || this.getAge() > 150) {
            throw new AgeException();
        }
    }

    public void take(Thing t){

    }

    @Override
    public int hashCode() {
        return this.getName().hashCode() * 3 + this.getSkills().hashCode() + this.getRecipeBook().hashCode() + this.getThings().hashCode() + (this.getAge() * 10);
    }
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Creature.class && obj.hashCode() == this.hashCode()) {
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        String s = new String();
        s += this.getName() + this.getAge() + " ";
        for(Thing i : this.getThings()) {
            s += i.toString() + " ";
        }
        for(Skill i : this.getSkills()) {
            s += i.toString() + " ";
        }
        return  s + this.getRecipeBook().toString();
    }
}