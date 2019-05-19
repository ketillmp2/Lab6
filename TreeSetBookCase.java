import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * Class for containing and processing TreeSet.
 */

public class TreeSetBookCase {
    /**
     * Contains TreeSet. All operation will be used for this TreeSet.
     */
    public ConcurrentSkipListSet<RecipeBook> treeSet = new ConcurrentSkipListSet<RecipeBook>(((RecipeBook r1, RecipeBook r2) -> (int) (r1.countAverageLikes() - r2.countAverageLikes())));
    /**
     * Path for saving TreeSet
     */
    public String savePath;
    /**
     * Parser for csv format
     */
    public CsvParser csvParser;

    String dateOfCreation;

    boolean haveDate = false;

    /**
     * Constructor that creates new TreeSetBookCase with current savePath.
     *
     * @param savePath - path for saving TreeSet
     */

    public TreeSetBookCase(String savePath) {
        this.savePath = savePath;
        this.csvParser = new CsvParser(treeSet);
    }

    /**
     * Adds RecipeBooks from file with filePath path. If program doesn't have access to the file or file does not exist, prints message.
     */
    public String addBooksFromFile(String filePath) {
        FileReader reader = new FileReader();
        reader.readFile(filePath);
        String message = "";
        if (reader.getFileContent() == "") {
            return "Wrong object format. Print help to get more information.";
        }
        String[] wrongSplit = reader.getFileContent().split("" + (char) 10);
        String correctSplit = "";
        for (String s : wrongSplit) {
            correctSplit += s + " ";
        }
        String[] commands = correctSplit.split("RecipeBook");

        if(!this.haveDate) {
            String date = "";
            for (int i = 0; i < commands[0].length(); i++) {
                if (date == "" && commands[0].charAt(i) == ' ') {
                    continue;
                }
                date += commands[0].charAt(i);
                if (date.compareTo("Date of creation:") == 0) {
                    haveDate = true;
                }
            }

            if (haveDate) {
                this.dateOfCreation = date;
            }else{
                this.dateOfCreation = new Date().toString();
            }
        }

        int counter = 0;
        for (String command : commands) {
            if (haveDate && counter == 0) {
                counter++;
                continue;
            }
            if (command.compareTo("") != 0) {
                try {
                    RecipeBook buildedRecipeBook = csvParser.commandRecognizer.recognizeRecipeBook("RecipeBook " + command);
                    message += this.add(buildedRecipeBook) + "\n";
                } catch (IncorrectCommandArgsException e) {
                    message += "Wrong object format. Print help to get more information.\n";
                    continue;
                }
            }
        }
        return message + "RecipeBooks added from file " + filePath;
    }

    /**
     * Resolve command for TreeSet or throws Exception if command is incorrect
     *
     * @param command current command
     * @throws IncorrectCommandArgsException
     */
    public String doCommand(Command command) {

        String message = "";

        if (command.getCommand() == null) {
            return "";
        }

        if (command.getCommand() == "checkConnection") {
            return "Connection completed";
        }

        if (command.getCommand().compareToIgnoreCase("incorrectCommand") == 0) {
            return "Wrong command. Write help to get more information about commands.";
        }

        if (command.getCommand().compareToIgnoreCase("takeSavePath") == 0) {
            return this.savePath;
        }

        if (command.getCommand().compareToIgnoreCase("show") == 0) {
            return this.showTreeSetAsString(this.treeSet);
        }

        if (command.getCommand().compareToIgnoreCase("addBooksFromFile") == 0) {
            return this.addBooksFromFile(command.getArgument().getName());
        }

        if (command.getCommand().compareTo("info") == 0) {
            message += "Information about TreeSet:\n";
            if(this.haveDate) {
                message += this.dateOfCreation + "\n";
            }else {
                message += "Date of TreeSet creation: " + new Date().toString() + "\n";
            }
            message += "TreeSet contains " + this.treeSet.size() + " RecipeBooks \n";
            if (this.treeSet.size() == 0) {
                return message;
            }
            message += "First element has " + this.treeSet.first().countAverageLikes() + " likes" + "\n";
            message += "Last element has " + this.treeSet.last().countAverageLikes() + " likes" + "\n";
            return message;
        }

        if (command.getCommand().compareTo("help") == 0) {
            message += ("List of commands:\n");
            message += ("add { element } - adds element into TreeSet\n");
            message += ("remove { element } - removes element from TreeSet\n");
            message += ("add_if_min { element } - adds element into TreeSet if it's lower than lowest element\n");
            message += ("add_if_max { element } - adds element into TreeSet if it's higher than highest element\n");
            message += ("remove_lower { element } - removes all elements from TreeSet which are lower than element\n");
            message += ("show - shows information about elements from TreeSet\n");
            message += ("info - shows information about TreeSet\n");
            message += ("help - shows list of commands\n");
            message += ("element_example - shows correct element example\n");
            message += ("element_info - shows information about RecipeBook's fields\n");
            message += ("save - saves TreeSet into file with path from args in csv format\n");
            return message;
        }

        if (command.getCommand().compareTo("element_example") == 0) {
            message += ("Correct element example:\n");
            message += ("{\n" +
                    "    \"name\": \"Simple_book_name\",\n" +
                    "    \"recipes\": [\n" +
                    "         {\n" +
                    "            \"name\": \"Simple_recipe_name_1\",\n" +
                    "            \"ingredients\": [\n" +
                    "                \"onion\",\n" +
                    "                \"apple\"\n" +
                    "            ],\n" +
                    "            \"description\": \"Simple_description\",\n" +
                    "            \"result\": \"any_result\",\n" +
                    "            \"likes\": \"10\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"name\": \"Salad\",\n" +
                    "            \"ingredients\": [\n" +
                    "                \"tomato\",\n" +
                    "                \"onion\",\n" +
                    "                \"nuts\"\n" +
                    "            ],\n" +
                    "            \"description\": \"Mix it\",\n" +
                    "            \"result\": \"bad_salad\",\n" +
                    "            \"likes\": \"-2\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"owner\": {\n" +
                    "        \"name\": \"Oleg\",\n" +
                    "        \"age\": \"12\"\n" +
                    "    },\n" +
                    "    \"xCoordinate\": \"4\",\n" +
                    "    \"yCoordinate\": \"-3\"\n" +
                    "}");
            return message;
        }

        if (command.getCommand().compareTo("element_info") == 0) {
            message += ("Recipe book contains:\n");
            message += ("String name\n");
            message += ("Human owner\n");
            message += ("List<Recipe> recipes - there can be any number of Recipes\n");
            message += ("Human contains:\n");
            message += ("String name\n");
            message += ("int age\n");
            message += ("Recipe contains:\n");
            message += ("String name\n");
            message += ("String[] ingredients\n");
            message += ("String description\n");
            message += ("String result\n");
            message += ("int likes\n");
            message += ("int xCoordinate\n");
            message += ("int yCoordinate\n");
            message += ("Date date\n");
            return message;
        }

        if (command.getCommand().compareTo("save") == 0) {
            FileWriter writer = new FileWriter();
            if (savePath.compareTo("") == 0) {
                message += ("There were no savePath in args.");
                return message;
            }
            message += writer.writeIntoFile(this, this.savePath);
            return message;
        }

        if (command.getCommand().compareTo("exit") == 0) {
            return message;
        }

        try {
            RecipeBook buildedRecipeBook = command.getArgument();
            if (buildedRecipeBook == null) {
                throw new IncorrectCommandArgsException();
            }

            if (command.getCommand().compareToIgnoreCase("add") == 0) {
                return this.add(buildedRecipeBook);
            }

            if (command.getCommand().compareToIgnoreCase("remove") == 0) {
                return this.remove(buildedRecipeBook);
            }

            if (command.getCommand().compareToIgnoreCase("remove_lower") == 0) {
                return this.removeLower(buildedRecipeBook);
            }

            if (command.getCommand().compareToIgnoreCase("add_if_min") == 0) {
                return this.addIfMin(buildedRecipeBook);
            }

            if (command.getCommand().compareToIgnoreCase("add_if_max") == 0) {
                return this.addIfMax(buildedRecipeBook);
            }
            throw new IncorrectCommandArgsException();
        } catch (IncorrectCommandArgsException e) {
            return "";
        }
    }

    /**
     * Add buildedRecipeBook into TreeSet
     */
    public String add(RecipeBook buildedRecipeBook) {
        this.treeSet.add(buildedRecipeBook);
        return (buildedRecipeBook.getName() + " added to TreeSet");
    }

    /**
     * Add buildedRecipeBook into TreeSet if it's more that current maximum element in TreeSet
     *
     * @param buildedRecipeBook
     */
    public synchronized String addIfMax(RecipeBook buildedRecipeBook) {
        if (this.treeSet.isEmpty()) {
            this.treeSet.add(buildedRecipeBook);
            return (buildedRecipeBook.getName() + " added to TreeSet");
        }
        if (this.treeSet.last().countAverageLikes() < buildedRecipeBook.countAverageLikes()) {
            this.treeSet.add(buildedRecipeBook);
            return (buildedRecipeBook.getName() + " added to TreeSet");
        }
        return (buildedRecipeBook.getName() + " is lower than highest element in TreeSet");
    }

    /**
     * Add buildedRecipeBook into TreeSet if it's less that cur
     * rent minimum element in TreeSet
     *
     * @param buildedRecipeBook
     */
    public synchronized String addIfMin(RecipeBook buildedRecipeBook) {
        if (this.treeSet.isEmpty()) {
            this.treeSet.add(buildedRecipeBook);
            return (buildedRecipeBook.getName() + " added to TreeSet");
        }
        if (this.treeSet.first().countAverageLikes() > buildedRecipeBook.countAverageLikes()) {
            this.treeSet.add(buildedRecipeBook);
            return (buildedRecipeBook.getName() + " added to TreeSet");
        }
        return (buildedRecipeBook.getName() + " is higher than lowest element in TreeSet");
    }

    /**
     * Remove elements that are lower than buildedRecipeBook from TreeSet
     *
     * @param buildedRecipeBook
     */
    public synchronized String removeLower(RecipeBook buildedRecipeBook) {
        if (this.treeSet.isEmpty()) {
            return ("TreeSet is empty");
        }

        this.treeSet.stream().filter(r -> r.countAverageLikes() < buildedRecipeBook.countAverageLikes()).forEach(r -> this.treeSet.remove(r));

        return ("Elements lower than " + buildedRecipeBook.getName() + " removed from TreeSet");
    }

    /**
     * remove buildedRecipeBook from TreeSet
     * If there is no object like this, print message
     *
     * @param buildedRecipeBook - will be removed
     */
    public String remove(RecipeBook buildedRecipeBook) {
        if (this.treeSet.remove(buildedRecipeBook)) {
            return (buildedRecipeBook.getName() + " removed");
        } else {
            return ("there is no " + buildedRecipeBook.getName() + " in TreeSet");
        }
    }

    /**
     * Shows TreeSet as String
     *
     * @param treeSet
     */
    public String showTreeSetAsString(ConcurrentSkipListSet<RecipeBook> treeSet) {
        return this.treeSet.toString();
    }
}