import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.ApiSteps;

import static org.hamcrest.core.IsEqual.equalTo;
import io.qameta.allure.junit4.DisplayName;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create user with relevant data")
    public void createCourierSuccess() {
        Response response = ApiSteps.createNewCourier(PreparedData.LOGIN_ONE,
                PreparedData.PASSWORD_ONE, PreparedData.FIRST_NAME_ONE);

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Create user twice/duplicate")
    public void createCourierDuplicate() {

        // Create user
        ApiSteps.createNewCourier(PreparedData.LOGIN_TWO, PreparedData.PASSWORD_TWO, PreparedData.FIRST_NAME_TWO);

        // Duplicate user
        Response response = ApiSteps.createNewCourier(PreparedData.LOGIN_TWO, PreparedData.PASSWORD_TWO,
                PreparedData.FIRST_NAME_TWO);

        response.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("Create user without login and password")
    public void createCourierIncompleteData() {

        Response response = ApiSteps.createNewCourierNoSomeData(PreparedData.FIRST_NAME_ONE);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @After
    public void cleanUp() {
        int id = ApiSteps.getCourierIDbyLogin(PreparedData.LOGIN_ONE, PreparedData.PASSWORD_ONE);
        ApiSteps.deleteCourierByID(id);
    }

}
