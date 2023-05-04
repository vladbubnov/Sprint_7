package functions;

import java.io.File;
import data.DataCourier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierFunctions {
    @Step("Send POST request to /api/v1/courier for create courier")
    public Response sendPostRequestCreateCourier(DataCourier dataCourier) {
        return given()
                .header("Content-type", "application/json")
                .body(dataCourier).post("/api/v1/courier");
    }

    @Step("Send POST request to /api/v1/courier for login courier")
    public Response sendPostRequestLoginCourier(DataCourier dataCourier) {
        return given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .post("/api/v1/courier/login");
    }

    @Step("Compare status code and body 'String'")
    public void compareStatusCodeAndBodyWithString(Response response, int statusCode, String body, String message) {
        response.then().statusCode(statusCode).and().body(body, equalTo(message));
    }

    @Step("Compare status code and body 'Boolean'")
    public void compareStatusCodeAndBodyWithBoolean(Response response, int statusCode, String body, Boolean successFlag) {
        response.then().statusCode(statusCode).and().body(body, equalTo(successFlag));
    }

    @Step("Compare status code and body 'Integer'")
    public void compareStatusCodeAndBodyWithInteger(Response response, Integer statusCode, String body, Integer message) {
        response.then().statusCode(statusCode).body(body, equalTo(message));
    }

    @Step("Send POST request to /api/v1/courier for create courier")
    public void createCourier(DataCourier dataCourier) {
        given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .post("/api/v1/courier");
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