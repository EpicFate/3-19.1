package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static Report.Report.filters;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DemoWebShopTest {

        @Test
        public void registrationTest() {

            Response response = given()
                    .log().uri()
                    .when()
                    .get("http://demowebshop.tricentis.com/register")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .response();

            String value = response.htmlPath().getString("**.find{it.@name == '__RequestVerificationToken'}.@value");

            Map<String, Object> requestData = new HashMap<>();
            requestData.put("__RequestVerificationToken", value);
            requestData.put("Gender", "M");
            requestData.put("FirstName", "Name");
            requestData.put("LastName", "Last");
            requestData.put("Email", "666666666@aaa.com");
            requestData.put("Password", "123321");
            requestData.put("ConfirmPassword", "123321");
            requestData.put("register-button", "Register");

            Response loggedResponse  = given()
                    .contentType(ContentType.JSON)
                    .body(requestData)
                    .filter(filters().customTemplates())
                    .log().uri()
                    .when()
                    .post("http://demowebshop.tricentis.com/register")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract()
                    .response();

            String logged = loggedResponse.htmlPath().getString("**.find{it.@head}");
            assertThat(logged, is("Object movedhere"));
        }
}

