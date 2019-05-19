import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class CommandThread extends Thread {

    public SocketAddress address;
    public TreeSetBookCase treeSetBookCase;
    public byte[] commandData;
    public DatagramChannel datagramChannel;

    public CommandThread(TreeSetBookCase treeSetBookCase, SocketAddress address, byte[] commandData, DatagramChannel datagramChannel) {
        this.treeSetBookCase = treeSetBookCase;
        this.address = address;
        this.commandData = commandData;
        this.datagramChannel = datagramChannel;
    }

    public void run() {
        byte messageData[] = new byte[2789123];
        String username = "new User";
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(commandData));
            Command command = (Command) objectInputStream.readObject();
            String message = treeSetBookCase.doCommand(command);
            if (command.getCommand().compareToIgnoreCase("checkConnection") == 0) {
                System.out.println(username + " has joined the server.");
            }
            if (command.getCommand().compareToIgnoreCase("exit") == 0){
                System.out.println(username + " has disconnected.");
            }
            if (command.getCommand().compareToIgnoreCase("save") == 0){
                System.out.println("TreeSet saved.");
            }
            ByteBuffer byteMessage = ByteBuffer.wrap(messageData);
            byteMessage.clear();
            byteMessage.put(message.getBytes());
            byteMessage.flip();
            datagramChannel.send(byteMessage, address);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Exception cached in command processing.");
        }
    }
}
