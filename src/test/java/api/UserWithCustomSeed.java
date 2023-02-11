package api;

import api.PojoClasses.Result;

import static io.restassured.RestAssured.given;

public class UserWithCustomSeed {
    public static Result generate(String seed) {
        return given()
                .when()
                .params("seed", seed)
                .get()
                .then()
                .extract().body().jsonPath().getList("results", Result.class).get(0);
    }
}
