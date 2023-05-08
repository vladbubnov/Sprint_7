package courier.createcourier;

import org.junit.Test;
import org.junit.After;
import data.DataCourier;
import org.junit.Before;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;
import functions.CourierFunctions;
import org.junit.runners.Parameterized;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;

@RunWith(Parameterized.class)
public class CreateCourierValidDataTest extends CourierFunctions {
    private final String login;
    private final String password;
    private final String firstName;

    public CreateCourierValidDataTest(String login, String password, String firstName) {
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
    }

    @Test
    @DisplayName("Check status code and message creating a courier without a firstName")
    public void createCourierWithOutFirstName() {
        DataCourier dataCourier = new DataCourier(login, password, null);
        Response response = sendPostRequestCreateCourier(dataCourier);
        compareStatusCodeAndBodyWithBoolean(response, 201, "ok", true);
    }

    @Test
    @DisplayName("Check the creation of an exist courier")
    public void createExistsCourier() {
        DataCourier dataCourier = new DataCourier(login, password, firstName);
        Response firstResponse = sendPostRequestCreateCourier(dataCourier);
        Response secondResponse = sendPostRequestCreateCourier(dataCourier);
        compareStatusCodeAndBodyWithString(secondResponse, 409, "message",
                "Этот логин уже используется. Попробуйте другой.");
    }

    @After
    public void deleteCouriers() {
        deleteCourier();
    }
}