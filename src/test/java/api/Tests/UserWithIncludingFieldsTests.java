package api.Tests;

import api.responseStructure.Result;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserWithIncludingFieldsTests extends BaseTest {
    // Сценарий - Получение случайно сгенерированного пользователя с исключенными полями
    @Test
    public void getRandomUserWithExcludingFieldsGenderNameLocationEmailLoginRegistered() {
        Result randomUser = given()
                .when()
                .param("exc", "gender,name,location,email,login,registered")
                .get()
                .then().assertThat().statusCode(200)
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
        Result randomUser = given()
                .when()
                .param("exc", "dob,phone,cell,id,picture,nat")
                .get()
                .then().assertThat().statusCode(200)
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
    @Test
    public void getRandomUserWithIncludingFieldsGenderNameLocationEmailLoginRegistered() {
        Result randomUser = given()
                .when()
                .param("inc", "gender,name,location,email,login,registered")
                .get()
                .then().assertThat().statusCode(200)
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

    @Test
    public void getRandomUserWithIncludingFieldsDobPhoneCellIdPictureNat() {
        Result randomUser = given()
                .when()
                .param("inc", "dob,phone,cell,id,picture,nat")
                .get()
                .then().assertThat().statusCode(200)
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
}
