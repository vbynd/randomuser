package api.Tests;

import api.specifications.Specifications;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected static final String URL = "https://randomuser.me/api/";
    protected static final String successfulFullResponseSchema = "./src/test/resources/successfulFullResponseSchema.json";
    @BeforeAll
    protected static void setUp() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));
    }
}
