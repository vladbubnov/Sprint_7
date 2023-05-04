package courier.logincourier;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import data.DataCourier;
import io.restassured.RestAssured;
import functions.CourierFunctions;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;

@DisplayName("Check login courier with valid data")
public class LoginCourierValidDataTest extends CourierFunctions {

    @Before
    public void setUp() {
        DataCourier dataCourier = new DataCourier("bubaaa1", "123", null);
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        createCourier(dataCourier);
    }

    @Test
    public void loginCourierValidTest() {
        DataCourier dataCourier = new DataCourier("bubaaa1", "123", null);
        Response response = sendPostRequestLoginCourier(dataCourier);
        compareStatusCodeAndBodyWithInteger(response, 200, "id", response.then().extract()
                .body().path("id"));
    }

    @After
    public void deleteUp() {
        deleteCourier();
    }
}