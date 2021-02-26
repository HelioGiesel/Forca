import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerWriter implements Runnable {

    private Socket socket;
    private DataOutputStream write;
    private Scanner input;

    public ServerWriter(Socket socket) {
        this.socket = socket;
        input = new Scanner(System.in);
    }

    @Override
    public void run() {
        try {
            write = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*System.out.println("Enter your username: ");
        String userName = name.nextLine();*/

        while (true) {

            String sentence = input.nextLine();

            try {
                write.writeBytes(sentence + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                write.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
