import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private final Socket socket;
    private final ExecutorService fixedPool;
    private String host;
    private int portNumber;

    public Client() throws IOException {
        setPortNumber();
        socket = new Socket(host, portNumber);
        fixedPool = Executors.newFixedThreadPool(8);
        start();
    }

    /**
     * Method start
     */
    public void start() {

        System.out.println("Client started: " + socket);
        System.out.println("Waiting for a server connection...");

        ServerListener serverListener = new ServerListener(socket);
        ServerWriter serverWriter = new ServerWriter(socket);

        fixedPool.submit(serverWriter);
        fixedPool.submit(serverListener);

    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPortNumber(){
        Scanner sysIn = new Scanner(System.in);
        do{
            System.out.println("========= Welcome to ForcaU =========");
            System.out.print("Host: ");
            host = sysIn.nextLine();
            System.out.print("PortNumber: ");
            portNumber = sysIn.nextInt();
        } while (portNumber == 0 && host == null);
    }
}
