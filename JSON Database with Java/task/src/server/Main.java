package server;

import com.google.gson.Gson;
import shared.Arguments;
import shared.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

                    Gson gson = new Gson();

                    Arguments command = gson.fromJson(receivedMessage, Arguments.class);

                    Response response;

                    switch (command.getType()) {
                        case "get": {
                            response = database.get(command.getKey());
                            break;
                        }
                        case "set": {
                            response = database.set(command.getKey(), command.getValue());
                            break;
                        }
                        case "delete": {
                            response = database.delete(command.getKey());
                            break;
                        }
                        case "exit": {
                            response = new Response(Response.STATUS.OK);
                            isRunning = false;
                            break;
                        }
                        default: {
                            response = new Response(Response.STATUS.ERROR);
                        }
                    }

                    output.writeUTF(gson.toJson(response));
                    System.out.println("Sent: " + gson.toJson(response));
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
