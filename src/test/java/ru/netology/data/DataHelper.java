package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    static QueryRunner runner = new QueryRunner();
    static Faker faker = new Faker(new Locale("en"));
    static String userId = faker.idNumber().valid();
    static String userLogin = faker.name().firstName().toLowerCase(Locale.ROOT);
    static String userPassword = "qwerty123";
    static String invalidPassword = faker.internet().password();

    @SneakyThrows
    public static Connection connect() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void cleanData() {
        try (
                Connection conn = connect();
        ) {
            runner.update(conn, "DELETE FROM card_transactions;");
            runner.update(conn, "DELETE FROM cards;");
            runner.update(conn, "DELETE FROM auth_codes;");
            runner.update(conn, "DELETE FROM users;");
        }
    }

    @SneakyThrows
    public static void setDemoData() {
        String demoUserSQL = "INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        String demoCardSQL = "INSERT INTO cards(id, user_id, number, balance_in_kopecks) VALUES (?, ?, ?, ?);";
        try (
                Connection conn = connect();
        ) {
            runner.update(conn, demoUserSQL,
                    "42fe4c27-4c68-4a5c-9445-1da81d8afcd6",
                    "petya",
                    "$2a$10$O8dMKYQu85XBb7d7LZRzB.njf5CGMmoH4fgGB5cpSr7v/iyEauPjW");
            runner.update(conn, demoUserSQL,
                    "c813190c-4fc4-4423-86ee-592776857adb",
                    "vasya",
                    "$2a$10$zQo0Idf9OWJXuMGxRsx/bON4NeQgBMOFFvIdKO/Oqa5NCD601eveu");
            runner.update(conn, demoCardSQL,
                    "0f3f5c2a-249e-4c3d-8287-09f7a039391d",
                    "c813190c-4fc4-4423-86ee-592776857adb",
                    "5559 0000 0000 0002",
                    "1000000");
            runner.update(conn, demoCardSQL,
                    "92df3f1c-a033-48e6-8390-206f6b1f56c0",
                    "c813190c-4fc4-4423-86ee-592776857adb",
                    "5559 0000 0000 0001",
                    "1000000");
        }
    }


    @SneakyThrows
    public static void setUser() {
        String userSQL = "INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        try (
                Connection conn = connect();
        ) {
            runner.update(conn, userSQL,
                    userId,
                    userLogin,
                    "$2a$10$zQo0Idf9OWJXuMGxRsx/bON4NeQgBMOFFvIdKO/Oqa5NCD601eveu");
        }
    }
    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo(userLogin, userPassword);
    }


    public static AuthInfo getInvalidAuthInfo() {
        return new AuthInfo(userLogin, invalidPassword);
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @SneakyThrows
    public static String getUserId() {
        String idSQL = "SELECT id FROM users WHERE login = ?;";
        try (
                Connection conn = connect();
        ) {
            return runner.query(conn, idSQL, userLogin, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getUserCode() {
        String codeSQL = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1;";
        try (
                Connection conn = connect();
        ) {
            Thread.sleep(1000);
            return runner.query(conn, codeSQL, getUserId(), new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode(getUserCode());
    }
}