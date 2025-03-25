package client;

import com.beust.jcommander.JCommander;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    public static void main(String[] args) {

        Arguments arguments = new Arguments();
        JCommander.newBuilder().addObject(arguments).build().parse(args);

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {
            System.out.println("Client started!");
            String prepareMessage = arguments.type + " " + arguments.index;

            if (!arguments.message.isEmpty()) {
                prepareMessage += " " + arguments.message;
            }

            output.writeUTF(prepareMessage);
            System.out.println("Sent: " + prepareMessage);

            String receivedMessage = input.readUTF();
            System.out.println("Received: " + receivedMessage);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
