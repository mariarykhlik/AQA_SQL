package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;

public class SystemEntranceTest {

    @BeforeAll
    static void setUP() {
        DataHelper.cleanData();
        DataHelper.setDemoData();
        DataHelper.setUser();
    }

    @AfterAll
    static void ShutDown() {
        DataHelper.cleanData();
    }

    @BeforeEach
    void openWebService() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldLoginSuccess() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldBlockSystem() {
        var loginPage = new LoginPage();
        var invalidAuthInfo = DataHelper.getInvalidAuthInfo();
        loginPage.invalidLogin(invalidAuthInfo);
        loginPage.invalidLoginRepeat(invalidAuthInfo);
        loginPage.invalidLoginRepeat(invalidAuthInfo);
        loginPage.block();
    }
}
