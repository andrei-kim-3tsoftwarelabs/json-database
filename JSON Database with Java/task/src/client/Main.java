package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import shared.Arguments;
import shared.SocketConfig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        Arguments arguments = new Arguments();
        JCommander.newBuilder().addObject(arguments).build().parse(args);

        try (Socket socket = new Socket(InetAddress.getByName(SocketConfig.getAddress()), SocketConfig.getPort());
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {
            System.out.println("Client started!");
            String message = formatJsonForRequest(arguments);

            output.writeUTF(message);
            System.out.println("Sent: " + message);

            String receivedMessage = input.readUTF();
            System.out.println("Received: " + receivedMessage);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    static String formatJsonForRequest(Arguments args) {
        Gson gson = new Gson();
        return gson.toJson(args);
    }
}
