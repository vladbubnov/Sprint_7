package courier.createcourier;

import java.io.File;
import org.junit.Test;
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

@RunWith(Parameterized.class)
public class CreateCourierTest {
    private final String login;
    private final String password;
    private final String firstName;

    public CreateCourierTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getDataCourier() {
        return new Object[][] {
                {"bubaaa1", "123", "Ivan"}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check the creation of a courier with valid data")
    public void createCourierWithValidData() {
        DataCourier dataCourier = new DataCourier(login, password, firstName);
        Response response = sendPostRequestCreateCourier(dataCourier);
        compareStatusCodeAndBodyWithBoolean(response, 201, "ok", true);
        getCourierId();
        deleteCourier();
    }

    @Test
    @DisplayName("Check the creation of a courier without a login")
    public void createCourierWithOutLogin() {
        DataCourier dataCourier = new DataCourier(null, password, firstName);
        Response response = sendPostRequestCreateCourier(dataCourier);
        compareStatusCodeAndBodyWithString(response, 400, "message",
                "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check the creation of a courier without a password")
    public void createCourierWithOutPassword() {
        DataCourier dataCourier = new DataCourier(login, null, firstName);
        Response response = sendPostRequestCreateCourier(dataCourier);
        compareStatusCodeAndBodyWithString(response, 400, "message",
                "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check status code and message creating a courier without a firstName")
    public void createCourierWithOutFirstName() {
        DataCourier dataCourier = new DataCourier(login, password, null);
        Response response = sendPostRequestCreateCourier(dataCourier);
        compareStatusCodeAndBodyWithBoolean(response, 201, "ok", true);
        getCourierId();
        deleteCourier();
    }

    @Test
    @DisplayName("Check the creation of an exist courier")
    public void createExistsCourier() {
        DataCourier dataCourier = new DataCourier(login, password, firstName);
        Response firstResponse = sendPostRequestCreateCourier(dataCourier);
        Response secondResponse = sendPostRequestCreateCourier(dataCourier);
        compareStatusCodeAndBodyWithString(secondResponse, 409, "message",
                "Этот логин уже используется. Попробуйте другой.");
        getCourierId();
        deleteCourier();
    }

    @Step("Send POST request to /api/v1/courier for create courier")
    public Response sendPostRequestCreateCourier(DataCourier dataCourier) {
        return given()
                .header("Content-type", "application/json")
                .body(dataCourier).post("/api/v1/courier");
    }

    @Step("Compare status code and body 'String'")
    public void compareStatusCodeAndBodyWithString(Response response, int statusCode, String body, String message) {
        response.then().statusCode(statusCode).and().body(body, equalTo(message));
    }

    @Step("Compare status code and body 'Boolean'")
    public void compareStatusCodeAndBodyWithBoolean(Response response, int statusCode, String body, Boolean message) {
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