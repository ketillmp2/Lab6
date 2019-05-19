import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

//Вариант 10211

public class Server {

    public static void main(String args[]) {

        byte commandData[] = new byte[2789123];

        System.out.println("Welcome to the Server! Write port (from 1024 to 65535):");

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

        String savePath = "";
        if (args.length > 0) {
            savePath = args[0];
            System.out.println("Save path is " + savePath);
        } else {
            System.out.println("Warning! There is no save path!");
        }

        TreeSetBookCase treeSetBookCase = new TreeSetBookCase(savePath);

        for (int i = 1; i <= args.length - 1; i++) {
            System.out.println("Adding RecipeBooks from file " + args[i]);
            System.out.println(treeSetBookCase.doCommand(new Command("addBooksFromFile", new RecipeBook(args[i]))));
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println(treeSetBookCase.doCommand(new Command("save", new RecipeBook())));
                System.out.println("Program ended");
                return;
            } catch (Exception e) {
                if (args.length > 0) {
                    System.out.println(treeSetBookCase.doCommand(new Command("save", new RecipeBook())));
                    System.out.println("Program ended");
                }
            }
        }));

        SocketAddress address = new InetSocketAddress(port);
        try {
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.bind(address);
            ByteBuffer byteBuffer = ByteBuffer.wrap(commandData);
            byteBuffer.clear();

            while (true) {

                byteBuffer.clear();
                address = datagramChannel.receive(byteBuffer);
                new CommandThread(treeSetBookCase, address, commandData, datagramChannel).start();

            }

            } catch (IOException e) {
            System.out.println("Exception cached. Closing server...");
            System.out.println(treeSetBookCase.doCommand(new Command("save", new RecipeBook())));
        }
    }
}