package api.Tests;

import api.responseStructure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

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
