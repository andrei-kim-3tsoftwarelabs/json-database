package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static String address = "127.0.0.1";
    private static int port = 23456;
    private ServerSocket socket;

    public static void main(String[] args) {
        Database database = new Database();

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            boolean isRunning = true;
            System.out.println("Server started!");

            while (isRunning) {
                Socket socket = server.accept();
                try (DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                ) {
                    String receivedMessage = input.readUTF();
                    System.out.println("Received: " + receivedMessage);

                    String[] inputCommands = formatInput(receivedMessage);

                    String response = "";

                    switch (inputCommands[0]) {
                        case "get": {
                            response = database.get(Integer.parseInt(inputCommands[1]));
                            break;
                        }
                        case "set": {
                            response = database.set(Integer.parseInt(inputCommands[1]), inputCommands[2]);
                            break;
                        }
                        case "delete": {
                            response = database.delete(Integer.parseInt(inputCommands[1]));
                            break;
                        }
                        case "exit": {
                            response = "OK";
                            isRunning = false;
                            break;
                        }
                        default: {
                            response = "ERROR";
                        }
                    }

                    output.writeUTF(response);
                    System.out.println("Sent: " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String[] formatInput(String input) {
        String type = "";
        String index = "";
        String message = "";

        String[] splitArgs = input.split(" ");

        type = splitArgs[0];

        if (type.equals("exit")) {
            return new String[]{type};
        }

        index = splitArgs[1];

        if (type.equals("set")) {
            int indexPlace = input.indexOf(index);
            message = input.substring(indexPlace + index.length(), input.length()).trim();
            return new String[]{type, index, message};
        }

        return new String[]{type, index};
    }
}
