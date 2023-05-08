package order;

import org.junit.Test;
import java.util.List;
import org.junit.Before;
import java.util.ArrayList;
import functions.OrderFunctions;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;
import org.junit.runners.Parameterized;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;

@DisplayName("Check get list orders")
@RunWith(Parameterized.class)
public class GetListOrdersTest extends OrderFunctions {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final Integer rentTime;
    private final String deliveryDate;
    private final String comment;
    private final ArrayList<String> color;

    public GetListOrdersTest(String firstName, String lastName, String address, String metroStation, String phone,
                           Integer rentTime, String deliveryDate, String comment, ArrayList<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getDataCustomer() {
        return new Object[][] {
                {"Ivan", "Austin", "Lenina str. 21", "2", "+7 999 123 45 67", 3, "2023-01-01", "kvartira 21",
                        new ArrayList<>(List.of("GREY"))},
                {"Vadim", "Chicago", "Lenina str. 22", "3", "+7 999 123 45 68", 3, "2023-02-02", "kvartira 22",
                        new ArrayList<>(List.of("GREY"))},
                {"Denis", "Brooklyn", "Lenina str. 23", "4", "+7 999 123 45 69", 3, "2023-03-03", "kvartira 23",
                        new ArrayList<>(List.of("GREY", "BLACK"))},
                {"Nikolay", "Vegas", "Lenina str. 24", "5", "+7 999 123 45 70", 3, "2023-04-04", "kvartira 24",
                        null},
                {"Anatoliy", "Texas", "Lenina str. 25", "6", "+7 999 123 45 71", 4, "2023-04-05", "kvartira 25",
                        new ArrayList<>(List.of("BLACK"))},
                {"Valeriy", "Portland", "Lenina str. 26", "7", "+7 999 123 45 72", 4, "2023-04-06", "kvartira 26",
                        null}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void getListOrdersTest() {
        createOrdersList(firstName, lastName, address,
                metroStation, phone, rentTime, deliveryDate,
                comment, color);
        Response response = sendGetRequestGetOrdersList();
        checkGetOrdersList(response, "orders", 200);
    }
}