import java.net.Socket;

class ChatClient {
    private String id;

    private String name;

    private Socket socket;

    private MessageSender sender;

    private MessageReceiver receiver;

    private Destination dest;

    ChatClient(ClientConfiguration conf) {
        id = conf.getId();
        name = conf.getName();

        socket = ServerAccessManager.access(this);

        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
    }

    void setSocket(Socket socket) {
        this.socket = socket;
    }

    void sendMessage(String msg) {
        sender.send(msg);
    }
}
