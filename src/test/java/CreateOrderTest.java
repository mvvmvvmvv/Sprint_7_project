import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.NewOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import io.qameta.allure.junit4.DisplayName;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation,
                           String phone, int rentTime, String deliveryDate, String comment,
                           String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address =address;
        this.metroStation = metroStation;
        this.phone = phone;
        this. rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color =color;
    }

    @Parameterized.Parameters
    public static Object[][] getDataForm() {
        String[] oneColor = new String[] {"BLACK"};
        String[] twoColors = new String[] {"BLACK", "GREY"};
        String[] noColor = new String[] {};

        return new Object[][] {
                {"Фёдор", "Достоевский", "Улица Ленина", "5", "2128506", 7, "2023-03-12", "Бес", oneColor},
                {"Фёдор", "Конюхов", "Улица Достоевского", "3", "+72128506", 2, "2023-02-23", "Плавали-знаем", twoColors},
                {"Vladimir", "Lenin", "Krasnay Ploschad, 1", "3", "+7777", 1, "2023-02-23", "", noColor},
        };
    }

    @Test
    @DisplayName("Create orders")
    public void createOrderOneColorSuccess() {
        NewOrder newOrder = new NewOrder(firstName, lastName, address, metroStation, phone, rentTime,
                deliveryDate, comment, color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(newOrder)
                        .when()
                        .post("/api/v1/orders");

        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}
