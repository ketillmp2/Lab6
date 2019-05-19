import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

public class Client {
    public static void main(String args[]) {

        System.out.println("Welcome to TreeBookCase. Write port (from 1024 to 65535):");
        Scanner scanner = new Scanner(System.in);
        int port = 0;

        while(port < 1024 || port > 65535) {
            port = 0;
            String portString  = scanner.nextLine();
            for (int i = 0; i < portString.length(); i++) {
                if (portString.charAt(i) != ' ') {
                    if ((int) portString.charAt(i) >= (int) '0' && (int) portString.charAt(i) <= (int) '9') {
                        port = port*10;
                        port += (int) portString.charAt(i) - (int) '0';
                        if(port > 65535){
                            break;
                        }
                    }else{
                        break;
                    }
                }else{
                    if (port != 0){
                        break;
                    }
                }
            }
            if(port < 1024 || port > 65535){
                System.out.println("Incorrect port. Please try again...");
            }
        }

        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
        Chain chain = new Chain(socketAddress);
        MyParser parser = new MyParser();
        String savePath1 = "";

        try {
            chain.checkConnection();
        }catch (IOException e) {
            System.out.println("Closing client...");
            System.out.println("Program ended");
            return;
        }

        System.out.println("We are going to add RecipeBooks from server and from files with paths from arguments.");

        try {
            savePath1 = chain.sendCommand(new Command("TakeSavePath", null));
            System.out.println("Save path on server is: " + savePath1);
        } catch (IOException e) {
            System.out.println("Closing client...");
            System.out.println("Program ended");
            return;
        }

        final String savePath = savePath1;

        for (int i = 0; i <= args.length - 1; i++) {
            System.out.println("Adding RecipeBooks from file " + args[i]);
            try {
                System.out.println(chain.sendCommand(new Command("addBooksFromFile", new RecipeBook(args[i]))));
            } catch (IOException e) {
                System.out.println("Closing client...");
                System.out.println("Program ended");
                return;
            }
        }

        System.out.println("To get more information about commands print help");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println(chain.sendCommand(parser.doFirstCommand("save " + savePath)));
                chain.sendCommand(parser.doFirstCommand("exit"));
                System.out.println("Closing client...");
                System.out.println("Program ended");
                return;
            } catch (Exception e) {
                try {
                    if (args.length > 0) {
                        System.out.println(chain.sendCommand(parser.doFirstCommand("save " + savePath)));
                    }
                } catch (IOException q) {
                    System.out.println("Closing client...");
                    System.out.println("Program ended");
                }
            }
        }));

        while (!chain.isCloseFlag()) {
            System.out.println(" ");
            System.out.println("Print command: ");
            try {
                String content = scanner.nextLine();
                System.out.println(" ");
                String command = "";
                int size = 0;
                if (content.length() == 0) {
                    continue;
                }
                while (content.charAt(0) == ' ') {
                    if (content.length() <= 1) {
                        break;
                    }
                    content = content.substring(1, content.length());
                    if (content.length() <= 1) {
                        break;
                    }
                }
                if (content.length() > 0) {
                    if (content.charAt(0) == ' ') {
                        continue;
                    }
                    if (parser.isSimpleCommand(content.split(" ")[0])) {
                        try {
                            System.out.println(chain.sendCommand(parser.doFirstCommand(content)));
                        } catch (IOException e) {
                            System.out.println("Closing client...");
                            System.out.println("Program ended");
                            return;
                        }
                        continue;
                    } else {
                        if (content.split(" ")[0].compareTo("save") == 0) {
                            try {
                                System.out.println(chain.sendCommand(parser.doFirstCommand(content)));
                            } catch (IOException e) {
                                System.out.println("Closing client...");
                                System.out.println("Program ended");
                                return;
                            }
                            continue;
                        } else {
                            if (content.split(" ")[0].compareTo("add") == 0 || content.split(" ")[0].compareTo("remove") == 0 ||
                                    content.split(" ")[0].compareTo("add_if_min") == 0 || content.split(" ")[0].compareTo("add_if_max") == 0 ||
                                    content.split(" ")[0].compareTo("remove_lover") == 0) {
                                command += content + "\n";
                                int beacketCounter = 0;
                                if (content.contains("{")) {
                                    beacketCounter++;
                                }
                                if (content.contains("}")) {
                                    beacketCounter--;
                                }
                                while (beacketCounter > 0) {
                                    size++;
                                    content = scanner.nextLine();
                                    if (content.contains("{")) {
                                        beacketCounter++;
                                    }
                                    if (content.contains("}")) {
                                        beacketCounter--;
                                    }
                                    command += content + "\n";
                                    if (size >= 1000) {
                                        System.out.println("Command is too large");
                                        break;
                                    }
                                }
                                try {
                                    System.out.println(chain.sendCommand(parser.doFirstCommand(command)));
                                } catch (IOException e) {
                                    System.out.println("Closing client...");
                                    System.out.println("Program ended");
                                    return;
                                }
                            } else {
                                try {
                                    System.out.println(chain.sendCommand(parser.doFirstCommand(content)));
                                } catch (IOException e) {
                                    System.out.println("Closing client...");
                                    System.out.println("Program ended");
                                    return;
                                }
                            }
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                try{
                    chain.sendCommand(parser.doFirstCommand("exit"));
                    chain.setCloseFlag(true);
                } catch (IOException q){
                }
                return;
            }
        }
        try{
            chain.setCloseFlag(false);
            System.out.println(chain.sendCommand(parser.doFirstCommand("save " + savePath)));
            chain.setCloseFlag(true);
        } catch (IOException e){
        }
    }
}
