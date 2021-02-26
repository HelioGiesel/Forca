import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener implements Runnable {

    private Socket socket;
    private BufferedReader read;
    private String lineRead = "";

    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try { read = new BufferedReader(new InputStreamReader(socket.getInputStream())); }
        catch (IOException e) { e.printStackTrace(); }

        while (true) {
            try { lineRead = read.readLine(); }
            catch (IOException e) { e.printStackTrace(); }
            System.err.println(lineRead);
        }

    }

}
