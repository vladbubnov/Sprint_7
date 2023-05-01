package courier.logincourier;

import java.io.File;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import data.DataCourier;
import io.qameta.allure.Step;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;
import org.junit.runners.Parameterized;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Check login courier with invalid data")
@RunWith(Parameterized.class)
public class LoginCourierInvalidTest {
    private final String login;
    private final String password;
    private final Integer statusCode;
    private final String keyBody;
    private final String valueBody;

    public LoginCourierInvalidTest(String name, String login, String password, Integer statusCode,
                                   String keyBody, String valueBody) {
        this.login = login;
        this.password = password;
        this.statusCode = statusCode;
        this.keyBody = keyBody;
        this.valueBody = valueBody;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getData() {
        return new Object[][] {
                {"Test with invalid login", "bubaaaaaa1", "123", 404, "message", "Учетная запись не найдена"},
                {"Test with invalid password", "bubaaa1", "123456", 404, "message", "Учетная запись не найдена"},
                {"Test without login", "", "123456", 400, "message", "Недостаточно данных для входа"},
                {"Test without password", "bubaaa1", "", 400, "message", "Недостаточно данных для входа"},
        };
    }

    @Before
    public void setUp() {
        DataCourier dataCourier = new DataCourier("bubaaa1", "123", null);
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        createCourier(dataCourier);
    }

    @Test
    public void loginCourierInvalidTest() {
        DataCourier dataCourier = new DataCourier(login, password, null);
        Response response = sendPostRequestLoginCourier(dataCourier);
        compareStatusCodeAndBody(response, statusCode, keyBody, valueBody);
    }

    @After
    public void deleteUp() {
        deleteCourier();
    }

    @Step("Send POST request to /api/v1/courier for create courier")
    public void createCourier(DataCourier dataCourier) {
        given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .post("/api/v1/courier");
    }

    @Step("Send POST request to /api/v1/courier for login courier")
    public Response sendPostRequestLoginCourier(DataCourier dataCourier) {
        return given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .post("/api/v1/courier/login");
    }

    @Step("Compare status code and body")
    public void compareStatusCodeAndBody(Response response, Integer statusCode, String body, String message) {
        response.then().statusCode(statusCode).and().body(body, equalTo(message));
    }

    @Step("Get courier id")
    public Integer getCourierId() {
        File json = new File("src/test/resources/dataCourier.json");
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
    }

    @Step("Delete courier")
    public void deleteCourier() {
        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/" + getCourierId());
    }
}