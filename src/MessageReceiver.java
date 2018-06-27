import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class MessageReceiver {
    private BufferedReader reader;

    MessageReceiver(Socket socket) {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }
}
