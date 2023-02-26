package org.example;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    public static Response createNewCourier(String login, String password, String firstName) {
        Courier courier = new Courier(login, password, firstName);
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    public static Response createNewCourierNoSomeData(String firstName) {
        Courier courier = new Courier(firstName);
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    public static Response loginCourier(LoginCourier login) {
        return given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/api/v1/courier/login");
    }

    public static int getCourierIDbyLogin(String login, String password) {
        LoginCourier auth = new LoginCourier(login, password);
        return loginCourier(auth).then().extract().path("id");
    }

    public static void deleteCourierByID(int id) {
        given()
                .header("Content-type", "application/json")
                .body(id)
                .when()
                .post("/api/v1/courier/:id");
    }
}
