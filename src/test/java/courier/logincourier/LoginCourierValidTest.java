package courier.logincourier;

import java.io.File;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import data.DataCourier;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Check login courier with valid data")
public class LoginCourierValidTest {

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
        compareStatusCodeAndBody(response, 200, "id", response.then().extract().body().path("id"));
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
    public void compareStatusCodeAndBody(Response response, Integer statusCode, String body, Integer message) {
        response.then().statusCode(statusCode).body(body, equalTo(message));
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