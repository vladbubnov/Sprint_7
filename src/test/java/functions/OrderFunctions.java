package functions;

import data.DataCustomer;
import java.util.ArrayList;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderFunctions {
    @Step("Send POST request to /api/v1/orders for create order")
    public Response sendPostRequestCreateOrder(DataCustomer customer) {
        return given().header("Content-type", "application/json")
                .body(customer)
                .post("/api/v1/orders");
    }

    @Step("Send GET request to /api/v1/orders to get list orders")
    public Response sendGetRequestGetOrdersList() {
        return given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
    }

    @Step("Compare that response body contain track")
    public void compareContainTrack(Response response, Integer statusCode, String key) {
        response.then().statusCode(statusCode).and().body("$", hasKey(key));
    }

    @Step("Create list orders")
    public void createOrdersList(String firstName, String lastName, String address, String metroStation, String phone,
                                 Integer rentTime, String deliveryDate, String comment, ArrayList<String> color) {
        DataCustomer customer = new DataCustomer(firstName, lastName, address,
                metroStation, phone, rentTime, deliveryDate,
                comment, color);
        given().header("Content-type", "application/json")
                .body(customer)
                .post("/api/v1/orders");
    }

    @Step("Check get list orders")
    public void checkGetOrdersList(Response response, String body, Integer statusCode) {
        response.then().assertThat().body(body, notNullValue()).and().statusCode(statusCode);
    }
}