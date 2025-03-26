package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.Arguments;
import shared.SocketConfig;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Main {
    private String FILE_REQUEST_PATH_TEST_ENVIRONMENT = System.getProperty("user.dir") + "/src/client/data/";
    private static String FILE_REQUEST_PATH_LOCAL_ENVIRONMENT = System.getProperty("user.dir") + "/JSON Database with Java/task/src/client/data/";

    public static void main(String[] args) {

        Arguments arguments = new Arguments();
        JCommander.newBuilder().addObject(arguments).build().parse(args);

        try (Socket socket = new Socket(InetAddress.getByName(SocketConfig.getAddress()), SocketConfig.getPort());
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
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
        if (args.isFileForRequestProvided()) {
            return loadRequestFromFile(args.getFilename());
        }

        return new Gson().toJson(args);
    }

    static String loadRequestFromFile(String filename) {
        File file = new File(getPathToRequestFile(filename));

        JsonElement json = new JsonObject();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
                json = JsonParser.parseReader(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return json.toString();
    }

    private static String getPathToRequestFile(String filename) {
        return FILE_REQUEST_PATH_LOCAL_ENVIRONMENT + filename;
    }
}
