package org.example;

import org.junit.ClassRule;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestcontainersTestcode {
    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:11.1");

    private Connection create() throws Exception {
        String url = postgres.getJdbcUrl();
        String user = postgres.getUsername();
        String password = postgres.getPassword();
        return DriverManager.getConnection(url, user, password);
    }

    protected void withConnection(ConnectionConsumer c) throws Exception {
        Connection connection = create();
        setup(connection);
        c.executeWith(connection);
    }

    private void setup(Connection c) throws SQLException {
        String dropStmt = "DROP TABLE IF EXISTS authors";
        String createStmt =
                "CREATE TABLE authors (id SERIAL, name varchar(255))";
        try (PreparedStatement drop = c.prepareStatement(dropStmt);
             PreparedStatement create = c.prepareStatement(createStmt)) {
            drop.execute();
            create.execute();
        }
    }

    interface ConnectionConsumer {
        void executeWith(Connection connection) throws Exception;
    }
}
