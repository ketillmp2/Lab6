import java.io.*;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;

public class FileWriter {
    private BufferedOutputStream bufferedOutputStream;
    private String fileContent;

    private String treeSetIntoCsv(TreeSetBookCase bookCase) {
        ConcurrentSkipListSet<RecipeBook> treeSet = bookCase.treeSet;
        String result = "";
        RecipeBook[] recipeBooks = treeSet.toArray(new RecipeBook[0]);

        if (bookCase.haveDate) {
            result += bookCase.dateOfCreation + "\n\n";
        } else result += "Date of creation: " + new Date().toString() + "\n \n";
        for (RecipeBook recipeBook : recipeBooks) {
            result += "RecipeBook\n";
            result += "name " + recipeBook.getName() + "\n";
            result += "owner\n";
            result += "Human, name " + recipeBook.getOwner().getName() + ", age " + recipeBook.getOwner().getAge() + "\n";
            result += "recipes\n";
            for (Recipe recipe : recipeBook.getRecipes()) {
                result += "Recipe, name " + recipe.getName() + ", ingredients ";
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    if (i == recipe.getIngredients().size() - 1) {
                        result += recipe.getIngredients().get(i) + ", ";
                    } else {
                        result += recipe.getIngredients().get(i) + " ";
                    }
                }
                result += "description " + recipe.getDescription() + ", ";
                result += "result " + recipe.getResult() + ", ";
                result += "likes " + recipe.getLikes();
                result += "\n";
            }
            result += "xCoordinate " + recipeBook.getxCoordinate() + "\n";
            result += "yCoordinate " + recipeBook.getyCoordinate() + "\n";
            result += "date " + recipeBook.dateOfCreation + "\n \n";
        }
        return result;
    }

    public String writeIntoFile(TreeSetBookCase bookCase, String filePath) {
        fileContent = treeSetIntoCsv(bookCase);
        try {
            this.bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            try {
                byte[] bytes = fileContent.getBytes();
                bufferedOutputStream.write(bytes, 0, bytes.length);
                bufferedOutputStream.close();
                return "TreeSet saved into file with path " + filePath;
            } catch (IOException notAllowed) {
                return "You don't have access to file with path " + filePath;
            }
        } catch (FileNotFoundException wrongPath) {
            return "You don't have access to file with path " + filePath;
        }
    }
}