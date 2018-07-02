import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChatClient extends JFrame implements Runnable, ActionListener {
    //アプリケーション名
    private static final String APPNAME = "チャットクライアント";

    //接続先サーバーのホスト名
    private static final String HOST = "localhost";

    //接続先ポート番号
    private static final int PORT = 2815;

    //このアプリケーションのクライアントソケット
    private Socket socket;

    //メッセージ受信監視用スレッド
    private Thread thread;

    //現在入室中のチャットルーム名
    private String roomName;

    //以下、コンポーネント
    private JList<String> roomList;	//チャットルームのリスト
    private JList userList;	//現在入室中のチャットルームのユーザー
    private JTextArea msgTextArea;		//メッセージを表示するテキストエリア
    private JTextField msgTextField;	//メッセージ入力用の一行テキスト
    private JTextField nameTextField;	//ユーザー名やチャットルーム名を入力する一行テキスト
    private JButton submitButton;		//「送信」ボタン
    private JButton renameButton;		//「名前の変更」ボタン
    private JButton addRoomButton;		//「部屋を追加」ボタン
    private JButton enterRoomButton;	//「入室・退室」ボタン

    public ChatClient() {
        super(APPNAME);

        JPanel topPanel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        JPanel roomPanel = new JPanel();
        JPanel userPanel = new JPanel();

        roomList = new JList<>();
        userList = new JList();
        msgTextArea = new JTextArea();
        msgTextField = new JTextField();
        nameTextField = new JTextField();
        submitButton = new JButton("送信");
        renameButton = new JButton("名前の変更");
        addRoomButton = new JButton("部屋を追加");
        enterRoomButton = new JButton("入室");

        submitButton.addActionListener(this);
        submitButton.setActionCommand("submit");

        renameButton.addActionListener(this);
        renameButton.setActionCommand("rename");

        addRoomButton.addActionListener(this);
        addRoomButton.setActionCommand("addRoom");

        enterRoomButton.addActionListener(this);
        enterRoomButton.setActionCommand("enterRoom");

        roomPanel.setLayout(new BorderLayout());
        roomPanel.add(new JLabel("チャットルーム"), BorderLayout.NORTH);
        roomPanel.add(new JScrollPane(roomList), BorderLayout.CENTER);
        roomPanel.add(enterRoomButton, BorderLayout.SOUTH);

        userPanel.setLayout(new BorderLayout());
        userPanel.add(new JLabel("参加ユーザー"), BorderLayout.NORTH);
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("名前"));
        topPanel.add(nameTextField);
        topPanel.add(renameButton);
        topPanel.add(addRoomButton);

        nameTextField.setPreferredSize(new Dimension(200, nameTextField.getPreferredSize().height));

        leftPanel.setLayout(new GridLayout(2, 1));
        leftPanel.add(roomPanel);
        leftPanel.add(userPanel);

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(msgTextField, BorderLayout.CENTER);
        bottomPanel.add(submitButton, BorderLayout.EAST);

        //テキストエリアはメッセージを表示するだけなので編集不可に設定
        msgTextArea.setEditable(false);

        //コンポーネントの状態を退室状態で初期化
        exitedRoom();

        this.getContentPane().add(new JScrollPane(msgTextArea), BorderLayout.CENTER);
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try { close(); }
                catch(Exception err) { }
            }
        });
        connectServer();

        //メッセージ受信監視用のスレッドを生成してスタートさせる
        thread = new Thread(this);
        thread.start();

        //現在の部屋を取得する
        sendMessage("getRooms");
    }

    public static void launch() {
        ChatClient window = new ChatClient();
        window.setSize(800, 600);
        window.setVisible(true);
    }

    //サーバーに接続する
    public void connectServer() {
        try {
            socket = new Socket(HOST, PORT);
            msgTextArea.append(">サーバーに接続しました\n");
        }
        catch(Exception err) {
            msgTextArea.append("ERROR>" + err + "\n");
        }
    }

    //サーバーから切断する
    public void close() throws IOException {
        sendMessage("close");
        socket.close();
    }

    //メッセージをサーバーに送信する
    public void sendMessage(String msg) {
        try {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output);

            writer.println(msg);
            writer.flush();
        }
        catch(Exception err) {
            msgTextArea.append("ERROR>" + err + "\n");
        }
    }

    //サーバーから送られてきたメッセージの処理
    public void reachedMessage(String name, String value) {
        //チャットルームのリストに変更が加えられた
        if (name.equals("rooms")) {
            if (value.equals("")) {
                roomList.setModel(new DefaultListModel());
            }
            else {
                String[] rooms = value.split(" ");
                roomList.setListData(rooms);
            }
        }
        //ユーザーが入退室した
        else if (name.equals("users")) {
            if (value.equals("")) {
                userList.setModel(new DefaultListModel());
            }
            else {
                String[] users = value.split(" ");
                userList.setListData(users);
            }
        }
        //メッセージが送られてきた
        else if (name.equals("msg")) {
            msgTextArea.append(value + "\n");
        }
        //処理に成功した
        else if (name.equals("successful")) {
            if (value.equals("setName")) msgTextArea.append(">名前を変更しました\n");
        }
        //エラーが発生した
        else if (name.equals("error")) {
            msgTextArea.append("ERROR>" + value + "\n");
        }
    }

    //メッセージ監視用のスレッド
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            while(!socket.isClosed()) {
                String line = reader.readLine();

                String[] msg = line.split(" ", 2);
                String msgName = msg[0];
                String msgValue = (msg.length < 2 ? "" : msg[1]);

                reachedMessage(msgName, msgValue);
            }
        }
        catch(Exception err) { }
    }

    //ボタンが押されたときのイベント処理
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "submit":    //送信
                sendMessage("msg " + msgTextField.getText());
                msgTextField.setText("");
                break;
            case "rename":    //名前の変更
                sendMessage("setName " + nameTextField.getText());
                break;
            case "addRoom": {  //部屋を作成
                String roomName = nameTextField.getText();
                sendMessage("addRoom " + roomName);
                enteredRoom(roomName);
                sendMessage("getUsers " + roomName);
                break;
            }
            case "enterRoom": {  //入室
                String roomName = roomList.getSelectedValue();
                if (roomName != null) {
                    sendMessage("enterRoom " + roomName);
                    enteredRoom(roomName);
                }
                break;
            }
            case "exitRoom":    //退室
                sendMessage("exitRoom " + roomName);
                exitedRoom();
                break;
        }
    }

    //部屋に入室している状態のコンポーネント設定
    private void enteredRoom(String roomName) {
        this.roomName = roomName;
        setTitle(APPNAME + " " + roomName);

        msgTextField.setEnabled(true);
        submitButton.setEnabled(true);

        addRoomButton.setEnabled(false);
        enterRoomButton.setText("退室");
        enterRoomButton.setActionCommand("exitRoom");
    }

    //部屋に入室していない状態のコンポーネント設定
    private void exitedRoom() {
        roomName = null;
        setTitle(APPNAME);

        msgTextField.setEnabled(false);
        submitButton.setEnabled(false);

        addRoomButton.setEnabled(true);
        enterRoomButton.setText("入室");
        enterRoomButton.setActionCommand("enterRoom");
        userList.setModel(new DefaultListModel());
    }
}