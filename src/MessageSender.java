import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

class MessageSender {
    private BufferedWriter writer;

    MessageSender(Socket socket) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    void send(String msg) {

    }
}
