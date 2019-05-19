import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/*RecipeBook
name abc
recipes
Recipe, name salad, ingredients onion tomato, description mix it, result good_salad, likes 5
Recipe, name salad, ingredients tomato brokoly, description mix it, result bad_salad, likes -2
owner
Human, name Kirill, age 18*/


/*
"RecipeBook":{
    "name": "Simple_book_name",
    "recipes": [

    ],
    "owner": {
        "name": "Oleg",
        "age": "12"
    },
    "xCoordinate": "123",
    "yCoordinate": "456"
}
 */

public class JsonIntoCsvParser {
    private List<String> commands = new ArrayList<String>();
    private int currentCommand = 0;
    private String parsedCommand = null;

    public JsonIntoCsvParser(String command) throws IncorrectCommandArgsException {
        String[] a = command.split("\n");
        for (String s : a) {
            this.commands.add(s);
        }
        try {
            if(this.getCommands().size() == 0){
                throw new IncorrectCommandArgsException();
            }
            parsedCommand = this.nextObject(this.getCommands().get(0), 0);
            this.makeCorrectOrder();
        } catch (IncorrectCommandArgsException | IndexOutOfBoundsException e) {
            throw e;
        }
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public String getParsedCommand() {
        return this.parsedCommand;
    }

    private void makeCorrectOrder() throws IncorrectCommandArgsException {

        String result = "";
        String[] parced = parsedCommand.split(" ");
        boolean flag = false;
        int point = 1;
        if (parced.length < 1) {
            throw new IncorrectCommandArgsException();
        }
        result += parced[0];
        result += " ";
        while (point <= parced.length - 1) {
            boolean flag2 = false;
            if (parced[point].compareTo("name") == 0) {
                if (point + 2 <= parced.length - 1) {
                    if (parced[point + 2].compareTo("age") == 0 || parced[point + 2].compareTo("ingredients") == 0 || parced[point + 2].compareTo("description") == 0 || parced[point + 2].compareTo("result") == 0 || parced[point + 2].compareTo("likes") == 0) {
                        flag2 = true;
                    }
                }
                if (!flag2) {
                    result += parced[point] + " ";
                    if (parced.length - 1 == point) {
                        throw new IncorrectCommandArgsException();
                    }
                    result += parced[point + 1];
                    result += " ";
                    flag = true;
                    break;
                }
            }
            point++;
        }
        if (!flag) {
            throw new IncorrectCommandArgsException();
        }
        flag = false;
        point = 0;

        while (point <= parced.length - 1) {
            if (parced[point].compareTo("owner") == 0) {
                if (parced.length - point - 1 < 4) {
                    throw new IncorrectCommandArgsException();
                }
                result += "owner Human, name ";
                if (parced[point + 1].compareTo("name") == 0) {
                    result += parced[point + 2] + " ";
                    result += "age " + parced[point + 4] + " ";
                } else {
                    result += parced[point + 4] + " ";
                    result += "age " + parced[point + 2] + " ";
                }
                flag = true;
            }
            point++;
        }
        if (!flag) {
            throw new IncorrectCommandArgsException();
        }
        flag = false;
        point = 0;

        while (point <= parced.length - 1) {
            if (parced[point].compareTo("recipes") == 0) {
                result += parced[point] + " ";
                point++;
                if(parced.length - 1 == point){
                    flag = true;
                    break;
                }
                if (parced.length - 1 < point) {
                    throw new IncorrectCommandArgsException();
                }
                while (parced[point].compareTo("Recipe") == 0) {
                    result += "Recipe, ";
                    point++;
                    if (parced.length - 1 < point) {
                        throw new IncorrectCommandArgsException();
                    }
                    while (parced[point].compareTo("Recipe") != 0 && parced[point].compareTo("owner") != 0) {
                        result += parced[point] + " ";
                        point++;
                        if (parced.length - 1 < point) {
                            throw new IncorrectCommandArgsException();
                        }
                    }
                    result = result.substring(0, result.length() - 1);
                    if (parced.length - point - 1 <= 3) {
                        break;
                    }
                }
                flag = true;
                break;
            }
            point++;
        }

        if (!flag) {
            throw new IncorrectCommandArgsException();
        }
        this.parsedCommand = result;
    }
    /*RecipeBook
name abc
recipes
Recipe, name salad, ingredients onion tomato, description mix it, result good_salad, likes 5
Recipe, name salad, ingredients tomato brokoly, description mix it, result bad_salad, likes -2
owner
Human, name Kirill, age 18*/

    private String nextObject(String object, int currentCommand) throws IncorrectCommandArgsException {
        int currentPoint = this.backwardSkip(object, object.length() - 1);
        if(object.length() == 0){
            throw new IncorrectCommandArgsException();
        }
        if (object.charAt(currentPoint) == '"') {
            try {
                return makeField(object);
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }
        if (object.charAt(currentPoint) == ',') {
            currentPoint = this.backwardSkip(object, currentPoint - 1);
            if (object.charAt(currentPoint) == '"') {
                try {
                    return makeField(object);
                } catch (IncorrectCommandArgsException e) {
                    throw e;
                }
            }
        }
        if (object.charAt(currentPoint) == '[') {
            try {
                return makeElementsArray(object, this.currentCommand);
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }
        if (object.charAt(currentPoint) == '{') {
            try {
                return makeObject(object, this.currentCommand);
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }
        throw new IncorrectCommandArgsException();
    }

    private String makeObject(String s, int currentCommand) throws IncorrectCommandArgsException {
        String objectName = new String();
        String objectFields = new String();
        boolean haveLastField = false;
        int currentPoint = this.skip(s, 0);
        if (this.commands.size() - 1 < this.currentCommand) {
            throw new IncorrectCommandArgsException();
        }
        if (s.charAt(currentPoint) != '"') {
            throw new IncorrectCommandArgsException();
        }
        objectName = this.between(s, currentPoint);
        currentPoint = this.betweenForInt(s, currentPoint);
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != ':') {
            throw new IncorrectCommandArgsException();
        }
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != '{') {
            throw new IncorrectCommandArgsException();
        }
        if (s.length() - 1 < currentPoint) {
            if (s.charAt(this.skip(s, currentPoint + 1)) != ' ') {
                throw new IncorrectCommandArgsException();
            }
        }
        while (true) {
            this.currentCommand++;
            if (this.commands.size() - 1 < this.currentCommand) {
                throw new IncorrectCommandArgsException();
            }
            currentPoint = this.skip(this.commands.get(this.currentCommand), 0);
            if (this.commands.size() - 1 < this.currentCommand) {
                throw new IncorrectCommandArgsException();
            }
            if (this.commands.get(this.currentCommand).charAt(currentPoint) == '}') {
                if (currentPoint == this.commands.get(this.currentCommand).length() - 1) {
                    break;
                }
                currentPoint = this.skip(this.commands.get(this.currentCommand), currentPoint + 1);
                if (currentPoint == this.commands.get(this.currentCommand).length() - 1) {
                    if (this.commands.get(this.currentCommand).charAt(currentPoint) == ',') {
                        break;
                    }
                }
            }
            try {
                String nextObject = this.nextObject(this.commands.get(this.currentCommand), this.currentCommand);
                if (nextObject.charAt(nextObject.length() - 1) == '"') {
                    if (haveLastField == true) {
                        throw new IncorrectCommandArgsException();
                    } else {
                        haveLastField = true;
                        objectFields += nextObject.substring(0, nextObject.length() - 1);
                    }
                } else {
                    objectFields += nextObject;
                }
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
            objectFields += " ";
        }
        return objectName + " " + objectFields;
    }

    private String makeField(String s) throws IncorrectCommandArgsException {
        String fieldName = new String();
        String fieldValue = new String();
        int currentPoint = this.skip(s, 0);
        if (s.charAt(currentPoint) != '"') {
            throw new IncorrectCommandArgsException();
        }
        try {
            fieldName = this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != ':') {
            throw new IncorrectCommandArgsException();
        }
        currentPoint = this.skip(s, currentPoint + 1);
        try {
            fieldValue = this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != ',') {
            try {
                return makeLastField(s);
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }
        if (s.length() - 1 > currentPoint) {
            currentPoint = this.skip(s, currentPoint + 1);
            if (s.charAt(currentPoint) != ' ') {
                throw new IncorrectCommandArgsException();
            }
        }

        return fieldName + " " + fieldValue + ",";
    }

    private String makeLastField(String s) throws IncorrectCommandArgsException {
        String fieldName = new String();
        String fieldValue = new String();
        int currentPoint = this.skip(s, 0);
        if (s.charAt(currentPoint) != '"') {
            throw new IncorrectCommandArgsException();
        }
        try {
            fieldName = this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != ':') {
            throw new IncorrectCommandArgsException();
        }
        currentPoint = this.skip(s, currentPoint + 1);
        try {
            fieldValue = this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }
        if (s.length() - 1 > currentPoint) {
            currentPoint = this.skip(s, currentPoint + 1);
            if (s.charAt(currentPoint) != ' ') {
                throw new IncorrectCommandArgsException();
            }
        }
        return fieldName + " " + fieldValue + '"';
    }

    private String makeArrayElement(String s) throws IncorrectCommandArgsException {
        String elementValue = new String();
        int currentPoint = this.skip(s, 0);
        try {
            elementValue = this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) == ',') {
            if (currentPoint < s.length() - 1) {
                if (s.charAt(this.skip(s, currentPoint + 1)) != ' ') {
                    throw new IncorrectCommandArgsException();
                }
            }
            return elementValue;
        } else {
            try {
                return makeLastArrayElement(s);
            } catch (IncorrectCommandArgsException e) {
                throw e;
            }
        }
    }

    private String makeLastArrayElement(String s) throws IncorrectCommandArgsException {
        String elementValue = new String();
        int currentPoint = this.skip(s, 0);
        try {
            elementValue = this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }
        currentPoint = this.skip(s, currentPoint + 1);
        if (currentPoint < s.length() - 1) {
            throw new IncorrectCommandArgsException();
        }
        return elementValue;
    }

    private String makeElementsArray(String s, int currentCommand) throws IncorrectCommandArgsException {
        /*"ingredients": [
        "onion",
        "brokoly"
        ],*/
        String arrayName = new String();
        List<String> arrayElements = new ArrayList<String>();
        String result = new String();
        int currentPoint = this.skip(s, 0);
        boolean haveLastElement = false;
        if (this.currentCommand > this.commands.size() - 1) {
            throw new IncorrectCommandArgsException();
        }

        if (s.charAt(currentPoint) != '"') {
            throw new IncorrectCommandArgsException();
        }

        try {
            arrayName += this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }

        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != ':') {
            throw new IncorrectCommandArgsException();
        }

        currentPoint++;
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != '[') {
            throw new IncorrectCommandArgsException();
        }

        if (s.length() - 1 > currentPoint) {
            currentPoint = this.skip(s, currentPoint + 1);
            if (s.charAt(currentPoint) != ' ') {
                throw new IncorrectCommandArgsException();
            }
        }

        while (true != false) {
            this.currentCommand++;
            if (this.currentCommand > this.commands.size() - 1) {
                throw new IncorrectCommandArgsException();
            }
            if (this.commands.get(this.currentCommand).charAt(this.backwardSkip(this.commands.get(this.currentCommand), this.commands.get(this.currentCommand).length() - 1)) != ',') {
                if (haveLastElement) {
                    throw new IncorrectCommandArgsException();
                } else {
                    try {
                        arrayElements.add(makeLastArrayElement(commands.get(this.currentCommand)));
                    } catch (IncorrectCommandArgsException e) {
                        throw e;
                    }
                    haveLastElement = true;
                    this.currentCommand++;
                    if (this.currentCommand > this.commands.size() - 1) {
                        throw new IncorrectCommandArgsException();
                    }
                    int point = this.skip(commands.get(this.currentCommand), 0);
                    if (commands.get(this.currentCommand).charAt(point) == ']') {
                        point = this.skip(commands.get(this.currentCommand), point + 1);
                        if (point == commands.get(this.currentCommand).length() - 1) {
                            if (commands.get(this.currentCommand).charAt(point) == ',') {
                                break;
                            }
                        }
                    }
                }
            } else {
                try {
                    arrayElements.add(makeArrayElement(commands.get(this.currentCommand)));
                } catch (IncorrectCommandArgsException e) {
                    throw e;
                }
            }
        }
        result += arrayName;
        for (String i : arrayElements) {
            result += " " + i;
        }
        return result + ",";
    }

    private String makeLastElementsArray(String s, int currentCommand) throws IncorrectCommandArgsException {
        /*"ingredients": [
        "onion",
        "brokoly"
        ]*/
        String arrayName = new String();
        List<String> arrayElements = new ArrayList<String>();
        String result = new String();
        int currentPoint = this.skip(s, 0);
        boolean haveLastElement = false;
        if (this.currentCommand > this.commands.size() - 1) {
            throw new IncorrectCommandArgsException();
        }

        if (s.charAt(currentPoint) != '"') {
            throw new IncorrectCommandArgsException();
        }

        try {
            arrayName += this.between(s, currentPoint);
            currentPoint = this.betweenForInt(s, currentPoint);
        } catch (IncorrectCommandArgsException e) {
            throw e;
        }

        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != ':') {
            throw new IncorrectCommandArgsException();
        }
        currentPoint++;
        currentPoint = this.skip(s, currentPoint + 1);
        if (s.charAt(currentPoint) != '[') {
            throw new IncorrectCommandArgsException();
        }

        if (s.length() - 1 > currentPoint) {
            currentPoint = this.skip(s, currentPoint + 1);
            if (s.charAt(currentPoint) != ' ') {
                throw new IncorrectCommandArgsException();
            }
        }
        while (true != false) {
            this.currentCommand++;
            if (this.currentCommand > this.commands.size() - 1) {
                throw new IncorrectCommandArgsException();
            }
            if (this.commands.get(this.currentCommand).charAt(this.backwardSkip(this.commands.get(this.currentCommand), this.commands.get(this.currentCommand).length() - 1)) != ',') {
                if (haveLastElement) {
                    throw new IncorrectCommandArgsException();
                } else {
                    try {
                        arrayElements.add(makeLastArrayElement(commands.get(this.currentCommand)));
                    } catch (IncorrectCommandArgsException e) {
                        throw e;
                    }
                    haveLastElement = true;
                    this.currentCommand++;
                    if (this.currentCommand > this.commands.size() - 1) {
                        throw new IncorrectCommandArgsException();
                    }
                    if (commands.get(this.currentCommand).split(" ")[0] == "]" && commands.get(this.currentCommand).split(" ").length == 1) {
                        if (!haveLastElement) {
                            throw new IncorrectCommandArgsException();
                        }
                        break;
                    } else {
                        throw new IncorrectCommandArgsException();
                    }
                }
            } else {
                try {
                    arrayElements.add(makeArrayElement(commands.get(this.currentCommand)));
                } catch (IncorrectCommandArgsException e) {
                    throw e;
                }
            }
        }
        result += arrayName;
        for (String i : arrayElements) {
            result += " " + i;
        }
        return result + '"';
    }

    private String between(String s, int currentPoint) throws IncorrectCommandArgsException {
        String result = new String();
        char openSymbol;
        char closeSymbol;
        if (s.length() <= currentPoint + 1) {
            throw new IncorrectCommandArgsException();
        }
        openSymbol = s.charAt(currentPoint);
        if (openSymbol == '"') {
            closeSymbol = '"';
        } else {
            throw new IncorrectCommandArgsException();
        }
        currentPoint++;
        while (s.charAt(currentPoint) != closeSymbol) {
            result += s.charAt(currentPoint);
            currentPoint++;
            if (s.length() <= currentPoint) {
                throw new IncorrectCommandArgsException();
            }
        }
        return result;
    }

    private int betweenForInt(String s, int currentPoint) throws IncorrectCommandArgsException {
        String result = new String();
        char openSymbol;
        char closeSymbol;
        if (s.length() <= currentPoint + 1) {
            throw new IncorrectCommandArgsException();
        }
        openSymbol = s.charAt(currentPoint);
        if (openSymbol == '"') {
            closeSymbol = '"';
        } else {
            throw new IncorrectCommandArgsException();
        }
        currentPoint++;
        while (s.charAt(currentPoint) != closeSymbol) {
            result += s.charAt(currentPoint);
            currentPoint++;
            if (s.length() <= currentPoint) {
                throw new IncorrectCommandArgsException();
            }
        }
        return currentPoint;
    }

    private String backwardBetween(String s, int currentPoint) throws IncorrectCommandArgsException {
        String result = new String();
        char openSymbol;
        char closeSymbol;
        if (currentPoint - 1 < 0) {
            throw new IncorrectCommandArgsException();
        }
        openSymbol = s.charAt(currentPoint);
        if (openSymbol == '"') {
            closeSymbol = '"';
        } else {
            throw new IncorrectCommandArgsException();
        }
        currentPoint--;
        while (s.charAt(currentPoint) != closeSymbol) {
            result += s.charAt(currentPoint);
            currentPoint--;
            if (currentPoint < 0) {
                throw new IncorrectCommandArgsException();
            }
        }
        return this.reverse(result);
    }

    private String reverse(String s) {
        String result = new String();
        for (int i = s.length() - 1; i >= 0; i--) {
            result += s.charAt(i);
        }
        return result;
    }

    private int skip(String s, int i) {
        if (s.length() <= i) {
            if(s.length() == 0){
                return 0;
            }
            return s.length() - 1;
        } else {
            while (s.charAt(i) == ' ') {
                i++;
                if (s.length() <= i) {
                    return s.length() - 1;
                }
            }
            return i;
        }
    }

    private int backwardSkip(String s, int i) {
        if (s.length() <= i) {
            if(s.length() == 0){
                return 0;
            }
            return s.length();
        }
        if (i < 0) {
            return 0;
        }
        while (s.charAt(i) == ' ') {
            i--;
            if (i < 0) {
                return 0;
            }
        }
        return i;
    }
}
