import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {
    private InputStreamReader inputStreamReader;
    private String fileContent;

    public String getFileContent() {
        return this.fileContent;
    }

    public boolean readFile(String filePath) {
        fileContent = "";
        try {
            this.inputStreamReader = new InputStreamReader(new FileInputStream(filePath));
            try {
                int i = -1;
                while ((i = inputStreamReader.read()) != -1) {
                    if (i != 13) {
                        fileContent += (char) i;
                    }
                }
                return true;
            } catch (IOException notAllowed) {
                System.out.println("You don't have access to file with path " + filePath);
                return false;
            }
        } catch (FileNotFoundException wrongPath) {
            System.out.println("You don't have access to file with path " + filePath);
            return false;
        }
    }
}