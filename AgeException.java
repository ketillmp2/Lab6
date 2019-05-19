public class AgeException extends IllegalArgumentException{
    @Override
    public String getMessage() {
        return("You set wrong number for creature's age!");
    }
}