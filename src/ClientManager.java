import java.util.ArrayList;
import java.util.List;

class ClientManager {
    /* =================== SINGLETON =================== */
    private static ClientManager instance = new ClientManager();

    static ClientManager getInstance() {
        return instance;
    }
    /* ================================================= */

    private List<ChatClient> clients = new ArrayList<>();

    private ClientManager() {
    }

    void addClient(ChatClient client) {
        clients.add(client);
    }
}
