import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class Chain {

    private SocketAddress socketAddress;
    private boolean closeFlag = false;

    public Chain(SocketAddress socketAddress){
        this.socketAddress = socketAddress;
    }

    public void setCloseFlag(boolean closeFlag) {
        this.closeFlag = closeFlag;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public boolean isCloseFlag() {
        return closeFlag;
    }

    public boolean checkConnection() throws IOException{
        try {
            System.out.println("Trying to join the server...");
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(1500);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(new Command("checkConnection", null));
            objectOutputStream.flush();
            byte[] commandData = byteArrayOutputStream.toByteArray();

            for(int attemptNum = 1; attemptNum <= 5; attemptNum++){
                System.out.println("Attempt number " + attemptNum + "...");
                DatagramPacket datagramPacket = new DatagramPacket(commandData, commandData.length, this.socketAddress);
                datagramSocket.send(datagramPacket);
                byte[] messageData = new byte[123123];
                try{
                    datagramSocket.receive(new DatagramPacket(messageData, messageData.length));
                    System.out.println("Connection completed.\n");
                    return true;
                } catch (IOException e){
                    if(attemptNum >= 5){
                        System.out.println("Connection failed. Try again later.");
                        closeFlag = true;
                        throw e;
                    }
                }
            }
        } catch (IOException e){
            throw e;
        }
        return false;
    }

    public String sendCommand(Command command) throws IOException {
        try {

            if(this.closeFlag){
                return "";
            }

            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(1500);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(command);
            objectOutputStream.flush();
            byte[] commandData = byteArrayOutputStream.toByteArray();
            DatagramPacket datagramPacket = new DatagramPacket(commandData, commandData.length, this.socketAddress);
            try{
                datagramSocket.send(datagramPacket);
            } catch (IOException e){
                System.out.println("Connection lost");
                if(!this.checkConnection()){
                    throw e;
                }
            }
            byte[] messageData = new byte[123123];
            try{
                datagramSocket.receive(new DatagramPacket(messageData, messageData.length));
            } catch (IOException e){
                System.out.println("Connection lost");
                if(!this.checkConnection()){
                    throw e;
                }
            }
            String message = "";
            for (int j = 0; j < messageData.length; j++) {
                if (messageData[j] != 0) {
                    message += (char) messageData[j];
                }
            }

            if(command.getCommand().compareToIgnoreCase("exit") == 0){
                this.setCloseFlag(true);
            }


            return message;
        } catch (IOException e){
            throw e;
        }
    }
}
