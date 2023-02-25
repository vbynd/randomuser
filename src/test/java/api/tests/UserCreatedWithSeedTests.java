package api.tests;

import api.responseStructure.Result;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserCreatedWithSeedTests  extends BaseTest {
    // Сценарий - Получение случайно сгенерированного пользователя с указанным сидом
    @ParameterizedTest
    @ValueSource(strings = {
            "123",
            "321",
            "asd",
            "dsa"
    })
    public void compareSeveralRandomUsersGeneratedWithSameSeed(String seed) {
        Result randomUserOne = given()
                .when()
                .params("seed", seed)
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        Result randomUserTwo = given()
                .when()
                .params("seed", seed)
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        Result randomUserThree = given()
                .when()
                .params("seed", seed)
                .get()
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getList("results", Result.class).get(0);

        assertThat(randomUserOne).isEqualToComparingFieldByFieldRecursively(randomUserTwo);
        assertThat(randomUserTwo).isEqualToComparingFieldByFieldRecursively(randomUserThree);
        assertThat(randomUserOne).hasNoNullFieldsOrProperties();
    }
}
