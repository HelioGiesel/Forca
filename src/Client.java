import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private final Socket socket;
    private final ExecutorService fixedPool;

    public Client() throws IOException {
        socket = new Socket("localhost", 8083);
        fixedPool = Executors.newFixedThreadPool(8);
        start();
    }

    public void start() {

        System.out.println("Client started: " + socket);
        System.out.println("Waiting for a server connection...");

        ServerListener serverListener = new ServerListener(socket);
        ServerWriter serverWriter = new ServerWriter(socket);

        fixedPool.submit(serverWriter);
        fixedPool.submit(serverListener);

    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }
}
