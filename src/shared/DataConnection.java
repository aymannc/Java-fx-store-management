package shared;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConnection {
    private static DataConnection connectionSingle = null;
    private Connection connection;

    private DataConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/bdd_magasin";
            connection = DriverManager.getConnection(url, "root", "");
        } catch (Exception exp) {
            System.out.println("Can't connect to the server");
            System.exit(404);
            exp.printStackTrace();
        }

    }

    public static DataConnection getDataConnection() {
        if (connectionSingle == null) {
            connectionSingle = new DataConnection();
        }
        return connectionSingle;
    }

    public Connection getConnection() {
        return connection;
    }
}

