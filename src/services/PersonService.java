package services;

import java.sql.*;

public class PersonService {

    public Connection setConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connect = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/familytree?characterEncoding=utf8", "root", "root");
        Statement statement = connect.createStatement();
        statement.execute ("use FamilyTree;");
        return connect;
    }
}
