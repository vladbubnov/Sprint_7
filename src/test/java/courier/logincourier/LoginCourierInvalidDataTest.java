package courier.logincourier;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import data.DataCourier;
import org.junit.runner.RunWith;
import functions.CourierFunctions;
import io.restassured.RestAssured;
import org.junit.runners.Parameterized;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;

@DisplayName("Check login courier with invalid data")
@RunWith(Parameterized.class)
public class LoginCourierInvalidDataTest extends CourierFunctions {
    private final String login;
    private final String password;
    private final Integer statusCode;
    private final String keyBody;
    private final String valueBody;

    public LoginCourierInvalidDataTest(String login, String password, Integer statusCode,
                                       String keyBody, String valueBody) {
        this.login = login;
        this.password = password;
        this.statusCode = statusCode;
        this.keyBody = keyBody;
        this.valueBody = valueBody;
    }

    @Parameterized.Parameters(name = "Test check login with {0} and {1}")
    public static Object[][] getData() {
        return new Object[][] {
                {"bubaaaaaa1", "123", 404, "message", "Учетная запись не найдена"},
                {"bubaaa1", "123456", 404, "message", "Учетная запись не найдена"},
                {"", "123456", 400, "message", "Недостаточно данных для входа"},
                {"bubaaa1", "", 400, "message", "Недостаточно данных для входа"},
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
        compareStatusCodeAndBodyWithString(response, statusCode, keyBody, valueBody);
    }

    @After
    public void deleteUp() {
        deleteCourier();
    }
}