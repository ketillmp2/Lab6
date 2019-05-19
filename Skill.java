public class Skill {
    private String name;
    private int complexity;

    public Skill(String name){
        this.name = name;
    }

    public Skill(int complexity){
        this.complexity = complexity;
    }

    public Skill (String name, int complexity){
        this.name = name;
        this.complexity = complexity;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    protected void setComplexity(int comp) {
        this.complexity = comp;
    }
    public int getComplexity() {
        return this.complexity;
    }
    protected boolean hunger_check(Creature user){
        if(user.isAlive() == false) {
            System.out.println(user.getName() + " can't use skill " + this.getName() + " because of death");
            return false;
        }
        if(user.getHunger() + this.getComplexity() < 100) {
            user.setHunger(user.getHunger() + this.getComplexity());
            System.out.println(user.getName() + " gets " + this.getComplexity() + " hunger while using " + this.getName() + " skill");
            return true;
        }
        else {
            System.out.println(user.getName() + " can't use skill " + this.getName() + " because of " + user.getHunger() + " hunger");
            return false;
        }
    }
    public void use(Creature user) {
        if(user.haveSkill(this.getName()) == true) {
            System.out.println(this.name+" skill does nothing");
        }
        else {
            System.out.println(user.getName()+ " can't use skill " + this.name);
        }
    }
    @Override
    public int hashCode() {
        return (this.getComplexity() * 7) + (this.getName().hashCode() / 3);
    }
    @Override
    public String toString() {
        return "Skill"+this.getName() + this.getComplexity();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Skill.class && obj.hashCode() == this.hashCode()) {
            return true;
        }
        return false;
    }
}
