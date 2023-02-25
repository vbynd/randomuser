package api.specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {
    public static RequestSpecification requestSpecification(String URL) {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static void installSpecification(RequestSpecification requestSpecification) {
        RestAssured.requestSpecification = requestSpecification;
    }
}
