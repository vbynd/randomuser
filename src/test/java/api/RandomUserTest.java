package api;

import api.PojoClasses.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class RandomUserTest {
    private static final String URL = "https://randomuser.me/api/";

    // Сценарий - Получение случайно сгенерированного пользователя без указания дополнительных параметров
    @Test
    public void successfulGetOneRandomUser() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        JsonPath response = given()
                .when()
                .get()
                .then()
                .extract().body().jsonPath();

        Result randomUser = response.getList("results", Result.class).get(0);
        Info info = response.getObject("info", Info.class);

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

        Assertions.assertNotNull(info.getSeed());
        Assertions.assertEquals(1, info.getResults());
        Assertions.assertEquals(1, info.getPage());
        Assertions.assertTrue(info.getVersion().equals("1.4"));
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

        Assertions.assertEquals(specifiedGender, randomUser.getGender());
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

        Assertions.assertEquals(specifiedNationality, randomUser.getNat());
    }

    /*
    * Сценарий - Получение случайно сгенерированного пользователя с специфичным паролем
    * Комбинации сгенерированы с помощью техники попарного тестирования
    * */
    @ParameterizedTest
    @ValueSource(strings = {
            "upper,lower,special,number,1-10",
            "upper",
            "special,10-15",
            "lower,number"
    })
    public void getRandomUserWithSpecifiedPassword(String specifiedPassword) {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .params("password", specifiedPassword)
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        String password = randomUser.getLogin().getPassword();

        switch (specifiedPassword) {
            case "upper,lower,special,number,1-10":
                password.matches("^.{1,10}$");
                break;
            case "upper":
                password.matches("^[A-Z]*$");
                break;
            case "special,10-15":
                password.matches("^([^a-zA-Z]\\D){10,15}$");
                break;
            case "lower,number":
                password.matches("^([a-z]|\\d)*$");
                break;
        }
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

        Assertions.assertEquals("male", randomUser.getGender());
        Assertions.assertEquals("US", randomUser.getNat());
        Assertions.assertTrue(randomUser.getLogin().getPassword().matches("^([a-z]|\\d)*$"));
    }

    // Сценарий - Получение случайно сгенерированного пользователя с исключенными полями
    @ParameterizedTest
    @ValueSource(strings = {"gender,name,location,email,login,registered",
                            "dob,phone,cell,id,picture,nat"})
    public void getRandomUserWithExcludingFields(String excludedFields) {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        Result randomUser = given()
                .when()
                .param("exc", excludedFields)
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        switch (excludedFields){
            case "gender,name,location,email,login,registered":
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
            case "dob,phone,cell,id,picture,nat":
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
        }
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
