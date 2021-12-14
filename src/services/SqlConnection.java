package services;

import java.sql.*;

public class SqlConnection {

    public Connection setConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/finalgenealogy?characterEncoding=utf8", "root", "root");
        Statement statement = connection.createStatement();
        statement.execute ("USE finalgenealogy;");
        return connection;
    }
}
