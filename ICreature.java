public interface ICreature{
    public void move(Terrain terrain);
    public void take(Thing thing);
    public void put(Thing thing);
    public void learnSkill(Skill skill);
    public void forgetSkill(Skill skill);
    public void useSkill(String name);
}