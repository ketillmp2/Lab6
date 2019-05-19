import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeBook extends Thing implements Readable, Serializable {
    private List<Recipe> recipes = new ArrayList<Recipe>();
    private int xCoordinate;
    private int yCoordinate;
    public Date date;
    public String dateOfCreation;

    {
        this.date = new Date();
    }

    public RecipeBook() {
        super("RecipeBook", 0);
    }

    public RecipeBook(String name) {
        super(name, 0);
    }

    public RecipeBook(Human owner) {
        super("RecipeBook");
        this.setOwner(owner);
    }

    public RecipeBook(String name, Human owner) {
        this(owner);
        this.setName(name);
    }

    public RecipeBook(List<Recipe> recipes){
        this();
        for (Recipe r : recipes) {
            this.addRecipe(r);
        }
    }

    public RecipeBook(List<Recipe> recipes, Human owner){
        this(owner);
        for (Recipe r : recipes) {
            this.addRecipe(r);
        }
    }

    public RecipeBook(String name, List<Recipe> recipes, Human owner) {
        super(name, owner);
        for (Recipe r : recipes) {
            this.addRecipe(r);
        }
    }

    public RecipeBook(String name, List<Recipe> recipes, Human owner, int xCoordinate, int yCoordinate, String dateOfCreation) {
        this(name, recipes, owner);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.dateOfCreation = dateOfCreation;
    }

    public RecipeBook(String name, List<Recipe> recipes, Human owner, int xCoordinate, int yCoordinate) {
        this(name, recipes, owner);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.dateOfCreation = new Date().toString();
    }

    public void addRecipe(Recipe recipe) {
        for (Recipe i : this.recipes) {
            if (i == recipe) {
                return;
            }
        }
        this.recipes.add(recipe);
    }

    public List<Recipe> getRecipes() {
        return this.recipes;
    }

    public void removeRecipe(Recipe recipe) {
        if (this.recipes.contains(recipe) == true) {
            this.recipes.remove(recipe);
        } else {
            System.out.println("There is no " + recipe.getName() + " in " + this.getName());
        }
    }

    public void read() {
        System.out.println(this);
    }

    public double countAverageLikes(){
        double allLikes = 0;
        for(Recipe r : this.recipes){
            allLikes += r.getLikes();
        }
        if (this.recipes.size() == 0){
            return 0;
        }
        return allLikes/this.recipes.size();
    }

    public int getxCoordinate(){
        return this.xCoordinate;
    }

    public int getyCoordinate(){
        return this.yCoordinate;
    }

    @Override
    public int hashCode() {
        return super.hashCode() +  this.getRecipes().hashCode();
    }
    @Override
    public boolean equals(Object o){
        if(o.getClass() == this.getClass()){
            return this.hashCode() == o.hashCode();
        }
        return false;
    }
    @Override
    public String toString(){
        int a = 0;
        String s = new String();
        for(int j = 0; j < 70; j++){
            s += "_";
        }
        s += "\n";
        s += this.getOwner().getName() + " owns book with name " + this.getName() + "(" + this.countAverageLikes() + " likes in average):\n";
        s += "xCoordinate: " + this.xCoordinate + "\n";
        s += "yCoordinate: " + this.yCoordinate + "\n";
        s += "Date of creation: " + this.date + "\n";
        s += "recipes: \n";
        for(int i = 0; i < 70; i++){
            s += "_";
        }
        for (Recipe i : this.getRecipes()) {
            s += i;
            for(int j = 0; j < 70; j++){
                s += "_";
            }
        }
        s += "\n";
        return s;
    }
}