package ru.netology.page;

import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();
        return new VerificationPage();
    }
    public LoginPage invalidLogin(DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));
        return null;
    }

    public LoginPage invalidLoginRepeat(DataHelper.AuthInfo info) {
        $(".notification__content").shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=login] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=password] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        invalidLogin(info);
        return null;
    }

    public LoginPage block() {
        $(".notification__content").shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=login] input").shouldNotBe(enabled);
        $("[data-test-id=password] input").shouldNotBe(enabled);
        $("[data-test-id=action-login]").shouldNotBe(enabled);
        return null;
    }
}