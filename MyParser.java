import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class MyParser {

    public Command doFirstCommand(String commanda) {
        String command = commanda.replaceAll("\t", "");

        try {
            if (command.length() <= 0) {
                throw new IncorrectCommandArgsException();
            }
            String[] splitedCommand = command.split(" ");
            String commandForJson = "";
            for (int i = 1; i < splitedCommand.length; i++) {
                commandForJson += splitedCommand[i];
            }
            if (splitedCommand.length >= 2) {
                if (splitedCommand[0].compareTo("save") == 0) {
                    try {
                        return this.doCommand(command);
                    } catch (IncorrectCommandArgsException | ParseException e) {
                        return new Command("incorrectCommand", null);
                    }
                }
            }

            if (splitedCommand[0].compareTo("add") != 0 && splitedCommand[0].compareTo("remove") != 0 && splitedCommand[0].compareTo("add_if_max") != 0 && splitedCommand[0].compareTo("add_if_min") != 0 && splitedCommand[0].compareTo("remove_lower") != 0) {
                try {
                    return this.doCommand(splitedCommand[0]);
                } catch (IncorrectCommandArgsException | ParseException e) {
                    return new Command("incorrectCommand", null);
                }
            } else {
                try {
                    return this.doCommand(splitedCommand[0] + " " + commandForJson);
                } catch (IncorrectCommandArgsException | ParseException e) {
                    return new Command("incorrectCommand", null);
                }
            }
        } catch (IncorrectCommandArgsException e) {
            return new Command("incorrectCommand", null);
        }
    }

    /**
     * Adds RecipeBooks from file with filePath path. If program doesn't have access to the file or file does not exist, prints message.
     */
    public Command addBooksFromFile(String filePath) {
        return new Command("addBooksFromFile", new RecipeBook(filePath));
    }

    public boolean isSimpleCommand(String command) {
        if (command.compareTo("exit") == 0 || command.compareTo("show") == 0 || command.compareTo("help") == 0 || command.compareTo("info") == 0 || command.compareTo("element_info") == 0 || command.compareTo("element_example") == 0) {
            return true;
        } else return false;
    }

    public String findCommandFromString(String command) {
        String[] a = command.split(" ");
        if (a.length == 0) {
            return "noCommand";
        }
        return a[0];
    }

    public Command doCommand(String command) throws IncorrectCommandArgsException, ParseException {
        String recognizedCommand = this.findCommandFromString(command);

        if (recognizedCommand.compareToIgnoreCase("show") == 0) {
            return new Command("show", null);
        }

        if (recognizedCommand.compareTo("info") == 0) {
            return new Command("info", null);
        }

        if (recognizedCommand.compareTo("help") == 0) {
            return new Command("help", null);
        }

        if (recognizedCommand.compareTo("element_example") == 0) {
            return new Command("element_example", null);
        }

        if (recognizedCommand.compareTo("element_info") == 0) {
            return new Command("element_info", null);
        }

        if (recognizedCommand.compareTo("save") == 0) {
            return new Command("save", null);
        }

        if (recognizedCommand.compareTo("exit") == 0) {
            return new Command("exit", null);
        }

        try {
            String commandForParce = command;
            while (commandForParce.charAt(0) == ' ') {
                if (commandForParce.length() <= 1) {
                    throw new IncorrectCommandArgsException();
                }
                commandForParce = commandForParce.substring(1);
            }

            while (commandForParce.charAt(0) != ' ') {
                if (commandForParce.length() <= 1) {
                    throw new IncorrectCommandArgsException();
                }
                commandForParce = commandForParce.substring(1);
            }

            while (commandForParce.charAt(0) == ' ') {
                if (commandForParce.length() <= 1) {
                    throw new IncorrectCommandArgsException();
                }
                commandForParce = commandForParce.substring(1);
            }

            RecipeBook buildedRecipeBook = null;
            JSONObject obj = (JSONObject) new JSONParser().parse(commandForParce);

            String parcedName = (String) obj.get("name");

            ArrayList<Recipe> parcedRecipes = new ArrayList<>();

            for (Object k : ((JSONArray) obj.get("recipes"))) {
                JSONObject o = (JSONObject) k;
                String parcedRecName = (String) o.get("name");
                ArrayList<String> parcedRecIngredients = new ArrayList<>();
                for (Object s : ((JSONArray) o.get("ingredients")).toArray()) {
                    parcedRecIngredients.add((String) s);
                }
                String parcedRecDesc = (String) o.get("description");
                String parcedRecResult = (String) o.get("result");
                int parcedRecLikes = 0;
                try {
                    parcedRecLikes = Integer.parseInt((String) o.get("likes"));
                } catch (NumberFormatException e) {
                    throw new IncorrectCommandArgsException();
                }
                parcedRecipes.add(new Recipe(parcedRecName, parcedRecIngredients, parcedRecDesc, parcedRecResult, parcedRecLikes));
            }

            int parcedHumanAge = 0;
            int parcedXCoordinate = 0;
            int parcedYCoordinate = 0;
            String parcedHumanName = "";
            try {
                parcedHumanAge = Integer.parseInt((String) ((JSONObject) obj.get("owner")).get("age"));
                parcedXCoordinate = Integer.parseInt((String) obj.get("xCoordinate"));
                parcedYCoordinate = Integer.parseInt((String) obj.get("yCoordinate"));
                parcedHumanName = (String) obj.get("name");
            } catch (NumberFormatException e) {
                throw new IncorrectCommandArgsException();
            }
            Human parcedHuman = new Human(parcedHumanName, parcedHumanAge);
            buildedRecipeBook = new RecipeBook(parcedName, parcedRecipes, parcedHuman, parcedXCoordinate, parcedYCoordinate);

            if (buildedRecipeBook == null) {
                throw new IncorrectCommandArgsException();
            }

            if (recognizedCommand.compareToIgnoreCase("add") == 0) {
                return new Command("add", buildedRecipeBook);
            }

            if (recognizedCommand.compareToIgnoreCase("remove") == 0) {
                return new Command("remove", buildedRecipeBook);
            }

            if (recognizedCommand.compareToIgnoreCase("remove_lower") == 0) {
                return new Command("remove_lower", buildedRecipeBook);
            }

            if (recognizedCommand.compareToIgnoreCase("add_if_min") == 0) {
                return new Command("add_if_min", buildedRecipeBook);
            }

            if (recognizedCommand.compareToIgnoreCase("add_if_max") == 0) {
                return new Command("add_if_max", buildedRecipeBook);
            }
            return new Command("incorrectCommand", null);
        } catch (IncorrectCommandArgsException | ParseException | NullPointerException e) {
            return new Command("incorrectCommand", null);
        }
    }
}
