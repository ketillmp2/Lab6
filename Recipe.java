import java.util.ArrayList;
import java.util.List;

public class Recipe extends Thing implements Readable {
    private List<String> ingredients = new ArrayList<String>();
    private String description;
    private String result;
    private int likes;

    public Recipe(String name, List<String> ingredients, String description, String result, int likes) {
        super(name, 0);
        for (String i : ingredients) {
            this.addIngredient(i);
        }
        this.description = description;
        this.result = result;
        this.likes = likes;
    }

    @Override
    public void setTaste(int taste) {
        super.setTaste(0);
    }

    public void addIngredient(String ingredient) {
        for (String i : this.getIngredients()) {
            if (i == ingredient) {
                return;
            }
        }
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(String ingredient) {
        if (this.ingredients.contains(ingredient) == true) {
            this.ingredients.remove(ingredient);
        } else {
            System.out.println("there is no " + ingredient + " in the " + this.getName() + " recipe");
        }
    }

    public List<String> getIngredients() {
        return this.ingredients;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addDescription(String description) {
        this.description += " " + description;
    }

    public void removeDescription() {
        this.description = "Put it all into microwave";
    }

    public String getDescription() {
        return this.description;
    }

    public void read() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        String s = new String();
        s += "\n";
        s += this.getName() + " (" + this.likes + " likes) " + ":\n";
        s += "List of ingredients:\n";
        for (String i : this.ingredients) {
            s += i + "\n";
        }
        s += "Description: " + this.description + "\n";
        s += "You will get " + this.getResult() + "\n";
        return s;
    }

    public void like() {
        likes++;
    }

    public void dislike() {
        likes--;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + (this.getResult().hashCode() * 10) + this.getIngredients().hashCode();
    }
}