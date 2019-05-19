import java.io.Serializable;

public class Command implements Serializable {
    private RecipeBook argument;
    private String command;

    public Command(String command, RecipeBook argument){
        this.argument = argument;
        this.command = command;
    }

    public RecipeBook getArgument() {
        return this.argument;
    }

    public String getCommand() {
        return this.command;
    }
}
