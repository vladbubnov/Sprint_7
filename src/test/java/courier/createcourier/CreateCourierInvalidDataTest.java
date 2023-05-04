package courier.createcourier;

import org.junit.Test;
import org.junit.Before;
import data.DataCourier;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;
import functions.CourierFunctions;
import org.junit.runners.Parameterized;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;

@RunWith(Parameterized.class)
public class CreateCourierInvalidDataTest extends CourierFunctions {
    private final String login;
    private final String password;
    private final String firstName;

    public CreateCourierInvalidDataTest(String login, String password, String firstName) {
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
}