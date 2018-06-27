import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerAccessManager {
    private static ClientManager manager = ClientManager.getInstance();

    static Socket access(ChatClient client) {
        //noinspection TryWithIdenticalCatches
        try {
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 8080);

            manager.addClient(client);

            return socket;
        }
        catch (UnknownHostException err) {
            throw new AssertionError("UnknownHostException");
        }
        catch (IOException err) {
            throw new AssertionError("IOException");
        }
    }
}
