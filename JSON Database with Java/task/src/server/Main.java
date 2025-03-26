package server;

import shared.SocketConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        CountDownLatch latch = new CountDownLatch(1);


        try (ServerSocket server = new ServerSocket(SocketConfig.getPort(), 50, InetAddress.getByName(SocketConfig.getAddress()))) {
            System.out.println("Server started!");

            while (latch.getCount() > 0) {
                Session session = new Session(server.accept(), lock, database, latch);
                session.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
