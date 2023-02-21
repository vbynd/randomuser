package api;

import api.PojoClasses.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class RandomUserTest {
    private static final String URL = "https://randomuser.me/api/";
    private static final String successfulFullResponseSchema = "./src/test/resources/successfulFullResponseSchema.json";

    // Сценарий - Получение случайно сгенерированного пользователя без указания дополнительных параметров
    @Test
    public void successfulGetOneRandomUser() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        JsonPath response = given()
                .when()
                .get()
                .then()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(successfulFullResponseSchema)))
                .extract().body().jsonPath();
    }

    // Сценарий - Получение случайно сгенерированного пользователя с указанным гендером
    @ParameterizedTest
    @ValueSource(strings = {"male", "female"})
    public void getRandomUserWithSpecifiedGender(String specifiedGender) {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .params("gender", specifiedGender)
                .get()
                .then()
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
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .params("nat", specifiedNationality)
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        assertThat(randomUser.getNat()).isEqualTo(specifiedNationality);
    }

    /*
    * Сценарий - Получение случайно сгенерированного пользователя с специфичным паролем
    * Комбинации сгенерированы с помощью техники попарного тестирования
    * */
    @Test
    public void getRandomUserWithSpecifiedPasswordAllCharacters() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .params("password", "upper,lower,special,number,1-10")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^.{1,10}$")).isTrue();
    }

    @Test
    public void getRandomUserWithSpecifiedPasswordOnlyUpperCharacters() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .params("password", "upper")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^[A-Z]*$")).isTrue();
    }

    @Test
    public void getRandomUserWithSpecifiedPasswordOnlySpecialCharacters() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .params("password", "special,10-15")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^([[^a-zA-Z]&&[\\D]]){10,15}$")).isTrue();
    }

    @Test
    public void getRandomUserWithSpecifiedPasswordLowerCharactersAndNumbers() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .params("password", "lower,number")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        assertThat(password.matches("^([a-z]|\\d)*$")).isTrue();
    }

    // Сценарий - Получение случайно сгенерированного пользователя с указанным сидом
    @ParameterizedTest
    @ValueSource(strings = {
            "123",
            "321",
            "asd",
            "dsa"
    })
    public void compareSeveralRandomUsersGeneratedWithSameSeed(String seed) {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUserOne = UserWithCustomSeed.generate(seed);
        Result randomUserTwo = UserWithCustomSeed.generate(seed);
        Result randomUserThree = UserWithCustomSeed.generate(seed);

        assertThat(randomUserOne).isEqualToComparingFieldByFieldRecursively(randomUserTwo);
        assertThat(randomUserTwo).isEqualToComparingFieldByFieldRecursively(randomUserThree);
        assertThat(randomUserOne).hasNoNullFieldsOrProperties();

    }

    // Сценарий - Получение случайно сгенерированного пользователя старой версией API
    @Test
    public void getRandomUserWithOldAPIVersion() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Info info = given()
                .when()
                .get("1.3/")
                .then()
                .extract().body().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("1.3", info.getVersion());
    }

    // Сценарий - Получение случайно сгенерированного пользователя с указанной страницей
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void getRandomUserWithSpecifiedPage(int page) {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Info info = given()
                .when()
                .param("results", "10")
                .param("seed", "abc")
                .param("page", page)
                .get()
                .then()
                .extract().body().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals(page, info.getPage());
    }

    // Сценарий - Получение случайно сгенерированного пользователя с несколькими указанными параметрами
    @Test
    public void getRandomUserWithSeveralParamsSpecified() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .param("gender", "male")
                .param("nat", "US")
                .param("password", "lower,number")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(randomUser.getGender()).isEqualTo("male");
            softly.assertThat(randomUser.getNat()).isEqualTo("US");
            softly.assertThat(randomUser.getLogin().getPassword().matches("^([a-z]|\\d)*$")).isTrue();
        });
    }

    // Сценарий - Получение случайно сгенерированного пользователя с исключенными полями

    @Test
    public void getRandomUserWithExcludingFieldsGenderNameLocationEmailLoginRegistered() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .param("exc", "gender,name,location,email,login,registered")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(randomUser.getGender()).isNull();
            softly.assertThat(randomUser.getName()).isNull();
            softly.assertThat(randomUser.getLocation()).isNull();
            softly.assertThat(randomUser.getEmail()).isNull();
            softly.assertThat(randomUser.getLogin()).isNull();
            softly.assertThat(randomUser.getRegistered()).isNull();
            softly.assertThat(randomUser.getDob()).isNotNull();
            softly.assertThat(randomUser.getPhone()).isNotNull();
            softly.assertThat(randomUser.getCell()).isNotNull();
            softly.assertThat(randomUser.getId()).isNotNull();
            softly.assertThat(randomUser.getPicture()).isNotNull();
            softly.assertThat(randomUser.getNat()).isNotNull();
        });
    }

    @Test
    public void getRandomUserWithExcludingFieldsDobPhoneCellIdPictureNat() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .param("exc", "dob,phone,cell,id,picture,nat")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(randomUser.getGender()).isNotNull();
            softly.assertThat(randomUser.getName()).isNotNull();
            softly.assertThat(randomUser.getLocation()).isNotNull();
            softly.assertThat(randomUser.getEmail()).isNotNull();
            softly.assertThat(randomUser.getLogin()).isNotNull();
            softly.assertThat(randomUser.getRegistered()).isNotNull();
            softly.assertThat(randomUser.getDob()).isNull();
            softly.assertThat(randomUser.getPhone()).isNull();
            softly.assertThat(randomUser.getCell()).isNull();
            softly.assertThat(randomUser.getId()).isNull();
            softly.assertThat(randomUser.getPicture()).isNull();
            softly.assertThat(randomUser.getNat()).isNull();
        });
    }

    // Сценарий - Получение случайно сгенерированного пользователя только с включенными полями
    @ParameterizedTest
    @ValueSource(strings = {"gender,name,location,email,login,registered",
            "dob,phone,cell,id,picture,nat"})
    public void getRandomUserWithIncludingFields(String includedFields) {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .param("inc", includedFields)
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        switch (includedFields) {
            case "gender,name,location,email,login,registered":
                Assertions.assertNotNull(randomUser.getGender());
                Assertions.assertNotNull(randomUser.getName());
                Assertions.assertNotNull(randomUser.getLocation());
                Assertions.assertNotNull(randomUser.getEmail());
                Assertions.assertNotNull(randomUser.getLogin());
                Assertions.assertNotNull(randomUser.getRegistered());
                Assertions.assertNull(randomUser.getDob());
                Assertions.assertNull(randomUser.getPhone());
                Assertions.assertNull(randomUser.getCell());
                Assertions.assertNull(randomUser.getId());
                Assertions.assertNull(randomUser.getPicture());
                Assertions.assertNull(randomUser.getNat());
                break;
            case "dob,phone,cell,id,picture,nat":
                Assertions.assertNull(randomUser.getGender());
                Assertions.assertNull(randomUser.getName());
                Assertions.assertNull(randomUser.getLocation());
                Assertions.assertNull(randomUser.getEmail());
                Assertions.assertNull(randomUser.getLogin());
                Assertions.assertNull(randomUser.getRegistered());
                Assertions.assertNotNull(randomUser.getDob());
                Assertions.assertNotNull(randomUser.getPhone());
                Assertions.assertNotNull(randomUser.getCell());
                Assertions.assertNotNull(randomUser.getId());
                Assertions.assertNotNull(randomUser.getPicture());
                Assertions.assertNotNull(randomUser.getNat());
                break;
        }
    }

    // Сценарий - Получение случайно сгенерированного пользователя в указанном формате
    @ParameterizedTest
    @ValueSource(strings = {"csv", "yaml", "xml", "json"})
    public void getRandomUserInSpecifiedFormat(String specifiedFormat) {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Response response = given()
                .when()
                .param("format", specifiedFormat)
                .get();

        String contentType = response.header("Content-Type");

        switch (specifiedFormat) {
            case "yaml":
                Assertions.assertEquals("text/x-yaml; charset=utf-8", contentType);
                break;
            case "json":
                Assertions.assertEquals("application/json; charset=utf-8", contentType);
                break;
            default:
                Assertions.assertEquals("text/" + specifiedFormat + "; charset=utf-8", contentType);
                break;
        }
    }

    // Сценарий - Получаем ошибку при отправке запроса с неверным методом
    @Test
    public void errorAfterIncorrectMethodSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationNOTFOUND404());

        given()
                .when()
                .post();

        given()
                .when()
                .put();

        given()
                .when()
                .patch();

        given()
                .when()
                .delete();
    }

    // Сценарий - Неверно указанное название параметра в запросе игнорируется
    @Test
    public void getRandomUserWithIncorrectlySpecifiedParamName() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .param("basd", "asd")
                .param("12gyy23", "adsd")
                .param("idhed", "male")
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        assertThat(randomUser).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getLogin()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getName()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getLocation()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getDob()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getRegistered()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getId()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getPicture()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getLocation().getStreet()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getLocation().getCoordinates()).hasNoNullFieldsOrProperties();
        assertThat(randomUser.getLocation().getTimezone()).hasNoNullFieldsOrProperties();
    }

    // Сценарий - Неверно указанное возможное значение параметра в запросе игнорируется
    @Test
    public void getRandomUserWithIncorrectlySpecifiedParamValue() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        String gender = "someGender";
        String nat = "someNat";

        Result randomUser = given()
                .when()
                .param("gender", gender)
                .param("nat", nat)
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        Assertions.assertNotEquals(gender, randomUser.getGender());
        Assertions.assertNotEquals(nat, randomUser.getNat());
    }
}
