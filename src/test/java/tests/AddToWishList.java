package tests;

import api.Auth;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AddToWishList extends TestBase{

    @Test
    void addToWishListAnonymousTest() {
        Map<String, String> cookies = new Auth().getAnonymousCookies();

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));

        open("");
        $("a[href='/wishlist'] .wishlist-qty").shouldHave(text("(0)"));

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookies(cookies)
                .body("product_attribute_34_7_12=32&addtocart_34.EnteredQuantity=1")
        .when()
                .post("/addproducttocart/details/34/2")
        .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                .body("updatetopwishlistsectionhtml", equalTo("(1)"))
                .extract().response();

        open("");
        $("a[href='/wishlist'] .wishlist-qty").shouldHave(text("(1)"));
    }
}