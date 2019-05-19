import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**Class, which parse commands from csv and use them to TreeSet*/
public class CsvParser {
    /** Object, which recognizes commands*/
    public CommandRecognizer commandRecognizer = new CommandRecognizer();
    /** Object, which creates RecipeBook, Human, Recipe type objects*/
    public static Builder builder = new Builder();
    /** Commands uses this TreeSet */
    public ConcurrentSkipListSet<RecipeBook> recipeBooks;

    /**Constructor
     * @param recipeBooks Commands will use this TreeSet
     */
    public CsvParser(ConcurrentSkipListSet<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }
    /**Inner class, which recognizes commands.
     */
    public class CommandRecognizer {
        /**Current point of recognizing command and params*/
        public int point = 0;

        /**Return command from command
         * @param command current command*/
        public String findCommandFromString(String command) {
            String[] a = command.split(" ");
            if (a.length == 0) {
                return "noCommand";
            }
            return a[0];
        }

        /**Returns first founded word "name" and next word from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public String recognizeName(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].toLowerCase().compareTo("name") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            point++;
            if(a[this.point].charAt(a[this.point].length() - 1) == ',') {
                return a[this.point].substring(0, a[this.point].length() - 1);
            }
            else{
                return a[this.point];
            }
        }

        /**Returns first founded word "age" and next int from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public int recognizeAge(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].toLowerCase().compareTo("age") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            int age = 0;
            point++;
            try {
                age = Integer.parseInt(a[this.point]);
            } catch (NumberFormatException e) {
                throw new IncorrectCommandArgsException();
            }
            return age;
        }

        /**Returns first founded word "ingredients" and words until ',' from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public List<String> recognizeIngredients(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            List<String> ingredients = new ArrayList<String>();
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].toLowerCase().compareTo("ingredients") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point + 1].charAt(a[this.point + 1].length() - 1) != ',') {
                ingredients.add(a[this.point + 1]);
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            this.point++;
            ingredients.add(a[this.point].substring(0, a[this.point].length() - 1));
            return ingredients;
        }

        /**Returns first founded word "description" and all text until ',' from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public String recognizeDescription(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            String description = "";
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].toLowerCase().compareTo("description") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point + 1].charAt(a[this.point + 1].length() - 1) != ',') {
                description += a[this.point + 1];
                description += " ";
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            this.point++;
            description += a[this.point].substring(0, a[this.point].length() - 1);
            return description;
        }

        /**Returns first founded word "result" and next word from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public String recognizeResult(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            String result = "";
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].toLowerCase().compareTo("result") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point + 1].charAt(a[this.point + 1].length() - 1) != ',') {
                result += a[this.point + 1];
                result += " ";
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            this.point++;
            result += a[this.point].substring(0, a[this.point].length() - 1);
            return result;
        }

        /**Returns first founded word "likes" and next int from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public int recognizeLikes(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].toLowerCase().compareTo("likes") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            int likes = 0;
            try {
                point++;
                likes = Integer.parseInt(a[this.point]);
            } catch (NumberFormatException e) {
                throw new IncorrectCommandArgsException();
            }
            return likes;
        }

        /**Returns first founded word "Recipe" and builded Recipe from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public Recipe recognizeRecipe(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].compareTo("Recipe,") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            try {
                return CsvParser.builder.buildRecipe(recognizeName(command), recognizeIngredients(command), recognizeDescription(command), recognizeResult(command), recognizeLikes(command));
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }

        /**Returns first founded word "recipes" and builded Recipes from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public List<Recipe> recognizeRecipes(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            List<Recipe> recipes = new ArrayList<Recipe>();
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].compareTo("recipes") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            this.point++;
            if(a.length == this.point){
                return recipes;
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].compareTo("Recipe,") == 0) {
                try {
                    recipes.add(this.recognizeRecipe(command));
                } catch (IncorrectCommandArgsException e) {
                    throw e;
                }
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
                point++;
                if (a.length - 1 < this.point) {
                    break;
                }
            }
            return recipes;
        }

        /**Returns first founded word "owner" , Human and builded Human from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public Human recognizeOwner(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].compareTo("Human,") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            try {
                return CsvParser.builder.buildHuman(recognizeName(command), recognizeAge(command));
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }

        public int recognizeXCoordinate(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].compareTo("xCoordinate") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            int xCoordinate = 0;
            point++;
            try {
                xCoordinate = Integer.parseInt(a[this.point]);
            } catch (NumberFormatException e) {
                throw new IncorrectCommandArgsException();
            }
            return xCoordinate;
        }

        public int recognizeYCoordinate(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].compareTo("yCoordinate") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            int yCoordinate = 0;
            point++;
            try {
                yCoordinate = Integer.parseInt(a[this.point]);
            } catch (NumberFormatException e) {
                throw new IncorrectCommandArgsException();
            }
            return yCoordinate;
        }

        public String recognizeDate(String command) throws IncorrectCommandArgsException {
            String[] a = command.split(" ");
            String date = "";
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].toLowerCase().compareTo("date") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            this.point++;
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }

            while (a.length - 1 >= this.point) {
                date += a[this.point];
                date += " ";
                this.point++;
            }

            this.point++;
            return date;
        }

        /**Returns first founded word "RecipeBook" and builded RecipeBook from command from point. Else throws exception
         * @param command current command
         * @exception IncorrectCommandArgsException*/
        public RecipeBook recognizeRecipeBook(String command) throws IncorrectCommandArgsException {
            this.point = 0;
            String[] a = command.split(" ");
            if (a.length - 1 < this.point) {
                throw new IncorrectCommandArgsException();
            }
            while (a[this.point].compareTo("RecipeBook") != 0) {
                this.point++;
                if (a.length - 1 < this.point) {
                    throw new IncorrectCommandArgsException();
                }
            }
            try {
                return CsvParser.builder.buildRecipeBook(recognizeName(command), recognizeOwner(command), recognizeRecipes(command), recognizeXCoordinate(command), recognizeYCoordinate(command), recognizeDate(command));
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }
    }

    /**Class, which build Human, Recipe, RecipeBook type objects*/
    public static class Builder {
        /**Return Human with this name and age
         * @param age age for Human
         * @param name name for Human*/
        public Human buildHuman(String name, Integer age) {
            if (age == null) {
                if (name == null) {
                    return new Human();
                }
                return new Human(name);
            }
            if (name == null) {
                return new Human(age);
            }
            return new Human(name, age);
        }

        /**Return Human with this args*/
        public Recipe buildRecipe(String name, List<String> ingredients, String description, String result, Integer likes) {
            return new Recipe(name, ingredients, description, result, likes);
        }

        /**Return RecipeBook with this args*/
        public RecipeBook buildRecipeBook(String name, Human owner, List<Recipe> recipes, int xCoordinate, int yCoordinate, String date) {
            return new RecipeBook(name, recipes, owner, xCoordinate, yCoordinate, date);
        }
    }
}