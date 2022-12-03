package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseHelper {
    private DataBaseHelper(){
    }
    private static QueryRunner queryRunner;
    private static Connection connection;

    @SneakyThrows
    public static void setup() {
        queryRunner = new QueryRunner();
        connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/app", "user", "pass");
    }

    @SneakyThrows
    public static String getStatus() {
        setup();
        String code = "SELECT status FROM payment_entity;";
        return queryRunner.query(connection, code, new ScalarHandler<>());
    }

    @SneakyThrows
    public static void cleanAll(){
        setup();
        queryRunner.update(connection, "DELETE FROM credit_request_entity;");
        queryRunner.update(connection, "DELETE FROM order_entity;");
        queryRunner.update(connection, "DELETE FROM payment_entity;");
    }
}
