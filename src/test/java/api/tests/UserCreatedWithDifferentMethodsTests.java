package api.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserCreatedWithDifferentMethodsTests extends BaseTest {
    // Сценарий - Получаем ошибку при отправке запроса с неверным методом
    @Test
    public void errorAfterIncorrectMethodSendPost() {
        given()
                .when()
                .post()
                .then().assertThat().statusCode(404);
    }

    @Test
    public void errorAfterIncorrectMethodSendPut() {
        given()
                .when()
                .put()
                .then().assertThat().statusCode(404);
    }

    @Test
    public void errorAfterIncorrectMethodSendPatch() {
        given()
                .when()
                .patch()
                .then().assertThat().statusCode(404);
    }

    @Test
    public void errorAfterIncorrectMethodSendDelete() {
        given()
                .when()
                .delete()
                .then().assertThat().statusCode(404);
    }
}
