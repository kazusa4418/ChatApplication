import java.util.EventObject;

class MessageEvent extends EventObject {
    private ChatClientUser user;
    private String name;
    private String value;

    public MessageEvent(ChatClientUser user, String name, String value) {
        super(user);
        this.user = user;
        this.name = name;
        this.value = value;
    }

    //イベントを発生させたユーザー
    public ChatClientUser getUser() { return user; }

    //このイベントのコマンド名を返す
    public String getName() { return this.name; }

    //このイベントの
    public String getValue() { return this.value; }
}
