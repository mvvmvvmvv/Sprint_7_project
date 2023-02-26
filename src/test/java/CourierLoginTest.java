import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.ApiSteps;
import org.example.LoginCourier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import io.qameta.allure.junit4.DisplayName;

public class CourierLoginTest {

    private String courier;
    private String login;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        ApiSteps.createNewCourier(PreparedData.LOGIN_ONE, PreparedData.PASSWORD_ONE, PreparedData.FIRST_NAME_ONE);
    }

    @Test
    @DisplayName("Log in with relevant data")
    public void loginCourierSuccess() {
        LoginCourier login = new LoginCourier(PreparedData.LOGIN_ONE, PreparedData.PASSWORD_ONE );
        Response response = ApiSteps.loginCourier(login);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Log in without password")
    public void loginCourierNoPassword() {
        LoginCourier login = new LoginCourier(PreparedData.LOGIN_ONE, "");
        Response response = ApiSteps.loginCourier(login);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Log in with non-existent user")
    public void loginCourierNonExistentUser() {
        LoginCourier login = new LoginCourier(PreparedData.LOGIN_NON_EXISTENT, PreparedData.PASSWORD_ONE);
        Response response = ApiSteps.loginCourier(login);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Log in with wrong password")
    public void loginCourierWrongPassword() {
        LoginCourier login = new LoginCourier(PreparedData.LOGIN_ONE, PreparedData.PASSWORD_NON_EXISTENT);
        Response response = ApiSteps.loginCourier(login);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @After
    public void cleanUp() {
        int id = ApiSteps.getCourierIDbyLogin(PreparedData.LOGIN_ONE, PreparedData.PASSWORD_ONE);
        ApiSteps.deleteCourierByID(id);
    }
}
