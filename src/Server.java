import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ServerSocket serverSocket;
    private final ExecutorService fixedPool;
    private LinkedList<UserHandler> list;

    public Server(LinkedList<UserHandler> list) throws IOException {
        this.list = list;
        serverSocket = new ServerSocket(8083);
        fixedPool = Executors.newFixedThreadPool(2);
        start();
    }

    public void start() throws IOException {
        System.out.println("Waiting for a client connection");
        while (serverSocket.isBound()) {
            UserHandler userHandler = new UserHandler(serverSocket.accept(), list);
            fixedPool.submit(userHandler);
        }
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        LinkedList<UserHandler> list = new LinkedList<>();
        Server server = new Server(list);
    }

}
