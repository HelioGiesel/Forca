import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    //Fields
    private ServerSocket serverSocket;
    private final ExecutorService fixedPool;
    private int portNumber;
    private LinkedList<UserHandler> list;

    //Constructor
    public Server(LinkedList<UserHandler> list) throws IOException {
        this.list = list;
        serverSocket = new ServerSocket(setPortNumber());
        fixedPool = Executors.newFixedThreadPool(2);
        start();
    }

    /**
     * Method start
     */
    public void start() {
        while (serverSocket.isBound()) {
            try {
                System.out.println("Waiting for a client connection on PortNumber " + portNumber);
                UserHandler userHandler = new UserHandler(serverSocket.accept(), list);
                fixedPool.submit(userHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cleanUp(serverSocket);
    }

    public static void main(String[] args) {
        try {
            LinkedList<UserHandler> list = new LinkedList<>();
            Server server = new Server(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp(Closeable closeable){
        try{
            if(closeable != null){
                closeable.close();
            }
        } catch (IOException closeERROR){}
    }

    public int setPortNumber(){
        Scanner sysIn = new Scanner(System.in);
        portNumber = 0;
        do{
            System.out.print("Welcome ");
            portNumber = sysIn.nextInt();
        } while (portNumber == 0);
        return portNumber;
    }

}
