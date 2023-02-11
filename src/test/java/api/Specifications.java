package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {
    public static RequestSpecification requestSpecification(String URL) {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpecificationOK200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    public static ResponseSpecification responseSpecificationBADREQUEST400() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
    }

    public static ResponseSpecification responseSpecificationMETHODNOTALLOWED405() {
        return new ResponseSpecBuilder()
                .expectStatusCode(405)
                .build();
    }
    public static void installSpecification(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }
}
