import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.AuthenticationType.BASIC;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.CHROME;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.openqa.selenium.By.tagName;

public class ExampleTest {

    @BeforeAll
    static void setup() {
        baseUrl = "https://the-internet.herokuapp.com";
        browser = CHROME;
        remote = "http://127.0.0.1:4444/wd/hub";
    }

    @Test
    void canEditIFrameText() {
        var inputText = randomAlphabetic(200);
        open("/");
        $$(".columns ul > li a").find(matchText("WYSIWYG")).scrollTo().click();
        switchTo().frame("mce_0_ifr");
        var editor = $(".mce-content-body");
        editor.clear();
        editor.val(inputText);
        editor.shouldHave(exactText(inputText));
    }

    @Test
    void canPassBasicAuth() {
        open("/basic_auth", BASIC, "admin", "admin");
        $(tagName("p")).shouldHave(exactText("Congratulations! You must have the proper credentials."));
    }

    @Test
    void canPassForgotPassword() {
        open("/forgot_password");
        $("#email").val(randomAlphabetic(5) + "@icloude.com");
        $("#form_submit").click();
        $("#content").shouldHave(exactText("Your e-mail's been sent!"));
    }

    @Test
    void canLoginWithCredentials() {
        open("/login");
        $("#username").val("tomsmith");
        $("#password").val("SuperSecretPassword!");
        $(".radius").click();
        $(".flash").shouldHave(matchesText("You logged into a secure area!"));
    }

    @Test
    void canAddNewElement() {
        open("/add_remove_elements/");
        $("[onclick*='addElement']").click();
        $(".added-manually").shouldBe(visible);
    }

    @Test
    void canRemoveAddedElement() {
        open("/add_remove_elements/");
        $("[onclick*='addElement']").click();
        $(".added-manually").click();
        $(".added-manually").shouldBe(disappear);
    }
}
