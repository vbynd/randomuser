package api.tests;

import api.responseStructure.Result;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserWithSpecifiedParamsTests extends BaseTest {
    // Сценарий - Получение случайно сгенерированного пользователя с указанным гендером
    @ParameterizedTest
    @ValueSource(strings = {"male", "female"})
    public void getRandomUserWithSpecifiedGender(String specifiedGender) {
        Result randomUser = given()
                .when()
                .params("gender", specifiedGender)
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        assertThat(randomUser.getGender()).isEqualTo(specifiedGender);
    }

    // Сценарий - Получение случайно сгенерированного пользователя с указанной национальностью
    @ParameterizedTest
    @ValueSource(strings = {
            "AU", "BR", "CA",
            "CH", "DE", "DK",
            "ES", "FI", "FR",
            "GB", "IE", "IN",
            "IR", "MX", "NL",
            "NO", "NZ", "RS",
            "TR", "UA", "US"})
    public void getRandomUserWithSpecifiedNationality(String specifiedNationality) {
        Result randomUser = given()
                .when()
                .params("nat", specifiedNationality)
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        assertThat(randomUser.getNat()).isEqualTo(specifiedNationality);
    }

    /*
     * Сценарий - Получение случайно сгенерированного пользователя с специфичным паролем
     * Комбинации сгенерированы с помощью техники попарного тестирования
     * */
    @Test
    public void getRandomUserWithSpecifiedPasswordAllCharacters() {
        Result randomUser = given()
                .when()
                .params("password", "upper,lower,special,number,1-10")
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^.{1,10}$")).isTrue();
    }

    @Test
    public void getRandomUserWithSpecifiedPasswordOnlyUpperCharacters() {
        Result randomUser = given()
                .when()
                .params("password", "upper")
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^[A-Z]*$")).isTrue();
    }

    @Test
    public void getRandomUserWithSpecifiedPasswordOnlySpecialCharacters() {
        Result randomUser = given()
                .when()
                .params("password", "special,10-15")
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^([[^a-zA-Z]&&[\\D]]){10,15}$")).isTrue();
    }

    @Test
    public void getRandomUserWithSpecifiedPasswordLowerCharactersAndNumbers() {
        Result randomUser = given()
                .when()
                .params("password", "lower,number")
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^([a-z]|\\d)*$")).isTrue();
    }

    // Сценарий - Получение случайно сгенерированного пользователя с несколькими указанными параметрами
    @Test
    public void getRandomUserWithSeveralParamsSpecified() {
        Result randomUser = given()
                .when()
                .param("gender", "male")
                .param("nat", "US")
                .param("password", "lower,number")
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(randomUser.getGender()).isEqualTo("male");
            softly.assertThat(randomUser.getNat()).isEqualTo("US");
            softly.assertThat(randomUser.getLogin().getPassword().matches("^([a-z]|\\d)*$")).isTrue();
        });
    }

    // Сценарий - Неверно указанное название параметра в запросе игнорируется
    @Test
    public void getRandomUserWithIncorrectlySpecifiedParamName() {
        Result randomUser = given()
                .when()
                .param("basd", "asd")
                .param("12gyy23", "adsd")
                .param("idhed", "male")
                .get()
                .then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(successfulFullResponseSchema)))
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);
    }

    // Сценарий - Неверно указанное возможное значение параметра в запросе игнорируется
    @Test
    public void getRandomUserWithIncorrectlySpecifiedParamValue() {
        String gender = "someGender";
        String nat = "someNat";

        Result randomUser = given()
                .when()
                .param("gender", gender)
                .param("nat", nat)
                .get()
                .then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(successfulFullResponseSchema)))
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        assertThat(nat).isNotEqualTo(randomUser.getNat());
        assertThat(gender).isNotEqualTo(randomUser.getGender());
    }
}
