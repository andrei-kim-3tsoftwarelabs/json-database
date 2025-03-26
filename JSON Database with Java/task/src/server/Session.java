package server;

import com.google.gson.Gson;
import shared.Arguments;
import shared.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class Session extends Thread {
    private final Socket socket;
    private final Lock readLock;
    private final Lock writeLock;
    private final Database database;
    private final CountDownLatch latch;

    Session(Socket socket, ReadWriteLock lock, Database database, CountDownLatch latch) {
        this.socket = socket;
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
        this.latch = latch;
        this.database = database;
    }

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String receivedMessage = input.readUTF();
            System.out.println("Received: " + receivedMessage);

            Gson gson = new Gson();

            Arguments command = gson.fromJson(receivedMessage, Arguments.class);

            Response response;

            switch (command.getType()) {
                case "get": {
                    readLock.lock();
                    response = database.get(command.getKey());
                    readLock.unlock();
                    break;
                }
                case "set": {
                    writeLock.lock();
                    response = database.set(command.getKey(), command.getValue());
                    writeLock.unlock();
                    break;
                }
                case "delete": {
                    writeLock.lock();
                    response = database.delete(command.getKey());
                    writeLock.unlock();
                    break;
                }
                case "exit": {
                    latch.countDown();
                    System.exit(0);
                    response = new Response(Response.STATUS.OK);
                    break;
                }
                default: {
                    response = new Response(Response.STATUS.ERROR);
                }
            }

            output.writeUTF(gson.toJson(response));
            System.out.println("Sent: " + gson.toJson(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
