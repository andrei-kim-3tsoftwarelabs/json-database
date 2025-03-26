package shared;

public class SocketConfig {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    public static String getAddress() {
        return address;
    }

    public static int getPort() {
        return port;
    }
}
