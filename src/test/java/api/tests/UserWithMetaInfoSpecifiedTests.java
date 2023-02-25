package api.tests;

import api.responseStructure.Info;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserWithMetaInfoSpecifiedTests extends BaseTest {
    // Сценарий - Получение случайно сгенерированного пользователя старой версией API
    @Test
    public void getRandomUserWithOldAPIVersion() {
        Info info = given()
                .when()
                .get("1.3/")
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("1.3", info.getVersion());
    }

    // Сценарий - Получение случайно сгенерированного пользователя с указанной страницей
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void getRandomUserWithSpecifiedPage(int page) {
        Info info = given()
                .when()
                .param("results", "10")
                .param("seed", "abc")
                .param("page", page)
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals(page, info.getPage());
    }

    // Сценарий - Получение случайно сгенерированного пользователя в указанном формате
    @ParameterizedTest
    @ValueSource(strings = {"csv", "xml"})
    public void getRandomUserInSpecifiedFormatCsvXml(String specifiedFormat) {
        Response response = given()
                .when()
                .param("format", specifiedFormat)
                .get();

        String contentType = response.header("Content-Type");
        assertThat(contentType).isEqualTo("text/" + specifiedFormat + "; charset=utf-8");
        int statusCode = response.statusCode();
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void getRandomUserInSpecifiedFormatYaml() {
        Response response = given()
                .when()
                .param("format", "yaml")
                .get();

        String contentType = response.header("Content-Type");
        assertThat(contentType).isEqualTo("text/x-yaml; charset=utf-8");
        int statusCode = response.statusCode();
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void getRandomUserInSpecifiedFormatJson() {
        Response response = given()
                .when()
                .param("format", "json")
                .get();

        String contentType = response.header("Content-Type");
        assertThat(contentType).isEqualTo("application/json; charset=utf-8");
        int statusCode = response.statusCode();
        assertThat(statusCode).isEqualTo(200);
    }
}
