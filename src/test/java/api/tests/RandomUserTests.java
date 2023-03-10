package api.tests;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class RandomUserTests extends BaseTest {

    // Сценарий - Получение случайно сгенерированного пользователя без указания дополнительных параметров
    @Test
    public void successfulGetOneRandomUser() {
        JsonPath response = given()
                .when()
                .get()
                .then()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(successfulFullResponseSchema)))
                .assertThat().statusCode(200)
                .extract().body().jsonPath();
    }
}
