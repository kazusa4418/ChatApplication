import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataBase {
    private static Connection connection;

    private static Statement statement;

    static {
        //noinspection TryWithIdenticalCatches
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection =
                    DriverManager.getConnection("jdbc:mysql://localhost/CHAT_USERS", "root", "");

            statement = connection.createStatement();
        }
        catch (ClassNotFoundException err) {
            err.printStackTrace();
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
        assert connection != null: "connection is null";
        assert statement  != null: "statement is null";
    }

    static ClientConfiguration findUser(String id, String password) {
        final String ID_EXISTS = "SELECT COUNT(*) AS count FROM users WHERE user_id = '$id';".replace("$id", id);
        final String ID_SEARCH = "SELECT user_id, user_pw, user_name FROM users WHERE user_id = '$id';".replace("$id", id);

        try {
            ResultSet rowNum = statement.executeQuery(ID_EXISTS);

            rowNum.next();
            if (rowNum.getInt("count") == 0) {
                return null;
            }

            ResultSet result = statement.executeQuery(ID_SEARCH);

            result.next();
            String pw = result.getString("user_pw");
            String name = result.getString("user_name");

            if (!pw.equals(password)) {
                return null;
            }

            return new ClientConfiguration(id, name);
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }
}
