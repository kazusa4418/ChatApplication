import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        ClientConfiguration conf = login();

        ChatClient client = new ChatClient(conf);
    }

    private static ClientConfiguration login() {
        ClientConfiguration conf;

        do {
            String id = inputId();
            String pw = inputPassWord();

            conf = UserDataBase.findUser(id, pw);
        }
        while (!checkConf(conf));

        assert conf != null;
        System.out.println(conf.getName() + "としてログインしました。");

        return conf;
    }

    private static String inputId() {
        Scanner scanner = new Scanner(System.in);
        String id;
        do {
            System.out.print("id > ");
            id = scanner.nextLine();
        }
        while (!Checker.idFormatCheck(id));

        return id;
    }

    private static String inputPassWord() {
        Scanner scanner = new Scanner(System.in);
        String password;
        do {
            System.out.print("password > ");
            password = scanner.nextLine();
        }
        while (!Checker.pwFormatCheck(password));
        return password;
    }

    private static boolean checkConf(ClientConfiguration conf) {
        if (conf == null) {
            System.err.println("id or password is not valid.");
            return false;
        }
        return true;
    }
}
